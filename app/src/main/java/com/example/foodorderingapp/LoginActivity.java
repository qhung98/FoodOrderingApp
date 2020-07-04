package com.example.foodorderingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodorderingapp.model.User;

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
                User user = new User("0987654321", "asdf", "Nguyễn Văn Hoàng", "275 NGUYỄN TRÃI, THANH XUÂN, HÀ NỘI");
                Utils.setCurrentUser(LoginActivity.this, user);

                if(ckbRemember.isChecked()){
                    Utils.setRememberUser(LoginActivity.this, true);
                }

                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            }
        });

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, PhoneAuthenticationActivity.class));
            }
        });
    }


}
