package com.example;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

public class SinglePhotoFetcher {
    public static void download(String urlString, String outputFile) throws IOException {
        long startTime = System.currentTimeMillis();
        URL url = new URL(urlString);
        URLConnection connection = url.openConnection();

        connection.setRequestProperty("User-Agent", "Mozilla/5.0");

        try (InputStream inputStream = connection.getInputStream();
             FileOutputStream outputStream = new FileOutputStream(outputFile)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Downloaded to " + outputFile + " in " + (endTime - startTime) + " ms");
    }
}