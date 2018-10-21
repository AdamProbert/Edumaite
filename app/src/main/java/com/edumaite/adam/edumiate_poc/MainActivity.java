package com.edumaite.adam.edumiate_poc;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.persistence.room.Room;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
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

import com.edumaite.adam.edumiate_poc.dataCollection.AppCollector;
import com.edumaite.adam.edumiate_poc.db.AppViewModel;
import com.edumaite.adam.edumiate_poc.db.EdumaiteDB;
import com.edumaite.adam.edumiate_poc.models.App;
import com.edumaite.adam.edumiate_poc.receivers.NetworkChangeReceiver;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements
        UserSelectionFragment.OnFragmentInteractionListener,
        AppListFragment.OnFragmentInteractionListener,
        TeacherSplash.OnFragmentInteractionListener,
        StudentSplash.OnFragmentInteractionListener,
        BroadcastFragment.OnFragmentInteractionListener,
        HomeworkFragment.OnFragmentInteractionListener,
        PollsFragment.OnFragmentInteractionListener,
        StudentsFragment.OnFragmentInteractionListener,
        QuestionFragment.OnFragmentInteractionListener{

    private Context context;
    private DrawerLayout mDrawerLayout;

    // Network change receiver
    private BroadcastReceiver mNetworkReceiver;

    // Timer for blacklisted app checking
    private Timer timer;
    private TimerTask timerTask;
    private Handler handler = new Handler();
    private AppViewModel avm;

    public String user = null;

    NavigationView navigationView = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        setTitle("Edumaite");

        ActionBar actionbar = getSupportActionBar();

        Fragment fragment = null;
        Class fragmentClass;

        if(user == null) {
            fragmentClass = UserSelectionFragment.class;
        }
        else if(user == "teacher"){
            fragmentClass = TeacherSplash.class;
        }
        else if(user == "student"){
            fragmentClass = StudentSplash.class;
        }
        else{
            Log.i("adam", "Issue initialising splash fragment based on user." +
                    "setting UserSelectionFragment as default");
            fragmentClass=UserSelectionFragment.class;

        }
        // Set which fragment to yse
        replaceFragments(fragmentClass);

        // Sets the correct view for the siderbar based on the current user selected
        navigationView = findViewById(R.id.menu_view);
        changeUserView();

        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        mDrawerLayout = findViewById(R.id.drawer_layout);

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {

                        item.setChecked(true);

                        // Handle navigation view item clicks here.
                        int id = item.getItemId();
                        Fragment fragment = null;
                        Class fragmentClass = null;
                        if (id == R.id.user) {
                            fragmentClass = UserSelectionFragment.class;
                        } else if(id== R.id.apps){
                            fragmentClass = AppListFragment.class;
                        } else if (id == R.id.broadcast) {
                            fragmentClass = BroadcastFragment.class;
                        } else if (id == R.id.homework) {
                            fragmentClass = HomeworkFragment.class;
                        } else if (id == R.id.polls) {
                            fragmentClass = PollsFragment.class;
                        } else if (id == R.id.students) {
                            fragmentClass = StudentsFragment.class;
                        } else if (id == R.id.question) {
                            fragmentClass = QuestionFragment.class;
                        } else if (id == R.id.blocked_apps) {
                            fragmentClass = AppListFragment.class;
//                        } else if (id == R.id.question) {
//                            fragmentClass = QuestionFragment.class;
                        } else {
                            return true;
                        }

                        // Switch fragments
                        replaceFragments(fragmentClass);

                        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                        drawer.closeDrawer(GravityCompat.START);
                        return true;
                    }
                });

        //Initialise DB with installed apps
        // This needs to only be run once! As it will overwrite the previous entries!
        List<App> apps = new AppCollector(context).getInstalledApps();
        EdumaiteDB db = Room.databaseBuilder(this, EdumaiteDB.class, "edumaite_db")
                .build();


        avm = new AppViewModel(getApplication());
//        for(App app: apps) {
//            avm.insert(app);
//
//        }

        // Initialise network receiver
        mNetworkReceiver = new NetworkChangeReceiver(new Handler());
        registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        AppViewModel mAppViewModel;
        mAppViewModel = ViewModelProviders.of(this).get(AppViewModel.class);
        mAppViewModel.getAllApps().observe(this, new Observer<List<App>>() {
            @Override
            public void onChanged(@Nullable final List<App> apps) {
                Log.i("adam", "ToggleFragment on changed called");
                // Update the cached copy of the words in the adapter.
                List<String> app_names = new ArrayList<String>();
                for(App app : apps){
                    if(app.blacklisted){
                        app_names.add(app.packageName);
                    }
                }
                startTimer(context, app_names);

            }
        });
        // Initialise foreground app checker
        //startTimer(context);

    }

    public void changeUserView(){
        Toast.makeText(context, "Setting menus to " +user, Toast.LENGTH_LONG).show();
        if(user == "student") {
            navigationView.getMenu().setGroupVisible(R.id.student_group, true);
            navigationView.getMenu().setGroupVisible(R.id.teacher_group, false);
        }
        else{
            navigationView.getMenu().setGroupVisible(R.id.student_group, false);
            navigationView.getMenu().setGroupVisible(R.id.teacher_group, true);
        }
    }

    public void setTitle(String title){
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
    }

    public void replaceFragments(Class fragmentClass) {
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment)
                .commit();
    }


    // Need a better way of running this background task.
    // Needs to be able to handle live data and ideally run when the app is closed
    //To start timer
    private void startTimer(final Context context, List<String> apps){

        Log.i("adam","startTimer called with app list: " + apps);
        timer = new Timer();
        timerTask = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        String foregroundapp = getForegroundAppName(context);
                        Log.i("Adam", "Current foreground app: " + foregroundapp);
                        //Toast.makeText(context, "Current foreground app: " + foregroundapp, Toast.LENGTH_SHORT).show();
                        if(apps.contains(foregroundapp)){
                            Log.i("Adam", foregroundapp + " app has been caught!");
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
        //getSupportFragmentManager().popBackStack();
        unregisterNetworkChanges();
    }

}