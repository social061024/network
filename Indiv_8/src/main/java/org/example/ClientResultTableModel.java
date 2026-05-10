package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ClientResultTableModel extends DefaultTableModel {
    public ClientResultTableModel() {
        addColumn("Session ID");
        addColumn("Request");
        addColumn("Response");
    }

    public void addResult(int id, String request, String response) {
        SwingUtilities.invokeLater(() -> addRow(new Object[]{id, request, response}));
    }

    @Override
    public boolean isCellEditable(int row, int column) {

        return false;
    }
}
