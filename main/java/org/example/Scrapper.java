package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Scrapper {

    public static void main(String[] args) {
        String[] urls = {
                "https://epicentrk.ua/ua/shop/kuhonnye-stulya/",
                "https://epicentrk.ua/ua/shop/divany/",
                "https://epicentrk.ua/ua/shop/krovati/",
                "https://epicentrk.ua/ua/shop/mebel-dlya-spalni/",


        };

        String databaseURL = "jdbc:mysql://localhost:3306/red";
        String username = "root";
        String password = "Gerodot5";

        try {
            Connection connection = DriverManager.getConnection(databaseURL, username, password);

            for (String url : urls) {
                Document document = Jsoup.connect(url).get();
                Elements items = document.select("a._Spiue2"); // змініть селектори CSS відповідно до вашого сайту

                for (Element item : items) {
                    String productUrl = item.attr("href");
                    scrapeProduct(productUrl, connection);
                }
            }

            connection.close();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private static void scrapeProduct(String productUrl, Connection connection) throws IOException, SQLException {
        Document productDocument = Jsoup.connect("https://epicentrk.ua" + productUrl).get();
        String price = productDocument.select("div._tqVytn").attr("title").replace("ціна: ", "").replace(" ₴", "").trim();
        String width = productDocument.select("li._kdIOU1 span:contains(Ширина)").parents().select("span").text().replace("Ширина:", "").trim();
        String height = productDocument.select("li._kdIOU1 span:contains(Висота)").parents().select("span").text().replace("Висота:", "").trim();

        saveToDatabase(price, width, height, connection);
    }

    private static void saveToDatabase(String price, String width, String height, Connection connection) throws SQLException {
        String sql = "INSERT INTO Furniture (cost, ширина, висота) VALUES (?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, price);
        statement.setString(2, width);
        statement.setString(3, height);
        statement.executeUpdate();
        statement.close();
    }
}
