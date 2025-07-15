package com.example.accizardlucban;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;

public class RegistrationActivity extends AppCompatActivity {

    private EditText etFirstName, etLastName, etMobileNumber, etEmail, etPassword;
    private CheckBox cbTerms;
    private Button btnCreateAccount;
    private TextView tvSignIn;

    private static final String PREFS_NAME = "user_profile_prefs";
    private static final String KEY_FIRST_NAME = "first_name";
    private static final String KEY_LAST_NAME = "last_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_activity);

        initializeViews();
        setupClickListeners();
    }

    private void initializeViews() {
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etMobileNumber = findViewById(R.id.etMobileNumber);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        cbTerms = findViewById(R.id.cbTerms);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);
        tvSignIn = findViewById(R.id.tvSignIn);
    }

    private void setupClickListeners() {
        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    proceedToAddressInfo();
                }
            }
        });

        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to MainActivity (sign in screen)
                try {
                    Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish(); // Close current activity
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(RegistrationActivity.this, "Error navigating to sign in", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean validateInputs() {
        if (TextUtils.isEmpty(etFirstName.getText().toString().trim())) {
            etFirstName.setError("First name is required");
            etFirstName.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(etLastName.getText().toString().trim())) {
            etLastName.setError("Last name is required");
            etLastName.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(etMobileNumber.getText().toString().trim())) {
            etMobileNumber.setError("Mobile number is required");
            etMobileNumber.requestFocus();
            return false;
        }

        // Mobile number validation (Philippine format)
        String mobileNumber = etMobileNumber.getText().toString().trim();
        if (!isValidPhilippineMobileNumber(mobileNumber)) {
            etMobileNumber.setError("Please enter a valid Philippine mobile number");
            etMobileNumber.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(etEmail.getText().toString().trim())) {
            etEmail.setError("Email is required");
            etEmail.requestFocus();
            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString().trim()).matches()) {
            etEmail.setError("Please enter a valid email");
            etEmail.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(etPassword.getText().toString().trim())) {
            etPassword.setError("Password is required");
            etPassword.requestFocus();
            return false;
        }

        if (etPassword.getText().toString().trim().length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            etPassword.requestFocus();
            return false;
        }

        if (!cbTerms.isChecked()) {
            Toast.makeText(this, "Please agree to Terms and Conditions", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean isValidPhilippineMobileNumber(String mobileNumber) {
        // Remove spaces and dashes
        mobileNumber = mobileNumber.replaceAll("[\\s-]", "");

        // Check if it starts with +63 or 63 or 09
        if (mobileNumber.startsWith("+63")) {
            mobileNumber = mobileNumber.substring(3);
        } else if (mobileNumber.startsWith("63")) {
            mobileNumber = mobileNumber.substring(2);
        } else if (mobileNumber.startsWith("09")) {
            mobileNumber = mobileNumber.substring(1);
        }

        // Should be 10 digits starting with 9
        return mobileNumber.length() == 10 && mobileNumber.startsWith("9") && mobileNumber.matches("\\d+");
    }

    private void proceedToAddressInfo() {
        try {
            // Save first and last name to SharedPreferences
            SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(KEY_FIRST_NAME, etFirstName.getText().toString().trim());
            editor.putString(KEY_LAST_NAME, etLastName.getText().toString().trim());
            editor.apply();

            Intent intent = new Intent(RegistrationActivity.this, AddressInfoActivity.class);
            intent.putExtra("firstName", etFirstName.getText().toString().trim());
            intent.putExtra("lastName", etLastName.getText().toString().trim());
            intent.putExtra("mobileNumber", etMobileNumber.getText().toString().trim());
            intent.putExtra("email", etEmail.getText().toString().trim());
            intent.putExtra("password", etPassword.getText().toString().trim());
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error proceeding to address info: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}