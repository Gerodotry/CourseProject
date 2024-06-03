package org.example;

import spark.Spark;

import static spark.Spark.*;

public class Main {
    public static void main(String[] args) {
        port(4567); // Порт для запуску веб-додатку

        // Відповідаємо на GET-запит на кореневий URL
        get("/", (req, res) -> {
            return "Hello SparkJava!";
        });

        // Відповідаємо на GET-запит на URL для файлу An_init.html
        get("/An_init.html", (req, res) -> {
            res.type("text/html"); // Встановлюємо тип контенту як HTML
            return Spark.class.getResourceAsStream("/An_init.html");
        });
    }
}
