package org.example;

import java.io.IOException;
import java.net.*;
import java.nio.file.*;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class Server {
    // Список адрес клієнтів (IP + Port)
    private static final Set<InetSocketAddress> clientAddresses = new CopyOnWriteArraySet<>();

    public static void main(String[] args) throws IOException {
        // Створюємо UDP сокет на порту 5000
        DatagramSocket serverSocket = new DatagramSocket(5000);
        System.out.println("UDP Сервер моніторингу запущено на порту 5000...");

        // Створюємо папку для моніторингу
        String pathStr = "./monitored_dir";
        Path path = Paths.get(pathStr);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        // Запуск моніторингу в окремому потоці
        new Thread(() -> monitorDirectory(pathStr, serverSocket)).start();

        // Основний потік: реєстрація нових клієнтів
        byte[] buffer = new byte[1024];
        while (true) {
            DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
            serverSocket.receive(receivePacket); // Чекаємо на "REG" пакет

            InetSocketAddress clientAddr = new InetSocketAddress(
                    receivePacket.getAddress(),
                    receivePacket.getPort()
            );

            if (clientAddresses.add(clientAddr)) {
                System.out.println("Новий клієнт підключився: " + clientAddr);
            }
        }
    }

    private static void monitorDirectory(String dirPath, DatagramSocket socket) {
        try (WatchService watcher = FileSystems.getDefault().newWatchService()) {
            Path path = Paths.get(dirPath);
            path.register(watcher,
                    StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_MODIFY,
                    StandardWatchEventKinds.ENTRY_DELETE);

            System.out.println("Моніторинг папки: " + path.toAbsolutePath());

            while (true) {
                WatchKey key = watcher.take();
                for (WatchEvent<?> event : key.pollEvents()) {
                    String msg = "Подія: " + event.kind() + " Файл: " + event.context();
                    broadcast(msg, socket);
                }
                key.reset();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void broadcast(String message, DatagramSocket socket) {
        byte[] data = message.getBytes();
        for (InetSocketAddress client : clientAddresses) {
            try {
                DatagramPacket packet = new DatagramPacket(
                        data, data.length, client.getAddress(), client.getPort()
                );
                socket.send(packet);
            } catch (IOException e) {
                System.err.println("Помилка надсилання клієнту " + client);
            }
        }
    }
}