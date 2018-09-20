package com.edumaite.adam.edumiate_poc.receivers;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

public class NetworkChangeReceiver extends BroadcastReceiver {

    private Handler handler = null;

    public NetworkChangeReceiver(){

    }
    public NetworkChangeReceiver(Handler handler){
        this.handler = handler;
    }

    @Override
    public void onReceive(final Context context, Intent intent)
    {
        try
        {
            final String onlineInfo = isInternetOn(context);
            if (onlineInfo != null) {
                Log.i("Adam", onlineInfo);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, onlineInfo, Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                Log.i("Adam", "Connection broken");
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "Connection broken", Toast.LENGTH_LONG).show();
                    }
                });
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public String isInternetOn(Context context) {


        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            switch (activeNetwork.getType()) {
                case ConnectivityManager.TYPE_WIFI:
                    String ssid = netInfo.getExtraInfo();
                    return "Connected to Wifi: " +ssid;
                case ConnectivityManager.TYPE_MOBILE:
                    return "Connected to mobile internet";
                default:
                    return "Device is connected some other way";
            }
        } else {
            return null;
        }
    }

}
