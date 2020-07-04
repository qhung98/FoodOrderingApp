package com.example.foodorderingapp.model;

public class User {
    private String phone;
    private String password;
    private String name;
    private String address;

    public User() {
    }

    public User(String phone, String password, String name, String address) {
        this.phone = phone;
        this.password = password;
        this.name = name;
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
