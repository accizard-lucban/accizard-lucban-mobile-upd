package com.example.accizardlucban;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class Facilities extends AppCompatActivity {

    // Timeline section views
    private LinearLayout timelineHeader;
    private LinearLayout timelineContent;
    private ImageView timelineArrow;

    // Incident Types section views
    private LinearLayout incidentTypesHeader;
    private LinearLayout incidentTypesContent;
    private ImageView incidentTypesArrow;

    // Emergency Support section views
    private LinearLayout emergencySupportHeader;
    private LinearLayout emergencySupportContent;
    private ImageView emergencySupportArrow;

    // Switches and checkboxes
    private Switch heatmapSwitch;
    private CheckBox roadAccidentCheck, fireCheck, medicalEmergencyCheck, floodingCheck;
    private CheckBox volcanicActivityCheck, landslideCheck, earthquakeCheck, civilDisturbanceCheck;
    private CheckBox armedConflictCheck, infectiousDiseaseCheck;
    private CheckBox evacuationCentersCheck, healthFacilitiesCheck, policeStationsCheck;
    private CheckBox fireStationsCheck, governmentOfficesCheck;

    // Timeline options
    private TextView todayOption, thisWeekOption, thisMonthOption, thisYearOption;
    private TextView selectedTimelineOption;
    private String selectedTimeRange = "Today";

    // Apply and Cancel buttons
    private Button applyFiltersButton;
    private Button cancelButton;

    // Section visibility states
    private boolean isTimelineExpanded = false;
    private boolean isIncidentTypesExpanded = false;
    private boolean isEmergencySupportExpanded = false;

    // Select All / Deselect All buttons
    private Button selectAllIncidentsButton;
    private Button deselectAllIncidentsButton;
    private Button selectAllFacilitiesButton;
    private Button deselectAllFacilitiesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facilities);

        initializeViews();
        loadFilterStates();
        setupClickListeners();
        setupButtons();
        setupTimelineOptions();
        setupSelectAllButtons();
    }

    private void initializeViews() {
        // Timeline section
        timelineHeader = findViewById(R.id.timelineHeader);
        timelineContent = findViewById(R.id.timelineContent);
        timelineArrow = findViewById(R.id.timelineArrow);

        // Incident Types section
        incidentTypesHeader = findViewById(R.id.incidentTypesHeader);
        incidentTypesContent = findViewById(R.id.incidentTypesContent);
        incidentTypesArrow = findViewById(R.id.incidentTypesArrow);

        // Emergency Support section
        emergencySupportHeader = findViewById(R.id.emergencySupportHeader);
        emergencySupportContent = findViewById(R.id.emergencySupportContent);
        emergencySupportArrow = findViewById(R.id.emergencySupportArrow);

        // Switches and checkboxes
        heatmapSwitch = findViewById(R.id.heatmapSwitch);
        roadAccidentCheck = findViewById(R.id.roadAccidentCheck);
        fireCheck = findViewById(R.id.fireCheck);
        medicalEmergencyCheck = findViewById(R.id.medicalEmergencyCheck);
        floodingCheck = findViewById(R.id.floodingCheck);
        volcanicActivityCheck = findViewById(R.id.volcanicActivityCheck);
        landslideCheck = findViewById(R.id.landslideCheck);
        earthquakeCheck = findViewById(R.id.earthquakeCheck);
        civilDisturbanceCheck = findViewById(R.id.civilDisturbanceCheck);
        armedConflictCheck = findViewById(R.id.armedConflictCheck);
        infectiousDiseaseCheck = findViewById(R.id.infectiousDiseaseCheck);
        evacuationCentersCheck = findViewById(R.id.evacuationCentersCheck);
        healthFacilitiesCheck = findViewById(R.id.healthFacilitiesCheck);
        policeStationsCheck = findViewById(R.id.policeStationsCheck);
        fireStationsCheck = findViewById(R.id.fireStationsCheck);
        governmentOfficesCheck = findViewById(R.id.governmentOfficesCheck);

        // Timeline options
        findTimelineOptions();

        // Buttons
        applyFiltersButton = findViewById(R.id.applyFiltersButton);
        cancelButton = findViewById(R.id.cancelButton);

        // Select All / Deselect All buttons

        // Set initial visibility for collapsible sections
        if (timelineContent != null) {
            timelineContent.setVisibility(View.GONE);
        }
        if (incidentTypesContent != null) {
            incidentTypesContent.setVisibility(View.GONE);
        }
        if (emergencySupportContent != null) {
            emergencySupportContent.setVisibility(View.GONE);
        }
    }

    private void findTimelineOptions() {
        // Find timeline option TextViews
        todayOption = findViewById(R.id.todayOption);
        thisWeekOption = findViewById(R.id.thisWeekOption);
        thisMonthOption = findViewById(R.id.thisMonthOption);
        thisYearOption = findViewById(R.id.thisYearOption);

        // Set default selection
        if (todayOption != null) {
            selectedTimelineOption = todayOption;
        }
    }

    private void loadFilterStates() {
        // Load filter states from Intent extras (passed from MapViewActivity)
        Intent intent = getIntent();

        // Load heatmap state
        if (heatmapSwitch != null) {
            heatmapSwitch.setChecked(intent.getBooleanExtra("heatmapEnabled", false));
        }

        // Load timeline selection
        selectedTimeRange = intent.getStringExtra("selectedTimeRange");
        if (selectedTimeRange == null) selectedTimeRange = "Today";

        // Load incident filter states
        if (roadAccidentCheck != null) {
            roadAccidentCheck.setChecked(intent.getBooleanExtra("roadAccident", true));
        }
        if (fireCheck != null) {
            fireCheck.setChecked(intent.getBooleanExtra("fire", true));
        }
        if (medicalEmergencyCheck != null) {
            medicalEmergencyCheck.setChecked(intent.getBooleanExtra("medicalEmergency", true));
        }
        if (floodingCheck != null) {
            floodingCheck.setChecked(intent.getBooleanExtra("flooding", true));
        }
        if (volcanicActivityCheck != null) {
            volcanicActivityCheck.setChecked(intent.getBooleanExtra("volcanicActivity", true));
        }
        if (landslideCheck != null) {
            landslideCheck.setChecked(intent.getBooleanExtra("landslide", true));
        }
        if (earthquakeCheck != null) {
            earthquakeCheck.setChecked(intent.getBooleanExtra("earthquake", true));
        }
        if (civilDisturbanceCheck != null) {
            civilDisturbanceCheck.setChecked(intent.getBooleanExtra("civilDisturbance", true));
        }
        if (armedConflictCheck != null) {
            armedConflictCheck.setChecked(intent.getBooleanExtra("armedConflict", true));
        }
        if (infectiousDiseaseCheck != null) {
            infectiousDiseaseCheck.setChecked(intent.getBooleanExtra("infectiousDisease", true));
        }

        // Load facility filter states
        if (evacuationCentersCheck != null) {
            evacuationCentersCheck.setChecked(intent.getBooleanExtra("evacuationCenters", true));
        }
        if (healthFacilitiesCheck != null) {
            healthFacilitiesCheck.setChecked(intent.getBooleanExtra("healthFacilities", true));
        }
        if (policeStationsCheck != null) {
            policeStationsCheck.setChecked(intent.getBooleanExtra("policeStations", true));
        }
        if (fireStationsCheck != null) {
            fireStationsCheck.setChecked(intent.getBooleanExtra("fireStations", true));
        }
        if (governmentOfficesCheck != null) {
            governmentOfficesCheck.setChecked(intent.getBooleanExtra("governmentOffices", true));
        }

        // Update timeline selection UI
        updateTimelineSelection();
    }

    private void setupClickListeners() {
        // Timeline section click listener
        if (timelineHeader != null) {
            timelineHeader.setOnClickListener(v -> toggleSection("timeline"));
        }

        // Incident Types section click listener
        if (incidentTypesHeader != null) {
            incidentTypesHeader.setOnClickListener(v -> toggleSection("incidents"));
        }

        // Emergency Support section click listener
        if (emergencySupportHeader != null) {
            emergencySupportHeader.setOnClickListener(v -> toggleSection("facilities"));
        }
    }

    private void setupButtons() {
        // Apply Filters button
        if (applyFiltersButton != null) {
            applyFiltersButton.setOnClickListener(v -> applyFilters());
        }

        // Cancel button
        if (cancelButton != null) {
            cancelButton.setOnClickListener(v -> {
                setResult(RESULT_CANCELED);
                finish();
            });
        }
    }

    private void setupTimelineOptions() {
        if (todayOption != null) {
            todayOption.setOnClickListener(v -> selectTimelineOption("Today", todayOption));
        }
        if (thisWeekOption != null) {
            thisWeekOption.setOnClickListener(v -> selectTimelineOption("This Week", thisWeekOption));
        }
        if (thisMonthOption != null) {
            thisMonthOption.setOnClickListener(v -> selectTimelineOption("This Month", thisMonthOption));
        }
        if (thisYearOption != null) {
            thisYearOption.setOnClickListener(v -> selectTimelineOption("This Year", thisYearOption));
        }
    }

    private void setupSelectAllButtons() {
        // Select All Incidents button
        if (selectAllIncidentsButton != null) {
            selectAllIncidentsButton.setOnClickListener(v -> selectAllIncidents(true));
        }

        // Deselect All Incidents button
        if (deselectAllIncidentsButton != null) {
            deselectAllIncidentsButton.setOnClickListener(v -> selectAllIncidents(false));
        }

        // Select All Facilities button
        if (selectAllFacilitiesButton != null) {
            selectAllFacilitiesButton.setOnClickListener(v -> selectAllFacilities(true));
        }

        // Deselect All Facilities button
        if (deselectAllFacilitiesButton != null) {
            deselectAllFacilitiesButton.setOnClickListener(v -> selectAllFacilities(false));
        }
    }

    private void toggleSection(String section) {
        switch (section) {
            case "timeline":
                isTimelineExpanded = !isTimelineExpanded;
                animateSection(timelineContent, timelineArrow, isTimelineExpanded);
                break;
            case "incidents":
                isIncidentTypesExpanded = !isIncidentTypesExpanded;
                animateSection(incidentTypesContent, incidentTypesArrow, isIncidentTypesExpanded);
                break;
            case "facilities":
                isEmergencySupportExpanded = !isEmergencySupportExpanded;
                animateSection(emergencySupportContent, emergencySupportArrow, isEmergencySupportExpanded);
                break;
        }
    }

    private void animateSection(LinearLayout content, ImageView arrow, boolean expand) {
        if (content == null || arrow == null) return;

        // Animate content visibility
        if (expand) {
            content.setVisibility(View.VISIBLE);
        } else {
            content.setVisibility(View.GONE);
        }

        // Animate arrow rotation
        float fromDegrees = expand ? 0f : 180f;
        float toDegrees = expand ? 180f : 0f;

        RotateAnimation rotateAnimation = new RotateAnimation(
                fromDegrees, toDegrees,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        rotateAnimation.setDuration(300);
        rotateAnimation.setFillAfter(true);
        arrow.startAnimation(rotateAnimation);
    }

    private void selectTimelineOption(String timeRange, TextView selectedOption) {
        // Reset all timeline options to default color
        resetTimelineOptions();

        // Set selected option
        selectedTimeRange = timeRange;
        selectedTimelineOption = selectedOption;

        // Highlight selected option
        if (selectedOption != null) {
            selectedOption.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_light));
            selectedOption.setBackground(ContextCompat.getDrawable(this, android.R.drawable.btn_default));
        }

        Toast.makeText(this, "Selected: " + timeRange, Toast.LENGTH_SHORT).show();
    }

    private void resetTimelineOptions() {
        int defaultColor = ContextCompat.getColor(this, android.R.color.black);

        if (todayOption != null) {
            todayOption.setTextColor(defaultColor);
            todayOption.setBackground(null);
        }
        if (thisWeekOption != null) {
            thisWeekOption.setTextColor(defaultColor);
            thisWeekOption.setBackground(null);
        }
        if (thisMonthOption != null) {
            thisMonthOption.setTextColor(defaultColor);
            thisMonthOption.setBackground(null);
        }
        if (thisYearOption != null) {
            thisYearOption.setTextColor(defaultColor);
            thisYearOption.setBackground(null);
        }
    }

    private void updateTimelineSelection() {
        // Update UI based on selected time range
        TextView optionToSelect = null;

        switch (selectedTimeRange) {
            case "Today":
                optionToSelect = todayOption;
                break;
            case "This Week":
                optionToSelect = thisWeekOption;
                break;
            case "This Month":
                optionToSelect = thisMonthOption;
                break;
            case "This Year":
                optionToSelect = thisYearOption;
                break;
        }

        if (optionToSelect != null) {
            selectTimelineOption(selectedTimeRange, optionToSelect);
        }
    }

    private void selectAllIncidents(boolean select) {
        if (roadAccidentCheck != null) roadAccidentCheck.setChecked(select);
        if (fireCheck != null) fireCheck.setChecked(select);
        if (medicalEmergencyCheck != null) medicalEmergencyCheck.setChecked(select);
        if (floodingCheck != null) floodingCheck.setChecked(select);
        if (volcanicActivityCheck != null) volcanicActivityCheck.setChecked(select);
        if (landslideCheck != null) landslideCheck.setChecked(select);
        if (earthquakeCheck != null) earthquakeCheck.setChecked(select);
        if (civilDisturbanceCheck != null) civilDisturbanceCheck.setChecked(select);
        if (armedConflictCheck != null) armedConflictCheck.setChecked(select);
        if (infectiousDiseaseCheck != null) infectiousDiseaseCheck.setChecked(select);

        Toast.makeText(this, select ? "All incidents selected" : "All incidents deselected", Toast.LENGTH_SHORT).show();
    }

    private void selectAllFacilities(boolean select) {
        if (evacuationCentersCheck != null) evacuationCentersCheck.setChecked(select);
        if (healthFacilitiesCheck != null) healthFacilitiesCheck.setChecked(select);
        if (policeStationsCheck != null) policeStationsCheck.setChecked(select);
        if (fireStationsCheck != null) fireStationsCheck.setChecked(select);
        if (governmentOfficesCheck != null) governmentOfficesCheck.setChecked(select);

        Toast.makeText(this, select ? "All facilities selected" : "All facilities deselected", Toast.LENGTH_SHORT).show();
    }

    private void applyFilters() {
        // Create result intent with all filter states
        Intent resultIntent = new Intent();

        // Add heatmap state
        resultIntent.putExtra("heatmapEnabled", heatmapSwitch != null ? heatmapSwitch.isChecked() : false);

        // Add timeline selection
        resultIntent.putExtra("selectedTimeRange", selectedTimeRange);

        // Add incident filter states
        resultIntent.putExtra("roadAccident", roadAccidentCheck != null ? roadAccidentCheck.isChecked() : true);
        resultIntent.putExtra("fire", fireCheck != null ? fireCheck.isChecked() : true);
        resultIntent.putExtra("medicalEmergency", medicalEmergencyCheck != null ? medicalEmergencyCheck.isChecked() : true);
        resultIntent.putExtra("flooding", floodingCheck != null ? floodingCheck.isChecked() : true);
        resultIntent.putExtra("volcanicActivity", volcanicActivityCheck != null ? volcanicActivityCheck.isChecked() : true);
        resultIntent.putExtra("landslide", landslideCheck != null ? landslideCheck.isChecked() : true);
        resultIntent.putExtra("earthquake", earthquakeCheck != null ? earthquakeCheck.isChecked() : true);
        resultIntent.putExtra("civilDisturbance", civilDisturbanceCheck != null ? civilDisturbanceCheck.isChecked() : true);
        resultIntent.putExtra("armedConflict", armedConflictCheck != null ? armedConflictCheck.isChecked() : true);
        resultIntent.putExtra("infectiousDisease", infectiousDiseaseCheck != null ? infectiousDiseaseCheck.isChecked() : true);

        // Add facility filter states
        resultIntent.putExtra("evacuationCenters", evacuationCentersCheck != null ? evacuationCentersCheck.isChecked() : true);
        resultIntent.putExtra("healthFacilities", healthFacilitiesCheck != null ? healthFacilitiesCheck.isChecked() : true);
        resultIntent.putExtra("policeStations", policeStationsCheck != null ? policeStationsCheck.isChecked() : true);
        resultIntent.putExtra("fireStations", fireStationsCheck != null ? fireStationsCheck.isChecked() : true);
        resultIntent.putExtra("governmentOffices", governmentOfficesCheck != null ? governmentOfficesCheck.isChecked() : true);

        // Set result and finish
        setResult(RESULT_OK, resultIntent);

        // Show confirmation message
        Toast.makeText(this, "Filters applied successfully!", Toast.LENGTH_SHORT).show();

        finish();
    }

    @Override
    public void onBackPressed() {
        // Handle back button press
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }
}