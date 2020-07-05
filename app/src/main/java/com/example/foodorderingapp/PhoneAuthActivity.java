package com.example.foodorderingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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

        getSupportActionBar().hide();

//        isRegister = this.getIntent().getBooleanExtra("isRegister", false);
        isRegister = Utils.getActivityState(this, "isRegister");
        isForgotPassword = Utils.getActivityState(this, "isForgotPassword");


        btnSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone = edPhone.getText().toString();

                if(phone.isEmpty()){
                    Toast.makeText(getBaseContext(), "Số điện thoại không thể bỏ trống!", Toast.LENGTH_SHORT).show();
                }
                else if(phone.length()>10 || phone.length()<10){
                    Toast.makeText(getBaseContext(), "Số điện thoại sai định dạng", Toast.LENGTH_SHORT).show();
                }
                else {
                    dbRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String formatPhone = "+84" + phone.substring(1);
                            if(dataSnapshot.child(phone).exists()){
                                isRegister = Utils.getActivityState(PhoneAuthActivity.this, "isRegister");
                                if(isRegister){
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
                            else {
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
                    if(isRegister){
                        intent.putExtra("isRegister", true);
                    }
                    startActivity(intent);
            }
        };
    }

}
