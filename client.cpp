#include "SocketBase.h"
#include "DataProcessor.h"
#include <iostream>
#include <vector>
#include <string>

class Client : public SocketBase {
public:
    void run(const std::string& serverIp, int port) {
        initWinsock();

        sock = socket(AF_INET, SOCK_STREAM, 0);
        if (sock == INVALID_SOCKET) {
            std::cerr << "Socket creation failed!" << std::endl;
            return;
        }

        addr.sin_family = AF_INET;
        addr.sin_port = htons(port);
        addr.sin_addr.s_addr = inet_addr(serverIp.c_str());

        if (connect(sock, (sockaddr*)&addr, sizeof(addr)) == SOCKET_ERROR) {
            std::cerr << "Connection failed!" << std::endl;
            return;
        }

        // Масив чисел для відправки
        std::vector<int> numbers = { 3, 5, 2, 7 };
        std::string serialized = DataProcessor::serializeData(numbers);

        send(sock, serialized.c_str(), serialized.size(), 0);

        char buffer[1024];
        int bytesReceived = recv(sock, buffer, sizeof(buffer), 0);
        if (bytesReceived > 0) {
            buffer[bytesReceived] = '\0';
            std::cout << "Received histogram:\n" << buffer << std::endl;
        }
        else {
            std::cerr << "No data received from server!" << std::endl;
        }

        cleanup();
    }
};

int main() {
    Client client;
    client.run("127.0.0.1", 54000);

    // Щоб програма не закривалась одразу
    std::cout << "\nPress Enter to exit...";
    std::cin.get();

    return 0;
}
