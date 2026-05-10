#include "time_server.h"
#include <iostream>

int main() {
    try {
        time_server server(9090);
        server.run();
    }
    catch (const std::exception& e) {
        std::cerr << "Error: " << e.what() << std::endl;
    }
    return 0;
}