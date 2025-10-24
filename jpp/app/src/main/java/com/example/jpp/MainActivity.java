package com.example.jpp;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.vuzix.sdk.barcode.BarcodeType2;
import com.vuzix.sdk.barcode.ScanResult2;
import com.vuzix.sdk.barcode.ScannerIntent;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_SCAN = 90001;
    private static final String TAG = "VUZIX_BARCODE";
    private boolean cameraToggle = false;

    private TextView tvResult;

    private final String[] requestedBarcodeTypes = {
            BarcodeType2.QR_CODE.name(),
            BarcodeType2.UPC_A.name(),
            BarcodeType2.CODE_128.name()
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvResult = findViewById(R.id.tvResult);
        Button btnScan = findViewById(R.id.btnScan);

        btnScan.setOnClickListener(v -> startScan());
    }

    private void startScan() {
        Intent scannerIntent = new Intent(ScannerIntent.ACTION);
        scannerIntent.putExtra(ScannerIntent.EXTRA_BARCODE2_TYPES, requestedBarcodeTypes);

        scannerIntent.putExtra(ScannerIntent.EXTRA_CAMERA_ID, cameraToggle ? 0 : 1);
        cameraToggle = !cameraToggle;

        try {
            startActivityForResult(scannerIntent, REQUEST_CODE_SCAN);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "L'application de scan Vuzix n'est pas disponible.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_SCAN) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                ScanResult2 scanResult = data.getParcelableExtra(ScannerIntent.RESULT_EXTRA_SCAN_RESULT2);
                if (scanResult != null) {
                    String resultText = scanResult.getText();
                    Log.d(TAG, "Résultat: " + resultText);
                    tvResult.setText(resultText);

                    if (resultText.startsWith("http://") || resultText.startsWith("https://")) {
                        try {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, android.net.Uri.parse(resultText));
                            startActivity(browserIntent);
                        } catch (Exception e) {
                            Toast.makeText(this, "Impossible d'ouvrir le lien.", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "Erreur ouverture lien", e);
                        }
                    }

                } else {
                    Log.d(TAG, "Aucune donnée");
                    tvResult.setText("Aucune donnée");
                }
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
