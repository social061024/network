#pragma once
#include <winsock2.h>
#include <string>

class PingClient {
public:
    PingClient(const std::string& ip);
    ~PingClient();
    void sendEchoRequest();

private:
    SOCKET sock;
    sockaddr_in dest;
};
