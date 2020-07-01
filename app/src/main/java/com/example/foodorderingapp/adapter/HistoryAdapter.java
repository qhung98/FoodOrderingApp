package com.example.foodorderingapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderingapp.R;
import com.example.foodorderingapp.model.Order;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class HistoryAdapter extends FirebaseRecyclerAdapter<Order, HistoryAdapter.ViewHolder> {
    ArrayList<String> keys;

    public HistoryAdapter(@NonNull FirebaseRecyclerOptions<Order> options, ArrayList<String> keys) {
        super(options);
        this.keys = keys;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Order model) {
        holder.tvIdOrder.setText(keys.get(position));
        holder.tvHistoryUserName.setText(model.getName());
        holder.tvHistoryPhone.setText(model.getPhone());
        holder.tvHistoryAddress.setText(model.getAddress());
        holder.tvHistoryTime.setText(model.getTime());
        holder.tvHistorySum.setText(model.getSum());

        int status = model.getStatus();
        if(status==0) {
            holder.tvStatus.setText("ĐANG CHỜ XỬ LÝ");
        }
        else if(status==1) {
            holder.tvStatus.setText("ĐANG VẬN CHUYỂN");
        }
        else {
            holder.tvStatus.setText("ĐÃ THANH TOÁN");
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_history, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvIdOrder, tvHistoryUserName, tvHistoryPhone, tvHistoryAddress, tvHistoryTime, tvHistorySum, tvStatus;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvIdOrder = itemView.findViewById(R.id.tvOrderId);
            tvHistoryUserName = itemView.findViewById(R.id.tvHistoryUserName);
            tvHistoryPhone = itemView.findViewById(R.id.tvHistoryPhone);
            tvHistoryAddress = itemView.findViewById(R.id.tvHistoryAddress);
            tvHistoryTime = itemView.findViewById(R.id.tvHistoryTime);
            tvHistorySum = itemView.findViewById(R.id.tvHistorySum);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}
