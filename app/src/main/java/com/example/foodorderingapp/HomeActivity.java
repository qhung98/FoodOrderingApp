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
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.andremion.counterfab.CounterFab;
import com.example.foodorderingapp.adapter.MenuAdapter;
import com.example.foodorderingapp.model.Menu;
import com.example.foodorderingapp.model.User;
import com.example.foodorderingapp.notification.Token;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener, NetworkReceiver.ReceiverListener{
    DrawerLayout drawer;
    Toolbar toolbar;
    NavigationView nav_view;
    View nav_header;
    public static TextView nav_tvUsername, nav_tvPhone;
    public static CounterFab fab;
    RecyclerView listMenu;

    MenuAdapter menuAdapter;
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Menu");
    CartDatabaseHelper db;

    final static String CONNECTIVITY_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
    NetworkReceiver receiver;

    public static void updateFabCart(int count){
        fab.setCount(count);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);
        nav_view = findViewById(R.id.nav_view);
        fab = findViewById(R.id.fab);
        listMenu = findViewById(R.id.listMenu);

        nav_header = nav_view.getHeaderView(0);
        nav_tvUsername = nav_header.findViewById(R.id.nav_tvUsername);
        nav_tvPhone = nav_header.findViewById(R.id.nav_tvPhone);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("KFC");
        nav_view.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        Utils.setActivityState(this, "food", false);
        Utils.setActivityState(this, "search", false);

        User user = Utils.getCurrentUser(this);

        nav_tvUsername.setText(user.getName());
        nav_tvPhone.setText(String.valueOf(user.getPhone()));

        db = new CartDatabaseHelper(this);
        fab.setCount(db.getCartCount());

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, CartActivity.class));
            }
        });

        loadListMenu();

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        String tokenRefreshed = task.getResult().getToken();
                        updateToken(tokenRefreshed);
                    }
                });
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
        menuAdapter.stopListening();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
           this.moveTaskToBack(true);
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
            case R.id.nav_search:
                startActivity(new Intent(HomeActivity.this, SearchActivity.class));
                break;
            case R.id.nav_history:
                startActivity(new Intent(HomeActivity.this, OrderHistoryActivity.class));
                break;
            case R.id.nav_logout:
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Utils.setRememberUser(this, false);
                db.deleteCart();
                startActivity(intent);
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

    public static void updateDrawerMenu(User user){
        nav_tvUsername.setText(user.getName());
        nav_tvPhone.setText(String.valueOf(user.getPhone()));
    }

    @Override
    public void checkNetwork(boolean connect) {
        if (!connect) {
            receiver.showDialog();
        }
    }

    private void updateToken(String tokenRefreshed) {
        User user = Utils.getCurrentUser(this);
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Token");
        Token token = new Token(tokenRefreshed, false);
        dbRef.child(user.getPhone()).setValue(token);
    }
}
