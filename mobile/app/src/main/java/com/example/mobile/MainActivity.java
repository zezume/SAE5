package com.example.mobile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;


// MainActivity.java

public class MainActivity extends AppCompatActivity {
    private TextView questionText;
    private Spinner answerSpinner;
    private Button submitButton;

    private int currentQuestion = 0;
    private String[] questions = {
            "En moyenne, combien d’heures par jour passez-vous sur votre smartphone ?",
            "Combien de temps passez-vous chaque jour devant un ordinateur ?",
            "Combien d’heures de vidéos en streaming regardez-vous par semaine (Netflix, YouTube, Twitch, etc.) ?",
            "Regardez-vous vos vidéos en qualité :",
            "À quelle fréquence changez-vous de smartphone ?",
            "Combien d’appareils numériques possédez-vous actuellement (ordinateur, smartphone, tablette, console, TV connectée, etc.) ?",
            "Stockez-vous principalement vos photos et fichiers :",
            "Sauvegardez-vous régulièrement vos mails ou les laissez-vous indéfiniment dans votre boîte ?",
            "Avez-vous déjà réparé un appareil électronique au lieu de le remplacer ?",
            "Connaissez-vous l’impact énergétique de vos usages numériques par rapport à la moyenne nationale ?"
    };

    private String[][] answers = {
            {"Moins de 2h", "2h à 4h", "4h à 6h", "Plus de 6h"},
            {"Moins d’1h", "1h à 3h", "3h à 5h", "Plus de 5h"},
            {"Moins de 3h", "3h à 7h", "7h à 15h", "Plus de 15h"},
            {"Standard (SD)", "Haute définition (HD)", "Très haute définition (4K ou plus)"},
            {"Tous les ans", "Tous les 2 ans", "Tous les 3 à 4 ans", "Plus de 4 ans"},
            {"1 à 2", "3 à 4", "5 à 6", "Plus de 6"},
            {"Sur votre appareil (mémoire interne/disque dur)", "Sur un cloud (Google Drive, iCloud, etc.)", "Un mélange des deux"},
            {"Oui, je trie et supprime régulièrement", "Non, je garde tout"},
            {"Oui", "Non"},
            {"Oui", "Non"}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        questionText = findViewById(R.id.questionText);
        answerSpinner = findViewById(R.id.answerSpinner);
        submitButton = findViewById(R.id.submitButton);

        showQuestion();

        String[] userAnswers = new String[questions.length];

        submitButton.setOnClickListener(v -> {
            int selectedIndex = answerSpinner.getSelectedItemPosition();
            userAnswers[currentQuestion] = String.valueOf(selectedIndex);
            currentQuestion++;
            if (currentQuestion < questions.length) {
                showQuestion();
            } else {
                // Lancer l'activité de score
                Intent intent = new Intent(MainActivity.this, ScoreActivity.class);
                intent.putExtra("USER_ANSWERS", userAnswers);
                startActivity(intent);
                finish();
            }
        });


    }

    private void showQuestion() {
        questionText.setText(questions[currentQuestion]);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, answers[currentQuestion]);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        answerSpinner.setAdapter(adapter);
        if (currentQuestion == questions.length - 1) {
            submitButton.setText("Terminer");
        } else {
            submitButton.setText("Suivant");
        }
    }
}
