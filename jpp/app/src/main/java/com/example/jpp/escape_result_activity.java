package com.example.jpp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class escape_result_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escape_result);

        // user answer contient le nombre de bonne reponse
        int userAnswer = getIntent().getIntExtra("USER_ANSWER", 0);

        TextView scoreText = findViewById(R.id.scoreText);
        TextView interpretationText = findViewById(R.id.interpretationText);

        scoreText.setText(String.format("Votre score : %.0f/100", userAnswer));
    }
}
