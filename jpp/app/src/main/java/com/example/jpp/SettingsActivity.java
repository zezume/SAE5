package com.example.jpp;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "JppPrefs";
    private static final String KEY_PRIMARY = "bg_primary";
    private static final String KEY_SECONDARY = "bg_secondary";

    private final int[] COLORS = {
            0xFF1E88E5, 0xFF43A047, 0xFFF4511E, 0xFF8E24AA, 0xFF3949AB,
            0xFF00897B, 0xFF6D4C41, 0xFF546E7A, 0xFF212121, 0xFFFFFFFF
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        View root = findViewById(R.id.main);
        Button btnPrimary = findViewById(R.id.btnPrimary);
        Button btnSecondary = findViewById(R.id.btnSecondary);

        applyPrimaryColor(root);

        btnPrimary.setOnClickListener(v ->
                showColorPicker(KEY_PRIMARY, root)
        );

        btnSecondary.setOnClickListener(v ->
                showColorPicker(KEY_SECONDARY, root)
        );
    }

    private void showColorPicker(String key, View root) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_color_picker, null);
        GridLayout grid = (GridLayout) dialogView;

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Choisir une couleur")
                .setView(dialogView)
                .create();

        for (int color : COLORS) {
            View colorView = new View(this);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 120;
            params.height = 120;
            params.setMargins(16, 16, 16, 16);
            colorView.setLayoutParams(params);
            colorView.setBackgroundColor(color);

            colorView.setOnClickListener(v -> {
                saveColor(key, color);
                if (key.equals(KEY_PRIMARY)) {
                    root.setBackgroundColor(color);
                }
                dialog.dismiss();
            });

            grid.addView(colorView);
        }

        dialog.show();
    }

    private void saveColor(String key, int color) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        prefs.edit().putInt(key, color).apply();
    }

    private void applyPrimaryColor(View root) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int color = prefs.getInt(KEY_PRIMARY, Color.WHITE);
        root.setBackgroundColor(color);
    }
}
