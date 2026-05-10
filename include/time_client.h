#pragma once
#include <winsock2.h>
#include <string>

class time_client {
public:
    time_client(const std::string& ip, int port);
    ~time_client();
    void requestTime();

private:
    SOCKET clientSocket;
    sockaddr_in serverAddr;
};