package com.edumaite.adam.edumiate_poc;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends Activity {

    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=this;
        isInternetOn();
    }

    public boolean isInternetOn() {

        Log.i("Adam", "isInternetOn currently running");
        // get Connectivity Manager object to check connection
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        Log.i("Adam", "Connectivity manager intialised");

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            Log.i("Adam", "Device is connected to the network");
            Toast.makeText(context, "Device is connected to the network", Toast.LENGTH_LONG).show();
            switch (activeNetwork.getType()) {
                case ConnectivityManager.TYPE_WIFI:
                    Log.i("Adam", "Device is connected to the wifi");
                    Toast.makeText(context, "Device is connected to the wifi", Toast.LENGTH_LONG).show();
                    // connected to wifi
                    break;
                case ConnectivityManager.TYPE_MOBILE:
                    // connected to mobile data
                    Log.i("Adam", "Device is using mobile data");
                    Toast.makeText(context, "Device is using mobile data", Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
            return true;
        } else {
            Log.i("Adam", "Device is not connected to the network");
            Toast.makeText(context, "Device is not connected to the network", Toast.LENGTH_LONG).show();
            return false;
        }
    }
}

