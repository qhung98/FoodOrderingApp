package com.example.foodorderingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.foodorderingapp.adapter.HistoryAdapter;
import com.example.foodorderingapp.model.Order;
import com.example.foodorderingapp.model.User;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderHistoryActivity extends AppCompatActivity {
    RecyclerView listHistory;
    HistoryAdapter historyAdapter;

    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Order");
    String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        listHistory = findViewById(R.id.listHistory);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("LỊCH SỬ ĐẶT HÀNG");

        User user = Utils.getCurrentUser(this);
        phone = user.getPhone();

        loadListHOrderHistory();
    }

    private void loadListHOrderHistory() {
        final ArrayList<String> keys = new ArrayList<>();

        Query query1 = dbRef.orderByKey();

        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    Order order = ds.getValue(Order.class);
                    if(order.getPhone().equals(phone)){
                        keys.add(ds.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Query query2 = dbRef.orderByChild("phone").equalTo(phone);

        FirebaseRecyclerOptions<Order> options = new FirebaseRecyclerOptions.Builder<Order>().setQuery(query2, Order.class).build();
        historyAdapter = new HistoryAdapter(options, keys);

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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
