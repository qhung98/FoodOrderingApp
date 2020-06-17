package com.example.foodorderingapp;
//Hiếu đã ghé qua noi đây!!!
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.andremion.counterfab.CounterFab;
import com.example.foodorderingapp.adapter.FoodAdapter;
import com.example.foodorderingapp.model.Food;
import com.example.foodorderingapp.model.Menu;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.security.PrivateKey;

public class SearchActivity extends AppCompatActivity {
    MaterialSearchBar searchBar;
    RecyclerView listSearch;
    CounterFab fab;
    private FoodAdapter foodAdapter;

    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Restaurants").child("01").child("detail").child("Foods");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchBar = findViewById(R.id.searchBar);
        listSearch = findViewById(R.id.listSearch);
        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SearchActivity.this, CartActivity.class));
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("TÌM KIẾM");

        loadListSearch();
    }

    private void loadListSearch() {
        Query query = dbRef.orderByKey();

        FirebaseRecyclerOptions<Food> options = new FirebaseRecyclerOptions.Builder<Food>().setQuery(query, Food.class).build();
        foodAdapter = new FoodAdapter(options, this);

        listSearch.setHasFixedSize(true);
        listSearch.setLayoutManager(new LinearLayoutManager(this));
        listSearch.setAdapter(foodAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        foodAdapter.startListening();
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
}
