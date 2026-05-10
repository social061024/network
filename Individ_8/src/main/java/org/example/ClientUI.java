package org.example;

import javax.swing.*;
import java.awt.*;

public class ClientUI extends JFrame {
    private JTextField sessionsField;
    private JButton startButton;
    private JTable resultTable;
    private ClientResultTableModel tableModel;

    public ClientUI() {
        setTitle("Load Test Client");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        sessionsField = new JTextField("100", 10);
        startButton = new JButton("Start");
        tableModel = new ClientResultTableModel();
        resultTable = new JTable(tableModel);

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Sessions:"));
        topPanel.add(sessionsField);
        topPanel.add(startButton);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(resultTable), BorderLayout.CENTER);

        startButton.addActionListener(e -> {
            int count = Integer.parseInt(sessionsField.getText());
            tableModel.setRowCount(0);
            for (int i = 0; i < count; i++) {
                new Thread(new ClientSession(i, tableModel)).start();
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ClientUI ui = new ClientUI();
            ui.setVisible(true);
        });

    }
}