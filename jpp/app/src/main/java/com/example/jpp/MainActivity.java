package com.example.jpp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vuzix.hud.actionmenu.ActionMenuActivity;

public class MainActivity extends ActionMenuActivity {

    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title = findViewById(R.id.tvTitle);
        title.setText("Bienvenue");
    }
    @Override
    protected boolean onCreateActionMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // ---- Questionnaire ----
        MenuItem questionnaireItem = menu.findItem(R.id.action_questionnaire);
        LinearLayout qLayout = (LinearLayout) questionnaireItem.getActionView();
        TextView qView = qLayout.findViewById(R.id.menuItemText);
        ImageView qIcon = qLayout.findViewById(R.id.menuItemIcon);

        qView.setText("Questionnaire");
        qIcon.setImageResource(R.drawable.img_menu_question);

        // ---- Scan ----
        MenuItem scanItem = menu.findItem(R.id.action_scan);
        LinearLayout sLayout = (LinearLayout) scanItem.getActionView();
        TextView sView = sLayout.findViewById(R.id.menuItemText);
        ImageView sIcon = sLayout.findViewById(R.id.menuItemIcon);

        sView.setText("Scanner");
        sIcon.setImageResource(R.drawable.img_menu_qrcode);

        // ---- Contour épais pour item actif (optionnel) ----
        qView.setBackgroundResource(R.drawable.menu_item_active);

        return true;
    }

    @Override
    protected boolean alwaysShowActionMenu() {
        return true; // Le menu reste affiché en permanence
    }

    @Override
    protected int getDefaultAction() {
        return 0;  // premier item du menu sélectionné par défaut
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
}
