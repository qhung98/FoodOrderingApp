package com.example.foodorderingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.foodorderingapp.adapter.HistoryAdapter;
import com.example.foodorderingapp.model.Order;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class OrderHistoryActivity extends AppCompatActivity {
    RecyclerView listHistory;
    HistoryAdapter historyAdapter;

    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Order");
    String phone = "0352929696";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        listHistory = findViewById(R.id.listHistory);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("LỊCH SỬ ĐẶT HÀNG");

        loadListHOrderHistory();
    }

    private void loadListHOrderHistory() {
       Query query = dbRef.orderByChild("phone").equalTo(phone);
//        Query query = dbRef.orderByKey();

        FirebaseRecyclerOptions<Order> options = new FirebaseRecyclerOptions.Builder<Order>().setQuery(query, Order.class).build();
        historyAdapter = new HistoryAdapter(options);

        listHistory.setHasFixedSize(true);
        listHistory.setLayoutManager(new LinearLayoutManager(this));
        listHistory.setAdapter(historyAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        historyAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        historyAdapter.stopListening();
    }
}
