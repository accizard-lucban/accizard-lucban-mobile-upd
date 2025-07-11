package com.example.accizardlucban;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    private ImageView backButton, profilePicture, editPictureButton;
    private Button saveButton;
    private EditText firstNameEdit, lastNameEdit, mobileNumberEdit,
            provinceEdit, cityEdit, passwordEdit;
    private Spinner barangaySpinner;

    private static final String PREFS_NAME = "user_profile_prefs";
    private static final String KEY_FIRST_NAME = "first_name";
    private static final String KEY_LAST_NAME = "last_name";
    private static final String KEY_MOBILE = "mobile_number";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PROVINCE = "province";
    private static final String KEY_CITY = "city";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_BARANGAY = "barangay";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        initViews();
        setupSpinner();
        setupClickListeners();
        loadUserData();
    }

    private void initViews() {
        backButton = findViewById(R.id.back_button);
        profilePicture = findViewById(R.id.profile_picture);
        editPictureButton = findViewById(R.id.edit_picture_button);
        saveButton = findViewById(R.id.save_button);

        firstNameEdit = findViewById(R.id.first_name_edit);
        lastNameEdit = findViewById(R.id.last_name_edit);
        mobileNumberEdit = findViewById(R.id.mobile_number_edit);
        provinceEdit = findViewById(R.id.province_edit);
        cityEdit = findViewById(R.id.city_edit);
        passwordEdit = findViewById(R.id.password_edit);
        barangaySpinner = findViewById(R.id.barangay_spinner);
    }

    private void setupSpinner() {
        String[] barangays = {
                "Select Barangay",
                "Barangay 1 (Poblacion)",
                "Barangay 2 (Poblacion)",
                "Barangay 3 (Poblacion)",
                "Barangay 4 (Poblacion)",
                "Barangay 5 (Poblacion)",
                "Barangay 6 (Poblacion)",
                "Barangay 7 (Poblacion)",
                "Barangay 8 (Poblacion)",
                "Anlilising",
                "Ayaas",
                "Bukid",
                "Silangan",
                "Kanlurang"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, barangays);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        barangaySpinner.setAdapter(adapter);
    }

    private void setupClickListeners() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        editPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Open image picker for profile picture
                Toast.makeText(EditProfileActivity.this, "Image picker not implemented yet", Toast.LENGTH_SHORT).show();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfile();
            }
        });
    }

    private SharedPreferences getUserPrefs() {
        return getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
    }

    private void loadUserData() {
        SharedPreferences prefs = getUserPrefs();
        firstNameEdit.setText(prefs.getString(KEY_FIRST_NAME, ""));
        lastNameEdit.setText(prefs.getString(KEY_LAST_NAME, ""));
        mobileNumberEdit.setText(prefs.getString(KEY_MOBILE, ""));
        provinceEdit.setText(prefs.getString(KEY_PROVINCE, ""));
        cityEdit.setText(prefs.getString(KEY_CITY, ""));
        // Don't set password for security
        // Set barangay spinner selection
        String barangay = prefs.getString(KEY_BARANGAY, "Select Barangay");
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) barangaySpinner.getAdapter();
        if (adapter != null && barangay != null) {
            int position = adapter.getPosition(barangay);
            if (position >= 0) {
                barangaySpinner.setSelection(position);
            }
        }
    }

    private void saveProfile() {
        if (!validateForm()) {
            return;
        }
        String firstName = firstNameEdit.getText().toString().trim();
        String lastName = lastNameEdit.getText().toString().trim();
        String mobileNumber = mobileNumberEdit.getText().toString().trim();
        String province = provinceEdit.getText().toString().trim();
        String city = cityEdit.getText().toString().trim();
        String password = passwordEdit.getText().toString().trim();
        String barangay = barangaySpinner.getSelectedItem().toString();

        // Save to SharedPreferences
        SharedPreferences.Editor editor = getUserPrefs().edit();
        editor.putString(KEY_FIRST_NAME, firstName);
        editor.putString(KEY_LAST_NAME, lastName);
        editor.putString(KEY_MOBILE, mobileNumber);
        // Do NOT save email here; only after Firebase update (but now removed)
        editor.putString(KEY_PROVINCE, province);
        editor.putString(KEY_CITY, city);
        editor.putString(KEY_BARANGAY, barangay);
        // Do NOT save password here; only after Firebase update
        editor.apply();

        // Update Firebase Auth password if changed
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            boolean needToUpdate = false;
            // Email update removed
            if (!password.isEmpty()) {
                user.updatePassword(password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            SharedPreferences.Editor pwEditor = getUserPrefs().edit();
                            pwEditor.putString(KEY_PASSWORD, password);
                            pwEditor.apply();
                            Toast.makeText(this, "Password updated successfully!", Toast.LENGTH_SHORT).show();
                        } else {
                            String errorMsg = (task.getException() != null) ? task.getException().getMessage() : "Unknown error";
                            Toast.makeText(this, "Failed to update password in Firebase: " + errorMsg, Toast.LENGTH_LONG).show();
                            if (errorMsg != null && errorMsg.toLowerCase().contains("recent login")) {
                                Toast.makeText(this, "Please re-authenticate and try again.", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                needToUpdate = true;
            }

            // Update Firestore user profile
            String uid = user.getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            Map<String, Object> userProfile = new HashMap<>();
            userProfile.put("firstName", firstName);
            userProfile.put("lastName", lastName);
            userProfile.put("fullName", firstName + " " + lastName);
            userProfile.put("mobileNumber", mobileNumber);
            // userProfile.put("email", email); // removed
            userProfile.put("province", province);
            userProfile.put("city", city);
            userProfile.put("barangay", barangay);
            db.collection("users").document(uid)
                .update(userProfile)
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to update profile on server: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });

            if (needToUpdate) {
                Toast.makeText(this, "Profile and Firebase Auth updated successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    private boolean validateForm() {
        boolean isValid = true;

        // Validate first name
        if (TextUtils.isEmpty(firstNameEdit.getText().toString().trim())) {
            firstNameEdit.setError("First name is required");
            isValid = false;
        }

        // Validate last name
        if (TextUtils.isEmpty(lastNameEdit.getText().toString().trim())) {
            lastNameEdit.setError("Last name is required");
            isValid = false;
        }

        // Validate mobile number
        String mobile = mobileNumberEdit.getText().toString().trim();
        if (TextUtils.isEmpty(mobile)) {
            mobileNumberEdit.setError("Mobile number is required");
            isValid = false;
        } else if (!mobile.matches("^09\\d{9}$")) {
            mobileNumberEdit.setError("Invalid mobile number format");
            isValid = false;
        }

        // Email validation removed

        // Validate province
        if (TextUtils.isEmpty(provinceEdit.getText().toString().trim())) {
            provinceEdit.setError("Province is required");
            isValid = false;
        }

        // Validate city
        if (TextUtils.isEmpty(cityEdit.getText().toString().trim())) {
            cityEdit.setError("City/Town is required");
            isValid = false;
        }

        // Validate barangay selection
        if (barangaySpinner.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Please select a barangay", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        // Validate password if provided
        String password = passwordEdit.getText().toString().trim();
        if (!TextUtils.isEmpty(password) && password.length() < 6) {
            passwordEdit.setError("Password must be at least 6 characters");
            isValid = false;
        }

        return isValid;
    }
}