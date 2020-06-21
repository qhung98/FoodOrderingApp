package com.example.foodorderingapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderingapp.CartDatabaseHelper;
import com.example.foodorderingapp.R;
import com.example.foodorderingapp.Utils;
import com.example.foodorderingapp.model.Cart;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewmHolder>{
    Context context;
    ArrayList<Cart> listOrder;

    public OrderAdapter(Context context, ArrayList<Cart> listOrder) {
        this.context = context;
        this.listOrder = listOrder;
    }

    @NonNull
    @Override
    public ViewmHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new ViewmHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewmHolder holder, int position) {
        int price = listOrder.get(position).getPrice();
        int quantity = listOrder.get(position).getQuantity();

        holder.tvOrderNameAndQuantity.setText(listOrder.get(position).getFoodName() + " X " + Integer.toString(quantity));
        holder.tvOrderPrice.setText(Utils.formatPrice(price*quantity));
    }

    @Override
    public int getItemCount() {
        return listOrder.size();
    }

    public class ViewmHolder extends RecyclerView.ViewHolder{
        TextView tvOrderNameAndQuantity, tvOrderPrice;

        public ViewmHolder(@NonNull View itemView) {
            super(itemView);

            tvOrderNameAndQuantity = itemView.findViewById(R.id.tvOrderNameAndQuantity);
            tvOrderPrice = itemView.findViewById(R.id.tvOrdersPrice);
        }
    }
}
