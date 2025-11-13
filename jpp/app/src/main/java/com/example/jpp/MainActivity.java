package com.example.jpp;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TextView questionText;
    private Spinner answerSpinner;
    private Button submitButton;
    private Button backButton;
    private TextToSpeech tts;

    private int currentQuestion = 0;
    private String[] questions = {
            "1/10 : En moyenne, combien d’heures par jour passez-vous sur votre smartphone ?",
            "2/10 : Combien de temps passez-vous chaque jour devant un ordinateur ?",
            "3/10 : Combien d’heures de vidéos en streaming regardez-vous par semaine (Netflix, YouTube, Twitch, etc.) ?",
            "4/10 : Regardez-vous vos vidéos en qualité :",
            "5/10 : À quelle fréquence changez-vous de smartphone ?",
            "6/10 : Combien d’appareils numériques possédez-vous actuellement (ordinateur, smartphone, tablette, console, TV connectée, etc.) ?",
            "7/10 : Stockez-vous principalement vos photos et fichiers :",
            "8/10 : Sauvegardez-vous régulièrement vos mails ou les laissez-vous indéfiniment dans votre boîte ?",
            "9/10 : Avez-vous déjà réparé un appareil électronique au lieu de le remplacer ?",
            "10/10 : Connaissez-vous l’impact énergétique de vos usages numériques par rapport à la moyenne nationale ?"
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
        backButton = findViewById(R.id.backButton);

        // Initialisation du TextToSpeech en français
        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                int res = tts.setLanguage(Locale.FRANCE);
                if (res == TextToSpeech.LANG_MISSING_DATA || res == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Toast.makeText(this, "TTS: langue Française non disponible", Toast.LENGTH_SHORT).show();
                } else {
                    // Lire la question initiale si déjà affichée
                    speakCurrentQuestion();
                }
            } else {
                Toast.makeText(this, "TTS non initialisé", Toast.LENGTH_SHORT).show();
            }
        });

        showQuestion();

        String[] userAnswers = new String[questions.length];

        submitButton.setOnClickListener(v -> {
            int selectedIndex = answerSpinner.getSelectedItemPosition();
            userAnswers[currentQuestion] = String.valueOf(selectedIndex);
            currentQuestion++;
            if (currentQuestion < questions.length) {
                showQuestion();
            } else {
                Intent intent = new Intent(MainActivity.this, ScoreActivity.class);
                intent.putExtra("USER_ANSWERS", userAnswers);
                startActivity(intent);
                finish();
            }
        });

        backButton.setOnClickListener(v -> {
            if (currentQuestion > 0) {
                currentQuestion--;
                showQuestion();
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
        // Lire la question à voix haute
        speakCurrentQuestion();
    }

    private void speakCurrentQuestion() {
        if (tts == null) return;
        String text = questions[currentQuestion];
        // Utilise QUEUE_FLUSH pour remplacer l'éventuelle lecture en cours
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "Q" + currentQuestion);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tts != null) {
            tts.stop();
            tts.shutdown();
            tts = null;
        }
    }
}
