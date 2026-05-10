package com.example.mailreceiver;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import com.sun.mail.imap.IMAPFolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class EmailReceiverApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailReceiverApplication.class);
    private static final String CONFIG_FILE = "email.properties";
    private final EmailReceiverConfig config;
    private final Set<String> seenFallbackKeys = new HashSet<>();

    public EmailReceiverApplication(EmailReceiverConfig config) {
        this.config = config;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Авторизація у поштовій системі ===");
        System.out.print("Введіть ваш Email: ");
        String inputEmail = scanner.nextLine();

        System.out.print("Введіть Пароль додатка (16 символів): ");
        String inputPassword = scanner.nextLine();

        try {
            // Завантажуємо базові налаштування з файлу
            EmailReceiverConfig fileConfig = loadConfig(CONFIG_FILE);

            // Створюємо конфігурацію з введеними вручну даними
            EmailReceiverConfig finalConfig = new EmailReceiverConfig(
                    fileConfig.protocol(),
                    fileConfig.host(),
                    fileConfig.port(),
                    inputEmail,
                    inputPassword,
                    fileConfig.folderName(),
                    fileConfig.sslEnabled(),
                    fileConfig.startTlsEnabled(),
                    fileConfig.pollIntervalSeconds()
            );

            EmailReceiverApplication app = new EmailReceiverApplication(finalConfig);
            app.start();
        } catch (Exception e) {
            LOGGER.error("Помилка при запуску: {}", e.getMessage(), e);
        }
    }

    private static EmailReceiverConfig loadConfig(String configPath) throws IOException {
        Path path = Paths.get(configPath);
        if (!Files.exists(path)) {
            throw new IOException("Файл конфігурації не знайдено: " + configPath);
        }
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream(configPath)) {
            properties.load(input);
        }
        return EmailReceiverConfig.fromProperties(properties);
    }

    public void start() {
        LOGGER.info("Запуск приймача. Інтервал перевірки: {} сек.", config.pollIntervalSeconds());
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(this::safePollInbox, 0, config.pollIntervalSeconds(), TimeUnit.SECONDS);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOGGER.info("Зупинка програми...");
            executor.shutdown();
            try {
                if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                executor.shutdownNow();
            }
        }));
    }

    private void safePollInbox() {
        try {
            pollInbox();
        } catch (Exception e) {
            LOGGER.error("Помилка під час перевірки пошти: {}", e.getMessage());
        }
    }

    private void pollInbox() throws MessagingException {
        Properties sessionProperties = new Properties();
        sessionProperties.put("mail.store.protocol", config.protocol());
        sessionProperties.put("mail." + config.protocol() + ".host", config.host());
        sessionProperties.put("mail." + config.protocol() + ".port", String.valueOf(config.port()));
        sessionProperties.put("mail." + config.protocol() + ".ssl.enable", String.valueOf(config.sslEnabled()));
        sessionProperties.put("mail." + config.protocol() + ".starttls.enable", String.valueOf(config.startTlsEnabled()));

        Session session = Session.getInstance(sessionProperties);

        // ВАЖЛИВО: Store не підтримує try-with-resources в цій версії
        Store store = session.getStore(config.protocol());
        try {
            store.connect(config.host(), config.port(), config.username(), config.password());
            Folder folder = store.getFolder(config.folderName());

            if (folder != null) {
                folder.open(Folder.READ_ONLY);
                try {
                    Message[] messages = folder.getMessages();
                    for (Message message : messages) {
                        if (isNewMessage(folder, message)) {
                            logMessage(message);
                        }
                    }
                } finally {
                    if (folder.isOpen()) {
                        folder.close(false);
                    }
                }
            }
        } finally {
            if (store != null) {
                store.close();
            }
        }
    }

    private boolean isNewMessage(Folder folder, Message message) throws MessagingException {
        if (folder instanceof IMAPFolder) {
            IMAPFolder imapFolder = (IMAPFolder) folder;
            long uid = imapFolder.getUID(message);
            return seenFallbackKeys.add("UID:" + uid);
        }
        String fallbackKey = "MSG:" + message.getMessageNumber() +
                "|DATE:" + (message.getSentDate() != null ? message.getSentDate().getTime() : 0L);
        return seenFallbackKeys.add(fallbackKey);
    }

    private void logMessage(Message message) throws MessagingException {
        String sender = "Невідомо";
        if (message.getFrom() != null && message.getFrom().length > 0) {
            if (message.getFrom()[0] instanceof InternetAddress) {
                sender = ((InternetAddress) message.getFrom()[0]).getAddress();
            }
        }
        String subject = message.getSubject() != null ? message.getSubject() : "(без теми)";
        LOGGER.info("Новий лист -> Від: {} | Тема: {}", sender, subject);
    }
}