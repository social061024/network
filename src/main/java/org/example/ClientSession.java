package org.example;

import java.io.*;
import java.net.*;
import java.util.Random;

public class ClientSession implements Runnable {
    private int id;
    private ClientResultTableModel model;
    private static final String HOST = "localhost";
    private static final int PORT = 5000;

    public ClientSession(int id, ClientResultTableModel model) {
        this.id = id;
        this.model = model;
    }

    @Override
    public void run() {
        try (Socket socket = new Socket(HOST, PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            String randomString = generateRandomString(10);
            out.println(randomString);

            String response = in.readLine();
            model.addResult(id, randomString, response);

        } catch (IOException e) {
            model.addResult(id, "Error", e.getMessage());
        }
    }

    private String generateRandomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
