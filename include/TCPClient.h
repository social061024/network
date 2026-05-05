#ifndef TCP_CLIENT_H
#define TCP_CLIENT_H

#include "TCPSocket.h"

class TCPClient : public TCPSocket {
public:
    bool connectToServer(const std::string& ip, int port);
};

#endif