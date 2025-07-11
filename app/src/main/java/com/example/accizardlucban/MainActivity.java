package com.example.accizardlucban;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;
import androidx.annotation.NonNull;
import android.content.SharedPreferences;

public class MainActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button signInButton;
    private TextView forgotPasswordText, signUpText, emergencyText;
    private LinearLayout callLucbanLayout; // Changed from TextView to LinearLayout
    private static final String PREFS_NAME = "user_profile_prefs";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);

        // FirebaseAuth instance
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        // Remove or comment out the anonymous sign-in example
        // mAuth.signInAnonymously() ...

        try {
            setContentView(R.layout.activity_main);
            initializeViews();
            loadSavedCredentials();
            setupClickListeners(mAuth);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error loading main activity: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void initializeViews() {
        try {
            emailEditText = findViewById(R.id.email_edit_text);
            passwordEditText = findViewById(R.id.password_edit_text);
            signInButton = findViewById(R.id.sign_in_button);
            forgotPasswordText = findViewById(R.id.forgot_password_text);
            signUpText = findViewById(R.id.sign_up_text);
            emergencyText = findViewById(R.id.emergency_text);
            callLucbanLayout = findViewById(R.id.call_lucban_text); // Changed to LinearLayout
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error initializing views: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void loadSavedCredentials() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String savedEmail = prefs.getString(KEY_EMAIL, "");
        String savedPassword = prefs.getString(KEY_PASSWORD, "");
        if (emailEditText != null) emailEditText.setText(savedEmail);
        if (passwordEditText != null) passwordEditText.setText(savedPassword);
    }

    // Update setupClickListeners to accept FirebaseAuth
    private void setupClickListeners(FirebaseAuth mAuth) {
        if (signInButton != null) {
            signInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String email = emailEditText.getText() != null ? emailEditText.getText().toString().trim() : "";
                    String password = passwordEditText.getText() != null ? passwordEditText.getText().toString().trim() : "";

                    if (email.isEmpty()) {
                        emailEditText.setError("Email is required");
                        Toast.makeText(MainActivity.this, "Email is required", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (password.isEmpty()) {
                        passwordEditText.setError("Password is required");
                        Toast.makeText(MainActivity.this, "Password is required", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull com.google.android.gms.tasks.Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(MainActivity.this, "Firebase Auth: Signed in successfully", Toast.LENGTH_SHORT).show();
                                    // Navigate to MainDashboard
                                    Intent intent = new Intent(MainActivity.this, MainDashboard.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(MainActivity.this, "Firebase Auth failed: " + (task.getException() != null ? task.getException().getMessage() : "Unknown error"), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                }
            });
        }

        if (forgotPasswordText != null) {
            forgotPasswordText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(MainActivity.this, ResetPasswordActivity.class);
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "Error navigating to reset password", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        if (signUpText != null) {
            signUpText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Intent intent = new Intent(MainActivity.this, RegistrationActivity.class);
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "Error navigating to registration", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        if (emergencyText != null) {
            emergencyText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "Emergency feature", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Changed to use LinearLayout instead of TextView
        if (callLucbanLayout != null) {
            callLucbanLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "Calling Lucban LDRRMO...", Toast.LENGTH_SHORT).show();
                    // You can add actual calling functionality here
                    // For example:
                    // Intent callIntent = new Intent(Intent.ACTION_CALL);
                    // callIntent.setData(Uri.parse("tel:+1234567890"));
                    // startActivity(callIntent);
                }
            });
        }
    }

    private void handleSignIn() {
        try {
            String email = "";
            String password = "";

            if (emailEditText != null) {
                email = emailEditText.getText().toString().trim();
            }

            if (passwordEditText != null) {
                password = passwordEditText.getText().toString().trim();
            }

            if (email.isEmpty()) {
                if (emailEditText != null) {
                    emailEditText.setError("Email is required");
                }
                Toast.makeText(this, "Email is required", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.isEmpty()) {
                if (passwordEditText != null) {
                    passwordEditText.setError("Password is required");
                }
                Toast.makeText(this, "Password is required", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if account needs verification
            if (email.equals("unverified@example.com")) {
                Intent intent = new Intent(MainActivity.this, AccountVerificationActivity.class);
                startActivity(intent);
            } else {
                // Successful login - navigate to MainDashboard
                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, MainDashboard.class);
                startActivity(intent);
                // Optional: finish current activity so user can't go back to login with back button
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error during sign in: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}