package com.example.moc2go.database;

public class Order {
    private String name;
    private String price;
    private int quantity;
    //private double price;

    public Order() {

    }

    public Order(String name, String price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public int getQuantity() { return quantity; }
}
