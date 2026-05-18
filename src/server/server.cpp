#include "TCPServer.h"
#include <iostream>

int main() {
    TCPServer server(8080);
    std::cout << "--- TCP SERVER START ---" << std::endl;
    std::cout << "Waiting for connections..." << std::endl;

    TCPSocket client = server.acceptConnection();
    std::cout << "Client connected!" << std::endl;

    std::string msg;
    while (true) {
        client >> msg;
        if (msg == "exit" || msg.empty()) break;
        std::cout << "Client: " << msg << std::endl;

        std::cout << "You (Server): ";
        std::getline(std::cin, msg);
        client << msg;
        if (msg == "exit") break;
    }
    return 0;
}