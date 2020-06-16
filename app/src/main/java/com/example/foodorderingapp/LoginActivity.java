package com.example.foodorderingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    EditText edLoginPhone, edLoginPassword;
    CheckBox ckbRemember;
    Button btnLoginForm;
    TextView tvForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edLoginPhone = findViewById(R.id.edLoginPhone);
        edLoginPassword = findViewById(R.id.edLoginPassword);
        ckbRemember = findViewById(R.id.ckbRemember);
        btnLoginForm = findViewById(R.id.btnLoginForm);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);

        getSupportActionBar().hide();

        btnLoginForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                Toast.makeText(getApplicationContext(), "LoginHUIDĐ", Toast.LENGTH_LONG).show();
            }
        });

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "Forgot_Password", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
