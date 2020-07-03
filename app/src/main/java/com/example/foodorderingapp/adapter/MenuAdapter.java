package com.example.foodorderingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderingapp.FoodActivity;
import com.example.foodorderingapp.R;
import com.example.foodorderingapp.model.Food;
import com.example.foodorderingapp.model.Menu;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class MenuAdapter extends FirebaseRecyclerAdapter<Menu, MenuAdapter.ViewHolder> {
    Context context;
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Menu");
    public MenuAdapter(@NonNull FirebaseRecyclerOptions<Menu> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, final int position, @NonNull final Menu model) {
        Picasso.get().load(model.getImage()).into(holder.ivMenuImage);
        holder.tvMenuName.setText(model.getName());
        holder.cardMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                       String menuId = getSnapshots().getSnapshot(position).getKey();

                       Intent intent = new Intent(context, FoodActivity.class);
                       intent.putExtra("menuId", menuId);
                       intent.putExtra("menuName", model.getName());
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ivMenuImage;
        TextView tvMenuName;
        CardView cardMenu;

        public ViewHolder (@NonNull View itemView) {
            super(itemView);

            ivMenuImage = itemView.findViewById(R.id.ivMenuImage);
            tvMenuName = itemView.findViewById(R.id.tvMenuName);
            cardMenu = itemView.findViewById(R.id.cardMenu);
        }
    }
}
