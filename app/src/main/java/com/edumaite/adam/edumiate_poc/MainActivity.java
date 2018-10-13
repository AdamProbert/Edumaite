package com.edumaite.adam.edumiate_poc;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.arch.persistence.room.Room;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.edumaite.adam.edumiate_poc.dataCollection.AppCollector;
import com.edumaite.adam.edumiate_poc.db.AppDao;
import com.edumaite.adam.edumiate_poc.db.AppViewModel;
import com.edumaite.adam.edumiate_poc.db.EdumaiteDB;
import com.edumaite.adam.edumiate_poc.db.EdumaiteRepository;
import com.edumaite.adam.edumiate_poc.models.App;
import com.edumaite.adam.edumiate_poc.receivers.NetworkChangeReceiver;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements
        TestFragment2.OnFragmentInteractionListener,
        TestFragment1.OnFragmentInteractionListener,
        FToggleFragment.OnFragmentInteractionListener {

    private Context context;
    private DrawerLayout mDrawerLayout;

    // Network change receiver
    private BroadcastReceiver mNetworkReceiver;

    // Timer for blacklisted app checking
    private Timer timer;
    private TimerTask timerTask;
    private Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Edumaite");
        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar();

        Fragment fragment = null;
        Class fragmentClass;
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
        navigationView.setCheckedItem(R.id.bot);
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
                        } else if (id == R.id.vader) {
                            fragmentClass = FToggleFragment.class;
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

        context = this;

        // Try something like this to speed it up: https://virtuooza.com/add-data-to-sqlite-room-and-display-it-into-recyclerview/
//        Runnable r = new Runnable(){
//            @Override
//            public void run() {
//                items = db.databaseInterface().getAllItems();
//                recyclerView= (RecyclerView)findViewById(R.id.recyclerview);
//                recyclerView.setLayoutManager(new LinearLayoutManager(getApplication()));
//                adapter= new UserAdapter(items);
//                adapter.notifyDataSetChanged();
//                recyclerView.setAdapter(adapter);
//
//            }
//        };
//
//        Thread newThread= new Thread(r);
//        newThread.start();


        //Initialise DB with installed apps
        // This needs to only be run once! As it will overwrite the previous entries!
//        List<App> apps = new AppCollector(context).getInstalledApps();
//        EdumaiteDB db = Room.databaseBuilder(this, EdumaiteDB.class, "edumaite_db")
//                .build();
//
//
//        AppViewModel avm = new AppViewModel(getApplication());
//        for(App app: apps) {
//            avm.insert(app);
//
//        }

        // Initialise network receiver
        mNetworkReceiver = new NetworkChangeReceiver(new Handler());
        registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        // Initialise foreground app checker
        startTimer(context);

    }

    //To start timer
    private void startTimer(final Context context){
        timer = new Timer();
        timerTask = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        String foregroundapp = getForegroundAppName(context);
                        Log.i("Adam", "Current foreground app: " + foregroundapp);
                        //Toast.makeText(context, "Current foreground app: " + foregroundapp, Toast.LENGTH_SHORT).show();
                        if(foregroundapp.equals("com.google.android.googlequicksearchbox")){
                            Log.i("Adam", "blacklisted app has been caught!");
                            Intent intent = new Intent(context, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                            startActivity(intent);
                            Log.i("Adam", "Activity should have relaunched!");

                        }
                    }
                });
            }
        };
        timer.schedule(timerTask, 3000, 3000);
    }

    private void createHandler() {
        Thread thread = new Thread() {
            public void run() {
                Looper.prepare();

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do Work
                        String foregroundapp = getForegroundAppName(context);
                        Log.i("Adam","Current foreground app: " +foregroundapp);
                        handler.removeCallbacks(this);
                        Looper.myLooper().quit();
                    }
                }, 2000);

                Looper.loop();
                Log.e("Adam", "Looper called");
            }
        };
        thread.start();
        Log.e("Adam", "Thread started");
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static boolean needPermissionForBlocking(Context context){
        try {
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            int mode = appOpsManager.checkOpNoThrow("android:get_usage_stats", applicationInfo.uid, applicationInfo.packageName);
            return  (mode != AppOpsManager.MODE_ALLOWED);
        } catch (Throwable e) {
            return true;
        }
    }

    public String getForegroundAppName(Context context) {

        final String tag = "getForegroundAppName";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

            if(needPermissionForBlocking(context)) {
                Toast.makeText(context, "Turn on Settings->Security->[Apps with usage access]", Toast.LENGTH_SHORT).show();
                Log.e(tag, "Need to Turn on Settings->Security->[Apps with usage access]");
            }

            @SuppressWarnings("WrongConstant")
            UsageStatsManager usm = (UsageStatsManager) context.getSystemService("usagestats");
            long now = System.currentTimeMillis();
            List<UsageStats> usageStatsList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, now - 1000 * 1000, now);
            if (usageStatsList != null && usageStatsList.size() > 0) {
                long time = 0;
                String name = "";
                for(UsageStats usageStats : usageStatsList) {
                    if(usageStats.getLastTimeUsed() > time) {
                        time = usageStats.getLastTimeUsed();
                        name = usageStats.getPackageName();
                    }
                }
                return name;
            }
        } else {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            try {
                ActivityManager.RunningTaskInfo foregroundTaskInfo = am.getRunningTasks(1).get(0);
                return foregroundTaskInfo.topActivity.getPackageName();
            } catch (Exception e) {
                Log.e("Adam", "can not get foreground app: " + e.getMessage(), e);
            }
        }
        return "";
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



    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    protected void unregisterNetworkChanges() {
        try {
            unregisterReceiver(mNetworkReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getSupportFragmentManager().popBackStack();
        unregisterNetworkChanges();
    }

}