#include "TCPClient.h"
#include <iostream>

int main() {
    TCPClient client;
    std::string ip;

    std::cout << "--- TCP CLIENT START ---" << std::endl;
    std::cout << "Enter Server IP: ";
    std::cin >> ip;
    std::cin.ignore();

    if (client.connectToServer(ip, 8080)) {
        std::cout << "Connected to " << ip << std::endl;
        std::string msg;
        while (true) {
            std::cout << "You (Client): ";
            std::getline(std::cin, msg);
            client << msg;
            if (msg == "exit") break;

            client >> msg;
            if (msg == "exit" || msg.empty()) break;
            std::cout << "Server: " << msg << std::endl;
        }
    }
    else {
        std::cout << "Connection failed!" << std::endl;
    }
    return 0;
}