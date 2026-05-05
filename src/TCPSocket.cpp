#include "TCPSocket.h"
#include <iostream>

TCPSocket::TCPSocket() : sock(INVALID_SOCKET) {
    WSADATA wsaData;
    if (WSAStartup(MAKEWORD(2, 2), &wsaData) != 0) {
        std::cerr << "WSAStartup failed\n";
    }
}

TCPSocket::TCPSocket(SOCKET s) : sock(s) {}

TCPSocket::~TCPSocket() {
    close();
}

void TCPSocket::close() {
    if (sock != INVALID_SOCKET) {
        closesocket(sock);
        sock = INVALID_SOCKET;
    }
}

TCPSocket& operator<<(TCPSocket& s, const std::string& msg) {
    send(s.sock, msg.c_str(), (int)msg.length(), 0);
    return s;
}

TCPSocket& operator>>(TCPSocket& s, std::string& msg) {
    char buffer[1024] = { 0 };
    int bytes = recv(s.sock, buffer, 1024, 0);
    if (bytes > 0) msg = std::string(buffer, bytes);
    else msg = "";
    return s;
}