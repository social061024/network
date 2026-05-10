#include "time_client.h"
#include <iostream>   

int main() {
    try {
        time_client client("127.0.0.1", 9090);

        while (true) {
            std::cout << "Press ENTER to request Unix time (or type 'exit' to quit): ";
            std::string input;
            std::getline(std::cin, input);

            if (input == "exit") {
                break;
            }

            client.requestTime();
        }

    }
    catch (const std::exception& e) {
        std::cerr << "Error: " << e.what() << std::endl;
    }
    return 0;
}