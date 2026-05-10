package org.example;

import java.io.IOException;
import java.net.*;

public class ClientSession implements Runnable {
    private final int id;
    private final ClientResultTableModel tableModel;

    public ClientSession(int id, ClientResultTableModel tableModel) {
        this.id = id;
        this.tableModel = tableModel;
    }

    @Override
    public void run() {
        try (DatagramSocket socket = new DatagramSocket()) {
            InetAddress serverAddr = InetAddress.getByName("localhost");

            // Реєстрація клієнта на сервері
            byte[] regMsg = "REG".getBytes();
            DatagramPacket regPacket = new DatagramPacket(regMsg, regMsg.length, serverAddr, 5000);
            socket.send(regPacket);

            tableModel.addResult(id, "UDP READY", "Registered at server, waiting...");

            byte[] buffer = new byte[2048];
            while (true) {
                // Очікування пакетів від сервера
                DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
                socket.receive(receivePacket);

                String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
                tableModel.addResult(id, "UPDATE", message);
            }
        } catch (IOException e) {
            tableModel.addResult(id, "ERROR", e.getMessage());
        }
    }
}