#include "MulticastSender.h"
#include <iostream>
#include <string>

int main() {
    try {
        std::string groupIP;
        int port;
        std::cout << "Enter multicast group IP (e.g., 239.255.0.1): ";
        std::cin >> groupIP;
        std::cout << "Enter port (e.g., 9090): ";
        std::cin >> port;

        MulticastSender server(groupIP, port);

        std::string msg;
        std::cin.ignore(); 
        while (true) {
            std::cout << "Enter message to send (or 'exit'): ";
            std::getline(std::cin, msg);
            if (msg == "exit") break;
            server.sendMessage(msg);
        }
    }
    catch (const std::exception& e) {
        std::cerr << "Error: " << e.what() << std::endl;
    }
    return 0;
}
