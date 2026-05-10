package com.prac8_1;

import jakarta.mail.MessagingException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

class Main {
    public static void main(String[] args) {
        try {
            SmtpConfig smtpConfig = new SmtpConfig(
                    getEnvOrThrow("SMTP_HOST"),
                    Integer.parseInt(getEnvOrDefault("SMTP_PORT", "587")),
                    getEnvOrDefault("SMTP_USERNAME", ""),
                    getEnvOrDefault("SMTP_PASSWORD", ""),
                    Boolean.parseBoolean(getEnvOrDefault("SMTP_AUTH", "true")),
                    Boolean.parseBoolean(getEnvOrDefault("SMTP_STARTTLS", "true")),
                    Boolean.parseBoolean(getEnvOrDefault("SMTP_SSL", "false")));

            // Тут ми отримуємо шлях до картинки
            List<String> recipients = splitCsv(getEnvOrThrow("MAIL_TO"));
            List<String> attachments = splitCsv(getEnvOrDefault("MAIL_ATTACHMENTS", ""));

            EmailMessageConfig messageConfig = new EmailMessageConfig(
                    getEnvOrThrow("MAIL_FROM"),
                    recipients,
                    getEnvOrDefault("MAIL_SUBJECT", "Practical #8.1 - HTML email with Image"),
                    getEnvOrDefault("MAIL_HTML", "<h1>Hello!</h1><p>Це лист із зображенням.</p>"),
                    attachments);

            System.out.println("Sending email to: " + recipients);
            System.out.println("Attachments to send: " + attachments); // Перевірка в консолі

            new EmailSender().sendHtmlEmailWithAttachments(smtpConfig, messageConfig);
            System.out.println("E-mail sent successfully.");

        } catch (MessagingException e) {
            System.err.println("Failed to send e-mail: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Configuration error: " + e.getMessage());
        }
    }

    private static String getEnvOrThrow(String key) {
        String value = System.getenv(key);
        if (value == null || value.trim().isEmpty()) {
            switch (key) {
                case "SMTP_HOST": return "smtp.gmail.com";
                case "MAIL_FROM": return "pp002831@gmail.com";
                case "MAIL_TO":   return "polinapakhomova0507@gmail.com";
                default:
                    throw new IllegalArgumentException("Missing required environment variable: " + key);
            }
        }
        return value;
    }

    private static String getEnvOrDefault(String key, String defaultValue) {
        String value = System.getenv(key);
        if (value == null || value.trim().isEmpty()) {
            // ПЕРЕНЕСІТЬ УСІ НАЛАШТУВАННЯ СЮДИ
            if (key.equals("SMTP_USERNAME")) return "pp002831@gmail.com";
            if (key.equals("SMTP_PASSWORD")) return "ydpb tlie ogmk oobn";

            // ТЕПЕР ПРОГРАМА ПОБАЧИТЬ ШЛЯХ:
            if (key.equals("MAIL_ATTACHMENTS")) return "C:/Users/User/Desktop/Networking/Practic/Practic_8_1/Practic_8_1/files/imagen.png";
            return defaultValue;
        }
        return value;
    }

    private static List<String> splitCsv(String input) {
        if (input == null || input.trim().isEmpty()) return Collections.emptyList();
        return Arrays.stream(input.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }
}