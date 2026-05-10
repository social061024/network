#include "time_server.h"
#include <iostream>
#include <ctime>

time_server::time_server(int port) {
    WSADATA wsaData;
    WSAStartup(MAKEWORD(2, 2), &wsaData);

    serverSocket = socket(AF_INET, SOCK_DGRAM, IPPROTO_UDP);
    if (serverSocket == INVALID_SOCKET) {
        throw std::runtime_error("Failed to create socket");
    }

    serverAddr.sin_family = AF_INET;
    serverAddr.sin_port = htons(port);
    serverAddr.sin_addr.s_addr = INADDR_ANY;

    if (bind(serverSocket, (sockaddr*)&serverAddr, sizeof(serverAddr)) == SOCKET_ERROR) {
        closesocket(serverSocket);
        WSACleanup();
        throw std::runtime_error("Bind failed");
    }
}

time_server::~time_server() {
    closesocket(serverSocket);
    WSACleanup();
}

void time_server::run() {
    std::cout << "UDP Time Server running..." << std::endl;

    char buffer[256];
    sockaddr_in clientAddr;
    int clientAddrSize = sizeof(clientAddr);

    while (true) {
        int bytesReceived = recvfrom(serverSocket, buffer, sizeof(buffer), 0,
            (sockaddr*)&clientAddr, &clientAddrSize);
        if (bytesReceived == SOCKET_ERROR) {
            std::cerr << "Receive failed" << std::endl;
            break;
        }

        time_t unixTime = time(nullptr);
        std::string reply = std::to_string(unixTime);

        sendto(serverSocket, reply.c_str(), reply.size(), 0,
            (sockaddr*)&clientAddr, clientAddrSize);

        std::cout << "Sent Unix time: " << reply << std::endl;
    }
}