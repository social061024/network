package scanner;

public class NetworkScanner {
    private ResultTableModel model;

    public NetworkScanner(ResultTableModel model) {
        this.model = model;
    }

    public void scanRange(String startIP, String endIP) {
        String[] startParts = startIP.split("\\.");
        String[] endParts = endIP.split("\\.");
        int start = Integer.parseInt(startParts[3]);
        int end = Integer.parseInt(endParts[3]);

        String base = startParts[0] + "." + startParts[1] + "." + startParts[2] + ".";

        for (int i = start; i <= end; i++) {
            String ip = base + i;
            new Thread(new ScanTask(ip, model)).start();
        }
    }
}