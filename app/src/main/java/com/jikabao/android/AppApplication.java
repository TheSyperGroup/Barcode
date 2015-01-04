package com.jikabao.android;

import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;

import com.jikabao.android.util.EFLogger;
import com.jikabao.android.util.FontManager;
import com.jikabao.android.util.LocalPathResolver;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

//public class AppApplication extends Application implements ActivityLifecycleCallbacks {
public class AppApplication extends Application {

	private static final String TAG = AppApplication.class.getName();

	private static Context context;

	private Timer mActivityTransitionTimer;
	private TimerTask mActivityTransitionTimerTask;
	private final long MAX_ACTIVITY_TRANSITION_TIME_MS = 2000;

    private int resumed = 0;
    private int paused = 0;
    private int started = 0;
    private int stopped = 0;

	public static boolean isDebug() {
		return ApplicationConfig.getInstance().isDebug();
	}
	
	@Override
	public void onCreate() {
        context = getApplicationContext();
        ApplicationConfig.init(context);
        setupStrictMode();

		super.onCreate();

//		registerActivityLifecycleCallbacks(this);
        LocalPathResolver.init(getBaseDir());

        FontManager.getInstance().initialize(getApplicationContext(), R.xml.fonts);

        ExecutorService backgroundExecutor = Executors.newSingleThreadExecutor(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r, "BackgroundExecutorThread");
                thread.setPriority(Thread.MIN_PRIORITY);
                return thread;
            }
        });

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

	@Override
	public void onLowMemory() {
		EFLogger.w(TAG, "onLowMemory");
	}
	
	@Override
	public void onTerminate() {
		EFLogger.d(TAG, "onTerminate");
		

	}

//	@Override
//	public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
//
//
//	}
//	@Override
//	public void onActivityStarted(Activity activity) {
//        ++started;
//	}
//
//	@Override
//	public void onActivityResumed(Activity activity) {
//		EFLogger.d(TAG, "onActivityResumed ");
//        ++resumed;
//    }
//
//
//
//	@Override
//	public void onActivityPaused(Activity activity) {
//		EFLogger.d(TAG, "onActivityPaused");
//        ++paused;
//	}
//
//	@Override
//	public void onActivityStopped(Activity activity) {
//        ++stopped;
//	}
//
//	@Override
//	public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
//
//	}
//
//	@Override
//	public void onActivityDestroyed(Activity activity) {
//
//	}
	
//    public boolean isApplicationVisible() {
//        return started > stopped;
//    }
//
//    public boolean isApplicationInForeground() {
//        return resumed > paused;
//    }
	
	public static Context getContext(){
		return context;
	}

}
