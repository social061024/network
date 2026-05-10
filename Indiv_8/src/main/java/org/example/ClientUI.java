package org.example;

import javax.swing.*;
import java.awt.*;

public class ClientUI extends JFrame {
    private JTextField sessionsField;
    private JButton startButton;
    private JTable resultTable;
    private ClientResultTableModel tableModel;

    public ClientUI() {
        setTitle("UDP Directory Monitor Client");
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        sessionsField = new JTextField("10", 10);
        startButton = new JButton("Start Monitoring");
        tableModel = new ClientResultTableModel();
        resultTable = new JTable(tableModel);

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Sessions:"));
        topPanel.add(sessionsField);
        topPanel.add(startButton);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(resultTable), BorderLayout.CENTER);

        startButton.addActionListener(e -> {
            try {
                int count = Integer.parseInt(sessionsField.getText().trim());
                tableModel.setRowCount(0);
                for (int i = 0; i < count; i++) {
                    new Thread(new ClientSession(i, tableModel)).start();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number");
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ClientUI().setVisible(true);
        });
    }
}