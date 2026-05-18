cmake_minimum_required(VERSION 3.10)
project(TCP_Chat_Simple)

set(CMAKE_CXX_STANDARD 17)
include_directories(include)

if(WIN32)
    set(SOCKET_LIBS ws2_32)
endif()

set(CORE_SOURCES 
    src/TCPSocket.cpp 
    src/TCPServer.cpp 
    src/TCPClient.cpp
)

add_executable(ServerApp src/server/server.cpp ${CORE_SOURCES})
target_link_libraries(ServerApp ${SOCKET_LIBS})

add_executable(ClientApp src/client/client.cpp ${CORE_SOURCES})
target_link_libraries(ClientApp ${SOCKET_LIBS})