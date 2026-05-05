#include "TCPClient.h"
#include <ws2tcpip.h>

bool TCPClient::connectToServer(const std::string& ip, int port) {
    sock = socket(AF_INET, SOCK_STREAM, 0);

    sockaddr_in addr;
    addr.sin_family = AF_INET;
    addr.sin_port = htons(port);
    inet_pton(AF_INET, ip.c_str(), &addr.sin_addr);

    return connect(sock, (struct sockaddr*)&addr, sizeof(addr)) != SOCKET_ERROR;
}