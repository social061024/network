package org.example;

import javax.swing.*;
import java.awt.*;

public class ClientUI extends JFrame {
    private JTextField sessionCountField;
    private JButton startButton;
    private JTable resultTable;

    public ClientUI() {
        setTitle("Multithreaded Client");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        sessionCountField = new JTextField("1000");
        startButton = new JButton("Start");
        resultTable = new JTable(new ClientResultTableModel());

        JPanel topPanel = new JPanel(new GridLayout(1, 2));
        topPanel.add(sessionCountField);
        topPanel.add(startButton);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(resultTable), BorderLayout.CENTER);

        startButton.addActionListener(e -> {
            int sessions = Integer.parseInt(sessionCountField.getText());
            for (int i = 0; i < sessions; i++) {
                new Thread(new ClientSession(i, (ClientResultTableModel) resultTable.getModel())).start();
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ClientUI().setVisible(true));
    }
}
