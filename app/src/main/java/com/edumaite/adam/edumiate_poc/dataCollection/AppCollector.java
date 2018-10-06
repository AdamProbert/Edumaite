package com.edumaite.adam.edumiate_poc.dataCollection;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

import com.edumaite.adam.edumiate_poc.models.App;

import java.util.ArrayList;
import java.util.List;

public class AppCollector {

    private Context context;

    public AppCollector(Context context){
        this.context = context;

    }

    public List<App> getInstalledApps () {
        Log.i("Adam", "Collecting installed AppCollector");
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<App> resolvedApps = new ArrayList<App>();
        List<ResolveInfo> pkgAppsList = context.getPackageManager().queryIntentActivities( mainIntent, 0);
        for(ResolveInfo app:pkgAppsList)
        {
            PackageManager packageManager= context.getPackageManager();
            String appName = "";

            try{
                appName = (String) packageManager.getApplicationLabel(packageManager.getApplicationInfo(app.activityInfo.packageName, PackageManager.GET_META_DATA));
                Log.i("InstalledApp", "\nApp name: " +appName);

            }
            catch (PackageManager.NameNotFoundException e){
                Log.e("Adam","Package not found" + e);
                continue;
            }

            Log.i("InstalledApp", "App icon resource: " +app.getIconResource());
            Log.i("InstalledApp", "------------------------------------------------------------------------");

            resolvedApps.add(new App(appName, "image"));
        }
        Log.i("Adam", "Collected installed AppCollector");
        return resolvedApps;
    }
}
