cmake_minimum_required(VERSION 3.10)

# Назва проєкту
project(ClientServerWinSock2 VERSION 1.0 LANGUAGES CXX)

# Використовуємо стандарт C++17
set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED True)

# Додаємо директорію з заголовками
include_directories(${PROJECT_SOURCE_DIR}/include)

# Загальні файли для обох програм
set(SOURCES
    src/SocketBase.cpp
    src/DataProcessor.cpp
    src/HistogramBuilder.cpp
)

# Вказуємо директорію для виконуваних файлів
set(CMAKE_RUNTIME_OUTPUT_DIRECTORY ${PROJECT_SOURCE_DIR}/bin)

# Сервер
add_executable(server server.cpp ${SOURCES})
target_link_libraries(server ws2_32)

# Клієнт
add_executable(client client.cpp ${SOURCES})
target_link_libraries(client ws2_32)
