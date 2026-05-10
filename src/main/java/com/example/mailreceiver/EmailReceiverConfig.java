package com.example.mailreceiver;

import java.util.Properties;

public class EmailReceiverConfig {
    private final String protocol;
    private final String host;
    private final int port;
    private final String username;
    private final String password;
    private final String folderName;
    private final boolean sslEnabled;
    private final boolean startTlsEnabled;
    private final int pollIntervalSeconds;

    public EmailReceiverConfig(String protocol, String host, int port, String username, String password,
                               String folderName, boolean sslEnabled, boolean startTlsEnabled, int pollIntervalSeconds) {
        this.protocol = protocol;
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.folderName = folderName;
        this.sslEnabled = sslEnabled;
        this.startTlsEnabled = startTlsEnabled;
        this.pollIntervalSeconds = pollIntervalSeconds;
    }

    public static EmailReceiverConfig fromProperties(Properties properties) {
        return new EmailReceiverConfig(
                properties.getProperty("mail.protocol", "imaps"),
                properties.getProperty("mail.host", "imap.gmail.com"),
                Integer.parseInt(properties.getProperty("mail.port", "993")),
                properties.getProperty("mail.username", ""), // Буде введено вручну
                properties.getProperty("mail.password", ""), // Буде введено вручну
                properties.getProperty("mail.folder", "INBOX"),
                Boolean.parseBoolean(properties.getProperty("mail.ssl.enable", "true")),
                Boolean.parseBoolean(properties.getProperty("mail.starttls.enable", "false")),
                Integer.parseInt(properties.getProperty("mail.poll.interval.seconds", "30"))
        );
    }

    // Геттери
    public String protocol() { return protocol; }
    public String host() { return host; }
    public int port() { return port; }
    public String username() { return username; }
    public String password() { return password; }
    public String folderName() { return folderName; }
    public boolean sslEnabled() { return sslEnabled; }
    public boolean startTlsEnabled() { return startTlsEnabled; }
    public int pollIntervalSeconds() { return pollIntervalSeconds; }
}