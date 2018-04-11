package com.colonelfund.colonelfund;

import android.app.Application;
import android.content.Context;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * Leak Canary class for mem leak detection.
 */
public class LeakCanaryApp extends Application {
    private RefWatcher refWatcher;

    /**
     * Sets reference water for app.
     *
     * @param context for activity.
     * @return reference watcher.
     */
    public static RefWatcher getRefWatcher(Context context) {
        LeakCanaryApp application = (LeakCanaryApp) context.getApplicationContext();
        return application.refWatcher;
    }

    /**
     * overrides on create to add leak canary.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        refWatcher = LeakCanary.install(this);
    }
}
