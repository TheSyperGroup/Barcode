package com.jikabao.android.merchant.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.jikabao.android.common.activity.BaseActivity;
import com.jikabao.android.merchant.R;

/**
 * Created by pjq on 1/4/15.
 */
public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash_layout);

        new SplashAsyncTask().execute((Void)null);
    }

    class SplashAsyncTask extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] params) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            Intent intent = new Intent();
            intent.setClass(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
