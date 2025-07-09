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

public class EditProfileActivity extends AppCompatActivity {

    private ImageView backButton, profilePicture, editPictureButton;
    private Button saveButton;
    private EditText firstNameEdit, lastNameEdit, mobileNumberEdit, emailEdit,
            provinceEdit, cityEdit, passwordEdit;
    private Spinner barangaySpinner;

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
        emailEdit = findViewById(R.id.email_edit);
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

    private void loadUserData() {
        // TODO: Load existing user data from SharedPreferences or database
        // For demonstration, setting placeholder data
        firstNameEdit.setText("Juan");
        lastNameEdit.setText("Dela Cruz");
        mobileNumberEdit.setText("09123456789");
        emailEdit.setText("juan.delacruz@email.com");
        provinceEdit.setText("Quezon");
        cityEdit.setText("Lucban");
        // Don't set password for security
    }

    private void saveProfile() {
        // Validate required fields
        if (!validateForm()) {
            return;
        }

        // Get form data
        String firstName = firstNameEdit.getText().toString().trim();
        String lastName = lastNameEdit.getText().toString().trim();
        String mobileNumber = mobileNumberEdit.getText().toString().trim();
        String email = emailEdit.getText().toString().trim();
        String province = provinceEdit.getText().toString().trim();
        String city = cityEdit.getText().toString().trim();
        String password = passwordEdit.getText().toString().trim();
        String barangay = barangaySpinner.getSelectedItem().toString();

        // TODO: Save data to SharedPreferences or send to server
        // For now, just show success message
        Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();

        // Return to profile activity
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

        // Validate email
        String email = emailEdit.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            emailEdit.setError("Email is required");
            isValid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEdit.setError("Invalid email format");
            isValid = false;
        }

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