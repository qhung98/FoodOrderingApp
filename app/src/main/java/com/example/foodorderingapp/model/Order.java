package com.example.foodorderingapp.model;

import java.util.ArrayList;

public class Order {
    private String id;
    private String name;
    private String phone;
    private String address;
    private String time;
    private String sum;
    private ArrayList<Cart> listFood;
    private int status;

    public Order() {
    }

    public Order(String name, String phone, String address, String time, String sum, ArrayList<Cart> listFood, int status) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.time = time;
        this.sum = sum;
        this.listFood = listFood;
        this.status = status;
    }

    public Order(String id, String name, String phone, String address, String time, String sum, ArrayList<Cart> listFood, int status) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.time = time;
        this.sum = sum;
        this.listFood = listFood;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public ArrayList<Cart> getListFood() {
        return listFood;
    }

    public void setListFood(ArrayList<Cart> listFood) {
        this.listFood = listFood;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
