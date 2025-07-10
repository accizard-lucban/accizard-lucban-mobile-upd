package com.example.accizardlucban;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class AlertsActivity extends AppCompatActivity {

    private Spinner filterSpinner;
    private ImageView profileIcon;
    private LinearLayout navHome, navChat, navReport, navMap, navAlerts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alerts);

        initViews();
        setupSpinner();
        setupClickListeners();
    }

    private void initViews() {
        filterSpinner = findViewById(R.id.filter_spinner);
        profileIcon = findViewById(R.id.profile_icon);

        // Bottom navigation
        navHome = findViewById(R.id.nav_home);
        navChat = findViewById(R.id.nav_chat);
        navReport = findViewById(R.id.nav_report);
        navMap = findViewById(R.id.nav_map);
        navAlerts = findViewById(R.id.nav_alerts);
    }

    private void setupSpinner() {
        String[] filterOptions = {"All", "Weather Warning", "Flood", "Landslide", "Earthquake", "Road Closure", "Evacuation Order", "Missing Person", "Informational"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, filterOptions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterSpinner.setAdapter(adapter);
    }

    private void setupClickListeners() {
        profileIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlertsActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        navHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Already on home screen
                Intent intent = new Intent(AlertsActivity.this, MainDashboard.class);
                startActivity(intent);
                // Optional: Add transition animation
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });

        navChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Navigate to Chat Activity
                Intent intent = new Intent(AlertsActivity.this, ChatActivity.class);
                startActivity(intent);
                // Optional: Add transition animation
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });

        navReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Navigate to Report Activity
                Intent intent = new Intent(AlertsActivity.this, ReportSubmissionActivity.class);
                startActivity(intent);
                // Optional: Add transition animation
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });

        navMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Navigate to Map Activity
                Intent intent = new Intent(AlertsActivity.this, MapViewActivity.class);
                startActivity(intent);
                // Optional: Add transition animation
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });

        navAlerts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Already on alerts/announcements screen
                Intent intent = new Intent(AlertsActivity.this, AlertsActivity.class);
                startActivity(intent);
                // Optional: Add transition animation
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });
    }
}