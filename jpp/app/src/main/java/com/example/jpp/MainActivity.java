package com.example.jpp;

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

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView question = findViewById(R.id.questionText);
        Spinner answerSpinner = findViewById(R.id.answerSpinner);
        Button submit = findViewById(R.id.submitButton);

        String[] answers = {"Paris", "Lyon", "Marseille", "Toulouse"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, answers);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        answerSpinner.setAdapter(adapter);

        submit.setOnClickListener(v -> {
            String selectedAnswer = answerSpinner.getSelectedItem().toString();
            Toast.makeText(this, "Réponse sélectionnée : " + selectedAnswer, Toast.LENGTH_SHORT).show();
        });
    }
}
