package com.example.accizardlucban;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddressInfoActivity extends AppCompatActivity {

    private EditText etProvince, etCityTown;
    private Spinner spinnerBarangay;
    private Button btnNext, btnBack;
    private String firstName, lastName, mobileNumber, email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_info);

        initializeViews();
        setupSpinner();
        getIntentData();
        setupClickListeners();
    }

    private void initializeViews() {
        etProvince = findViewById(R.id.etProvince);
        etCityTown = findViewById(R.id.etCityTown);
        spinnerBarangay = findViewById(R.id.spinnerBarangay);
        btnNext = findViewById(R.id.btnNext);
        btnBack = findViewById(R.id.btnBack);
    }

    private void setupSpinner() {
        String[] barangays = {
                "Choose a barangay", "Abang", "Aliliw", "Atulinao", "Ayuti (Poblacion)", "Barangay 1 (Poblacion)", "Barangay 2 (Poblacion)", "Barangay 3 (Poblacion)", "Barangay 4 (Poblacion)", "Barangay 5 (Poblacion)", "Barangay 6 (Poblacion)", "Barangay 7 (Poblacion)", "Barangay 8 (Poblacion)", "Barangay 9 (Poblacion)", "Barangay 10 (Poblacion)", "Igang", "Kabatete", "Kakawit", "Kalangay", "Kalyaat", "Kilib", "Kulapi", "Mahabang Parang", "Malupak", "Manasa", "May-It", "Nagsinamo", "Nalunao", "Palola", "Piis", "Samil", "Tiawe", "Tinamnan"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, barangays);
        spinnerBarangay.setAdapter(adapter);
    }

    private void getIntentData() {
        Intent intent = getIntent();
        firstName = intent.getStringExtra("firstName");
        lastName = intent.getStringExtra("lastName");
        mobileNumber = intent.getStringExtra("mobileNumber");
        email = intent.getStringExtra("email");
        password = intent.getStringExtra("password");
    }

    private void setupClickListeners() {
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    proceedToProfilePicture();
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private boolean validateInputs() {
        if (TextUtils.isEmpty(etProvince.getText().toString().trim())) {
            etProvince.setError("Province is required");
            etProvince.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(etCityTown.getText().toString().trim())) {
            etCityTown.setError("City/Town is required");
            etCityTown.requestFocus();
            return false;
        }

        if (spinnerBarangay.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Please select a barangay", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void proceedToProfilePicture() {
        Intent intent = new Intent(AddressInfoActivity.this, ProfilePictureActivity.class);
        intent.putExtra("firstName", firstName);
        intent.putExtra("lastName", lastName);
        intent.putExtra("mobileNumber", mobileNumber);
        intent.putExtra("email", email);
        intent.putExtra("password", password);
        intent.putExtra("province", etProvince.getText().toString().trim());
        intent.putExtra("cityTown", etCityTown.getText().toString().trim());
        intent.putExtra("barangay", spinnerBarangay.getSelectedItem().toString());
        startActivity(intent);
    }
}