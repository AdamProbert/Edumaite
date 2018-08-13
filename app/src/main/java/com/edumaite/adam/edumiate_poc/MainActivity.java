package com.edumaite.adam.edumiate_poc;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import junit.framework.Test;

public class MainActivity extends AppCompatActivity
        implements TestFragment2.OnFragmentInteractionListener,
        TestFragment1.OnFragmentInteractionListener{

    private Context context;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar();

        Fragment fragment = null;
        Class fragmentClass = null;
        fragmentClass = TestFragment1.class;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {

                        item.setChecked(true);

                        // Handle navigation view item clicks here.
                        int id = item.getItemId();
                        Fragment fragment = null;
                        Class fragmentClass = null;
                        if (id == R.id.bot) {
                            fragmentClass = TestFragment1.class;
                        } else if (id == R.id.yoda) {
                            fragmentClass = TestFragment2.class;
                        } else {
                            return true;
                        }

                        try {
                            fragment = (Fragment) fragmentClass.newInstance();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

                        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                        drawer.closeDrawer(GravityCompat.START);
                        return true;
                    }
                });

        context=this;
        isInternetOn();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

}

