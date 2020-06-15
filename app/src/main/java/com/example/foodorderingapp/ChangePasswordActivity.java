package com.example.foodorderingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ChangePasswordActivity extends AppCompatActivity {
    EditText edCurrentPassword, edNewPassword, edConfirmPassword;
    Button btnUpdatePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        edCurrentPassword = findViewById(R.id.edCurrentPassword);
        edNewPassword = findViewById(R.id.edNewPassword);
        edConfirmPassword = findViewById(R.id.edConfirmPassword);
        btnUpdatePassword = findViewById(R.id.btnUpdatePassword);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("KFC");

        btnUpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
