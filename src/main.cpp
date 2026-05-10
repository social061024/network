#include <winsock2.h>
#include <windows.h>
#include <iostream>
#include <string>
#include <chrono>

struct NetworkICMPPacket {
    unsigned char  type;       
    unsigned char  code;
    unsigned short checksum;
    unsigned short id;
    unsigned short sequence;
};

unsigned short CalculateChecksum(unsigned short* buffer, int size) {
    unsigned long sum = 0;
    while (size > 1) {
        sum += *buffer++;
        size -= 2;
    }
    if (size) {
        sum += *(unsigned char*)buffer;
    }
    sum = (sum >> 16) + (sum & 0xffff);
    sum += (sum >> 16);
    return (unsigned short)(~sum);
}

void ExecutePingRequest(const std::string& target_ip) {
    WSADATA wsa_data;
    if (WSAStartup(MAKEWORD(2, 2), &wsa_data) != 0) {
        std::cerr << "WSAStartup failed." << std::endl;
        return;
    }

    // Створення RAW сокета. Увага: потребує прав адміністратора!
    SOCKET raw_socket = socket(AF_INET, SOCK_RAW, IPPROTO_ICMP);
    if (raw_socket == INVALID_SOCKET) {
        std::cerr << "Error: Raw socket creation failed. Run as Administrator." << std::endl;
        WSACleanup();
        return;
    }

    // Налаштування тайм-ауту отримання (1 секунда)
    int timeout = 1000;
    setsockopt(raw_socket, SOL_SOCKET, SO_RCVTIMEO, (const char*)&timeout, sizeof(timeout));

    sockaddr_in dest_addr;
    dest_addr.sin_family = AF_INET;
    dest_addr.sin_addr.s_addr = inet_addr(target_ip.c_str());

    std::cout << "Pinging " << target_ip << " with 32 bytes of data:" << std::endl;

    for (int i = 1; i <= 4; i++) {
        NetworkICMPPacket packet = {};
        packet.type = 8;
        packet.code = 0;
        packet.id = (unsigned short)GetCurrentProcessId();
        packet.sequence = (unsigned short)i;
        packet.checksum = 0;
        packet.checksum = CalculateChecksum((unsigned short*)&packet, sizeof(packet));

        auto start_time = std::chrono::high_resolution_clock::now();

        int send_result = sendto(raw_socket, (char*)&packet, sizeof(packet), 0,
            (sockaddr*)&dest_addr, sizeof(dest_addr));

        if (send_result == SOCKET_ERROR) {
            std::cerr << "Iteration " << i << ": Send failed." << std::endl;
            continue;
        }

        char rx_buffer[1024];
        sockaddr_in from_addr;
        int from_len = sizeof(from_addr);

        int bytes_received = recvfrom(raw_socket, rx_buffer, sizeof(rx_buffer), 0,
            (sockaddr*)&from_addr, &from_len);

        auto end_time = std::chrono::high_resolution_clock::now();
        auto elapsed_ms = std::chrono::duration_cast<std::chrono::milliseconds>(end_time - start_time).count();

        if (bytes_received > 0) {
            std::cout << "Reply from " << target_ip
                << ": bytes=" << bytes_received
                << " time=" << elapsed_ms << "ms"
                << " seq=" << i << std::endl;
        }
        else {
            std::cout << "Request timed out for seq=" << i << std::endl;
        }

        Sleep(1000);
    }

    closesocket(raw_socket);
    WSACleanup();
}

int main() {
    std::string ip_input;
    std::cout << "Enter target IP: ";
    std::getline(std::cin, ip_input);

    if (ip_input.empty()) ip_input = "8.8.8.8"; // Значення за замовчуванням (Google DNS)

    ExecutePingRequest(ip_input);

    std::cout << "\nPress Enter to exit...";
    std::cin.get();
    return 0;
}