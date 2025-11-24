package com.example.jpp;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.jpp.api.ApiClient;
import com.example.jpp.api.ApiService;
import com.example.jpp.model.Conseil;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScoreActivity extends AppCompatActivity {

    // Facteurs d'impact et moyennes nationales
    private final double[][] impactFactors = {
            { 50, 100, 150, 200 }, // Smartphone (gCO2/jour)
            { 30, 80, 120, 180 }, // Ordinateur (gCO2/jour)
            { 200, 500, 1000, 1500 }, // Streaming (gCO2/semaine)
            { 1.0, 1.5, 3.0 }, // Qualité vidéo (multiplicateur)
            { 300, 150, 100, 75 }, // Renouvellement smartphone (gCO2/jour)
            { 200, 400, 600, 800 }, // Nombre d'appareils (gCO2/jour)
            { 50, 100, 75 }, // Stockage (gCO2/jour)
            { 20, 50 }, // Mails (gCO2/jour)
            { 0.8, 1.0 }, // Réparation (multiplicateur)
            { 1.0, 1.2 } // Sensibilisation (multiplicateur)
    };

    private final double[] moyennesNationales = {
            125, // Smartphone moyen
            100, // Ordinateur moyen
            750, // Streaming moyen
            1.5, // Qualité vidéo moyenne
            125, // Renouvellement moyen
            500, // Appareils moyen
            75, // Stockage moyen
            35, // Mails moyen
            0.9, // Réparation moyenne
            1.1 // Sensibilisation moyenne
    };

    private final double[] ponderations = {
            0.15, 0.12, 0.20, 0.08, 0.15, 0.10, 0.05, 0.03, 0.07, 0.05
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        String[] userAnswers = getIntent().getStringArrayExtra("USER_ANSWERS");

        double scoreGlobal = calculerScore(userAnswers);

        TextView scoreText = findViewById(R.id.scoreText);
        TextView interpretationText = findViewById(R.id.interpretationText);

        scoreText.setText(String.format("Votre score : %.0f/100", scoreGlobal));
        interpretationText.setText(getInterpretation(scoreGlobal));

        // Fetch advice
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.getAllConseils().enqueue(new Callback<List<Conseil>>() {
            @Override
            public void onResponse(Call<List<Conseil>> call, Response<List<Conseil>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    List<Conseil> conseils = response.body();
                    // Pick a random advice
                    int randomIndex = (int) (Math.random() * conseils.size());
                    Conseil conseil = conseils.get(randomIndex);

                    String currentText = interpretationText.getText().toString();
                    String adviceText = "\n\nConseil : " + conseil.getTitre() + "\n" + conseil.getDescription();
                    interpretationText.setText(currentText + adviceText);
                }
            }

            @Override
            public void onFailure(Call<List<Conseil>> call, Throwable t) {
                // Ignore failure
            }
        });
    }

    private double calculerScore(String[] reponses) {
        double sommePonderee = 0;
        double sommePoids = 0;

        for (int i = 0; i < reponses.length && i < impactFactors.length; i++) {
            int indexReponse = Integer.parseInt(reponses[i]);

            double impactUtilisateur = getImpactUtilisateur(i, indexReponse);
            double scorePartiel = (impactUtilisateur / moyennesNationales[i]) * 100;

            sommePonderee += scorePartiel * ponderations[i];
            sommePoids += ponderations[i];
        }

        return sommePonderee / sommePoids;
    }

    private double getImpactUtilisateur(int questionIndex, int reponseIndex) {
        if (questionIndex == 3) { // Qualité vidéo (multiplicateur)
            return impactFactors[questionIndex][reponseIndex];
        } else if (questionIndex >= 7) { // Questions binaires
            return impactFactors[questionIndex][reponseIndex];
        } else {
            return impactFactors[questionIndex][reponseIndex];
        }
    }

    private String getInterpretation(double score) {
        if (score < 80) {
            return "Excellent ! Votre impact numérique est bien inférieur à la moyenne.";
        } else if (score < 100) {
            return "Bien ! Votre impact est légèrement inférieur à la moyenne.";
        } else if (score < 120) {
            return "Moyen. Votre impact est proche de la moyenne nationale.";
        } else {
            return "À améliorer. Votre impact numérique est supérieur à la moyenne.";
        }
    }
}
