package com.example.accizardlucban;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SuccessActivity extends AppCompatActivity {

    private Button btnGoBackToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        initializeViews();
        setupClickListeners();
    }

    private void initializeViews() {
        btnGoBackToLogin = findViewById(R.id.btnGoBackToLogin);
    }

    private void setupClickListeners() {
        btnGoBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to login screen
                Intent intent = new Intent(SuccessActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Disable back button to prevent going back to registration flow
        // User must click "Go Back to Login" button
    }
}