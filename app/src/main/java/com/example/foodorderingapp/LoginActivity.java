package com.example.foodorderingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity{
    EditText edLoginPhone, edLoginPassword;
    CheckBox ckbRemember;
    Button btnLoginForm;
    TextView tvForgotPassword;
    ProgressDialog progressDialog;

    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("User");
    String phone, password;


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
                progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setMessage("Loading...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();

                phone = edLoginPhone.getText().toString();
                password = edLoginPassword.getText().toString();

                if(phone.isEmpty() || password.isEmpty()){
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Vui lòng điền số điện thoại và mật khẩu!", Toast.LENGTH_SHORT).show();
                }
                else {
                    dbRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child(phone).exists()) {
                                User user = dataSnapshot.child(phone).getValue(User.class);
                                if (password.equals(user.getPassword())) {
                                    Utils.setCurrentUser(LoginActivity.this, user);

                                    if (ckbRemember.isChecked()) {
                                        Utils.setRememberUser(LoginActivity.this, true);
                                    }

                                    progressDialog.dismiss();
                                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(LoginActivity.this, "Sai mật khẩu!", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "Số điện thoại không tồn tại!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.setActivityState(LoginActivity.this, "isForgotPassword", true);
                startActivity(new Intent(LoginActivity.this, PhoneAuthActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}
