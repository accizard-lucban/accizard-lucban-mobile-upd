package com.example.accizardlucban;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MainDashboard extends AppCompatActivity {

    private TextView userNameText, locationText;
    private TextView reportCountText, emergencyCountText;
    private CardView roadSafetyCard, floodSafetyCard, fireSafetyCard,
            earthquakeSafetyCard, landslideSafetyCard, volcanicSafetyCard;
    private LinearLayout homeTab, chatTab, reportTab, mapTab, alertsTab;
    private View callButton;

    // NEW: ImageView buttons
    private ImageView helpButton, profileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            setContentView(R.layout.activity_dashboard);
            initializeViews();
            setupClickListeners();
            loadUserData();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error loading dashboard: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void initializeViews() {
        try {
            // User info
            userNameText = findViewById(R.id.userNameText);
            locationText = findViewById(R.id.locationText);

            // Report summaries
            reportCountText = findViewById(R.id.reportCountText);
            emergencyCountText = findViewById(R.id.emergencyCountText);

            // Safety tip cards
            roadSafetyCard = findViewById(R.id.roadSafetyCard);
            floodSafetyCard = findViewById(R.id.floodSafetyCard);
            fireSafetyCard = findViewById(R.id.fireSafetyCard);
            earthquakeSafetyCard = findViewById(R.id.earthquakeSafetyCard);
            landslideSafetyCard = findViewById(R.id.landslideSafetyCard);
            volcanicSafetyCard = findViewById(R.id.volcanicSafetyCard);

            // Bottom navigation
            homeTab = findViewById(R.id.homeTab);
            chatTab = findViewById(R.id.chatTab);
            reportTab = findViewById(R.id.reportTab);
            mapTab = findViewById(R.id.mapTab);
            alertsTab = findViewById(R.id.alertsTab);

            // Call button
            callButton = findViewById(R.id.callButton);

            // NEW: Initialize ImageView buttons
            helpButton = findViewById(R.id.helpButton);
            profileButton = findViewById(R.id.profileButton);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error initializing views: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void setupClickListeners() {
        try {
            // Safety tips cards with null checks
            if (roadSafetyCard != null) {
                roadSafetyCard.setOnClickListener(v -> openSafetyTips("Road Safety"));
            }
            if (floodSafetyCard != null) {
                floodSafetyCard.setOnClickListener(v -> openSafetyTips("Flood Safety"));
            }
            if (fireSafetyCard != null) {
                fireSafetyCard.setOnClickListener(v -> openSafetyTips("Fire Safety"));
            }
            if (earthquakeSafetyCard != null) {
                earthquakeSafetyCard.setOnClickListener(v -> openSafetyTips("Earthquake Safety"));
            }
            if (landslideSafetyCard != null) {
                landslideSafetyCard.setOnClickListener(v -> openSafetyTips("Landslide Safety"));
            }
            if (volcanicSafetyCard != null) {
                volcanicSafetyCard.setOnClickListener(v -> openSafetyTips("Volcanic Safety"));
            }

            // Bottom navigation with null checks
            if (homeTab != null) {
                homeTab.setOnClickListener(v -> {
                    // Already on home
                    Toast.makeText(MainDashboard.this, "Already on Home", Toast.LENGTH_SHORT).show();
                });
            }

            if (chatTab != null) {
                chatTab.setOnClickListener(v -> {
                    try {
                        // Navigate to ChatActivity
                        Intent intent = new Intent(MainDashboard.this, ChatActivity.class);
                        startActivity(intent);
                        // Optional: Add transition animation
                        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(MainDashboard.this, "Error opening chat: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            // CORRECTED REPORT TAB CLICK LISTENER
            if (reportTab != null) {
                reportTab.setOnClickListener(v -> {
                    try {
                        // Navigate to ReportSubmissionActivity
                        Intent intent = new Intent(MainDashboard.this, ReportSubmissionActivity.class);
                        startActivity(intent);

                        // Optional: Add transition animation
                        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

                        // Optional: Show confirmation
                        Toast.makeText(MainDashboard.this, "Opening Report Submission", Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(MainDashboard.this, "Error opening report submission: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }

            if (mapTab != null) {
                mapTab.setOnClickListener(v -> {
                    try {
                        Intent intent = new Intent(MainDashboard.this, MapViewActivity.class);
                        startActivity(intent);
                        // Optional: Add transition animation
                        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(MainDashboard.this, "Map feature coming soon", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            if (alertsTab != null) {
                alertsTab.setOnClickListener(v -> {
                    try {
                        Intent intent = new Intent(MainDashboard.this, AlertsActivity.class);
                        startActivity(intent);
                        // Optional: Add transition animation
                        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(MainDashboard.this, "Alerts feature coming soon", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            // Call button
            if (callButton != null) {
                callButton.setOnClickListener(v -> {
                    try {
                        Intent intent = new Intent(MainDashboard.this, EmergencyCallActivity.class);
                        startActivity(intent);
                        // Optional: Add transition animation
                        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(MainDashboard.this, "Emergency call feature coming soon", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            // NEW: ImageView button click listeners
            if (helpButton != null) {
                helpButton.setOnClickListener(v -> {
                    try {
                        // Navigate to HelpActivity or show help dialog
                        Intent intent = new Intent(MainDashboard.this, OnBoardingActivity.class);
                        startActivity(intent);
                        // Optional: Add transition animation
                        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

                        // Alternative: Show help information in a dialog
                        // showHelpDialog();

                    } catch (Exception e) {
                        e.printStackTrace();
                        // Fallback: Show simple help message
                        Toast.makeText(MainDashboard.this, "Help & Support: Contact us for assistance", Toast.LENGTH_LONG).show();
                    }
                });
            }

            if (profileButton != null) {
                profileButton.setOnClickListener(v -> {
                    try {
                        // Navigate to ProfileActivity
                        Intent intent = new Intent(MainDashboard.this, ProfileActivity.class);
                        startActivity(intent);

                        // Optional: Add transition animation
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(MainDashboard.this, "Opening Profile...", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error setting up click listeners: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void loadUserData() {
        try {
            // Load user data from preferences or database
            if (userNameText != null) {
                userNameText.setText("Winter");
            }
            if (locationText != null) {
                locationText.setText("Brgy. Tinamnan");
            }
            if (reportCountText != null) {
                reportCountText.setText("50");
            }
            if (emergencyCountText != null) {
                emergencyCountText.setText("2");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error loading user data: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void openSafetyTips(String safetyType) {
        try {
            Intent intent = new Intent(MainDashboard.this, SafetyTipsActivity.class);
            intent.putExtra("safety_type", safetyType);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Safety tips feature coming soon", Toast.LENGTH_SHORT).show();
        }
    }

    // NEW: Optional method to show help dialog instead of navigating to activity
    private void showHelpDialog() {
        // You can implement a dialog here instead of navigating to a new activity
        Toast.makeText(this, "Help: This app helps you report emergencies and access safety information", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh data when returning to dashboard
        loadUserData();
    }

    @Override
    public void onBackPressed() {
        // Handle back button press - you might want to show exit confirmation
        super.onBackPressed();
    }
}