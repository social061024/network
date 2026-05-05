#ifndef TCP_SERVER_H
#define TCP_SERVER_H

#include "TCPSocket.h"

class TCPServer : public TCPSocket {
public:
    TCPServer(int port);
    TCPSocket acceptConnection();
};

#endif