package org.example;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server {

    private static final List<PrintWriter> clientOutputs = new CopyOnWriteArrayList<>();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(5000);
        System.out.println("Сервер запущено на порту 5000...");

        String pathStr = "./monitored_dir";
        Path path = Paths.get(pathStr);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        new Thread(() -> monitorDirectory(pathStr)).start();

        while (true) {
            Socket s = serverSocket.accept();
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);
            clientOutputs.add(out);
            System.out.println("Клієнт підключився.");
        }
    }

    private static void monitorDirectory(String dirPath) {
        try (WatchService watcher = FileSystems.getDefault().newWatchService()) {
            Path path = Paths.get(dirPath);
            path.register(watcher,
                    StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_MODIFY,
                    StandardWatchEventKinds.ENTRY_DELETE);

            while (true) {
                WatchKey key = watcher.take();
                for (WatchEvent<?> event : key.pollEvents()) {
                    String msg = "Подія: " + event.kind() + " Файл: " + event.context();
                    // Розсилка
                    for (PrintWriter client : clientOutputs) {
                        client.println(msg);
                    }
                }
                key.reset();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}