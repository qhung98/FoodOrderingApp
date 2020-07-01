package com.example.foodorderingapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

public class NetworkReceiver  extends BroadcastReceiver {
    ReceiverListener receiverListener;
    public static Context context;

    public NetworkReceiver(ReceiverListener receiverListener, Context context) {
        this.receiverListener = receiverListener;
        this.context = context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean connect = checkForInternet();

        if(receiverListener!=null){
            receiverListener.checkNetwork(connect);
        }
    }

    public static boolean checkForInternet() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if(cm!=null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                NetworkCapabilities capabilities = cm.getNetworkCapabilities(cm.getActiveNetwork());
                if(capabilities!=null){
                    if(capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)){
                        return true;
                    }
                    else if(capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)){
                        return true;
                    }
                }
            }
            else {
                NetworkInfo networkInfo = cm.getActiveNetworkInfo();
                return networkInfo != null && networkInfo.isConnectedOrConnecting();
            }
        }

        return false;
    }

    public static void showDialog(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("KHÔNG CÓ KẾT NỐI INTERNET")
                .setCancelable(false)
                .setPositiveButton("THỬ LẠI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean check = checkForInternet();
                if(check){
                    alertDialog.dismiss();
                }
            }
        });
    }

    public interface ReceiverListener{
        void checkNetwork(boolean connect);
    }

}
