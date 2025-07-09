package com.example.accizardlucban; // Replace with your actual package name

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ReportSubmissionActivity extends AppCompatActivity {

    // UI Components
    private Spinner reportTypeSpinner;
    private EditText descriptionEditText;
    private EditText locationEditText;
    private ImageView locationButton;
    private Button uploadImagesButton;
    private Button submitReportButton;
    private ImageButton profileButton;
    private RecyclerView reportLogRecyclerView;

    // Bottom Navigation
    private LinearLayout homeTab;
    private LinearLayout chatTab;
    private LinearLayout reportTab;
    private LinearLayout mapTab;
    private LinearLayout alertsTab;
    private LinearLayout profileTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_submission);

        // Initialize UI components
        initializeViews();

        // Setup spinner
        setupReportTypeSpinner();

        // Setup RecyclerView
        setupReportLogRecyclerView();

        // Setup click listeners
        setupClickListeners();
    }

    private void initializeViews() {
        // Form components
        reportTypeSpinner = findViewById(R.id.reportTypeSpinner);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        locationEditText = findViewById(R.id.locationEditText);
        locationButton = findViewById(R.id.locationButton);
        uploadImagesButton = findViewById(R.id.uploadImagesButton);
        submitReportButton = findViewById(R.id.submitReportButton);
        profileButton = findViewById(R.id.profile);
        reportLogRecyclerView = findViewById(R.id.reportLogRecyclerView);

        // Bottom navigation
        homeTab = findViewById(R.id.homeTab);
        chatTab = findViewById(R.id.chatTab);
        reportTab = findViewById(R.id.reportTab);
        mapTab = findViewById(R.id.mapTab);
        alertsTab = findViewById(R.id.alertsTab);
        profileTab = findViewById(R.id.profileTab);
    }

    private void setupReportTypeSpinner() {
        // Create array of report types
        String[] reportTypes = {
                "Select Report Type",
                "Road Accident",
                "Fire Emergency",
                "Medical Emergency",
                "Flooding",
                "Landslide",
                "Criminal Activity",
                "Infrastructure Damage",
                "Power Outage",
                "Water Supply Issue",
                "Other"
        };

        // Create adapter and set to spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                reportTypes
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reportTypeSpinner.setAdapter(adapter);
    }

    private void setupReportLogRecyclerView() {
        // Setup RecyclerView with LinearLayoutManager
        reportLogRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        reportLogRecyclerView.setNestedScrollingEnabled(false);

        // You can add your RecyclerView adapter here if you want to load dynamic data
        // ReportLogAdapter adapter = new ReportLogAdapter(reportList);
        // reportLogRecyclerView.setAdapter(adapter);
    }

    private void setupClickListeners() {
        // Profile button click
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToProfile();
            }
        });

        // Location button click
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentLocation();
            }
        });

        // Upload images button click
        uploadImagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImages();
            }
        });

        // Submit report button click
        submitReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitReport();
            }
        });

        // Bottom navigation click listeners
        homeTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToHome();
            }
        });

        chatTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToChat();
            }
        });

        reportTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Already on report screen - show toast or refresh
                Toast.makeText(ReportSubmissionActivity.this, "You're already on the Report screen", Toast.LENGTH_SHORT).show();
            }
        });

        mapTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToMap();
            }
        });

        alertsTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToAlerts();
            }
        });

        profileTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToProfile();
            }
        });
    }

    // Navigation methods
    private void navigateToHome() {
        Intent intent = new Intent(this, MainDashboard.class);
        startActivity(intent);
        finish(); // Optional: remove this activity from stack
    }

    private void navigateToChat() {
        Intent intent = new Intent(this, ChatActivity.class);
        startActivity(intent);
    }

    private void navigateToMap() {
        Intent intent = new Intent(this, MapViewActivity.class);
        startActivity(intent);
    }

    private void navigateToAlerts() {
        Intent intent = new Intent(this, AlertsActivity.class);
        startActivity(intent);
    }

    private void navigateToProfile() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    // Functional methods
    private void getCurrentLocation() {
        // TODO: Implement location retrieval using GPS or network
        // For now, show a toast
        Toast.makeText(this, "Getting current location...", Toast.LENGTH_SHORT).show();

        // Example: Set dummy location
        locationEditText.setText("Current Location: Lat 14.1234, Lng 121.5678");
    }

    private void uploadImages() {
        // TODO: Implement image upload functionality
        // You can use Intent to pick images from gallery or camera
        Toast.makeText(this, "Image upload functionality will be implemented", Toast.LENGTH_SHORT).show();

        // Example intent to pick image from gallery:
        /*
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, IMAGE_PICK_REQUEST);
        */
    }

    private void submitReport() {
        // Validate form data
        if (validateForm()) {
            // Get form data
            String reportType = reportTypeSpinner.getSelectedItem().toString();
            String description = descriptionEditText.getText().toString().trim();
            String location = locationEditText.getText().toString().trim();

            // TODO: Submit report to server or local database
            Toast.makeText(this, "Report submitted successfully!", Toast.LENGTH_LONG).show();

            // Clear form after successful submission
            clearForm();

            // Optional: Navigate to another screen or refresh the report log
            // navigateToHome();
        }
    }

    private boolean validateForm() {
        // Check if report type is selected
        if (reportTypeSpinner.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Please select a report type", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Check if description is provided
        String description = descriptionEditText.getText().toString().trim();
        if (description.isEmpty()) {
            descriptionEditText.setError("Description is required");
            descriptionEditText.requestFocus();
            return false;
        }

        // Check if location is provided
        String location = locationEditText.getText().toString().trim();
        if (location.isEmpty()) {
            locationEditText.setError("Location is required");
            locationEditText.requestFocus();
            return false;
        }

        return true;
    }

    private void clearForm() {
        reportTypeSpinner.setSelection(0);
        descriptionEditText.setText("");
        locationEditText.setText("");
        descriptionEditText.clearFocus();
        locationEditText.clearFocus();
    }

    @Override
    public void onBackPressed() {
        // Handle back button press
        super.onBackPressed();
        // Optional: Navigate to specific activity instead of default back behavior
        // navigateToHome();
    }
}