package com.example.foodorderingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.IntentFilter;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.foodorderingapp.model.Cart;
import com.example.foodorderingapp.model.Food;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class FoodDetailsActivity extends AppCompatActivity implements NetworkReceiver.ReceiverListener {
    ImageView ivDetailFood;
    TextView tvDetailFoodName, tvDetailPrice, tvDescription;
    ElegantNumberButton btnDetailCount;

    Button btnDetailOrder;

    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Food");
    CartDatabaseHelper db;

    String foodId = "";
    Food food;

    final static String CONNECTIVITY_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
    NetworkReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);

        ivDetailFood = findViewById(R.id.ivDetailFoodImage);
        tvDetailFoodName = findViewById(R.id.tvDetailFoodName);
        tvDetailPrice = findViewById(R.id.tvDetailPrice);
        tvDescription = findViewById(R.id.tvDescription);
        btnDetailCount = findViewById(R.id.btnDetailCount);
        btnDetailOrder = findViewById(R.id.btnDetailOrder);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("CHI TIẾT");

        db = new CartDatabaseHelper(this);

        loadFoodDetails();

        btnDetailCount.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                int total = newValue * Integer.parseInt(food.getPrice());
                tvDetailPrice.setText(Utils.formatPrice(total));
            }
        });

        btnDetailOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = food.getName();
                int price = Integer.parseInt(food.getPrice());
                int quantity = Integer.parseInt(btnDetailCount.getNumber());

                if(db.checkFoodExist(name)) {
                    Cart cart = db.getCartByName(name);
                    cart.setQuantity(quantity);
                    db.updateCart(cart);

                    int count = db.getCartCount();
                    HomeActivity.updateFabCart(count);

                    boolean foodState = Utils.getActivityState(FoodDetailsActivity.this, "food");
                    if(foodState){
                        FoodActivity.updateFabCart(count);
                    }

                    boolean searchState = Utils.getActivityState(FoodDetailsActivity.this, "search");
                    if(searchState){
                        SearchActivity.updateFabCart(count);
                    }

                    Toast.makeText(FoodDetailsActivity.this, "Cập nhật giỏ hàng thành công!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Cart cart = new Cart(name, price, quantity);
                    db.addCart(cart);

                    int count = db.getCartCount();
                    HomeActivity.updateFabCart(count);

                    boolean foodState = Utils.getActivityState(FoodDetailsActivity.this, "food");
                    if(foodState){
                        FoodActivity.updateFabCart(count);
                    }

                    boolean searchState = Utils.getActivityState(FoodDetailsActivity.this, "search");
                    if(searchState){
                        SearchActivity.updateFabCart(count);
                    }

                    Toast.makeText(FoodDetailsActivity.this, "Thêm vào giỏ hàng thành công!", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void loadFoodDetails() {
        foodId = getIntent().getStringExtra("foodId");

        dbRef.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                food = dataSnapshot.getValue(Food.class);

                Picasso.get().load(food.getImage()).into(ivDetailFood);
                tvDetailFoodName.setText(food.getName());
                tvDescription.setText(food.getDescription());

                if(db.checkFoodExist(food.getName())){
                    Cart cart = db.getCartByName(food.getName());
                    btnDetailCount.setNumber(Integer.toString(cart.getQuantity()));

                    int price = Integer.parseInt(food.getPrice());
                    int quantity = cart.getQuantity();
                    tvDetailPrice.setText(Utils.formatPrice(price * quantity));
                }
                else {
                    tvDetailPrice.setText(Utils.formatPrice(Integer.parseInt(food.getPrice())));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void checkNetwork(boolean connect) {
        if (!connect) {
            receiver.showDialog();
        }
    }
}
