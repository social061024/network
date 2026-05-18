#include "TCPServer.h"
#include <iostream>

TCPServer::TCPServer(int port) {
    sock = socket(AF_INET, SOCK_STREAM, 0);

    sockaddr_in addr;
    addr.sin_family = AF_INET;
    addr.sin_port = htons(port);
    addr.sin_addr.s_addr = INADDR_ANY;

    bind(sock, (struct sockaddr*)&addr, sizeof(addr));
    listen(sock, SOMAXCONN);
}

TCPSocket TCPServer::acceptConnection() {
    SOCKET clientSock = accept(sock, NULL, NULL);
    return TCPSocket(clientSock);
}