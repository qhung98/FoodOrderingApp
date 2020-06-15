package com.example.foodorderingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderingapp.CartActivity;
import com.example.foodorderingapp.CartDatabaseHelper;
import com.example.foodorderingapp.FoodActivity;
import com.example.foodorderingapp.FoodDetailsActivity;
import com.example.foodorderingapp.HomeActivity;
import com.example.foodorderingapp.R;
import com.example.foodorderingapp.Utils;
import com.example.foodorderingapp.model.Cart;
import com.example.foodorderingapp.model.Food;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class FoodAdapter extends FirebaseRecyclerAdapter<Food, FoodAdapter.ViewHolder> {
    Context context;
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Restaurants").child("01").child("detail").child("Foods");
    CartDatabaseHelper db;

    public FoodAdapter(@NonNull FirebaseRecyclerOptions<Food> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, final int position, @NonNull final Food model) {
        Picasso.get().load(model.getImage()).into(holder.ivFoodImage);
        holder.tvFoodName.setText(model.getName());
        holder.tvPrice.setText(Utils.formatPrice(Integer.parseInt(model.getPrice())));


        holder.btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = new CartDatabaseHelper(context);
                String name = model.getName();

                if (db.checkFoodExist(name)){
                    Cart cart = db.getCartByName(name);
                    cart.setQuantity(cart.getQuantity() + 1);
                    db.updateCart(cart);

                    int count = db.getCartCount();
                    HomeActivity.updateFabCart(count);
                    FoodActivity.updateFabCart(count);
                    Toast.makeText(context, "Cập nhật giỏ hàng thành công!", Toast.LENGTH_SHORT).show();
                }
                else {
                    int price = Integer.parseInt(model.getPrice());
                    int quantity = 1;
                    Cart cart = new Cart(name, price, quantity);
                    db.addCart(cart);

                    int count = db.getCartCount();
                    HomeActivity.updateFabCart(count);
                    FoodActivity.updateFabCart(count);
                    Toast.makeText(context, "Thêm vào giỏ hàng thành công", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.cardFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String foodId = getSnapshots().getSnapshot(position).getKey();

                        Intent intent = new Intent(context, FoodDetailsActivity.class);
                        intent.putExtra("foodId", foodId);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ivFoodImage;
        TextView tvFoodName, tvPrice;
        Button btnOrder;
        CardView cardFood;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivFoodImage = itemView.findViewById(R.id.ivFoodImage);
            tvFoodName = itemView.findViewById(R.id.tvFoodName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            btnOrder = itemView.findViewById(R.id.btnOrder);
            cardFood = itemView.findViewById(R.id.cardFood);
        }
    }
}
