cmake_minimum_required(VERSION 3.10)
project(EchoServer)

set(CMAKE_CXX_STANDARD 17)

include_directories(include)

add_executable(EchoServer
    src/main.cpp
    src/EchoServer.cpp
)

add_executable(Client
    src/Client.cpp
)

target_link_libraries(EchoServer ws2_32)
target_link_libraries(Client ws2_32)