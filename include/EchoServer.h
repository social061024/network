#pragma once
#include <winsock2.h>
#include <ws2tcpip.h>
#include <thread>
#include <vector>

class EchoServer {
public:
    EchoServer(int port);
    ~EchoServer();

    bool start();
    void stop();

private:
    int port;
    SOCKET listenSocket;
    bool running;
    std::vector<std::thread> threads;
    int clientCounter; // лічильник клієнтів

    void handleClient(SOCKET clientSocket, int clientId);
};