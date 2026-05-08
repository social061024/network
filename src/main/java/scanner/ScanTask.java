package scanner;

import java.net.InetAddress;

public class ScanTask implements Runnable {
    private String ip;
    private ResultTableModel model;

    public ScanTask(String ip, ResultTableModel model) {
        this.ip = ip;
        this.model = model;
    }

    @Override
    public void run() {
        try {
            InetAddress address = InetAddress.getByName(ip);
            if (address.isReachable(1000)) {
                model.addResult(ip, "Active");
            } else {
                model.addResult(ip, "Inactive");
            }
        } catch (Exception e) {
            model.addResult(ip, "Error");
        }
    }
}