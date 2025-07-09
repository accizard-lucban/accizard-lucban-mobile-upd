package com.example.accizardlucban;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText emailEditText;
    private Button signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            setContentView(R.layout.activity_reset_password);
            initializeViews();
            setupClickListeners();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error loading reset password: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void initializeViews() {
        try {
            emailEditText = findViewById(R.id.email_edit_text);
            signInButton = findViewById(R.id.sign_in_button);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error initializing views: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void setupClickListeners() {
        if (signInButton != null) {
            signInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handlePasswordReset();
                }
            });
        }
    }

    private void handlePasswordReset() {
        try {
            String email = "";

            if (emailEditText != null) {
                email = emailEditText.getText().toString().trim();
            }

            if (email.isEmpty()) {
                if (emailEditText != null) {
                    emailEditText.setError("Email is required");
                }
                Toast.makeText(this, "Email is required", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                if (emailEditText != null) {
                    emailEditText.setError("Please enter a valid email address");
                }
                Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                return;
            }

            // Navigate to password recovery confirmation
            Intent intent = new Intent(ResetPasswordActivity.this, PasswordRecoveryActivity.class);
            intent.putExtra("email", email);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error processing password reset: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
