package com.example.foodorderingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class LoginActivity extends AppCompatActivity {
    EditText edLoginPhone, edLoginPassword;
    CheckBox ckbRemember;
    Button btnLoginForm;
    TextView tvForgotPassword;
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("User");
    public static final String REMEMBER_USER = "rememberUser";
    String phone, password, name, address;


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

//        SharedPreferences sharedPreferences = getSharedPreferences(REMEMBER_USER, MODE_PRIVATE);
//        phone = sharedPreferences.getString(phone, "");
//        password = sharedPreferences.getString(phone, "");
//        name = sharedPreferences.getString(name, "");
//        address = sharedPreferences.getString(address, "");

//        if(!phone.equals("")){
//            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
//        }

        btnLoginForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(edLoginPhone.getText().toString().isEmpty() || edLoginPassword.getText().toString().isEmpty()){
                            Toast.makeText(getBaseContext(), "Vui lòng nhập sđt, mật khẩu", Toast.LENGTH_SHORT).show();
                        }
                        if (dataSnapshot.child(edLoginPhone.getText().toString()).exists()) {
                            User user = dataSnapshot.child(edLoginPhone.getText().toString()).getValue(User.class);
                            phone = user.getPhone();
                            password = user.getPassword();

                            if (user.getPassword().equals(edLoginPassword.getText().toString())) {
                                Toast.makeText(LoginActivity.this, "Login successfully", Toast.LENGTH_SHORT).show();

                                name = user.getName();
                                address = user.getAddress();

                                if(ckbRemember.isChecked()){
                                    saveUser();
                                }

                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));

                            } else
                                Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "User not exsits", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    private void saveUser() {
        SharedPreferences sharedPreferences = getSharedPreferences(REMEMBER_USER, MODE_PRIVATE);
        SharedPreferences.Editor editor =sharedPreferences.edit();

        editor.putString("phone",phone);
        editor.putString("password", password);
        editor.putString("name", name);
        editor.putString("address", address);

        Toast.makeText(this, "SAVED", Toast.LENGTH_SHORT).show();
    }
    private void loadUser(){
        SharedPreferences sharedPreferences = getSharedPreferences(REMEMBER_USER, MODE_PRIVATE);
        phone = sharedPreferences.getString(phone, "");
        password = sharedPreferences.getString(phone, "");
        name = sharedPreferences.getString(name, "");
        address = sharedPreferences.getString(address, "");
    }
}
