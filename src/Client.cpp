#include <winsock2.h>
#include <ws2tcpip.h>
#include <iostream>
#include <string>

int main() {
    WSADATA wsaData;
    if (WSAStartup(MAKEWORD(2, 2), &wsaData) != 0) {
        std::cerr << "WSAStartup failed\n";
        return 1;
    }

    SOCKET clientSocket = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);
    if (clientSocket == INVALID_SOCKET) {
        std::cerr << "Socket creation failed\n";
        WSACleanup();
        return 1;
    }

    std::string ip;
    std::cout << "Enter server IP address: ";
    std::cin >> ip;

    sockaddr_in serverAddr{};
    serverAddr.sin_family = AF_INET;
    serverAddr.sin_port = htons(8080);

    if (inet_pton(AF_INET, ip.c_str(), &serverAddr.sin_addr) <= 0) {
        std::cerr << "Invalid IP address format\n";
        closesocket(clientSocket);
        WSACleanup();
        return 1;
    }

    if (connect(clientSocket, (sockaddr*)&serverAddr, sizeof(serverAddr)) == SOCKET_ERROR) {
        std::cerr << "Connection failed\n";
        closesocket(clientSocket);
        WSACleanup();
        return 1;
    }

    std::cout << "Connected to server " << ip << " on port 8080\n";

    std::cin.ignore();
    std::string input;
    char buffer[512];
    while (true) {
        std::cout << "Enter message (exit to quit): ";
        std::getline(std::cin, input);
        if (input == "exit") break;

        send(clientSocket, input.c_str(), (int)input.size(), 0);

        int bytesReceived = recv(clientSocket, buffer, sizeof(buffer) - 1, 0);
        if (bytesReceived > 0) {
            buffer[bytesReceived] = '\0';
            std::cout << "Echo: " << buffer << "\n";
        }
    }

    closesocket(clientSocket);
    WSACleanup();
    return 0;
}