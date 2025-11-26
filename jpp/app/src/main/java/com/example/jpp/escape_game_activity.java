package com.example.jpp;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;

public class escape_game_activity extends AppCompatActivity {
    private TextView questionText;
    private Spinner answerSpinner;
    private Button submitButton;
    private Button backButton;
    private TextToSpeech tts;
    private boolean isTtsInitialized = false;

    private int currentQuestion = 0;
    private String[] questions = {
            "1/10 : Combien de gramme de CO2 equivaut a un mail de 1Mo ?",
            "2/10 : Combien de kilommetre en voiture équivaut a la duré de vie d'un ordinateur ?",
            "3/10 : Quelle est la cause principale de l'emission de CO2 d'une tablette ?",
            "4/10 : Ecouter la radio pendant 1h equivaut a moins qu'un streaming sur internet ? vrai ou faux",
            "5/10 : D'ou provient 80% des emission de CO2 des appareils numerique ?",
            "6/10 : Combien d'année faut-il garder son smartphone pour diviser par deux l'empreinte numerique par rapport a une durée de 2ans ?",
            "7/10 : Le cloud est-il une solution qui n'emet pas de CO2 ?",
            "8/10 : Quel geste simple permet de réduire significativement la consommation énergétique d'un smartphone ?",
            "9/10 : Quel élément consomme le plus d’énergie lors d’une visioconférence ?",
            "10/10 : Est-il plus écologique de réparer un ordinateur plutôt que d'en acheter un neuf ?"
    };

    private String[][] answers = {
            {"A. 20 g de CO₂", "B. 10 g de CO₂", "C. 3 g de CO₂"},
            {"A. 800 km", "B. 1 000 km", "C. 1 400 km"},
            {"A. Le transport du produit", "B. La fabrication de l’appareil", "C. L’utilisation quotidienne"},
            {"Vrai", "Faux"},
            {"A. De leur utilisation", "B. De leur fin de vie (recyclage)", "C. De leur fabrication"},
            {"A. 3 ans", "B. 4 ans", "C. 5 ans"},
            {"Vrai", "Faux"},
            {"A. Réduire la luminosité de l’écran", "B. Désactiver le mode avion", "C. Utiliser un fond d’écran animé"},
            {"A. La caméra vidéo", "B. Le micro", "C. Le chargement de la page web"},
            {"Vrai", "Faux"}
    };

    private String[] userAnswers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escape_game);
        questionText = findViewById(R.id.questionText);
        answerSpinner = findViewById(R.id.answerSpinner);
        submitButton = findViewById(R.id.submitButton);
        backButton = findViewById(R.id.backButton);

        userAnswers = new String[questions.length];

        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                int res = tts.setLanguage(Locale.FRANCE);
                if (res == TextToSpeech.LANG_MISSING_DATA || res == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Toast.makeText(this, "TTS: langue Française non disponible", Toast.LENGTH_SHORT).show();
                } else {
                    isTtsInitialized = true;
                    speakCurrentQuestion();
                }
            } else {
                Toast.makeText(this, "TTS non initialisé", Toast.LENGTH_SHORT).show();
            }
        });

        showQuestion();

        // ... (dans la méthode onCreate, après l'initialisation des boutons)

        submitButton.setOnClickListener(v -> {
            int selectedIndex = answerSpinner.getSelectedItemPosition();
            String[] letterMap = {"A", "B", "C", "D"};

            if (selectedIndex >= 0 && selectedIndex < letterMap.length) {
                userAnswers[currentQuestion] = letterMap[selectedIndex];
            } else {
                userAnswers[currentQuestion] = "";
            }

            currentQuestion++;
            if (currentQuestion < questions.length) {
                showQuestion();
            } else {
                Intent intent = new Intent(escape_game_activity.this, escape_result_activity.class);
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

    // java
    private String normalizeForTts(String text) {
        if (text == null) return "";
        // Remplace "2/10" ou "2 / 10" par "2 sur 10"
        text = text.replaceAll("(\\d+)\\s*/\\s*(\\d+)", "$1 sur $2");
        return text;
    }

    private void speakCurrentQuestion() {
        if (tts == null || !isTtsInitialized) return;
        String raw = questions[currentQuestion];
        String toSpeak = normalizeForTts(raw);
        tts.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null, "Q" + currentQuestion);
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
