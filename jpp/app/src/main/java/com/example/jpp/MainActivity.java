package com.example.jpp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
