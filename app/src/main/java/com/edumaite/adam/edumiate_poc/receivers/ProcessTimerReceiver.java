//package com.edumaite.adam.edumiate_poc.receivers;
//
//import android.annotation.TargetApi;
//import android.app.ActivityManager;
//import android.app.AppOpsManager;
//import android.app.usage.UsageStats;
//import android.app.usage.UsageStatsManager;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.ApplicationInfo;
//import android.content.pm.PackageManager;
//import android.os.Build;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.edumaite.adam.edumiate_poc.MainActivity;
//
//import java.util.List;
//
//
//public class ProcessTimerReceiver extends BroadcastReceiver {
//
//    public String[] blacklist = {"com.google.android.googlequicksearchbox"};
//
//
//    @Override
//    public void onReceive(final Context context, Intent intent) {
//        String foregroundapp = getForegroundAppName(context);
//        Log.i("Adam", "Current foreground app: " + foregroundapp);
//        Toast.makeText(context, "Current foreground app: " + foregroundapp, Toast.LENGTH_SHORT).show();
//        if(isAppBlacklisted(foregroundapp)){
//            Log.i("Adam", "blacklisted app has been caught!");
//            Intent newActivityIntent = new Intent(context, MainActivity.class);
//            newActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
//            context.startActivity(intent);
//            Log.i("Adam", "Activity should have relaunched!");
//
//        }
//
//    }
//
//    private boolean isAppBlacklisted(String currentApp){
//        for(String app: blacklist) {
//            if(app.trim().contains(currentApp))
//                return true;
//        }
//        return false;
//    }
//
//    private String getForegroundAppName(Context context) {
//
//        final String tag = "getForegroundAppName";
//
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//
//            if(needPermissionForBlocking(context)) {
//                Toast.makeText(context, "Turn on Settings->Security->[Apps with usage access]", Toast.LENGTH_SHORT).show();
//                Log.e(tag, "Need to Turn on Settings->Security->[Apps with usage access]");
//            }
//
//            // intentionally using string value as Context.USAGE_STATS_SERVICE was
//            // strangely only added in API 22 (LOLLIPOP_MR1)
//            @SuppressWarnings("WrongConstant")
//            UsageStatsManager usm = (UsageStatsManager) context.getSystemService("usagestats");
//            long now = System.currentTimeMillis();
//            List<UsageStats> usageStatsList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, now - 1000 * 1000, now);
//            if (usageStatsList != null && usageStatsList.size() > 0) {
//                long time = 0;
//                String name = "";
//                for(UsageStats usageStats : usageStatsList) {
//                    if(usageStats.getLastTimeUsed() > time) {
//                        time = usageStats.getLastTimeUsed();
//                        name = usageStats.getPackageName();
//                    }
//                }
//                return name;
//            }
//        } else {
//            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//            try {
//                ActivityManager.RunningTaskInfo foregroundTaskInfo = am.getRunningTasks(1).get(0);
//                return foregroundTaskInfo.topActivity.getPackageName();
//            } catch (Exception e) {
//                Log.e("Adam", "can not get foreground app: " + e.getMessage(), e);
//            }
//        }
//        return "";
//    }
//
//    @TargetApi(Build.VERSION_CODES.KITKAT)
//    private static boolean needPermissionForBlocking(Context context){
//        try {
//            PackageManager packageManager = context.getPackageManager();
//            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
//            AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
//            int mode = appOpsManager.checkOpNoThrow("android:get_usage_stats", applicationInfo.uid, applicationInfo.packageName);
//            return  (mode != AppOpsManager.MODE_ALLOWED);
//        } catch (Throwable e) {
//            return true;
//        }
//    }
//
//}
