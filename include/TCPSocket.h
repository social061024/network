#ifndef TCP_SOCKET_H
#define TCP_SOCKET_H

#include <winsock2.h> 
#include <windows.h>
#include <string>

class TCPSocket {
protected:
    SOCKET sock;
public:
    TCPSocket();
    TCPSocket(SOCKET s);
    virtual ~TCPSocket();

    friend TCPSocket& operator<<(TCPSocket& s, const std::string& msg);
    friend TCPSocket& operator>>(TCPSocket& s, std::string& msg);

    void close();
    bool isValid() const { return sock != INVALID_SOCKET; }
};

#endif