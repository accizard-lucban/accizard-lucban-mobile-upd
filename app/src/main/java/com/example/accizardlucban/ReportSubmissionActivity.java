package com.example.accizardlucban;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class ReportSubmissionActivity extends AppCompatActivity {

    private static final String TAG = "ReportSubmissionActivity";

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

    // Firebase
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_submission);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();

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
                // Optional: Add transition animation
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });

        chatTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToChat();
                // Optional: Add transition animation
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
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
                // Optional: Add transition animation
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });

        alertsTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToAlerts();
                // Optional: Add transition animation
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
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
        if (validateForm()) {
            // Get current user
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser == null) {
                Toast.makeText(this, "Please sign in to submit a report", Toast.LENGTH_LONG).show();
                return;
            }

            // Show loading state
            submitReportButton.setEnabled(false);
            submitReportButton.setText("Submitting...");

            // Get form data
            String reportType = reportTypeSpinner.getSelectedItem().toString();
            String description = descriptionEditText.getText().toString().trim();
            String location = locationEditText.getText().toString().trim();

            // Create report data
            Map<String, Object> reportData = FirestoreHelper.createReportData(
                currentUser.getUid(),
                reportType,
                description,
                location,
                "medium", // Default priority
                reportType.toLowerCase()
            );

            // Save to Firestore
            FirestoreHelper.createReport(reportData,
                new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "Report submitted successfully with ID: " + documentReference.getId());
                        Toast.makeText(ReportSubmissionActivity.this, 
                            "Report submitted successfully!", Toast.LENGTH_SHORT).show();
                        clearForm();
                        submitReportButton.setEnabled(true);
                        submitReportButton.setText("Submit Report");
                    }
                },
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error submitting report", e);
                        Toast.makeText(ReportSubmissionActivity.this, 
                            "Error submitting report: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        submitReportButton.setEnabled(true);
                        submitReportButton.setText("Submit Report");
                    }
                });
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