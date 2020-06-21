package com.example.foodorderingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.andremion.counterfab.CounterFab;
import com.example.foodorderingapp.adapter.MenuAdapter;
import com.example.foodorderingapp.model.Menu;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener{
    DrawerLayout drawer;
    Toolbar toolbar;
    NavigationView nav_view;
    public static CounterFab fab;
    RecyclerView listMenu;

    MenuAdapter menuAdapter;
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Menu");
    CartDatabaseHelper db;

    public static void updateFabCart(int count){
        fab.setCount(count);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        SharedPreferences pref = getSharedPreferences("Activity", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("foodActivity", "notCreated");
        editor.commit();

        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);
        nav_view = findViewById(R.id.nav_view);
        fab = findViewById(R.id.fab);
        listMenu = findViewById(R.id.listMenu);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("KFC");
        nav_view.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        db = new CartDatabaseHelper(this);
        fab.setCount(db.getCartCount());

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, CartActivity.class));
            }
        });

        loadListMenu();
    }

    private void loadListMenu() {
        Query query = dbRef.orderByKey();

        FirebaseRecyclerOptions<Menu> options = new FirebaseRecyclerOptions.Builder<Menu>().setQuery(query, Menu.class).build();
        menuAdapter = new MenuAdapter(options, this);

        listMenu.setHasFixedSize(true);
        listMenu.setLayoutManager(new LinearLayoutManager(this));
        listMenu.setAdapter(menuAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        menuAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        menuAdapter.stopListening();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_info:
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                break;
            case R.id.nav_cart:
                startActivity(new Intent(HomeActivity.this, CartActivity.class));
                break;
            case R.id.nav_history:
                startActivity(new Intent(HomeActivity.this, OrderHistoryActivity.class));
                break;
            case R.id.nav_logout:
                Toast.makeText(getBaseContext(), "LogOut", Toast.LENGTH_LONG).show();
                break;
        }

        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.item_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menu_search) {
            startActivity(new Intent(HomeActivity.this, SearchActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }
}
