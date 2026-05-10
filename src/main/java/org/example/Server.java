package org.example;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

public class Server extends JFrame {
    private JTextArea chatArea;
    private JTextField inputField;
    private PrintWriter out;

    public Server() {
        setTitle("Server Chat");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        inputField = new JTextField();

        add(new JScrollPane(chatArea), BorderLayout.CENTER);
        add(inputField, BorderLayout.SOUTH);

        inputField.addActionListener(e -> {
            String msg = inputField.getText();
            chatArea.append("Server: " + msg + "\n");
            out.println(msg);
            inputField.setText("");
        });
    }

    public void startServer() throws IOException {
        ServerSocket serverSocket = new ServerSocket(5000);
        Socket socket = serverSocket.accept();

        out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        new Thread(() -> {
            try {
                String line;
                while ((line = in.readLine()) != null) {
                    chatArea.append("Client: " + line + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.setVisible(true);
        server.startServer();
    }
}