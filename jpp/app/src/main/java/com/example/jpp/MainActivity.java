package com.example.jpp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jpp.api.ApiClient;
import com.example.jpp.api.ApiService;
import com.example.jpp.model.Conseil;
import com.example.jpp.model.Utilisateur;
import com.google.android.material.card.MaterialCardView;
import com.vuzix.hud.actionmenu.ActionMenuActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends ActionMenuActivity {

    private TextView title;
    private ApiService apiService;
    private static final String PREFS_NAME = "JppPrefs";
    private static final String KEY_USER_ID = "userId";
    private TextView tvConseil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title = findViewById(R.id.tvTitle);
        title.setText("Bienvenue");

        tvConseil = findViewById(R.id.tvConseil);
        apiService = ApiClient.getClient().create(ApiService.class);

        checkUser();
        loadConseilDuJour();
    }

    @Override
    protected boolean onCreateActionMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // ---- Questionnaire ----
        LinearLayout qLayout = (LinearLayout) menu.findItem(R.id.action_questionnaire).getActionView();
        TextView qView = qLayout.findViewById(R.id.menuItemText);
        ImageView qIcon = qLayout.findViewById(R.id.menuItemIcon);
        qView.setText("Questionnaire");
        qIcon.setImageResource(R.drawable.img_menu_question);

        // ---- Scan ----
        LinearLayout sLayout = (LinearLayout) menu.findItem(R.id.action_scan).getActionView();
        TextView sView = sLayout.findViewById(R.id.menuItemText);
        ImageView sIcon = sLayout.findViewById(R.id.menuItemIcon);
        sView.setText("Scanner");
        sIcon.setImageResource(R.drawable.img_menu_qrcode);

        // ---- Escape Game ----
        LinearLayout eLayout = (LinearLayout) menu.findItem(R.id.action_escape).getActionView();
        TextView eView = eLayout.findViewById(R.id.menuItemText);
        ImageView eIcon = eLayout.findViewById(R.id.menuItemIcon);
        eView.setText("Escape Game");
        eIcon.setImageResource(R.drawable.img_menu_escape);

        return true;
    }

    @Override
    protected boolean alwaysShowActionMenu() {
        return true;
    }

    @Override
    protected int getDefaultAction() {
        return 0;
    }

    // --- Actions du menu ---

    public void openQuestionnaire(MenuItem item) {
        Intent intent = new Intent(this, QuestionnaireActivity.class);
        startActivity(intent);
    }

    public void openScan(MenuItem item) {
        Intent intent = new Intent(this, ScanActivity.class);
        startActivity(intent);
    }

    public void openEscape(MenuItem item) {
        Intent intent = new Intent(this, escape_game_activity.class);
        startActivity(intent);
    }

    // --- Conseil du jour ---

    private void loadConseilDuJour() {
        apiService.getAllConseils().enqueue(new Callback<List<Conseil>>() {
            @Override
            public void onResponse(Call<List<Conseil>> call, Response<List<Conseil>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    List<Conseil> conseils = response.body();
                    int randomIndex = (int) (Math.random() * conseils.size());
                    Conseil conseil = conseils.get(randomIndex);
                    tvConseil.setText("Conseil du jour :\n" + conseil.getTitre() + "\n" + conseil.getDescription());
                } else {
                    tvConseil.setText("Aucun conseil disponible pour le moment.");
                }
            }

            @Override
            public void onFailure(Call<List<Conseil>> call, Throwable t) {
                tvConseil.setText("Impossible de charger le conseil du jour.");
            }
        });
    }

    private void checkUser() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        long userId = settings.getLong(KEY_USER_ID, -1);

        if (userId == -1) {
            String email = "android@example.com";
            apiService.getUtilisateurByEmail(email).enqueue(new Callback<Utilisateur>() {
                @Override
                public void onResponse(Call<Utilisateur> call, Response<Utilisateur> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        saveUserId(response.body().getIdUtilisateur());
                    } else {
                        createDefaultUser();
                    }
                }

                @Override
                public void onFailure(Call<Utilisateur> call, Throwable t) {
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
