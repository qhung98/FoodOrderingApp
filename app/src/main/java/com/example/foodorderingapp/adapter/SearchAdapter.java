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

import com.example.foodorderingapp.CartDatabaseHelper;
import com.example.foodorderingapp.FoodActivity;
import com.example.foodorderingapp.FoodDetailsActivity;
import com.example.foodorderingapp.HomeActivity;
import com.example.foodorderingapp.R;
import com.example.foodorderingapp.SearchActivity;
import com.example.foodorderingapp.Utils;
import com.example.foodorderingapp.model.Cart;
import com.example.foodorderingapp.model.Food;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    Context context;
    ArrayList<Food> list;
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Food");
    CartDatabaseHelper db;

    public SearchAdapter(Context context, ArrayList<Food> list){
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false);
        return new SearchAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.ViewHolder holder, final int position) {
        Picasso.get().load(list.get(position).getImage()).into(holder.ivFoodImage);
        holder.tvFoodName.setText(list.get(position).getName());
        holder.tvPrice.setText(Utils.formatPrice(Integer.parseInt(list.get(position).getPrice())));

        holder.btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = new CartDatabaseHelper(context);
                String name = list.get(position).getName();

                if (db.checkFoodExist(name)){
                    Cart cart = db.getCartByName(name);
                    cart.setQuantity(cart.getQuantity() + 1);
                    db.updateCart(cart);

                    int count = db.getCartCount();
                    HomeActivity.updateFabCart(count);

                    boolean foodState = Utils.getActivityState(context, "food");
                    if(foodState) {
                        FoodActivity.updateFabCart(count);
                    }

                    boolean searchState = Utils.getActivityState(context, "search");
                    if(searchState){
                        SearchActivity.updateFabCart(count);
                    }

                    Toast.makeText(context, "Cập nhật giỏ hàng thành công!", Toast.LENGTH_SHORT).show();
                }
                else {
                    int price = Integer.parseInt(list.get(position).getPrice());
                    int quantity = 1;
                    Cart cart = new Cart(name, price, quantity);
                    db.addCart(cart);

                    int count = db.getCartCount();
                    HomeActivity.updateFabCart(count);

                    boolean foodState = Utils.getActivityState(context, "food");
                    if(foodState) {
                        FoodActivity.updateFabCart(count);
                    }

                    boolean searchState = Utils.getActivityState(context, "search");
                    if(searchState){
                        SearchActivity.updateFabCart(count);
                    }

                    Toast.makeText(context, "Thêm vào giỏ hàng thành công", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.cardFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = list.get(position).getName();
                Query query = dbRef.orderByKey();

                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                       for(DataSnapshot ds: dataSnapshot.getChildren()){
                           Food food = ds.getValue(Food.class);
                           if(food.getName().equals(name)){
                               String foodId = ds.getKey();

                               Intent intent = new Intent(context, FoodDetailsActivity.class);
                               intent.putExtra("foodId", foodId);
                               context.startActivity(intent);
                           }
                       }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
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
