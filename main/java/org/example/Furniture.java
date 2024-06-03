package org.example;

public class Furniture {
    private String name;
    private double price;
    private double width;
    private double height;

    public Furniture(String name, double price, double width, double height) {
        this.name = name;
        this.price = price;
        this.width = width;
        this.height = height;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }
}
