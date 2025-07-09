package com.example.accizardlucban;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class AccountVerificationActivity extends AppCompatActivity {

    private Button goBackButton;
    private TextView errorMessageTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_verification);

        // Initialize views
        initializeViews();

        // Set up click listeners
        setupClickListeners();

        // Handle intent data
        handleIntent();
    }

    private void initializeViews() {
        goBackButton = findViewById(R.id.go_back_button);
        errorMessageTextView = findViewById(R.id.error_message_text_view);
    }

    private void setupClickListeners() {
        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackToLogin();
            }
        });
    }

    private void handleIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            // Check if there's a custom error message
            String customMessage = intent.getStringExtra("verification_message");
            if (customMessage != null && !customMessage.isEmpty()) {
                errorMessageTextView.setText(customMessage);
            }

            // Check if there's user email to display
            String userEmail = intent.getStringExtra("user_email");
            if (userEmail != null && !userEmail.isEmpty()) {
                String message = "It looks like your account (" + userEmail + ") is not yet verified.\n" +
                        "We will notify you when your account is\nverified before you can login on the app.";
                errorMessageTextView.setText(message);
            }

            // Check if there's a specific verification status
            boolean isVerificationPending = intent.getBooleanExtra("verification_pending", false);
            if (isVerificationPending) {
                String message = "Your account verification is currently pending.\n" +
                        "Please check your email for verification instructions\n" +
                        "or contact support for assistance.";
                errorMessageTextView.setText(message);
            }
        }
    }

    private void goBackToLogin() {
        // Create intent to go back to login activity
        Intent intent = new Intent(AccountVerificationActivity.this, MainActivity.class);

        // Clear the activity stack so user can't go back to this screen
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        // Handle back button press - go back to login
        goBackToLogin();
    }

    // Static method to start this activity with default message
    public static void start(android.content.Context context) {
        Intent intent = new Intent(context, AccountVerificationActivity.class);
        context.startActivity(intent);
    }

    // Static method to start this activity with custom verification message
    public static void startWithMessage(android.content.Context context, String message) {
        Intent intent = new Intent(context, AccountVerificationActivity.class);
        intent.putExtra("verification_message", message);
        context.startActivity(intent);
    }

    // Static method to start this activity with user email
    public static void startWithEmail(android.content.Context context, String email) {
        Intent intent = new Intent(context, AccountVerificationActivity.class);
        intent.putExtra("user_email", email);
        context.startActivity(intent);
    }

    // Static method to start this activity for pending verification
    public static void startForPendingVerification(android.content.Context context, String email) {
        Intent intent = new Intent(context, AccountVerificationActivity.class);
        intent.putExtra("user_email", email);
        intent.putExtra("verification_pending", true);
        context.startActivity(intent);
    }
}