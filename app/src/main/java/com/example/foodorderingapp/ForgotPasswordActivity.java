package com.example.foodorderingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.foodorderingapp.model.Food;
import com.example.foodorderingapp.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ForgotPasswordActivity extends AppCompatActivity {
    EditText edNewPassword, edConfirmPassword;
    Button btnConfirm;

    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("User");
    String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        edNewPassword = findViewById(R.id.edNewPassword);
        edConfirmPassword = findViewById(R.id.edConfirmPassword);
        btnConfirm = findViewById(R.id.btnConfirm);

        phone = this.getIntent().getStringExtra("phone");

        Utils.setActivityState(this, "isForgotPassword", false);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPassword = edNewPassword.getText().toString();
                String confirmPassword = edConfirmPassword.getText().toString();

                if(newPassword.isEmpty() || confirmPassword.isEmpty()){
                    Toast.makeText(ForgotPasswordActivity.this, "Vui lòng điền vào các ô trống!", Toast.LENGTH_SHORT).show();
                }
                else if(!newPassword.equals(confirmPassword)){
                    Toast.makeText(ForgotPasswordActivity.this, "Mật khẩu xác nhận không trùng khớp!", Toast.LENGTH_SHORT).show();
                }
                else {
                    dbRef.child(phone).child("password").setValue(newPassword);
                    Toast.makeText(ForgotPasswordActivity.this, "Đặt mật khẩu thành công!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(ForgotPasswordActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });
    }
}
