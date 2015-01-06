package com.jikabao.android.merchant;

import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;

import com.jikabao.android.common.BaseApplication;
import com.jikabao.android.common.util.LocalPathResolver;

import java.io.File;

public class AppApplication extends BaseApplication {

	private static final String TAG = AppApplication.class.getName();

	public static boolean isDebug() {
		return ApplicationConfig.getInstance().isDebug();
	}
	
	@Override
	public void onCreate() {
        context = getApplicationContext();
        ApplicationConfig.init(context);
        setupStrictMode();

		super.onCreate();

        LocalPathResolver.init(getBaseDir());

        ProductConfig.INSTANCE.initialize(context);
    }

    private void setupStrictMode(){
        if(isDebug()) {
            StrictMode.enableDefaults();
        }
    }

    // Gets the root file storage directory.
    private String getBaseDir() {
        String base;
        if (ApplicationConfig.getInstance().isUsingSdcard()) {
            base = Environment.getExternalStorageDirectory().getPath();
        } else {
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                base = context.getExternalCacheDir().getPath();
            } else {
                File filesDir = context.getApplicationContext().getFilesDir();
                base = filesDir.getPath();
            }
        }
        return base;
    }
}
