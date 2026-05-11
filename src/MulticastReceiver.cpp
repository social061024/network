#include "MulticastReceiver.h"
#include <iostream>
#include <stdexcept>

MulticastReceiver::MulticastReceiver(const std::string& groupIP, int port) {
    WSADATA wsaData;
    WSAStartup(MAKEWORD(2, 2), &wsaData);

    sock = socket(AF_INET, SOCK_DGRAM, IPPROTO_UDP);
    if (sock == INVALID_SOCKET) throw std::runtime_error("Failed to create UDP socket");

    int reuse = 1;
    if (setsockopt(sock, SOL_SOCKET, SO_REUSEADDR, (char*)&reuse, sizeof(reuse)) == SOCKET_ERROR) {
        throw std::runtime_error("Failed to set SO_REUSEADDR");
    }

    sockaddr_in localAddr{};
    localAddr.sin_family = AF_INET;
    localAddr.sin_port = htons(port);
    localAddr.sin_addr.s_addr = INADDR_ANY;

    if (bind(sock, (sockaddr*)&localAddr, sizeof(localAddr)) == SOCKET_ERROR) {
        throw std::runtime_error("Bind failed");
    }

    mreq.imr_multiaddr.s_addr = inet_addr(groupIP.c_str());
    mreq.imr_interface.s_addr = INADDR_ANY;

    if (setsockopt(sock, IPPROTO_IP, IP_ADD_MEMBERSHIP, (char*)&mreq, sizeof(mreq)) == SOCKET_ERROR) {
        throw std::runtime_error("Failed to join multicast group");
    }

    std::cout << "Joined multicast group: " << groupIP << " on port " << port << std::endl;
}

MulticastReceiver::~MulticastReceiver() {
    setsockopt(sock, IPPROTO_IP, IP_DROP_MEMBERSHIP, (char*)&mreq, sizeof(mreq));
    closesocket(sock);
    WSACleanup();
}

void MulticastReceiver::receiveMessages() {
    char buffer[256];
    sockaddr_in sender{};
    int senderSize = sizeof(sender);

    std::cout << "Listening for multicast messages..." << std::endl;
    while (true) {
        int bytes = recvfrom(sock, buffer, sizeof(buffer) - 1, 0, (sockaddr*)&sender, &senderSize);
        if (bytes > 0) {
            buffer[bytes] = '\0';
            std::cout << "Received: " << buffer << std::endl;
        }
    }
}
