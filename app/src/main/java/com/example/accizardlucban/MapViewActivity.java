package com.example.accizardlucban;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mapbox.maps.MapView;
import com.mapbox.maps.Style;
import com.mapbox.maps.MapboxMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapViewActivity extends AppCompatActivity {

    private EditText searchLocationEditText;
    private FloatingActionButton emergencyFab;
    private FloatingActionButton alertFab;
    private ImageView filterButton;
    private ImageButton profile;

    // Navigation tabs
    private LinearLayout homeTab;
    private LinearLayout chatTab;
    private LinearLayout reportTab;
    private LinearLayout mapTab;
    private LinearLayout alertsTab;

    // Mapbox MapView
    private MapView mapView;
    private MapboxMap mapboxMap;

    // Activity Result Launcher for Facilities
    private ActivityResultLauncher<Intent> facilitiesLauncher;

    // Filter states
    private boolean heatmapEnabled = false;
    private Map<String, Boolean> incidentFilters = new HashMap<>();
    private Map<String, Boolean> facilityFilters = new HashMap<>();
    private String selectedTimeRange = "Today";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Initialize MapView
        mapView = findViewById(R.id.mapView);
        if (mapView != null) {
            mapboxMap = mapView.getMapboxMap();
            mapboxMap.loadStyleUri(Style.MAPBOX_STREETS);
        }

        initializeFilters();
        initializeViews();
        setupClickListeners();
        setupBottomNavigation();
        setMapTabAsSelected();
        setupFacilitiesLauncher();
    }

    private void initializeFilters() {
        // Initialize incident filters - all enabled by default
        incidentFilters.put("Road Accident", true);
        incidentFilters.put("Fire", true);
        incidentFilters.put("Medical Emergency", true);
        incidentFilters.put("Flooding", true);
        incidentFilters.put("Volcanic Activity", true);
        incidentFilters.put("Landslide", true);
        incidentFilters.put("Earthquake", true);
        incidentFilters.put("Civil Disturbance", true);
        incidentFilters.put("Armed Conflict", true);
        incidentFilters.put("Infectious Disease", true);

        // Initialize facility filters - all enabled by default
        facilityFilters.put("Evacuation Centers", true);
        facilityFilters.put("Health Facilities", true);
        facilityFilters.put("Police Stations", true);
        facilityFilters.put("Fire Stations", true);
        facilityFilters.put("Government Offices", true);
    }

    private void setupFacilitiesLauncher() {
        facilitiesLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();

                        // Get filter settings from Facilities activity
                        heatmapEnabled = data.getBooleanExtra("heatmapEnabled", false);
                        selectedTimeRange = data.getStringExtra("selectedTimeRange");
                        if (selectedTimeRange == null) selectedTimeRange = "Today";

                        // Update incident filters
                        incidentFilters.put("Road Accident", data.getBooleanExtra("roadAccident", true));
                        incidentFilters.put("Fire", data.getBooleanExtra("fire", true));
                        incidentFilters.put("Medical Emergency", data.getBooleanExtra("medicalEmergency", true));
                        incidentFilters.put("Flooding", data.getBooleanExtra("flooding", true));
                        incidentFilters.put("Volcanic Activity", data.getBooleanExtra("volcanicActivity", true));
                        incidentFilters.put("Landslide", data.getBooleanExtra("landslide", true));
                        incidentFilters.put("Earthquake", data.getBooleanExtra("earthquake", true));
                        incidentFilters.put("Civil Disturbance", data.getBooleanExtra("civilDisturbance", true));
                        incidentFilters.put("Armed Conflict", data.getBooleanExtra("armedConflict", true));
                        incidentFilters.put("Infectious Disease", data.getBooleanExtra("infectiousDisease", true));

                        // Update facility filters
                        facilityFilters.put("Evacuation Centers", data.getBooleanExtra("evacuationCenters", true));
                        facilityFilters.put("Health Facilities", data.getBooleanExtra("healthFacilities", true));
                        facilityFilters.put("Police Stations", data.getBooleanExtra("policeStations", true));
                        facilityFilters.put("Fire Stations", data.getBooleanExtra("fireStations", true));
                        facilityFilters.put("Government Offices", data.getBooleanExtra("governmentOffices", true));

                        // Apply filters to map
                        // applyFiltersToMap(); // This method is removed

                        // Show confirmation message with applied filters
                        showFilterAppliedMessage();
                    }
                }
        );
    }

    private void showFilterAppliedMessage() {
        StringBuilder message = new StringBuilder("Filters applied:\n");

        // Show heatmap status
        message.append("Heatmap: ").append(heatmapEnabled ? "ON" : "OFF").append("\n");
        message.append("Time Range: ").append(selectedTimeRange).append("\n");

        // Count active filters
        int activeIncidentFilters = 0;
        int activeFacilityFilters = 0;

        for (Boolean value : incidentFilters.values()) {
            if (value) activeIncidentFilters++;
        }

        for (Boolean value : facilityFilters.values()) {
            if (value) activeFacilityFilters++;
        }

        message.append("Incidents: ").append(activeIncidentFilters).append("/").append(incidentFilters.size()).append(" types\n");
        message.append("Facilities: ").append(activeFacilityFilters).append("/").append(facilityFilters.size()).append(" types");

        Toast.makeText(this, message.toString(), Toast.LENGTH_LONG).show();
    }

    private void initializeViews() {
        searchLocationEditText = findViewById(R.id.searchLocationEditText);
        emergencyFab = findViewById(R.id.emergencyFab);
        alertFab = findViewById(R.id.alertFab);
        filterButton = findViewById(R.id.filterButton);
        profile = findViewById(R.id.profile);

        // Initialize navigation tabs
        homeTab = findViewById(R.id.homeTab);
        chatTab = findViewById(R.id.chatTab);
        reportTab = findViewById(R.id.reportTab);
        mapTab = findViewById(R.id.mapTab);
        alertsTab = findViewById(R.id.alertsTab);
    }

    private void setupClickListeners() {
        emergencyFab.setOnClickListener(v -> {
            Toast.makeText(this, "Emergency services contacted", Toast.LENGTH_LONG).show();
        });

        alertFab.setOnClickListener(v -> {
            Toast.makeText(this, "Alert sent", Toast.LENGTH_SHORT).show();
        });

        filterButton.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(MapViewActivity.this, Facilities.class);
                intent.putExtra("heatmapEnabled", heatmapEnabled);
                intent.putExtra("selectedTimeRange", selectedTimeRange);
                intent.putExtra("roadAccident", incidentFilters.containsKey("Road Accident") ? incidentFilters.get("Road Accident") : true);
                intent.putExtra("fire", incidentFilters.containsKey("Fire") ? incidentFilters.get("Fire") : true);
                intent.putExtra("medicalEmergency", incidentFilters.containsKey("Medical Emergency") ? incidentFilters.get("Medical Emergency") : true);
                intent.putExtra("flooding", incidentFilters.containsKey("Flooding") ? incidentFilters.get("Flooding") : true);
                intent.putExtra("volcanicActivity", incidentFilters.containsKey("Volcanic Activity") ? incidentFilters.get("Volcanic Activity") : true);
                intent.putExtra("landslide", incidentFilters.containsKey("Landslide") ? incidentFilters.get("Landslide") : true);
                intent.putExtra("earthquake", incidentFilters.containsKey("Earthquake") ? incidentFilters.get("Earthquake") : true);
                intent.putExtra("civilDisturbance", incidentFilters.containsKey("Civil Disturbance") ? incidentFilters.get("Civil Disturbance") : true);
                intent.putExtra("armedConflict", incidentFilters.containsKey("Armed Conflict") ? incidentFilters.get("Armed Conflict") : true);
                intent.putExtra("infectiousDisease", incidentFilters.containsKey("Infectious Disease") ? incidentFilters.get("Infectious Disease") : true);
                intent.putExtra("evacuationCenters", facilityFilters.containsKey("Evacuation Centers") ? facilityFilters.get("Evacuation Centers") : true);
                intent.putExtra("healthFacilities", facilityFilters.containsKey("Health Facilities") ? facilityFilters.get("Health Facilities") : true);
                intent.putExtra("policeStations", facilityFilters.containsKey("Police Stations") ? facilityFilters.get("Police Stations") : true);
                intent.putExtra("fireStations", facilityFilters.containsKey("Fire Stations") ? facilityFilters.get("Fire Stations") : true);
                intent.putExtra("governmentOffices", facilityFilters.containsKey("Government Offices") ? facilityFilters.get("Government Offices") : true);
                facilitiesLauncher.launch(intent);
            } catch (Exception e) {
                Toast.makeText(this, "Error opening filters: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        profile.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(MapViewActivity.this, ProfileActivity.class);
                startActivity(intent);
                finish();
            } catch (Exception e) {
                Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
            }
        });

        searchLocationEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (!searchLocationEditText.getText().toString().trim().isEmpty()) {
                Toast.makeText(this, "Map search is unavailable.", Toast.LENGTH_SHORT).show();
            }
            return true;
        });
    }

    private void setupBottomNavigation() {
        homeTab.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(this, MainDashboard.class);
                startActivity(intent);
                // Optional: Add transition animation
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                finish();
            } catch (Exception e) {
                Toast.makeText(this, "Error navigating to Home", Toast.LENGTH_SHORT).show();
            }
        });

        chatTab.setOnClickListener(v -> {
            try{
                Intent intent = new Intent(this, ChatActivity.class);
                startActivity(intent);
                // Optional: Add transition animation
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                finish();
            } catch (Exception e) {
                Toast.makeText(this, "Chat feature coming soon", Toast.LENGTH_SHORT).show();
            }
        });

        reportTab.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(this, ReportSubmissionActivity.class);
                startActivity(intent);
                // Optional: Add transition animation
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                finish();
            } catch (Exception e) {
                Toast.makeText(this, "Error navigating to Report", Toast.LENGTH_SHORT).show();
            }
        });

        mapTab.setOnClickListener(v -> {
            // Already on map
            Toast.makeText(this, "You are already on the Map", Toast.LENGTH_SHORT).show();
        });

        alertsTab.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(this, AlertsActivity.class);
                startActivity(intent);
                // Optional: Add transition animation
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                finish();
            } catch (Exception e) {
                Toast.makeText(this, "Error navigating to Report", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setMapTabAsSelected() {
        // Reset all tabs to unselected state first
        resetAllTabs();

        // Set map tab as selected
        ImageView mapIcon = mapTab.findViewById(R.id.mapIcon);
        if (mapIcon != null) {
            // You can change the icon to selected state here if you have different icons
        }

        // Change text color to indicate selection
        android.widget.TextView mapText = mapTab.findViewById(R.id.mapText);
        if (mapText != null) {
            mapText.setTextColor(getResources().getColor(android.R.color.holo_red_light));
        }
    }

    private void resetAllTabs() {
        // Reset all tab colors to default
        int defaultColor = getResources().getColor(android.R.color.darker_gray);
        // You can add logic here to reset all tab text colors if needed
    }

    // Removed all Google Maps and marker logic

    @Override
    public void onStart() {
        super.onStart();
        // No mapView.onStart() in Mapbox SDK v11
    }

    @Override
    public void onResume() {
        super.onResume();
        setMapTabAsSelected();
        // No mapView.onResume() in Mapbox SDK v11
    }

    @Override
    public void onPause() {
        super.onPause();
        // No mapView.onPause() in Mapbox SDK v11
    }

    @Override
    public void onStop() {
        super.onStop();
        // No mapView.onStop() in Mapbox SDK v11
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        // No mapView.onLowMemory() in Mapbox SDK v11
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // No mapView.onDestroy() in Mapbox SDK v11
    }
}