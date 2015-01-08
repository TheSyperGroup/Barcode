package com.jikabao.android.merchant.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.jikabao.android.common.activity.BaseActivity;
import com.jikabao.android.common.activity.WechatActivity;
import com.jikabao.android.common.util.BarcodeUtil;
import com.jikabao.android.common.util.ToastUtil;
import com.jikabao.android.merchant.R;
import com.jikabao.android.merchant.activity.AboutActivity;
import com.jikabao.android.merchant.storage.AppPreference;


public class MainActivity extends WechatActivity implements View.OnClickListener {
    Button scannerButton;
    Button shareButton;
    Button generateButton;
    Button aboutButton;
    Button wechatLogin;

    TextView userInfo;
    ImageView qrImageView;
    EditText input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        scannerButton = (Button) findViewById(R.id.scannerButton);
        shareButton = (Button) findViewById(R.id.shareButton);
        generateButton = (Button) findViewById(R.id.generateButton);
        aboutButton = (Button) findViewById(R.id.aboutButton);
        wechatLogin = (Button) findViewById(R.id.wechatButton);
        qrImageView = (ImageView) findViewById(R.id.qrImageView);
        input = (EditText) findViewById(R.id.inputEditText);

        userInfo = (TextView) findViewById(R.id.userInfo);

        scannerButton.setOnClickListener(this);
        shareButton.setOnClickListener(this);
        generateButton.setOnClickListener(this);
        aboutButton.setOnClickListener(this);
        wechatLogin.setOnClickListener(this);

        userInfo.setText(AppPreference.getInstance().getUserId());
        input.setText(AppPreference.getInstance().getUserId());
        generateQRCode(AppPreference.getInstance().getUserId());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.scannerButton:
                startScanner();

                break;
            case R.id.shareButton:
                shareBarcode();
                break;

            case R.id.generateButton:
                generateQRCode();
                break;

            case R.id.aboutButton:
                showAbout();
                break;

            case R.id.wechatButton:
                loginWithWeixin();
                break;

            default:
                break;
        }

    }

    private void showAbout() {
        Intent intent = new Intent();
        intent.setClass(this, AboutActivity.class);
        startActivity(intent);
    }

    private void startScanner() {
        IntentIntegrator integrator = new IntentIntegrator(this);
//        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("Scan a QRCode");
        integrator.setResultDisplayDuration(0);
        integrator.autoWide();  // Wide scanning rectangle, may work better for 1D barcodes
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.initiateScan();
    }

    public void shareBarcode() {
        String text = input.getText().toString();
        if (TextUtils.isEmpty(text)) {
            ToastUtil.showToast(this, "Input some text!");
            return;
        }

        new IntentIntegrator(this).shareText(text);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                ToastUtil.showToast(getApplicationContext(), "Cancelled");
            } else {
                input.setText(result.getContents());
                ToastUtil.showToast(getApplicationContext(), "Scanned: " + result.getContents());
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    private void generateQRCode(String text) {
        Bitmap bitmap = BarcodeUtil.generateQRCode(text);
        if (null != bitmap) {
            qrImageView.setImageBitmap(bitmap);
        }
    }

    private void generateQRCode() {
        String text = input.getText().toString();
        if (TextUtils.isEmpty(text)) {
            ToastUtil.showToast(this, "Input some text!");
            return;
        }

        generateQRCode(text);
        userInfo.setText(text);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            if (pressAgainToExit()) {
                return true;
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    private long lastPressBackTime;
    private static final int PRESS_AGAIN_LIMIT_DURATION = 3;

    private boolean pressAgainToExit() {
        long current = System.currentTimeMillis();
        long duration = (current - lastPressBackTime) / 1000;

        if (duration <= PRESS_AGAIN_LIMIT_DURATION) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

//                    exitApp();
                    finish();
                }
            });
            return true;
        } else {
            lastPressBackTime = current;
            ToastUtil.showToast(getApplicationContext(), getString(R.string.press_again_to_exit));
        }

        return false;
    }

}
