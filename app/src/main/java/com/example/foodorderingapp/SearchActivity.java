package com.example.foodorderingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.andremion.counterfab.CounterFab;
import com.example.foodorderingapp.adapter.FoodAdapter;
import com.example.foodorderingapp.adapter.SearchAdapter;
import com.example.foodorderingapp.model.Food;
import com.example.foodorderingapp.model.Menu;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import java.security.PrivateKey;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    SearchView searchView;
    RecyclerView listSearch;
    static CounterFab fab;

    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Food");
    CartDatabaseHelper db;

    ArrayList<Food> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchView = findViewById(R.id.searchView);
        listSearch = findViewById(R.id.listSearch);
        fab = findViewById(R.id.fab);
        db = new CartDatabaseHelper(this);

        listSearch.setHasFixedSize(true);
        listSearch.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

        fab.setCount(db.getCartCount());

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SearchActivity.this, CartActivity.class));
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("TÌM KIẾM");

        Utils.setActivityState(this, "search", true);

        loadListSearch();

        if (searchView != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    search(newText);
                    return true;
                }
            });
        }
    }

    private void loadListSearch() {
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        list.add(ds.getValue(Food.class));
                    }
                    SearchAdapter searchAdapter = new SearchAdapter(SearchActivity.this, list);
                    listSearch.setAdapter(searchAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void search(String newText) {
        ArrayList<Food> filterList = new ArrayList<>();
        for(Food food:list){
            if(food.getName().toLowerCase().contains(newText.toLowerCase())){
                filterList.add(food);
            }
        }

        SearchAdapter searchAdapter = new SearchAdapter(this, filterList);
        listSearch.setAdapter(searchAdapter);
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
}
