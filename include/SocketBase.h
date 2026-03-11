#pragma once
#include <winsock2.h>
#include <iostream>

class SocketBase {
protected:
    WSADATA wsaData;
    SOCKET sock;
    sockaddr_in addr;

public:
    SocketBase();
    virtual ~SocketBase();
    void initWinsock();
    void cleanup();
};
