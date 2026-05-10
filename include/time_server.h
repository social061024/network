#pragma once
#include <winsock2.h>
#include <string>

class time_server {
public:
    time_server(int port);
    ~time_server();
    void run();

private:
    SOCKET serverSocket;
    sockaddr_in serverAddr;
};