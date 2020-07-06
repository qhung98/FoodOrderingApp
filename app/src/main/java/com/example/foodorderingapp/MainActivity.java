package com.example.foodorderingapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button btnLogin, btnRegister;
    CartDatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        db = new CartDatabaseHelper(this);

        getSupportActionBar().hide();

        boolean isRemember = Utils.getRememberUser(this);
        if(isRemember){
            startActivity(new Intent(MainActivity.this, HomeActivity.class));
        }
        else {
            db.deleteCart();
        }

        Utils.setActivityState(this, "isRegister", false);
        Utils.setActivityState(this, "isForgotPassword", false);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.setActivityState(MainActivity.this, "isRegister", true);
                Intent intent = new Intent(MainActivity.this, PhoneAuthActivity.class);
                intent.putExtra("isRegister", true);
                startActivity(intent);
            }
        });
    }

}
