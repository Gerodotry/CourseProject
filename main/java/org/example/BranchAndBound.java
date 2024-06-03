package org.example;

import org.example.Furniture;

import java.sql.*;
import java.util.*;

public class BranchAndBound {
    private double roomArea;
    private double budget;
    private List<Furniture> furnitureList;

    public BranchAndBound(double roomArea, double budget) {
        this.roomArea = roomArea;
        this.budget = budget;
        this.furnitureList = new ArrayList<>();
    }

    public void solve() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/red", "root", "Gerodot5");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM furniture");

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");
                double width = resultSet.getDouble("width");
                double height = resultSet.getDouble("height");

                Furniture furniture = new Furniture(name, price, width, height);
                furnitureList.add(furniture);
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        List<Furniture> selectedFurniture = branchAndBound(new ArrayList<>(), 0, 0, 0);
        if (selectedFurniture != null) {
            System.out.println("Selected furniture:");
            for (Furniture furniture : selectedFurniture) {
                System.out.println(furniture.getName());
            }
        } else {
            System.out.println("No solution found.");
        }
    }

    private List<Furniture> branchAndBound(List<Furniture> selected, double area, double cost, int index) {
        if (area > roomArea || cost > budget) {
            return null;
        }

        if (index == furnitureList.size()) {
            return selected;
        }

        List<Furniture> with = branchAndBound(new ArrayList<>(selected), area + furnitureList.get(index).getWidth(), cost + furnitureList.get(index).getPrice(), index + 1);
        List<Furniture> without = branchAndBound(new ArrayList<>(selected), area, cost, index + 1);

        if (with != null && without != null) {
            if (getTotalPrice(with) > getTotalPrice(without)) {
                return with;
            } else {
                return without;
            }
        } else if (with != null) {
            return with;
        } else {
            return without;
        }
    }

    private double getTotalPrice(List<Furniture> furnitureList) {
        double totalPrice = 0;
        for (Furniture furniture : furnitureList) {
            totalPrice += furniture.getPrice();
        }
        return totalPrice;
    }

    public static void main(String[] args) {
        BranchAndBound solver = new BranchAndBound(0, 0);
        solver.solve();
    }
}

