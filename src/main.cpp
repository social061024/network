#include "EchoServer.h"

int main() {
    EchoServer server(8080);
    if (server.start()) {
    }
    return 0;
}