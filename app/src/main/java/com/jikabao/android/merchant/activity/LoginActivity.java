package com.jikabao.android.merchant.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jikabao.android.common.activity.BaseActivity;
import com.jikabao.android.common.util.ToastUtil;
import com.jikabao.android.merchant.R;
import com.jikabao.android.merchant.storage.AppPreference;

/**
 * Created by pjq on 1/4/15.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private EditText username;
    private EditText password;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_layout);

        init();
    }

    private void init() {
        username = (EditText) findViewById(R.id.usernameEditText);
        password = (EditText) findViewById(R.id.passwordEditText);
        login = (Button) findViewById(R.id.loginButton);

        String user = AppPreference.getInstance().getUserId();
        String pass = AppPreference.getInstance().getUserPassword();
        username.setText(user);
        password.setText(pass);

        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.loginButton:
                clickLogin();

                break;
        }
    }

    private void clickLogin() {
        String user = username.getText().toString();
        String pass = password.getText().toString();
        if (TextUtils.isEmpty(user)) {
            ToastUtil.showToast(getApplicationContext(), getString(R.string.username_is_empty));
            return;
        }

        if (TextUtils.isEmpty(pass)) {
            ToastUtil.showToast(getApplicationContext(), getString(R.string.password_is_empty));
            return;
        }

        new LoginAsyncTask().execute((Void) null);
    }


    class LoginAsyncTask extends AsyncTask {
        ProgressDialog progressDialog;

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
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setTitle(getString(R.string.login));
            progressDialog.setMessage(getString(R.string.login_please_wait));
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            progressDialog.dismiss();
            progressDialog = null;

            onLoginSuccess();
        }
    }

    private void onLoginSuccess() {
        AppPreference.getInstance().setUserId(username.getText().toString());
        AppPreference.getInstance().setUserPassword(password.getText().toString());
        ToastUtil.showToast(getApplicationContext(), getString(R.string.login_success));
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
