package com.example.foodorderingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.foodorderingapp.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    EditText edRegisterPassword, edRegisterConfirmPassword, edRegisterAddress, edRegisterPersonName;
    Button btnRegisterForm;
    String phone;

    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("User");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edRegisterPassword = findViewById(R.id.edRegisterPassword);
        edRegisterConfirmPassword = findViewById(R.id.edRegisterConfirmPassword);
        edRegisterAddress = findViewById(R.id.edRegisterAddress);
        edRegisterPersonName = findViewById(R.id.edPersonName);
        btnRegisterForm = findViewById(R.id.btnRegisterForm);

        getSupportActionBar().hide();

        phone = getIntent().getStringExtra("phone");

        Utils.setActivityState(RegisterActivity.this, "isRegister", false);

        btnRegisterForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = edRegisterPassword.getText().toString();
                String confirmPassword = edRegisterConfirmPassword.getText().toString();
                String name = edRegisterPersonName.getText().toString();
                String address = edRegisterAddress.getText().toString();

                if(password.isEmpty() || confirmPassword.isEmpty() || name.isEmpty() || address.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Thông tin tài khoản không thể bỏ trống!", Toast.LENGTH_SHORT).show();
                }
                else if (!password.equals(confirmPassword)) {
                    Toast.makeText(RegisterActivity.this, "Mật khẩu xác nhận không trùng khớp!", Toast.LENGTH_SHORT).show();
                }
                else {
                    User user = new User(phone, password, name, address);
                    dbRef.child(phone).setValue(user);
                    Toast.makeText(RegisterActivity.this, "Đăng ký tài khoản thành công!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });
    }
}
