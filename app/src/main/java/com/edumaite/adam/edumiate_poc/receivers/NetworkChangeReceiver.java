package com.edumaite.adam.edumiate_poc.receivers;

import android.app.ActivityManager;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStatsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
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
            whatAppIsRunning(context);
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

    // This only works within my app! Fuck.
    public void whatAppIsRunning(Context context) {

        Log.i("Adam", "System service detecting");
        ActivityManager activityManager = (ActivityManager) context.getSystemService( Context.ACTIVITY_SERVICE );
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for(ActivityManager.RunningAppProcessInfo appProcess : appProcesses){
            Log.i("Adam", "Current foreground process: " +appProcess.processName);
            Toast.makeText(context, "Current foreground process: " +appProcess.processName, Toast.LENGTH_LONG).show();

        }
        Log.i("Adam", "System service done");

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
