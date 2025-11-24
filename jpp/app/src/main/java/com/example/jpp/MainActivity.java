package com.example.jpp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import com.example.jpp.api.ApiClient;
import com.example.jpp.api.ApiService;
import com.example.jpp.model.Utilisateur;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private ApiService apiService;
    private static final String PREFS_NAME = "JppPrefs";
    private static final String KEY_USER_ID = "userId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        apiService = ApiClient.getClient().create(ApiService.class);
        checkUser();

        Button btnQuestionnaire = findViewById(R.id.btnQuestionnaire);
        Button btnScan = findViewById(R.id.btnScan);

        btnQuestionnaire.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, QuestionnaireActivity.class);
            startActivity(intent);
        });

        btnScan.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ScanActivity.class);
            startActivity(intent);
        });

        loadConseilDuJour();
    }

    private void loadConseilDuJour() {
        TextView tvConseil = findViewById(R.id.tvConseil);
        apiService.getAllConseils().enqueue(new Callback<java.util.List<com.example.jpp.model.Conseil>>() {
            @Override
            public void onResponse(Call<java.util.List<com.example.jpp.model.Conseil>> call,
                    Response<java.util.List<com.example.jpp.model.Conseil>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    java.util.List<com.example.jpp.model.Conseil> conseils = response.body();
                    int randomIndex = (int) (Math.random() * conseils.size());
                    com.example.jpp.model.Conseil conseil = conseils.get(randomIndex);
                    tvConseil.setText("Conseil du jour :\n" + conseil.getTitre() + "\n" + conseil.getDescription());
                } else {
                    tvConseil.setText("Aucun conseil disponible pour le moment.");
                }
            }

            @Override
            public void onFailure(Call<java.util.List<com.example.jpp.model.Conseil>> call, Throwable t) {
                tvConseil.setText("Impossible de charger le conseil du jour.");
            }
        });
    }

    private void checkUser() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        long userId = settings.getLong(KEY_USER_ID, -1);

        if (userId == -1) {
            // No user found, try to find by email or create
            String email = "android@example.com";
            apiService.getUtilisateurByEmail(email).enqueue(new Callback<Utilisateur>() {
                @Override
                public void onResponse(Call<Utilisateur> call, Response<Utilisateur> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        saveUserId(response.body().getIdUtilisateur());
                    } else {
                        // User not found, create new
                        createDefaultUser();
                    }
                }

                @Override
                public void onFailure(Call<Utilisateur> call, Throwable t) {
                    // Network error, maybe try again later or ignore
                }
            });
        }
    }

    private void createDefaultUser() {
        Utilisateur newUser = new Utilisateur();
        newUser.setNom("Android User");
        newUser.setEmail("android@example.com");

        apiService.createUtilisateur(newUser).enqueue(new Callback<Utilisateur>() {
            @Override
            public void onResponse(Call<Utilisateur> call, Response<Utilisateur> response) {
                if (response.isSuccessful() && response.body() != null) {
                    saveUserId(response.body().getIdUtilisateur());
                }
            }

            @Override
            public void onFailure(Call<Utilisateur> call, Throwable t) {
                // Failed to create
            }
        });
    }

    private void saveUserId(Long id) {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(KEY_USER_ID, id);
        editor.apply();
    }
}
