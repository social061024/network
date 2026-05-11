#pragma once
#include <winsock2.h>
#include <ws2tcpip.h>
#include <string>

class MulticastSender {
public:
    MulticastSender(const std::string& groupIP, int port);
    ~MulticastSender();
    void sendMessage(const std::string& msg);

private:
    SOCKET sock;
    sockaddr_in groupAddr;
};
