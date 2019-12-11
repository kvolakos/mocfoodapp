package com.example.moc2go.database;

public class Item {
    private String name;
    private String price;
    //private double price;

    public Item() {

    }

    public Item(String name, String price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }
}
