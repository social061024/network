package scanner;

import javax.swing.*;
import java.awt.*;

public class ScannerUI extends JFrame {
    private JTextField startIP, endIP;
    private JButton scanButton;
    private JTable resultTable;

    public ScannerUI() {
        setTitle("Network Scanner");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        startIP = new JTextField("192.168.0.1");
        endIP = new JTextField("192.168.0.50");
        scanButton = new JButton("Scan");
        resultTable = new JTable(new ResultTableModel());

        JPanel inputPanel = new JPanel(new GridLayout(1, 3));
        inputPanel.add(startIP);
        inputPanel.add(endIP);
        inputPanel.add(scanButton);

        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(resultTable), BorderLayout.CENTER);

        scanButton.addActionListener(e -> {
            String start = startIP.getText();
            String end = endIP.getText();
            new NetworkScanner((ResultTableModel) resultTable.getModel()).scanRange(start, end);
        });
    }
}