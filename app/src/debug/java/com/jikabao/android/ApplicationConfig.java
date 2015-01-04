package com.jikabao.android;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Debug configuration.
 */
public class ApplicationConfig implements AppConfig{
    private static ApplicationConfig instance;
    private final String hostname;

    private ApplicationConfig(Context context) {
        final SharedPreferences sharedPref = context.getSharedPreferences(Constants.SHARED_PREF_NAME, context.MODE_PRIVATE);
        hostname = sharedPref.getString(Constants.SHARED_PREF_KEY_HOSTNAME, context.getString(R.string.hostname));
    }

    public static void init(Context context) {
        instance = new ApplicationConfig(context);
    }

    public static ApplicationConfig getInstance() {
        return instance;
    }

    @Override
    public boolean isDebug() {
        return true;
    }

    @Override
    public boolean isUsingSdcard() {
        return false;
    }

    @Override
    public boolean isDryRun() {
        return true;
    }

    @Override
    public String getHostname() {
        return hostname;
    }

    @Override
    public String getAppRevision() {
        return "DEBUG";
    }

}
