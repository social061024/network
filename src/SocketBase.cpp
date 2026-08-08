#include "SocketBase.h"

SocketBase::SocketBase() : sock(INVALID_SOCKET) {}

SocketBase::~SocketBase() {
    if (sock != INVALID_SOCKET) {
        closesocket(sock);
    }
    WSACleanup();
}

void SocketBase::initWinsock() {
    if (WSAStartup(MAKEWORD(2, 2), &wsaData) != 0) {
        std::cerr << "WSAStartup failed!" << std::endl;
        exit(1);
    }
}

void SocketBase::cleanup() {
    WSACleanup();
}