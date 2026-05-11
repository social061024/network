#pragma once
#include <winsock2.h>
#include <ws2tcpip.h>
#include <string>

class MulticastReceiver {
public:
    MulticastReceiver(const std::string& groupIP, int port);
    ~MulticastReceiver();
    void receiveMessages();

private:
    SOCKET sock;
    ip_mreq mreq;
};
