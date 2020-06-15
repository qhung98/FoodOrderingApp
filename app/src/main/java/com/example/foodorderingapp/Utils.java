package com.example.foodorderingapp;

import android.app.ActivityManager;
import android.content.Context;

import com.example.foodorderingapp.model.Cart;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static String formatPrice(int price) {
        String format = "";
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        format = decimalFormat.format(price);
        return format + "đ";
    }

    public static int calculateSum(ArrayList<Cart> list) {
        int sum = 0;
        for(Cart cart:list){
            sum += cart.getPrice() * cart.getQuantity();
        }
        return sum;
    }

}
