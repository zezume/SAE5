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

import com.example.jpp.api.ApiClient;
import com.example.jpp.api.ApiService;
import com.example.jpp.model.Question;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class escape_game_activity extends AppCompatActivity {
    private TextView questionText;
    private Spinner answerSpinner;
    private Button submitButton;
    private Button backButton;
    private TextToSpeech tts;
    private boolean isTtsInitialized = false;

    private int currentQuestion = 0;

    private String[] questions;
    private String[][] answers;
    private String[] userAnswers;

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escape_game);

        questionText = findViewById(R.id.questionText);
        answerSpinner = findViewById(R.id.answerSpinner);
        submitButton = findViewById(R.id.submitButton);
        backButton = findViewById(R.id.backButton);

        apiService = ApiClient.getClient().create(ApiService.class);

        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                int res = tts.setLanguage(Locale.FRANCE);
                if (res == TextToSpeech.LANG_MISSING_DATA || res == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Toast.makeText(this, "TTS: langue Française non disponible", Toast.LENGTH_SHORT).show();
                } else {
                    isTtsInitialized = true;
                    if (questions != null && questions.length > 0) {
                        speakCurrentQuestion();
                    }
                }
            } else {
                Toast.makeText(this, "TTS non initialisé", Toast.LENGTH_SHORT).show();
            }
        });

        loadQuestions();

        submitButton.setOnClickListener(v -> {
            if (questions == null || answers == null)
                return;

            int selectedIndex = answerSpinner.getSelectedItemPosition();
            // Assuming answers are mapped to A, B, C, D...
            String[] letterMap = { "A", "B", "C", "D" };

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

    private void loadQuestions() {
        apiService.getAllQuestions().enqueue(new Callback<List<Question>>() {
            @Override
            public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Question> questionsList = response.body();
                    questions = new String[questionsList.size()];
                    answers = new String[questionsList.size()][];
                    userAnswers = new String[questionsList.size()];

                    for (int i = 0; i < questionsList.size(); i++) {
                        questions[i] = questionsList.get(i).getIntitule();
                        // Mock answers since API doesn't provide them yet
                        answers[i] = new String[] { "Option A", "Option B", "Option C", "Option D" };
                    }

                    showQuestion();
                } else {
                    Toast.makeText(escape_game_activity.this, "Erreur de récupération des questions",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Question>> call, Throwable t) {
                Toast.makeText(escape_game_activity.this, "Erreur réseau: " + t.getMessage(), Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    private void showQuestion() {
        if (questions == null || questions.length == 0)
            return;

        questionText.setText(questions[currentQuestion]);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                answers[currentQuestion]);
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

    private String normalizeForTts(String text) {
        if (text == null)
            return "";
        // Remplace "2/10" ou "2 / 10" par "2 sur 10"
        text = text.replaceAll("(\\d+)\\s*/\\s*(\\d+)", "$1 sur $2");
        return text;
    }

    private void speakCurrentQuestion() {
        if (tts == null || !isTtsInitialized || questions == null)
            return;
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
