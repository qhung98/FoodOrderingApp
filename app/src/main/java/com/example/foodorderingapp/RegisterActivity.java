package com.example.foodorderingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.foodorderingapp.model.User;
import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {
    EditText edRegisterPhone, edRegisterPassword, edRegisterConfirmPassword, edRegisterAddress, edRegisterPersonName;
    Button btnRegisterForm;
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("User");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edRegisterPhone = findViewById(R.id.edRegisterPhone);
        edRegisterPassword = findViewById(R.id.edRegisterPassword);
        edRegisterConfirmPassword = findViewById(R.id.edRegisterConfirmPassword);
        edRegisterAddress = findViewById(R.id.edRegisterAddress);
        edRegisterPersonName = findViewById(R.id.edPersonName);
        btnRegisterForm = findViewById(R.id.btnRegisterForm);

        getSupportActionBar().hide();

        btnRegisterForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child(edRegisterPhone.getText().toString()).exists()){
                            Toast.makeText(RegisterActivity.this, "SỐ ĐIỆN THOẠI ĐÃ TỒN TẠI!", Toast.LENGTH_SHORT).show();
                        }
                        else if(!edRegisterPassword.getText().toString().equals(edRegisterConfirmPassword.getText().toString())){
                            Toast.makeText(RegisterActivity.this, "MẬT KHẨU KHÔNG TRÙNG KHỚP!", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            User user = new User(edRegisterPhone.getText().toString(), edRegisterPassword.getText().toString(), edRegisterPersonName.getText().toString(), edRegisterAddress.getText().toString());

                            dbRef.child(edRegisterPhone.getText().toString()).setValue(user);
                            Toast.makeText(RegisterActivity.this, "ĐĂNG KÝ THÀNH CÔNG!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
