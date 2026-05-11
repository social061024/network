package org.example;

import java.net.URI;
import java.net.URL;
import java.net.URISyntaxException;
import java.net.MalformedURLException;

public class UrlToUriConverter {
    public static void main(String[] args) {
        String inputUrl = args.length > 0 ? args[0] : "https://example.com/path/to/page";

        try {
            URL url = new URL(inputUrl);

            URI uri = url.toURI();

            System.out.println("Scheme: " + uri.getScheme());
            System.out.println("Host: " + uri.getHost());
            System.out.println("Path: " + uri.getPath());

        } catch (MalformedURLException e) {
            System.out.println("Помилка: Неправильний формат URL.");
        } catch (URISyntaxException e) {
            System.out.println("Помилка: Неможливо конвертувати URL у URI через порушення синтаксису.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}