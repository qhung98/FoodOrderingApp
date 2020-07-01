package com.example.foodorderingapp.notification;

public class Token {
    private String token;
    private boolean server;

    public Token() {
    }

    public Token(String token, boolean server) {
        this.token = token;
        this.server = server;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isServer() {
        return server;
    }

    public void setServer(boolean server) {
        this.server = server;
    }
}
