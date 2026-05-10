package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ClientResultTableModel extends DefaultTableModel {
    public ClientResultTableModel() {
        super(new String[]{"Session", "Random String", "Server Response"}, 0);
    }

    public void addResult(int id, String randomString, String response) {
        SwingUtilities.invokeLater(() -> {
            addRow(new Object[]{id, randomString, response});
        });
    }
}
