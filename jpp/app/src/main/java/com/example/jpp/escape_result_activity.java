package com.example.jpp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class escape_result_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escape_result);

        String[] userAnswers = getIntent().getStringArrayExtra("USER_ANSWERS");

        double scoreGlobal = calculerScore(userAnswers);

        TextView scoreText = findViewById(R.id.scoreText);
        TextView interpretationText = findViewById(R.id.interpretationText);

        scoreText.setText(String.format("Votre score : %.0f/100", scoreGlobal));
    }

    private double calculerScore(String[] reponses) {
        int nombrereponsebonne = 0;


        return nombrereponsebonne;
    }
}
