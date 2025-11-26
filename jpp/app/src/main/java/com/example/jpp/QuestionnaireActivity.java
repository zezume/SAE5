package com.example.jpp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;
import java.util.List;
import java.util.ArrayList;

import com.example.jpp.api.ApiClient;
import com.example.jpp.api.ApiService;
import com.example.jpp.model.Question;
import com.example.jpp.model.QuestionnaireQuestion;
import com.example.jpp.model.Reponse;
import com.example.jpp.model.Utilisateur;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionnaireActivity extends AppCompatActivity {
    private TextView questionText;
    private Spinner answerSpinner;
    private LinearLayout submitButton;
    private LinearLayout backButton;
    private TextToSpeech tts;
    private boolean isTtsInitialized = false;

    private int currentQuestion = 0;

    private long userId = -1;
    private static final String PREFS_NAME = "JppPrefs";
    private static final String KEY_USER_ID = "userId";

    // Local fallback questions
    private String[] localQuestions = {
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
            { "Moins de 2h", "2h à 4h", "4h à 6h", "Plus de 6h" },
            { "Moins d’1h", "1h à 3h", "3h à 5h", "Plus de 5h" },
            { "Moins de 3h", "3h à 7h", "7h à 15h", "Plus de 15h" },
            { "Standard (SD)", "Haute définition (HD)", "Très haute définition (4K ou plus)" },
            { "Tous les ans", "Tous les 2 ans", "Tous les 3 à 4 ans", "Plus de 4 ans" },
            { "1 à 2", "3 à 4", "5 à 6", "Plus de 6" },
            { "Sur votre appareil (mémoire interne/disque dur)", "Sur un cloud (Google Drive, iCloud, etc.)",
                    "Un mélange des deux" },
            { "Oui, je trie et supprime régulièrement", "Non, je garde tout" },
            { "Oui", "Non" },
            { "Oui", "Non" }
    };

    private List<Question> dbQuestions = new ArrayList<>();
    private String[] userAnswers;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        userId = settings.getLong(KEY_USER_ID, -1);

        questionText = findViewById(R.id.questionText);
        answerSpinner = findViewById(R.id.answerSpinner);
        submitButton = findViewById(R.id.btnSubmit);
        backButton = findViewById(R.id.btnBack);

        TextView textSubmit = submitButton.findViewById(R.id.btnText);
        ImageView iconSubmit = submitButton.findViewById(R.id.btnIcon);

        TextView textBack = backButton.findViewById(R.id.btnText);
        ImageView iconBack = backButton.findViewById(R.id.btnIcon);

        textSubmit.setText("Suivant");

        textBack.setText("Retour");
        iconBack.setImageResource(R.drawable.img_back);

        userAnswers = new String[localQuestions.length];

        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                int res = tts.setLanguage(Locale.FRANCE);
                if (res == TextToSpeech.LANG_MISSING_DATA || res == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Toast.makeText(this, "TTS: langue Française non disponible", Toast.LENGTH_SHORT).show();
                } else {
                    isTtsInitialized = true;
                }
            } else {
                Toast.makeText(this, "TTS non initialisé", Toast.LENGTH_SHORT).show();
            }
        });

        apiService = ApiClient.getClient().create(ApiService.class);
        fetchQuestions();

        submitButton.setOnClickListener(v -> {
            int selectedIndex = answerSpinner.getSelectedItemPosition();
            String selectedValue = (String) answerSpinner.getSelectedItem();

            if (currentQuestion < userAnswers.length) {
                userAnswers[currentQuestion] = String.valueOf(selectedIndex);
            }

            sendAnswer(selectedValue);

            currentQuestion++;
            int totalQuestions = dbQuestions.isEmpty() ? localQuestions.length : dbQuestions.size();

            if (currentQuestion < totalQuestions) {
                showQuestion();
            } else {
                Intent intent = new Intent(QuestionnaireActivity.this, ScoreActivity.class);
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

    private void fetchQuestions() {
        apiService.getQuestionsByQuestionnaire(1L).enqueue(new Callback<List<QuestionnaireQuestion>>() {
            @Override
            public void onResponse(Call<List<QuestionnaireQuestion>> call,
                    Response<List<QuestionnaireQuestion>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<QuestionnaireQuestion> qqs = response.body();
                    dbQuestions = new ArrayList<>();
                    for (QuestionnaireQuestion qq : qqs) {
                        if (qq.getQuestion() != null) {
                            dbQuestions.add(qq.getQuestion());
                        }
                    }

                    if (!dbQuestions.isEmpty()) {
                        userAnswers = new String[dbQuestions.size()];
                        showQuestion();
                    } else {
                        showQuestion();
                    }
                } else {
                    showQuestion();
                }
            }

            @Override
            public void onFailure(Call<List<QuestionnaireQuestion>> call, Throwable t) {
                Toast.makeText(QuestionnaireActivity.this, "Mode hors ligne: " + t.getMessage(), Toast.LENGTH_SHORT)
                        .show();
                showQuestion();
            }
        });
    }

    private void showQuestion() {
        String qText;
        String[] qAnswers;

        if (dbQuestions.isEmpty()) {
            if (currentQuestion < localQuestions.length) {
                qText = localQuestions[currentQuestion];
                qAnswers = answers[currentQuestion];
            } else {
                return;
            }
        } else {
            if (currentQuestion < dbQuestions.size()) {
                Question q = dbQuestions.get(currentQuestion);
                qText = q.getIntitule();

                List<Reponse> reponses = q.getReponses();
                if (reponses != null && !reponses.isEmpty()) {
                    qAnswers = new String[reponses.size()];
                    for (int i = 0; i < reponses.size(); i++) {
                        qAnswers[i] = reponses.get(i).getReponse();
                    }
                } else {
                    if (currentQuestion < answers.length) {
                        qAnswers = answers[currentQuestion];
                    } else {
                        qAnswers = new String[] { "Oui", "Non" };
                    }
                }
            } else {
                return;
            }
        }

        questionText.setText(qText);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, qAnswers);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        answerSpinner.setAdapter(adapter);

        int total = dbQuestions.isEmpty() ? localQuestions.length : dbQuestions.size();
        TextView textSubmit = submitButton.findViewById(R.id.btnText);
        if (currentQuestion == total - 1) {
            textSubmit.setText("Terminer");
        } else {
            textSubmit.setText("Suivant");
        }

        speakCurrentQuestion(qText);
    }

    private void sendAnswer(String value) {
        if (dbQuestions.isEmpty())
            return;

        if (currentQuestion < dbQuestions.size()) {
            Question q = dbQuestions.get(currentQuestion);
            Utilisateur u = null;
            if (userId != -1) {
                u = new Utilisateur(userId);
            }

            Reponse reponse = new Reponse(q, u, value);

            apiService.createReponse(reponse).enqueue(new Callback<Reponse>() {
                @Override
                public void onResponse(Call<Reponse> call, Response<Reponse> response) {
                    // Silent success
                }

                @Override
                public void onFailure(Call<Reponse> call, Throwable t) {
                    // Silent failure
                }
            });
        }
    }

    private String normalizeForTts(String text) {
        if (text == null)
            return "";
        text = text.replaceAll("(\\d+)\\s*/\\s*(\\d+)", "$1 sur $2");
        return text;
    }

    private void speakCurrentQuestion(String text) {
        if (tts == null || !isTtsInitialized)
            return;
        String toSpeak = normalizeForTts(text);
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
