package com.example.foodorderingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.rengwuxian.materialedittext.MaterialEditText;

public class CodeVerifyActivity extends AppCompatActivity {
    MaterialEditText edCode;
    Button btnVerifyCode;
    String codeSent;
    FirebaseAuth mAuth;
    String phone;
    boolean isRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_verify);

        edCode = findViewById(R.id.edCode);
        btnVerifyCode = findViewById(R.id.btnVerifyCode);
        mAuth = FirebaseAuth.getInstance();

        getSupportActionBar().hide();

        codeSent = getIntent().getStringExtra("code");
        phone = getIntent().getStringExtra("phone");
//        isRegister = getIntent().getBooleanExtra("isRegister", false);
        isRegister = Utils.getActivityState(this, "isRegister");

        btnVerifyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = edCode.getText().toString();

                if(code.isEmpty()){
                    Toast.makeText(CodeVerifyActivity.this, "Mã xác minh không thể bỏ trống!", Toast.LENGTH_SHORT).show();
                }
                else if(code.length()>6 || code.length()<6) {
                    Toast.makeText(CodeVerifyActivity.this, "Mã xác minh sai định dạng!", Toast.LENGTH_SHORT).show();
                }
                else {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, code);
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(CodeVerifyActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(CodeVerifyActivity.this, "Xác thực thành công!", Toast.LENGTH_SHORT).show();
                            if(isRegister){
                                Intent intent = new Intent(CodeVerifyActivity.this, RegisterActivity.class);
                                intent.putExtra("phone", phone);
                                startActivity(intent);
                                finish();
                            }
                            else {
                                Intent intent = new Intent(CodeVerifyActivity.this, ForgotPasswordActivity.class);
                                intent.putExtra("phone", phone);
                                startActivity(intent);
                                finish();
                            }
                        }
                        else {
                            Toast.makeText(CodeVerifyActivity.this, "Mã xác thực không chính xác!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
