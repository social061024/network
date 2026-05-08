package scanner;

import javax.swing.table.DefaultTableModel;

public class ResultTableModel extends DefaultTableModel {
    public ResultTableModel() {
        super(new String[]{"IP Address", "Status"}, 0);
    }

    public void addResult(String ip, String status) {
        addRow(new Object[]{ip, status});
    }
}