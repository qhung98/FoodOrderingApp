package com.example.foodorderingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodorderingapp.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChangePasswordActivity extends AppCompatActivity {
    EditText edCurrentPassword, edNewPassword, edConfirmPassword;
    Button btnUpdatePassword;
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("User");

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
                String inputPassword = edCurrentPassword.getText().toString();
                String newPassword = edNewPassword.getText().toString();
                String confirmPassword = edConfirmPassword.getText().toString();

                User currentUser = Utils.getCurrentUser(ChangePasswordActivity.this);
                String phone = currentUser.getPhone();
                String password = currentUser.getPassword();
                String name = currentUser.getName();
                String address = currentUser.getAddress();

                if (!inputPassword.equals(password)) {
                    Toast.makeText(getBaseContext(), "Mật khẩu hiện tại không chính xác!", Toast.LENGTH_SHORT).show();
                }
                else if (!confirmPassword.equals(newPassword)) {
                    Toast.makeText(getBaseContext(), "Mật khẩu xác nhận không khớp", Toast.LENGTH_SHORT).show();
                }
                else {
                    dbRef.child(phone).child("password").setValue(newPassword);
                    User editedUser = new User(phone,newPassword,name,address);
                    Utils.setCurrentUser(ChangePasswordActivity.this, editedUser);
                    Toast.makeText(getBaseContext(), "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                }

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
