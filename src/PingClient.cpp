#include "PingClient.h"
#include <iostream>
#include <chrono>
#include <stdexcept>
#include <windows.h>

struct ICMPHeader {
    unsigned char type;
    unsigned char code;
    unsigned short checksum;
    unsigned short id;
    unsigned short seq;
};

unsigned short checksum(unsigned short* buf, int size) {
    unsigned long sum = 0;
    while (size > 1) { sum += *buf++; size -= 2; }
    if (size) sum += *(unsigned char*)buf;
    sum = (sum >> 16) + (sum & 0xffff);
    sum += (sum >> 16);
    return (unsigned short)(~sum);
}

PingClient::PingClient(const std::string& ip) {
    WSADATA wsaData;
    WSAStartup(MAKEWORD(2, 2), &wsaData);
    sock = socket(AF_INET, SOCK_RAW, IPPROTO_ICMP);
    if (sock == INVALID_SOCKET) throw std::runtime_error("Failed to create raw ICMP socket");

    dest.sin_family = AF_INET;
    dest.sin_addr.s_addr = inet_addr(ip.c_str());
}

PingClient::~PingClient() {
    closesocket(sock);
    WSACleanup();
}

void PingClient::sendEchoRequest() {
    ICMPHeader icmp;
    icmp.type = 8; // Echo Request
    icmp.code = 0;
    icmp.id = (unsigned short)GetCurrentProcessId();
    icmp.seq = 1;
    icmp.checksum = 0;
    icmp.checksum = checksum((unsigned short*)&icmp, sizeof(icmp));

    auto start = std::chrono::high_resolution_clock::now();
    sendto(sock, (char*)&icmp, sizeof(icmp), 0, (sockaddr*)&dest, sizeof(dest));

    char buffer[1024];
    int len = sizeof(dest);
    int bytes = recvfrom(sock, buffer, sizeof(buffer), 0, (sockaddr*)&dest, &len);
    auto end = std::chrono::high_resolution_clock::now();
    auto duration = std::chrono::duration_cast<std::chrono::milliseconds>(end - start).count();

    if (bytes > 0) {
        std::cout << "ICMP Echo Reply received in " << duration << " ms" << std::endl;
    }
    else {
        std::cout << "ICMP Request timed out" << std::endl;
    }
}
