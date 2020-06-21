package com.example.foodorderingapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodorderingapp.adapter.OrderAdapter;
import com.example.foodorderingapp.model.Cart;
import com.example.foodorderingapp.model.Order;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class OrderActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    TextView tvOrderUserName, tvOrderPhone, tvOrderTime, tvOrderCartSum, tvOrderSum;
    RadioButton rdHomeAddress, rdNewAddress;
    ImageView ivEditAddress;
    RecyclerView listOrder;
    Button btnConfirmOrder;

    OrderAdapter orderAdapter;
    ArrayList<Cart> list;

    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Order");
    CartDatabaseHelper db;

    String homeAddress = "275 NGUYỄN TRÃI, THANH XUÂN, HÀ NỘI";
    String newAddress = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        tvOrderUserName = findViewById(R.id.tvOrderUserName);
        tvOrderPhone = findViewById(R.id.tvOrderPhone);
        tvOrderTime = findViewById(R.id.tvOrderTime);
        tvOrderCartSum = findViewById(R.id.tvOrderCartSum);
        tvOrderSum = findViewById(R.id.tvOrderSum);
        rdHomeAddress = findViewById(R.id.rdHomeAddress);
        rdNewAddress = findViewById(R.id.rdNewAddress);
        ivEditAddress = findViewById(R.id.ivEditAddress);
        listOrder = findViewById(R.id.listOrder);
        btnConfirmOrder = findViewById(R.id.btnConfirmOrder);

        db = new CartDatabaseHelper(this);
        list = db.getAllCartItems();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("ĐẶT HÀNG");

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss - dd/MM/yyyy");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+7"));
        tvOrderTime.setText(sdf.format(new Date()));

        rdHomeAddress.setChecked(true);
        rdHomeAddress.setText(homeAddress);
        rdHomeAddress.setOnCheckedChangeListener(this);
        rdNewAddress.setOnCheckedChangeListener(this);

        ivEditAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddressDialog();
            }
        });

        int cartSum = Utils.calculateSum(list);
        tvOrderCartSum.setText(Utils.formatPrice(cartSum));
        tvOrderSum.setText(Utils.formatPrice(cartSum + 10000));

        btnConfirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = tvOrderUserName.getText().toString();
                String phone = tvOrderPhone.getText().toString();
                String time = tvOrderTime.getText().toString();
                String sum = tvOrderSum.getText().toString();
                String address = "";
                if(rdHomeAddress.isChecked()){
                    address = homeAddress;
                }
                else {
                    address = newAddress;
                }

                Order order = new Order(name, phone, address, time, sum, list);

                dbRef.child(String.valueOf(System.currentTimeMillis())).setValue(order);
                Toast.makeText(OrderActivity.this, "SUCCESS!", Toast.LENGTH_SHORT).show();
                db.deleteCart();

                Intent intent = new Intent(getBaseContext(), FoodActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("EXIT", true);
                startActivity(intent);
            }
        });

        orderAdapter = new OrderAdapter(this, list);

        listOrder.setHasFixedSize(true);
        listOrder.setLayoutManager(new LinearLayoutManager(this));
        listOrder.setAdapter(orderAdapter);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked) {
            if(buttonView == rdHomeAddress){
                ivEditAddress.setVisibility(View.INVISIBLE);
                rdNewAddress.setChecked(false);
            }
            else {
                rdHomeAddress.setChecked(false);
                if(newAddress.equals("")) {
                    showAddressDialog();
                }
                else {
                    ivEditAddress.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void showAddressDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("ĐỊA CHỈ GIAO HÀNG");

        final EditText editText = new EditText(this);
        editText.setText(newAddress);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );

        editText.setLayoutParams(lp);
        alertDialog.setView(editText);

        alertDialog.setPositiveButton("THÊM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                newAddress = editText.getText().toString();
                rdNewAddress.setText(newAddress);
                ivEditAddress.setVisibility(View.VISIBLE);
            }
        });

        alertDialog.setNegativeButton("HỦY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                rdHomeAddress.setChecked(true);
            }
        });

        alertDialog.show();
    }

}
