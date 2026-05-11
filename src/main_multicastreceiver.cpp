#include "MulticastReceiver.h"
#include <iostream>

int main() {
    try {
        std::string groupIP;
        int port;
        std::cout << "Enter multicast group IP (e.g., 239.255.0.1): ";
        std::cin >> groupIP;
        std::cout << "Enter port (e.g., 9090): ";
        std::cin >> port;

        MulticastReceiver app(groupIP, port);
        app.receiveMessages();
    }
    catch (const std::exception& e) {
        std::cerr << "Error: " << e.what() << std::endl;
    }
    return 0;
}
