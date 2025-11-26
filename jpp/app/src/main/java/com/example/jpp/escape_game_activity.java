package com.example.jpp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jpp.api.ApiClient;
import com.example.jpp.api.ApiService;
import com.example.jpp.model.QuestionnaireQuestion;
import com.example.jpp.model.Reponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class escape_game_activity extends AppCompatActivity {
    private static final int REQUEST_CODE_SCAN = 1001;

    private TextView questionText;
    private Spinner answerSpinner;
    private Button submitButton;
    private Button scanButton;

    private TextToSpeech tts;
    private boolean isTtsInitialized = false;

    private ApiService apiService;
    private List<QuestionnaireQuestion> allQuestions = new ArrayList<>();
    private Map<Integer, String> userAnswers = new HashMap<>();
    private int currentScore = 0;

    private QuestionnaireQuestion currentQuestion = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escape_game);

        questionText = findViewById(R.id.questionText);
        answerSpinner = findViewById(R.id.answerSpinner);
        submitButton = findViewById(R.id.submitButton);
        scanButton = findViewById(R.id.backButton); // Repurposed as Scan Button
        scanButton.setText("Scanner QR Code");

        apiService = ApiClient.getClient().create(ApiService.class);

        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                int res = tts.setLanguage(Locale.FRANCE);
                if (res == TextToSpeech.LANG_MISSING_DATA || res == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Toast.makeText(this, "TTS: langue Française non disponible", Toast.LENGTH_SHORT).show();
                } else {
                    isTtsInitialized = true;
                }
            }
        });

        loadQuestions();

        scanButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, ScanActivity.class);
            startActivityForResult(intent, REQUEST_CODE_SCAN);
        });

        submitButton.setText("Continuer");
        submitButton.setOnClickListener(v -> {
            if (currentQuestion != null) {
                // Mark this question as viewed
                if (!userAnswers.containsKey(currentQuestion.getOrdre())) {
                    userAnswers.put(currentQuestion.getOrdre(), "viewed");
                    currentScore += 10;
                }
                
                showScanState();
                
                // Check if we have scanned all 10 questions
                int targetQuestions = allQuestions.isEmpty() ? 10 : allQuestions.size();
                if (userAnswers.size() >= targetQuestions) {
                    finishGame();
                }
            }
        });

        showScanState();
    }

    private void showScanState() {
        questionText.setText("Scannez un QR Code pour obtenir une question.");
        answerSpinner.setVisibility(View.GONE);
        submitButton.setVisibility(View.GONE);
        scanButton.setVisibility(View.VISIBLE);
        
        if (!userAnswers.isEmpty()) {
             questionText.append("\nQuestions répondues: " + userAnswers.size() + "/10");
        }
    }

    private void showQuestionState(QuestionnaireQuestion qq) {
        currentQuestion = qq;
        String questionText = qq.getQuestion().getIntitule();
        this.questionText.setText(questionText);
        answerSpinner.setVisibility(View.VISIBLE);
        submitButton.setVisibility(View.VISIBLE);
        scanButton.setVisibility(View.GONE);

        loadAnswersAndShowCorrect(qq.getQuestion().getIdQuestion());
        speakQuestion(questionText);
    }

    private void loadQuestions() {
        apiService.getQuestionsByQuestionnaire(2L).enqueue(new Callback<List<QuestionnaireQuestion>>() {
            @Override
            public void onResponse(Call<List<QuestionnaireQuestion>> call,
                    Response<List<QuestionnaireQuestion>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allQuestions = response.body();
                    Toast.makeText(escape_game_activity.this, "Questions chargées: " + allQuestions.size(),
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(escape_game_activity.this, "Erreur chargement questions", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<QuestionnaireQuestion>> call, Throwable t) {
                Toast.makeText(escape_game_activity.this, "Erreur réseau: " + t.getMessage(), Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    private void loadAnswersAndShowCorrect(Long questionId) {
        apiService.getReponsesByQuestion(questionId).enqueue(new Callback<List<Reponse>>() {
            @Override
            public void onResponse(Call<List<Reponse>> call, Response<List<Reponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Reponse> reponses = response.body();
                    
                    // Find the correct answer
                    String correctAnswer = "";
                    for (Reponse r : reponses) {
                        if (Boolean.TRUE.equals(r.getBonne())) {
                            correctAnswer = r.getReponse();
                            break;
                        }
                    }
                    
                    // Display all answers in spinner (for reference)
                    ArrayAdapter<Reponse> adapter = new ArrayAdapter<>(escape_game_activity.this,
                            android.R.layout.simple_spinner_item, reponses);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    answerSpinner.setAdapter(adapter);
                    
                    // Hide spinner and show correct answer in the question text
                    answerSpinner.setVisibility(View.GONE);
                    String currentText = questionText.getText().toString();
                    questionText.setText(currentText + "\n\nRéponse correcte:\n" + correctAnswer);
                }
            }

            @Override
            public void onFailure(Call<List<Reponse>> call, Throwable t) {
                Toast.makeText(escape_game_activity.this, "Erreur chargement réponses", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SCAN) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                String scanResult = data.getStringExtra("SCAN_RESULT");
                if (scanResult != null) {
                    handleScanResult(scanResult);
                }
            }
        }
    }

    private void handleScanResult(String result) {
        int order = -1;
        // Try to find a standalone number between 1 and 10
        // This regex matches a number 1-9 or 10 surrounded by word boundaries
        java.util.regex.Pattern p = java.util.regex.Pattern.compile("\\b([1-9]|10)\\b");
        java.util.regex.Matcher m = p.matcher(result);
        
        if (m.find()) {
            order = Integer.parseInt(m.group(1));
        } else {
            // Fallback: try to parse the whole string if it's just a number
            try {
                int val = Integer.parseInt(result.trim());
                if (val >= 1 && val <= 10) {
                    order = val;
                }
            } catch (NumberFormatException e) {
                // Ignore
            }
        }

        if (order != -1) {
            QuestionnaireQuestion found = null;
            for (QuestionnaireQuestion qq : allQuestions) {
                if (qq.getOrdre() == order) {
                    found = qq;
                    break;
                }
            }
            
            if (found != null) {
                if (userAnswers.containsKey(order)) {
                    Toast.makeText(this, "Question " + order + " déjà répondue !", Toast.LENGTH_SHORT).show();
                    showScanState();
                } else {
                    showQuestionState(found);
                }
            } else {
                Toast.makeText(this, "Question " + order + " non trouvée dans le chargement.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "QR Code invalide. Veuillez scanner un code contenant un numéro de 1 à 10. (Reçu: " + result + ")", Toast.LENGTH_LONG).show();
        }
    }

    private void speakQuestion(String text) {
        if (tts != null && isTtsInitialized) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    private void finishGame() {
        Intent intent = new Intent(this, escape_result_activity.class);
        intent.putExtra("USER_ANSWER", currentScore);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
}
