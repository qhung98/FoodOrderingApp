package com.example.foodorderingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.foodorderingapp.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChangeProfileActivity extends AppCompatActivity {
    EditText edNewName, edNewAddress;
    Button btnUpdateProfile;
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("User");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_profile);

        edNewName = findViewById(R.id.edNewName);
        edNewAddress = findViewById(R.id.edNewAddress);
        btnUpdateProfile = findViewById(R.id.btnUpdateProfile);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("KFC");

        final User currentUser = Utils.getCurrentUser(this);
        edNewName.setText(currentUser.getName());
        edNewAddress.setText(currentUser.getAddress());

        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = edNewName.getText().toString();
                String newAddress = edNewAddress.getText().toString();

                if (newName.isEmpty() || newAddress.isEmpty()){
                    Toast.makeText(ChangeProfileActivity.this, "Xin điền thông tin vào các ô trống!", Toast.LENGTH_SHORT).show();
                }
                else {
                    String password = currentUser.getPassword();
                    String phone = currentUser.getPhone();

                    dbRef.child(phone).child("name").setValue(newName);
                    dbRef.child(phone).child("address").setValue(newAddress);

                    User editedUser = new User(phone, password, newName, newAddress);
                    Utils.setCurrentUser(ChangeProfileActivity.this, editedUser);

                    HomeActivity.updateDrawerMenu(editedUser);

                    Toast.makeText(getBaseContext(), "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
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
