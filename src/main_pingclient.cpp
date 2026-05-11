#include "PingClient.h"
#include <iostream>

int main() {
    try {
        std::string ip;
        std::cout << "Enter IP address to ping: ";
        std::cin >> ip;

        PingClient app(ip);
        app.sendEchoRequest();
    }
    catch (const std::exception& e) {
        std::cerr << "Error: " << e.what() << std::endl;
    }
    return 0;
}
