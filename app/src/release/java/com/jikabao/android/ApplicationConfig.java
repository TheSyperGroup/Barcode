package com.jikabao.android;

import android.content.Context;


/**
 * Release configuration.
 */
public class ApplicationConfig implements AppConfig {
    private static ApplicationConfig instance;
    private final String hostname;
    private String httpsScheme = "https://";

    private ApplicationConfig(Context context) {
        hostname = context.getString(R.string.hostname);
    }

    public static void init(Context context) {
        instance = new ApplicationConfig(context);
    }

    public static ApplicationConfig getInstance() {
        return instance;
    }


    @Override
    public boolean isDebug() {
        return false;
    }

    @Override
    public boolean isUsingSdcard() {
        return false;
    }

    @Override
    public boolean isDryRun() {
        return false;
    }

    @Override
    public String getHostname() {
        return hostname;
    }

    @Override
    public String getAppRevision() {
        return "";
    }

}
