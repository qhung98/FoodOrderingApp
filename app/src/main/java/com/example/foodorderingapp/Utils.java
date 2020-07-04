package com.example.foodorderingapp;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.foodorderingapp.model.Cart;
import com.example.foodorderingapp.model.User;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static final String ACTIVITY_STATE ="activityState";
    public static final String CURRENT_USER = "currentUser";
    public static final String REMEMBER = "rememberUser";

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

    public static void setActivityState(Context context, String key, boolean state){
        SharedPreferences sharedPreferences = context.getSharedPreferences(ACTIVITY_STATE, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(key, state);
        editor.commit();
    }

    public static boolean getActivityState(Context context, String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences(ACTIVITY_STATE, context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, false);
    }

    public static void setCurrentUser(Context context, User currentUser){
        SharedPreferences sharedPreferences = context.getSharedPreferences(CURRENT_USER, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        User user = currentUser;
        Gson gson = new Gson();
        String json = gson.toJson(user);

        editor.putString("user", json);
        editor.commit();
    }

    public static User getCurrentUser(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(CURRENT_USER, context.MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sharedPreferences.getString("user", "");
        User currentUser = gson.fromJson(json, User.class);

        return currentUser;
    }

    public static void setRememberUser(Context context, boolean isRemember){
        SharedPreferences sharedPreferences = context.getSharedPreferences(REMEMBER, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean("remember", isRemember);
        editor.commit();
    }

    public static boolean getRememberUser(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(REMEMBER, context.MODE_PRIVATE);

        return  sharedPreferences.getBoolean("remember", false);
    }
}
