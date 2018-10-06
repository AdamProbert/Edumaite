package com.edumaite.adam.edumiate_poc.dataCollection;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.util.Log;

import java.util.List;

public class AppCollector {

    private Context context;

    public AppCollector(Context context){
        this.context = context;

    }

    public List<ResolveInfo> getInstalledApps (){
        Log.i("Adam", "Collecting installed AppCollector");
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> pkgAppsList = context.getPackageManager().queryIntentActivities( mainIntent, 0);
        for(ResolveInfo app:pkgAppsList)
        {
            Log.i("Adam", "App: " +app.toString());
        }
        Log.i("Adam", "Collected installed AppCollector");
        return pkgAppsList;
    }
}
