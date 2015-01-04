package com.jikabao.android;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {
    Button scannerButton;
    Button generateButton;
    TextView buildInfo;
    EditText input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        scannerButton = (Button) findViewById(R.id.scannerButton);
        generateButton = (Button) findViewById(R.id.generatorButton);
        input = (EditText) findViewById(R.id.inputEditText);
        buildInfo = (TextView) findViewById(R.id.buildInfo);

        scannerButton.setOnClickListener(this);
        generateButton.setOnClickListener(this);

        buildInfo.append("" + BuildConfig.BUILD_DATE + '\n');
        buildInfo.append("" + BuildConfig.BUILD_TYPE + '\n');
        buildInfo.append("" + BuildConfig.GIT_BRANCH + '\n');
        buildInfo.append("" + BuildConfig.GIT_COMMIT_ID + '\n');
        buildInfo.append("" + BuildConfig.VERSION_NAME + '\n');
        buildInfo.append("" + BuildConfig.VERSION_CODE + '\n');
        buildInfo.append("" + BuildConfig.FLAVOR + '\n');
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
            case R.id.generatorButton:
                encodeBarcode();
                break;

            default:
                break;
        }

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

    public void encodeBarcode() {
        String text = input.getText().toString();
        if (TextUtils.isEmpty(text)) {
            Toast.makeText(this, "Input some text!", Toast.LENGTH_LONG).show();
            return;
        }

        new IntentIntegrator(this).shareText(text);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                input.setText(result.getContents());
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
