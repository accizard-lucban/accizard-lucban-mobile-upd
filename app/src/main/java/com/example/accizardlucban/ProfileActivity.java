package com.example.accizardlucban;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    private ImageView backButton;
    private TextView signOutButton;
    private TextView residentName;
    private Button editProfileButton;
    private Switch locationSwitch, notificationSwitch;
    private LinearLayout termsLayout, deleteAccountLayout;

    // Navigation buttons
    private LinearLayout homeTab, chatTab, reportTab, mapTab, alertsTab, profileTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initViews();
        setupClickListeners();
        setupNavigationListeners();
        loadUserData();
    }

    private void initViews() {
        backButton = findViewById(R.id.back_button);
        signOutButton = findViewById(R.id.sign_out_button);
        residentName = findViewById(R.id.resident_name);
        editProfileButton = findViewById(R.id.edit_profile_button);
        locationSwitch = findViewById(R.id.location_switch);
        notificationSwitch = findViewById(R.id.notification_switch);
        termsLayout = findViewById(R.id.terms_layout);
        deleteAccountLayout = findViewById(R.id.delete_account_layout);

        // Initialize navigation tabs
        homeTab = findViewById(R.id.homeTab);
        chatTab = findViewById(R.id.chatTab);
        reportTab = findViewById(R.id.reportTab);
        mapTab = findViewById(R.id.mapTab);
        alertsTab = findViewById(R.id.alertsTab);
        profileTab = findViewById(R.id.profileTab);
    }

    private void setupClickListeners() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignOutDialog();
            }
        });

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });

        locationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Handle location permission toggle
            if (isChecked) {
                // Request location permission if needed
                Toast.makeText(ProfileActivity.this, "Location access enabled", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ProfileActivity.this, "Location access disabled", Toast.LENGTH_SHORT).show();
            }
        });

        notificationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Handle notification settings
            if (isChecked) {
                Toast.makeText(ProfileActivity.this, "Notifications enabled", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ProfileActivity.this, "Notifications disabled", Toast.LENGTH_SHORT).show();
            }
        });

        termsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Terms and Conditions Activity
                Intent intent = new Intent(ProfileActivity.this, TermAndConditionsActivity.class);
                startActivity(intent);
            }
        });

        deleteAccountLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteAccountDialog();
            }
        });
    }

    private void setupNavigationListeners() {
        homeTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Home/Main Activity
                Intent intent = new Intent(ProfileActivity.this, MainDashboard.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });

        chatTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Chat Activity
                Intent intent = new Intent(ProfileActivity.this, ChatActivity.class);
                startActivity(intent);
                finish();
            }
        });

        reportTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Report Activity
                Intent intent = new Intent(ProfileActivity.this, ReportSubmissionActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mapTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Map Activity
                Intent intent = new Intent(ProfileActivity.this, MapViewActivity.class);
                startActivity(intent);
                finish();
            }
        });

        alertsTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Alerts Activity
                Intent intent = new Intent(ProfileActivity.this, AlertsActivity.class);
                startActivity(intent);
                finish();
            }
        });

        profileTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Already in Profile Activity, do nothing or show toast
                Toast.makeText(ProfileActivity.this, "You are already in Profile", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadUserData() {
        // TODO: Load user data from SharedPreferences or database
        // For now, using placeholder data
        residentName.setText("Juan Dela Cruz");
    }

    private void showSignOutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sign Out");
        builder.setMessage("Are you sure you want to sign out?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO: Clear user session and navigate to login
                Toast.makeText(ProfileActivity.this, "Signed out successfully", Toast.LENGTH_SHORT).show();
                // Navigate to Login Activity or Main Activity
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void showDeleteAccountDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Account");
        builder.setMessage("Are you sure you want to delete your account? This action cannot be undone.");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO: Implement account deletion
                Toast.makeText(ProfileActivity.this, "Account deletion requested", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
}