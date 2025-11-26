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

import com.example.jpp.api.ApiClient;
import com.example.jpp.api.ApiService;
import com.example.jpp.model.Conseil;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScanActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_SCAN = 90001;
    private static final String TAG = "VUZIX_BARCODE";
    private boolean cameraToggle = false;

    private TextView tvResult;
    private TextView tvConseil;
    private ApiService apiService;

    private final String[] requestedBarcodeTypes = {
            BarcodeType2.QR_CODE.name(),
            BarcodeType2.UPC_A.name(),
            BarcodeType2.CODE_128.name()
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        tvResult = findViewById(R.id.tvResult);
        tvConseil = findViewById(R.id.tvConseil);
        Button btnScan = findViewById(R.id.btnScan);
        Button btnRetour = findViewById(R.id.btnRetour);

        apiService = ApiClient.getClient().create(ApiService.class);

        btnScan.setOnClickListener(v -> startScan());
        btnRetour.setOnClickListener(v -> finish());
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

                    // Fetch and display advice
                    fetchAndDisplayAdvice();

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

    private void fetchAndDisplayAdvice() {
        tvConseil.setText("Chargement du conseil...");
        apiService.getAllConseils().enqueue(new Callback<List<Conseil>>() {
            @Override
            public void onResponse(Call<List<Conseil>> call, Response<List<Conseil>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    List<Conseil> conseils = response.body();
                    int randomIndex = (int) (Math.random() * conseils.size());
                    Conseil conseil = conseils.get(randomIndex);
                    tvConseil.setText("Conseil : " + conseil.getTitre() + "\n" + conseil.getDescription());
                } else {
                    tvConseil.setText("Pas de conseil disponible.");
                }
            }

            @Override
            public void onFailure(Call<List<Conseil>> call, Throwable t) {
                tvConseil.setText("Erreur lors du chargement du conseil : " + t.getMessage());
                Log.e(TAG, "Erreur API", t);
            }
        });
    }
}
