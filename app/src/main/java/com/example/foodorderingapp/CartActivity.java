package com.example.foodorderingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodorderingapp.adapter.CartAdapter;
import com.example.foodorderingapp.model.Cart;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {
    RecyclerView listCart;
    public static TextView tvCartSum;
    Button btnCartOrder;

    CartAdapter cartAdapter;
    CartDatabaseHelper db;
    ArrayList<Cart> list;

    public static void updateSum(String sum){
        tvCartSum.setText(sum);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        listCart = findViewById(R.id.listCart);
        tvCartSum = findViewById(R.id.tvCartSum);
        btnCartOrder = findViewById(R.id.btnCartOrder);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("GIỎ HÀNG");

        db = new CartDatabaseHelper(this);
        list = db.getAllCartItems();

        tvCartSum.setText(Utils.formatPrice(Utils.calculateSum(list)));

        btnCartOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartActivity.this, OrderActivity.class));
            }
        });

        cartAdapter = new CartAdapter(this, list);

        listCart.setHasFixedSize(true);
        listCart.setLayoutManager(new LinearLayoutManager(this));
        listCart.setAdapter(cartAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
