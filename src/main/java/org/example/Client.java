package org.example;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

public class Client extends JFrame {
    private JTextArea chatArea;
    private JTextField inputField;
    private PrintWriter out;

    public Client() {
        setTitle("Client Chat");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        inputField = new JTextField();

        add(new JScrollPane(chatArea), BorderLayout.CENTER);
        add(inputField, BorderLayout.SOUTH);

        inputField.addActionListener(e -> {
            String msg = inputField.getText();
            chatArea.append("Client: " + msg + "\n");
            out.println(msg);
            inputField.setText("");
        });
    }

    public void startClient() throws IOException {
        Socket socket = new Socket("localhost", 5000);

        out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        new Thread(() -> {
            try {
                String line;
                while ((line = in.readLine()) != null) {
                    chatArea.append("Server: " + line + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.setVisible(true);
        client.startClient();
    }
}