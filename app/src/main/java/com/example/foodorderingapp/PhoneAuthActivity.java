package com.example.foodorderingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.concurrent.TimeUnit;

public class PhoneAuthActivity extends AppCompatActivity {
    MaterialEditText edPhone;
    Button btnSendCode;
    ProgressDialog progressDialog;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("User");
    String phone;
    boolean isRegister, isForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);

        edPhone = findViewById(R.id.edPhone);
        btnSendCode = findViewById(R.id.btnSendCode);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        isRegister = Utils.getActivityState(this, "isRegister");
        isForgotPassword = Utils.getActivityState(this, "isForgotPassword");

        if(isRegister){
            getSupportActionBar().setTitle("ĐĂNG KÝ");
        }
        else {
            getSupportActionBar().setTitle("QUÊN MẬT KHẨU");
        }


        btnSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(PhoneAuthActivity.this);
                progressDialog.setMessage("Loading...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();

                phone = edPhone.getText().toString();

                if(phone.isEmpty()){
                    progressDialog.dismiss();
                    Toast.makeText(getBaseContext(), "Số điện thoại không thể bỏ trống!", Toast.LENGTH_SHORT).show();
                }
                else if(phone.length()>10 || phone.length()<10){
                    progressDialog.dismiss();
                    Toast.makeText(getBaseContext(), "Số điện thoại sai định dạng", Toast.LENGTH_SHORT).show();
                }
                else {
                    dbRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String formatPhone = "+84" + phone.substring(1);
                            if(dataSnapshot.child(phone).exists()){
                                isRegister = Utils.getActivityState(PhoneAuthActivity.this, "isRegister");
                                isForgotPassword = Utils.getActivityState(PhoneAuthActivity.this, "isForgotPassword");
                                if(isRegister){
                                    progressDialog.dismiss();
                                    Toast.makeText(PhoneAuthActivity.this, "Số điện thoại đã tồn tại!", Toast.LENGTH_SHORT).show();
                                }
                                else if (isForgotPassword){
                                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                            formatPhone,
                                            60,
                                            TimeUnit.SECONDS,
                                            PhoneAuthActivity.this,
                                            mCallbacks
                                    );
                                }

                            }
                            else if(isRegister){
                                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                        formatPhone,
                                        60,
                                        TimeUnit.SECONDS,
                                        PhoneAuthActivity.this,
                                        mCallbacks
                                );
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                    Intent intent = new Intent(PhoneAuthActivity.this, CodeVerifyActivity.class);
                    intent.putExtra("code", s);
                    intent.putExtra("phone", phone);
                    progressDialog.dismiss();
                    startActivity(intent);
            }
        };
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Utils.setActivityState(this, "isRegister", false);
        Utils.setActivityState(this, "isForgotPassword", false);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        Utils.setActivityState(this, "isRegister", false);
        Utils.setActivityState(this, "isForgotPassword", false);
        return super.onOptionsItemSelected(item);
    }
}
