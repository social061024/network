#include "SocketBase.h"
#include "DataProcessor.h"
#include "HistogramBuilder.h"
#include <thread>
#include <vector>
#include <iostream>

class Server : public SocketBase {
public:
    void handleClient(SOCKET clientSock) {
        char buffer[1024];
        int bytesReceived = recv(clientSock, buffer, sizeof(buffer), 0);
        if (bytesReceived > 0) {
            buffer[bytesReceived] = '\0';

            std::vector<int> data = DataProcessor::parseData(buffer);
            std::string histogram = HistogramBuilder::buildHistogram(data);

            send(clientSock, histogram.c_str(), histogram.size(), 0);
        }
        closesocket(clientSock);
    }

    void run(int port) {
        initWinsock();

        sock = socket(AF_INET, SOCK_STREAM, 0);
        addr.sin_family = AF_INET;
        addr.sin_addr.s_addr = INADDR_ANY;
        addr.sin_port = htons(port);

        if (bind(sock, (sockaddr*)&addr, sizeof(addr)) == SOCKET_ERROR) {
            std::cerr << "Bind failed!" << std::endl;
            return;
        }

        listen(sock, 5);
        std::cout << "Server started. Waiting for clients... Press Ctrl+C to exit.\n";

        while (true) {
            SOCKET clientSock = accept(sock, nullptr, nullptr);
            if (clientSock == INVALID_SOCKET) {
                std::cerr << "Accept failed!" << std::endl;
                continue;
            }

            // Запускаємо новий потік для кожного клієнта
            std::thread t(&Server::handleClient, this, clientSock);
            t.detach(); // від’єднуємо, щоб він працював незалежно
        }

        cleanup();
    }
};

int main() {
    Server server;
    server.run(54000);

    // Щоб сервер не закривався одразу
    std::cout << "\nPress Enter to exit...";
    std::cin.get();

    return 0;
}
