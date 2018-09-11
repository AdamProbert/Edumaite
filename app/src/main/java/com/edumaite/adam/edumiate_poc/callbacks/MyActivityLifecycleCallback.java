package com.edumaite.adam.edumiate_poc.callbacks;

import android.app.Activity;
import android.app.Application;

public abstract class MyActivityLifecycleCallback implements Application.ActivityLifecycleCallbacks {

    private static int foregroundActivityCount = 0;

    // ...

    @Override
    public void onActivityResumed(final Activity activity) {
        foregroundActivityCount++;
    }

    @Override
    public void onActivityStopped(final Activity activity) {
        foregroundActivityCount--;
    }

    public static boolean isApplicationInForeground() {
        return foregroundActivityCount > 0;
    }
}