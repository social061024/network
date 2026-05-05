#include "EchoServer.h"
#include <iostream>

EchoServer::EchoServer(int port)
    : port(port), listenSocket(INVALID_SOCKET), running(false), clientCounter(0) {
}

EchoServer::~EchoServer() {
    stop();
    WSACleanup();
}

bool EchoServer::start() {
    WSADATA wsaData;
    if (WSAStartup(MAKEWORD(2, 2), &wsaData) != 0) {
        std::cerr << "WSAStartup failed\n";
        return false;
    }

    listenSocket = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);
    if (listenSocket == INVALID_SOCKET) {
        std::cerr << "Socket creation failed\n";
        return false;
    }

    sockaddr_in serverAddr{};
    serverAddr.sin_family = AF_INET;
    serverAddr.sin_addr.s_addr = INADDR_ANY;
    serverAddr.sin_port = htons(port);

    if (bind(listenSocket, (sockaddr*)&serverAddr, sizeof(serverAddr)) == SOCKET_ERROR) {
        std::cerr << "Bind failed\n";
        return false;
    }

    if (listen(listenSocket, SOMAXCONN) == SOCKET_ERROR) {
        std::cerr << "Listen failed\n";
        return false;
    }

    running = true;
    std::cout << "Server started on port " << port << "\n";

    while (running) {
        SOCKET clientSocket = accept(listenSocket, nullptr, nullptr);
        if (clientSocket == INVALID_SOCKET) {
            std::cerr << "Accept failed\n";
            continue;
        }
        clientCounter++;
        int clientId = clientCounter;
        std::cout << "Client " << clientId << " connected\n";
        threads.emplace_back(&EchoServer::handleClient, this, clientSocket, clientId);
    }

    return true;
}

void EchoServer::stop() {
    running = false;
    closesocket(listenSocket);
    for (auto& t : threads) {
        if (t.joinable()) t.join();
    }
}

void EchoServer::handleClient(SOCKET clientSocket, int clientId) {
    char buffer[512];
    int bytesReceived;
    do {
        bytesReceived = recv(clientSocket, buffer, sizeof(buffer), 0);
        if (bytesReceived > 0) {
            buffer[bytesReceived] = '\0';
            std::cout << "Client " << clientId << " sent: " << buffer << "\n";
            send(clientSocket, buffer, bytesReceived, 0);
        }
    } while (bytesReceived > 0);

    std::cout << "Client " << clientId << " disconnected\n";
    closesocket(clientSocket);
}