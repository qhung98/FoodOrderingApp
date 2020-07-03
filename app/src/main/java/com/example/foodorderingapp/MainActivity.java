package com.example.foodorderingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button btnLogin, btnRegister;

    public static final String REMEMBER_USER = "rememberUser";
    String phone, password, name, address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        getSupportActionBar().hide();

//        SharedPreferences sharedPreferences = getSharedPreferences(REMEMBER_USER, MODE_PRIVATE);
//        phone = sharedPreferences.getString(phone, "");
//        password = sharedPreferences.getString(phone, "");
//        name = sharedPreferences.getString(name, "");
//        address = sharedPreferences.getString(address, "");
//
//        if(!phone.equals("")){
//            startActivity(new Intent(MainActivity.this, HomeActivity.class));
//        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });
    }
}
