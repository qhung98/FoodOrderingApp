package com.example.foodorderingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.andremion.counterfab.CounterFab;
import com.example.foodorderingapp.adapter.FoodAdapter;
import com.example.foodorderingapp.model.Food;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FoodActivity extends AppCompatActivity implements NetworkReceiver.ReceiverListener {
    RecyclerView listFood;
    public static CounterFab fab;
    FoodAdapter foodAdapter;
    ArrayList<Food> list = new ArrayList<>();

    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Food");
    CartDatabaseHelper db;
    String menuId = "";

    final static String CONNECTIVITY_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
    NetworkReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        listFood = findViewById(R.id.listFood);
        fab = findViewById(R.id.fab);

        String menuName = getIntent().getStringExtra("menuName");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(menuName);

        db = new CartDatabaseHelper(this);

        fab.setCount(db.getCartCount());

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FoodActivity.this, CartActivity.class));
            }
        });

        Utils.setActivityState(this, "food", true);

        menuId = getIntent().getStringExtra("menuId");

        loadListFood();
    }

    private void loadListFood() {
        Query query = dbRef.orderByChild("menuId").equalTo(menuId);

        FirebaseRecyclerOptions<Food> options = new FirebaseRecyclerOptions.Builder<Food>().setQuery(query, Food.class).build();
        foodAdapter = new FoodAdapter(options, this);

        listFood.setHasFixedSize(true);
        listFood.setLayoutManager(new LinearLayoutManager(this));
        listFood.setAdapter(foodAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        foodAdapter.startListening();
    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CONNECTIVITY_ACTION);
        receiver = new NetworkReceiver(this, this);
        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onStop() {
        super.onStop();
        foodAdapter.stopListening();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public static void updateFabCart(int count){
        fab.setCount(count);
    }

    @Override
    public void checkNetwork(boolean connect) {
        if (!connect) {
            receiver.showDialog();
        }
    }
}
