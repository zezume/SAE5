package com.example.jpp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnQuestionnaire = findViewById(R.id.btnQuestionnaire);
        Button btnScan = findViewById(R.id.btnScan);

        btnQuestionnaire.setOnClickListener(v -> {
//à ajouter : questionnaire
        });

        btnScan.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ScanActivity.class);
            startActivity(intent);
        });
    }
}
