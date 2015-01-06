package com.jikabao.android.merchant.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.jikabao.android.common.activity.BaseActivity;
import com.jikabao.android.merchant.BuildConfig;
import com.jikabao.android.merchant.R;

/**
 * Created by pjq on 1/4/15.
 */
public class AboutActivity extends BaseActivity {
    TextView buildInfo;
    TextView aboutTitle;
    TextView aboutVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.about_layout);

        buildInfo = (TextView) findViewById(R.id.buildInfo);
        aboutTitle = (TextView) findViewById(R.id.aboutTitle);
        aboutVersion = (TextView) findViewById(R.id.aboutVersion);

        aboutVersion.setText(BuildConfig.VERSION_NAME + "(" + BuildConfig.VERSION_CODE + ")");

        buildInfo.append("" + BuildConfig.BUILD_DATE + '\n');
        buildInfo.append("" + BuildConfig.BUILD_TYPE + '\n');
        buildInfo.append("" + BuildConfig.GIT_BRANCH + '\n');
        buildInfo.append("" + BuildConfig.GIT_COMMIT_ID + '\n');
        buildInfo.append("" + BuildConfig.VERSION_NAME + '\n');
        buildInfo.append("" + BuildConfig.VERSION_CODE + '\n');
        buildInfo.append("" + BuildConfig.FLAVOR + '\n');
    }
}
