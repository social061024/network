#include "SocketBase.h"
#include "DataProcessor.h"
#include "HistogramBuilder.h"

class Server : public SocketBase {
public:
    void run(int port) {
        initWinsock();

        sock = socket(AF_INET, SOCK_STREAM, 0);
        addr.sin_family = AF_INET;
        addr.sin_addr.s_addr = INADDR_ANY;
        addr.sin_port = htons(port);

        bind(sock, (sockaddr*)&addr, sizeof(addr));
        listen(sock, 5);

        std::cout << "Server started. Press Ctrl+C to exit.\n";

        SOCKET clientSock = accept(sock, nullptr, nullptr);

        char buffer[1024];
        int bytesReceived = recv(clientSock, buffer, sizeof(buffer), 0);
        buffer[bytesReceived] = '\0';

        std::vector<int> data = DataProcessor::parseData(buffer);
        std::string histogram = HistogramBuilder::buildHistogram(data);

        send(clientSock, histogram.c_str(), histogram.size(), 0);

        closesocket(clientSock);
        cleanup();
    }
};

int main() {
    Server server;
    server.run(54000);
    return 0;
}
