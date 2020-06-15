package com.example.foodorderingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {
    EditText edRegisterPhone, edRegisterPassword, edRegisterConfirmPassword, edRegisterAddress, edRegisterPersonName;
    Button btnRegisterForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edRegisterPhone = findViewById(R.id.edRegisterPhone);
        edRegisterPassword = findViewById(R.id.edLoginPassword);
        edRegisterConfirmPassword = findViewById(R.id.edRegisterConfirmPassword);
        edRegisterAddress = findViewById(R.id.edRegisterAddress);
        edRegisterPersonName = findViewById(R.id.edPersonName);
        btnRegisterForm = findViewById(R.id.btnRegisterForm);

        getSupportActionBar().hide();

        btnRegisterForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
