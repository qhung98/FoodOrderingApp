package com.example.foodorderingapp.model;

public class Cart {
    private int id;
    private String foodName;
    private int price;
    private int quantity;

    public Cart() {
    }

    public Cart(String foodName, int price, int quantity) {
        this.foodName = foodName;
        this.price = price;
        this.quantity = quantity;
    }

    public Cart(int id, String foodName, int price, int quantity) {
        this.id = id;
        this.foodName = foodName;
        this.price = price;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
