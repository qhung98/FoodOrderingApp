package com.example.foodorderingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.rengwuxian.materialedittext.MaterialEditText;

public class PhoneAuthenticationActivity extends AppCompatActivity {
    MaterialEditText edPhone;
    Button btnSendCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_authentication);

        edPhone = findViewById(R.id.edPhone);
        btnSendCode = findViewById(R.id.btnSendCode);

        getSupportActionBar().hide();

        btnSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PhoneAuthenticationActivity.this, CodeVerificationActivity.class));
            }
        });

    }

}
