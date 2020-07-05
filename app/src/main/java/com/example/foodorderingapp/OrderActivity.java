package com.example.foodorderingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodorderingapp.adapter.OrderAdapter;
import com.example.foodorderingapp.model.Cart;
import com.example.foodorderingapp.model.Order;
import com.example.foodorderingapp.model.User;
import com.example.foodorderingapp.notification.MyResponse;
import com.example.foodorderingapp.notification.Notification;
import com.example.foodorderingapp.notification.RetrofitAPI;
import com.example.foodorderingapp.notification.RetrofitClient;
import com.example.foodorderingapp.notification.Sender;
import com.example.foodorderingapp.notification.Token;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, NetworkReceiver.ReceiverListener {
    TextView tvOrderUserName, tvOrderPhone, tvOrderTime, tvOrderCartSum, tvOrderSum;
    RadioButton rdHomeAddress, rdNewAddress;
    ImageView ivEditAddress;
    RecyclerView listOrder;
    Button btnConfirmOrder;

    OrderAdapter orderAdapter;
    ArrayList<Cart> list;

    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Order");
    CartDatabaseHelper db;

    String homeAddress;
    String newAddress = "";

    final static String CONNECTIVITY_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
    NetworkReceiver receiver;

    RetrofitAPI retrofitAPI;
    String oderId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        tvOrderUserName = findViewById(R.id.tvOrderUserName);
        tvOrderPhone = findViewById(R.id.tvOrderPhone);
        tvOrderTime = findViewById(R.id.tvOrderTime);
        tvOrderCartSum = findViewById(R.id.tvOrderCartSum);
        tvOrderSum = findViewById(R.id.tvOrderSum);
        rdHomeAddress = findViewById(R.id.rdHomeAddress);
        rdNewAddress = findViewById(R.id.rdNewAddress);
        ivEditAddress = findViewById(R.id.ivEditAddress);
        listOrder = findViewById(R.id.listOrder);
        btnConfirmOrder = findViewById(R.id.btnConfirmOrder);

        db = new CartDatabaseHelper(this);
        list = db.getAllCartItems();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("ĐẶT HÀNG");

        User user = Utils.getCurrentUser(this);
        tvOrderUserName.setText(user.getName());
        tvOrderPhone.setText(String.valueOf(user.getPhone()));
        homeAddress = user.getAddress();

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss - dd/MM/yyyy");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+7"));
        tvOrderTime.setText(sdf.format(new Date()));

        rdHomeAddress.setChecked(true);
        rdHomeAddress.setText(homeAddress);
        rdHomeAddress.setOnCheckedChangeListener(this);
        rdNewAddress.setOnCheckedChangeListener(this);

        ivEditAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddressDialog();
            }
        });

        int cartSum = Utils.calculateSum(list);
        tvOrderCartSum.setText(Utils.formatPrice(cartSum));
        tvOrderSum.setText(Utils.formatPrice(cartSum + 10000));

        btnConfirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = tvOrderUserName.getText().toString();
                String phone = tvOrderPhone.getText().toString();
                String time = tvOrderTime.getText().toString();
                String sum = tvOrderSum.getText().toString();
                String address = "";
                if(rdHomeAddress.isChecked()){
                    address = homeAddress;
                }
                else {
                    address = newAddress;
                }

                Order order = new Order(name, phone, address, time, sum, list, 0);

                oderId = String.valueOf(System.currentTimeMillis());
                dbRef.child(oderId).setValue(order);
                Toast.makeText(OrderActivity.this, "ĐẶT HÀNG THÀNH CÔNG!", Toast.LENGTH_SHORT).show();
                db.deleteCart();

                DatabaseReference tokenRef = FirebaseDatabase.getInstance().getReference("Token");
                Query query = tokenRef.orderByChild("server").equalTo(true);

                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                            Token token = snapshot.getValue(Token.class);
                            String serverToken = token.getToken();
                            Log.d("token", serverToken);
                            sendNotiToServer(serverToken);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                HomeActivity.updateFabCart(0);
                Intent intent = new Intent(OrderActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        orderAdapter = new OrderAdapter(this, list);

        listOrder.setHasFixedSize(true);
        listOrder.setLayoutManager(new LinearLayoutManager(this));
        listOrder.setAdapter(orderAdapter);
    }

    private void sendNotiToServer(String serverToken) {
        Notification notification = new Notification("ĐẶT HÀNG", "KFC CÓ 1 ĐƠN HÀNG MỚI ID: " + oderId);
        Sender sender = new Sender(serverToken, notification);

        retrofitAPI = RetrofitClient.getClient("https://fcm.googleapis.com/").create(RetrofitAPI.class);
        retrofitAPI.sendNotification(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if(response.code()==200){
                    if(response.body().success == 1) {
                        Log.d("Response", "SUCCESS");
                    }
                    else {
                        Log.d("Response", "FAILED");
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked) {
            if(buttonView == rdHomeAddress){
                ivEditAddress.setVisibility(View.INVISIBLE);
                rdNewAddress.setChecked(false);
            }
            else {
                rdHomeAddress.setChecked(false);
                if(newAddress.equals("")) {
                    showAddressDialog();
                }
                else {
                    ivEditAddress.setVisibility(View.VISIBLE);
                }
            }
        }
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

    private void showAddressDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("ĐỊA CHỈ GIAO HÀNG");

        final EditText editText = new EditText(this);
        editText.setText(newAddress);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );

        editText.setLayoutParams(lp);
        alertDialog.setView(editText);

        alertDialog.setPositiveButton("THÊM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                newAddress = editText.getText().toString();
                rdNewAddress.setText(newAddress);
                ivEditAddress.setVisibility(View.VISIBLE);
            }
        });

        alertDialog.setNegativeButton("HỦY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                rdHomeAddress.setChecked(true);
            }
        });

        alertDialog.show();
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
