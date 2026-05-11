#include "MulticastSender.h"
#include <iostream>
#include <stdexcept>

MulticastSender::MulticastSender(const std::string& groupIP, int port) {
    WSADATA wsaData;
    WSAStartup(MAKEWORD(2, 2), &wsaData);

    sock = socket(AF_INET, SOCK_DGRAM, IPPROTO_UDP);
    if (sock == INVALID_SOCKET) throw std::runtime_error("Failed to create UDP socket");

    groupAddr.sin_family = AF_INET;
    groupAddr.sin_port = htons(port);
    groupAddr.sin_addr.s_addr = inet_addr(groupIP.c_str());
}

MulticastSender::~MulticastSender() {
    closesocket(sock);
    WSACleanup();
}

void MulticastSender::sendMessage(const std::string& msg) {
    sendto(sock, msg.c_str(), msg.size(), 0, (sockaddr*)&groupAddr, sizeof(groupAddr));
    std::cout << "Sent: " << msg << std::endl;
}
