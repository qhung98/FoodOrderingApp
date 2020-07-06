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
    ProgressDialog progressDialog;

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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        codeSent = getIntent().getStringExtra("code");
        phone = getIntent().getStringExtra("phone");

        isRegister = Utils.getActivityState(this, "isRegister");

        if(isRegister){
            getSupportActionBar().setTitle("ĐĂNG KÝ");
        }
        else {
            getSupportActionBar().setTitle("QUÊN MẬT KHẨU");
        }

        btnVerifyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(CodeVerifyActivity.this);
                progressDialog.setMessage("Loading...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();

                String code = edCode.getText().toString();

                if(code.isEmpty()){
                    progressDialog.dismiss();
                    Toast.makeText(CodeVerifyActivity.this, "Mã xác minh không thể bỏ trống!", Toast.LENGTH_SHORT).show();
                }
                else if(code.length()>6 || code.length()<6) {
                    progressDialog.dismiss();
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
                                progressDialog.dismiss();
                                startActivity(intent);
                                finish();
                            }
                            else {
                                Intent intent = new Intent(CodeVerifyActivity.this, ForgotPasswordActivity.class);
                                intent.putExtra("phone", phone);
                                progressDialog.dismiss();
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
