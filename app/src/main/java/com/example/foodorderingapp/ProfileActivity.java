package com.example.foodorderingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.foodorderingapp.model.User;

public class ProfileActivity extends AppCompatActivity {
    TextView tvProfileName, tvUserName, tvUserPhone, tvUserAddress;
    Button btnChangePassword, btnChangeProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tvProfileName = findViewById(R.id.tvProfileName);
        tvUserName = findViewById(R.id.tvUserName);
        tvUserPhone = findViewById(R.id.tvUserPhone);
        tvUserAddress = findViewById(R.id.tvUserAddress);
        btnChangeProfile = findViewById(R.id.btnChangeProfile);
        btnChangePassword = findViewById(R.id.btnChangePassword);

        getSupportActionBar().setTitle("TÀI KHOẢN");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        User user = Utils.getCurrentUser(this);

        tvProfileName.setText(user.getName());
        tvUserName.setText(user.getName());
        tvUserPhone.setText(String.valueOf(user.getPhone()));
        tvUserAddress.setText(user.getAddress());

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(ProfileActivity.this, ChangePasswordActivity.class), 1);
            }
        });

        btnChangeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(ProfileActivity.this, ChangeProfileActivity.class), 2);
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK){
            startActivity(new Intent(this, ProfileActivity.class));
            this.finish();
        }

        if(requestCode == 2 && resultCode == RESULT_OK){
            startActivity(new Intent(this, ProfileActivity.class));
            this.finish();
        }
    }
}