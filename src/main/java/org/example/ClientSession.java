package org.example;

import java.io.*;
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
        try (Socket socket = new Socket("localhost", 5000);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            tableModel.addResult(id, "SYSTEM", "Connected to server!");

            String message;
            while ((message = in.readLine()) != null) {
                tableModel.addResult(id, "UPDATE", message);
            }
        } catch (IOException e) {
            tableModel.addResult(id, "ERROR", e.getMessage());
        }
    }
}