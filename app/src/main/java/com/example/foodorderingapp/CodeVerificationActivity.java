package com.example.foodorderingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.rengwuxian.materialedittext.MaterialEditText;

public class CodeVerificationActivity extends AppCompatActivity {
    MaterialEditText edCode;
    Button btnVerifyCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_verification);

        edCode = findViewById(R.id.edCode);
        btnVerifyCode = findViewById(R.id.btnVerifyCode);

        getSupportActionBar().hide();

        btnVerifyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
