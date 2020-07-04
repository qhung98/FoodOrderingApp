package com.example.foodorderingapp.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.foodorderingapp.CartActivity;
import com.example.foodorderingapp.CartDatabaseHelper;
import com.example.foodorderingapp.FoodActivity;
import com.example.foodorderingapp.HomeActivity;
import com.example.foodorderingapp.R;
import com.example.foodorderingapp.Utils;
import com.example.foodorderingapp.model.Cart;
import com.example.foodorderingapp.model.Food;

import java.util.ArrayList;

public class CartAdapter extends  RecyclerView.Adapter<CartAdapter.ViewHolder>{
    Context context;
    ArrayList<Cart> listCart;
    CartDatabaseHelper db;

    public CartAdapter(Context context, ArrayList<Cart> listCart) {
        this.context = context;
        this.listCart = listCart;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.tvCartFoodName.setText(listCart.get(position).getFoodName());
        holder.tvFoodPrice.setText(Utils.formatPrice(listCart.get(position).getPrice()));
        holder.btnCartCount.setNumber(Integer.toString(listCart.get(position).getQuantity()));

        int quantity = listCart.get(position).getQuantity();
        int price = listCart.get(position).getPrice();
        holder.tvCartPrice.setText(Utils.formatPrice(price * quantity));

        db = new CartDatabaseHelper(context);
        holder.ivCartRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setMessage("Xóa khỏi giỏ hàng")
                        .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Cart cart = listCart.get(position);
                                db.deleteCartItem(cart);

                                listCart.remove(position);
                                notifyDataSetChanged();

                                HomeActivity.updateFabCart(db.getCartCount());

                                boolean foodState = Utils.getActivityState(context, "food");
                                if(foodState){
                                    FoodActivity.updateFabCart(db.getCartCount());
                                }

                                ArrayList<Cart> updatedList = db.getAllCartItems();
                                CartActivity.updateSum(Utils.formatPrice(Utils.calculateSum(updatedList)));

                                Toast.makeText(context, "Xóa khỏi giỏ hàng thành công", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("HỦY", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create().show();
            }
        });

        holder.btnCartCount.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                Cart cart = listCart.get(position);

                int total = newValue * listCart.get(position).getPrice();
                holder.tvCartPrice.setText(Utils.formatPrice(total));

                cart.setQuantity(newValue);
                db.updateCart(cart);

                HomeActivity.updateFabCart(db.getCartCount());

                boolean foodState = Utils.getActivityState(context, "food");
                if(foodState){
                    FoodActivity.updateFabCart(db.getCartCount());
                }


                ArrayList<Cart> updatedList = db.getAllCartItems();
                CartActivity.updateSum(Utils.formatPrice(Utils.calculateSum(updatedList)));
            }
        });
    }

    @Override
    public int getItemCount() {
        return listCart.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvCartFoodName, tvFoodPrice, tvCartPrice;
        ImageView ivCartRemove;
        ElegantNumberButton btnCartCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvCartFoodName = itemView.findViewById(R.id.tvCartFoodName);
            tvFoodPrice = itemView.findViewById(R.id.tvFoodPrice);
            tvCartPrice = itemView.findViewById(R.id.tvCartPrice);
            ivCartRemove = itemView.findViewById(R.id.ivCartRemove);
            btnCartCount = itemView.findViewById(R.id.btnCartCount);
        }
    }
}
