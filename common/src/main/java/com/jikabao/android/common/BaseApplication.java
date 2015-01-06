package com.jikabao.android.common;

import android.app.Application;
import android.content.Context;

import com.jikabao.android.common.R;
import com.jikabao.android.common.util.EFLogger;
import com.jikabao.android.common.util.FontManager;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

//public class AppApplication extends Application implements ActivityLifecycleCallbacks {
public class BaseApplication extends Application {

	private static final String TAG = BaseApplication.class.getName();

	protected static Context context;

	private Timer mActivityTransitionTimer;
	private TimerTask mActivityTransitionTimerTask;
	private final long MAX_ACTIVITY_TRANSITION_TIME_MS = 2000;

    private int resumed = 0;
    private int paused = 0;
    private int started = 0;
    private int stopped = 0;

	@Override
	public void onCreate() {
        context = getApplicationContext();
		super.onCreate();

//		registerActivityLifecycleCallbacks(this);

        FontManager.getInstance().initialize(getApplicationContext(), R.xml.fonts);

        ExecutorService backgroundExecutor = Executors.newSingleThreadExecutor(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r, "BackgroundExecutorThread");
                thread.setPriority(Thread.MIN_PRIORITY);
                return thread;
            }
        });

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
