package com.example.accizardlucban;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mapbox.geojson.Point;
import com.mapbox.maps.CameraOptions;
import com.mapbox.maps.MapView;
import com.mapbox.maps.Style;
import com.mapbox.maps.MapboxMap;
import com.mapbox.maps.plugin.animation.CameraAnimationsPlugin;
import com.mapbox.maps.plugin.animation.MapAnimationOptions;
import android.animation.Animator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapViewActivity extends AppCompatActivity {

    private EditText searchLocationEditText;
    private ImageView clearSearchButton;
    private FloatingActionButton emergencyFab;
    private FloatingActionButton alertFab;
    private ImageView filterButton;
    private ImageButton profile;
    private RecyclerView searchResultsRecyclerView;
    private LinearLayout searchResultsContainer;

    // Navigation tabs
    private LinearLayout homeTab;
    private LinearLayout chatTab;
    private LinearLayout reportTab;
    private LinearLayout mapTab;
    private LinearLayout alertsTab;

    // Mapbox MapView
    private MapView mapView;
    private MapboxMap mapboxMap;
    private CameraAnimationsPlugin cameraAnimationsPlugin;

    // Search functionality
    private SimpleSearchAdapter simpleSearchAdapter;
    private List<SimpleSearchAdapter.SearchPlace> searchPlaces = new ArrayList<>();
    private android.os.Handler searchHandler = new android.os.Handler(android.os.Looper.getMainLooper());
    private Runnable searchRunnable;

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

            // Initialize camera animations plugin
            cameraAnimationsPlugin = mapView.getPlugin("camera");

            // Set initial camera position to Lucban center
            Point lucbanCenter = Point.fromLngLat(121.5564, 14.1136);
            CameraOptions initialCamera = new CameraOptions.Builder()
                    .center(lucbanCenter)
                    .zoom(14.0)
                    .build();
            mapboxMap.setCamera(initialCamera);

            Toast.makeText(this, "Map loaded successfully. Search for locations to navigate.", Toast.LENGTH_SHORT).show();
        }

        initializeSearchPlaces();
        initializeFilters();
        initializeViews();
        initializeSearchEngine();
        setupSearchListeners();
        setupClickListeners();
        setupBottomNavigation();
        setMapTabAsSelected();
        setupFacilitiesLauncher();
    }

    private void initializeSearchPlaces() {
        searchPlaces.clear();

        // Lucban Town Center and Government Buildings
        searchPlaces.add(new SimpleSearchAdapter.SearchPlace("Lucban Municipal Hall", "Lucban, Quezon", 14.1136, 121.5564));
        searchPlaces.add(new SimpleSearchAdapter.SearchPlace("Lucban Plaza", "Lucban, Quezon", 14.1135, 121.5562));
        searchPlaces.add(new SimpleSearchAdapter.SearchPlace("Lucban Public Market", "Lucban, Quezon", 14.1139, 121.5569));
        searchPlaces.add(new SimpleSearchAdapter.SearchPlace("Lucban Church (San Luis Obispo)", "Lucban, Quezon", 14.1133, 121.5561));

        // Health Facilities
        searchPlaces.add(new SimpleSearchAdapter.SearchPlace("Lucban District Hospital", "Lucban, Quezon", 14.1145, 121.5572));
        searchPlaces.add(new SimpleSearchAdapter.SearchPlace("Lucban Medical Center", "Lucban, Quezon", 14.1138, 121.5567));
        searchPlaces.add(new SimpleSearchAdapter.SearchPlace("Lucban Health Center", "Lucban, Quezon", 14.1140, 121.5568));
        searchPlaces.add(new SimpleSearchAdapter.SearchPlace("Barangay Health Station", "Lucban, Quezon", 14.1142, 121.5570));

        // Emergency Services
        searchPlaces.add(new SimpleSearchAdapter.SearchPlace("Lucban Police Station", "Lucban, Quezon", 14.1142, 121.5565));
        searchPlaces.add(new SimpleSearchAdapter.SearchPlace("Lucban Fire Station", "Lucban, Quezon", 14.1140, 121.5563));
        searchPlaces.add(new SimpleSearchAdapter.SearchPlace("Municipal Disaster Risk Reduction Office", "Lucban, Quezon", 14.1137, 121.5566));

        // Educational Institutions
        searchPlaces.add(new SimpleSearchAdapter.SearchPlace("Lucban Elementary School", "Lucban, Quezon", 14.1148, 121.5575));
        searchPlaces.add(new SimpleSearchAdapter.SearchPlace("Lucban National High School", "Lucban, Quezon", 14.1149, 121.5576));
        searchPlaces.add(new SimpleSearchAdapter.SearchPlace("Lucban Integrated School", "Lucban, Quezon", 14.1150, 121.5577));
        searchPlaces.add(new SimpleSearchAdapter.SearchPlace("Lucban Technical-Vocational School", "Lucban, Quezon", 14.1151, 121.5578));

        // Banks and Financial Services
        searchPlaces.add(new SimpleSearchAdapter.SearchPlace("Lucban Rural Bank", "Lucban, Quezon", 14.1137, 121.5565));
        searchPlaces.add(new SimpleSearchAdapter.SearchPlace("BPI Lucban Branch", "Lucban, Quezon", 14.1134, 121.5563));
        searchPlaces.add(new SimpleSearchAdapter.SearchPlace("Landbank Lucban", "Lucban, Quezon", 14.1138, 121.5566));

        // Tourist Spots and Landmarks
        searchPlaces.add(new SimpleSearchAdapter.SearchPlace("Kamay ni Hesus", "Lucban, Quezon", 14.1200, 121.5600));
        searchPlaces.add(new SimpleSearchAdapter.SearchPlace("Lucban Museum", "Lucban, Quezon", 14.1132, 121.5560));
        searchPlaces.add(new SimpleSearchAdapter.SearchPlace("Pahiyas Festival Site", "Lucban, Quezon", 14.1134, 121.5562));

        // Barangays (with more spread out coordinates)
        searchPlaces.add(new SimpleSearchAdapter.SearchPlace("Barangay 1 (Poblacion)", "Lucban, Quezon", 14.1136, 121.5564));
        searchPlaces.add(new SimpleSearchAdapter.SearchPlace("Barangay 2 (Poblacion)", "Lucban, Quezon", 14.1140, 121.5568));
        searchPlaces.add(new SimpleSearchAdapter.SearchPlace("Barangay 3 (Poblacion)", "Lucban, Quezon", 14.1132, 121.5560));
        searchPlaces.add(new SimpleSearchAdapter.SearchPlace("Barangay Ayuti", "Lucban, Quezon", 14.1200, 121.5650));
        searchPlaces.add(new SimpleSearchAdapter.SearchPlace("Barangay Atisan", "Lucban, Quezon", 14.1250, 121.5700));
        searchPlaces.add(new SimpleSearchAdapter.SearchPlace("Barangay Bagumbayan", "Lucban, Quezon", 14.1100, 121.5500));
        searchPlaces.add(new SimpleSearchAdapter.SearchPlace("Barangay Malaking Ambling", "Lucban, Quezon", 14.1300, 121.5800));
        searchPlaces.add(new SimpleSearchAdapter.SearchPlace("Barangay Mungkay", "Lucban, Quezon", 14.1050, 121.5450));
        searchPlaces.add(new SimpleSearchAdapter.SearchPlace("Barangay Nagcamalite", "Lucban, Quezon", 14.1180, 121.5620));
        searchPlaces.add(new SimpleSearchAdapter.SearchPlace("Barangay Tinamnan", "Lucban, Quezon", 14.1080, 121.5480));

        // Commercial Areas
        searchPlaces.add(new SimpleSearchAdapter.SearchPlace("Lucban Business District", "Lucban, Quezon", 14.1135, 121.5565));
        searchPlaces.add(new SimpleSearchAdapter.SearchPlace("Lucban Shopping Center", "Lucban, Quezon", 14.1141, 121.5571));
        searchPlaces.add(new SimpleSearchAdapter.SearchPlace("Lucban Terminal", "Lucban, Quezon", 14.1143, 121.5573));

        // Utilities and Services
        searchPlaces.add(new SimpleSearchAdapter.SearchPlace("Lucban Post Office", "Lucban, Quezon", 14.1134, 121.5563));
        searchPlaces.add(new SimpleSearchAdapter.SearchPlace("Lucban Water District", "Lucban, Quezon", 14.1139, 121.5567));
        searchPlaces.add(new SimpleSearchAdapter.SearchPlace("Lucban Electric Cooperative", "Lucban, Quezon", 14.1144, 121.5574));

        // Parks and Recreation
        searchPlaces.add(new SimpleSearchAdapter.SearchPlace("Lucban Town Park", "Lucban, Quezon", 14.1135, 121.5562));
        searchPlaces.add(new SimpleSearchAdapter.SearchPlace("Lucban Sports Complex", "Lucban, Quezon", 14.1160, 121.5590));
        searchPlaces.add(new SimpleSearchAdapter.SearchPlace("Lucban Basketball Court", "Lucban, Quezon", 14.1147, 121.5575));
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
        clearSearchButton = findViewById(R.id.clearSearchButton);
        emergencyFab = findViewById(R.id.emergencyFab);
        alertFab = findViewById(R.id.alertFab);
        filterButton = findViewById(R.id.filterButton);
        profile = findViewById(R.id.profile);
        searchResultsRecyclerView = findViewById(R.id.searchResultsRecyclerView);
        searchResultsContainer = findViewById(R.id.searchResultsContainer);

        // Initialize navigation tabs
        homeTab = findViewById(R.id.homeTab);
        chatTab = findViewById(R.id.chatTab);
        reportTab = findViewById(R.id.reportTab);
        mapTab = findViewById(R.id.mapTab);
        alertsTab = findViewById(R.id.alertsTab);
    }

    private void initializeSearchEngine() {
        try {
            simpleSearchAdapter = new SimpleSearchAdapter(new ArrayList<>(), this::onSearchResultSelected);
            searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            searchResultsRecyclerView.setAdapter(simpleSearchAdapter);
        } catch (Exception e) {
            Toast.makeText(this, "Error initializing search: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void setupSearchListeners() {
        // Text change listener for search
        searchLocationEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                if (query.length() >= 2) {
                    performSearch(query);
                    clearSearchButton.setVisibility(View.VISIBLE);
                } else {
                    hideSearchResults();
                    clearSearchButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Clear search button
        clearSearchButton.setOnClickListener(v -> {
            searchLocationEditText.setText("");
            hideSearchResults();
            clearSearchButton.setVisibility(View.GONE);
        });

        // Search action listener
        searchLocationEditText.setOnEditorActionListener((v, actionId, event) -> {
            String query = searchLocationEditText.getText().toString().trim();
            if (!query.isEmpty()) {
                performSearch(query);
                // Hide keyboard
                android.view.inputmethod.InputMethodManager imm = (android.view.inputmethod.InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(searchLocationEditText.getWindowToken(), 0);
                }
                return true;
            }
            return false;
        });

        // Focus change listener
        searchLocationEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                String query = searchLocationEditText.getText().toString().trim();
                if (query.length() >= 2) {
                    showSearchResults();
                }
            } else {
                // Hide search results when search field loses focus
                searchHandler.postDelayed(this::hideSearchResults, 200);
            }
        });
    }

    private void performSearch(String query) {
        // Cancel previous debounced search
        if (searchRunnable != null) {
            searchHandler.removeCallbacks(searchRunnable);
        }

        // Create new debounced search
        searchRunnable = () -> {
            List<SimpleSearchAdapter.SearchPlace> filteredPlaces = new ArrayList<>();
            String queryLower = query.toLowerCase();

            // Search through all places
            for (SimpleSearchAdapter.SearchPlace place : searchPlaces) {
                if (place.getName().toLowerCase().contains(queryLower) ||
                        place.getAddress().toLowerCase().contains(queryLower)) {
                    filteredPlaces.add(place);
                }
            }

            // Sort results by relevance (exact matches first, then partial matches)
            filteredPlaces.sort((a, b) -> {
                boolean aExact = a.getName().toLowerCase().startsWith(queryLower);
                boolean bExact = b.getName().toLowerCase().startsWith(queryLower);
                if (aExact && !bExact) return -1;
                if (!aExact && bExact) return 1;
                return a.getName().compareToIgnoreCase(b.getName());
            });

            // Limit results to prevent overwhelming the user
            final List<SimpleSearchAdapter.SearchPlace> finalFilteredPlaces;
            if (filteredPlaces.size() > 10) {
                finalFilteredPlaces = filteredPlaces.subList(0, 10);
            } else {
                finalFilteredPlaces = filteredPlaces;
            }

            runOnUiThread(() -> {
                simpleSearchAdapter.updatePlaces(finalFilteredPlaces);
                showSearchResults();
            });
        };

        // Delay search by 300ms to avoid excessive searching
        searchHandler.postDelayed(searchRunnable, 300);
    }

    private void onSearchResultSelected(SimpleSearchAdapter.SearchPlace place) {
        // Set the selected place name in the search field
        searchLocationEditText.setText(place.getName());

        // Hide search results
        hideSearchResults();

        // Hide keyboard
        android.view.inputmethod.InputMethodManager imm = (android.view.inputmethod.InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(searchLocationEditText.getWindowToken(), 0);
        }

        // Create point for the selected location
        Point selectedPoint = Point.fromLngLat(place.getLongitude(), place.getLatitude());

        // Show initial navigation message with coordinates
        String message = "Navigating to: " + place.getName() +
                        "\nCoordinates: " + String.format("%.4f, %.4f", place.getLatitude(), place.getLongitude());
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

        // Navigate to the location with smooth animation
        // Use different zoom levels based on place type
        double zoomLevel = getZoomLevelForPlace(place);

        // Add a small delay to ensure the toast is visible before animation starts
        new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
            animateToLocation(selectedPoint, zoomLevel);
        }, 500);
    }

    private double getZoomLevelForPlace(SimpleSearchAdapter.SearchPlace place) {
        String name = place.getName().toLowerCase();

        // Higher zoom for specific buildings and landmarks
        if (name.contains("hospital") || name.contains("medical") ||
            name.contains("police") || name.contains("fire") ||
            name.contains("school") || name.contains("bank") ||
            name.contains("church") || name.contains("hall")) {
            return 18.0; // Very close view for buildings
        }

        // Medium zoom for general areas
        if (name.contains("plaza") || name.contains("park") ||
            name.contains("market") || name.contains("terminal")) {
            return 16.0; // Medium view for public spaces
        }

        // Lower zoom for barangays and general areas
        if (name.contains("barangay") || name.contains("district")) {
            return 15.0; // Wider view for areas
        }

        // Default zoom for other places
        return 17.0;
    }

    private void showSearchResults() {
        searchResultsContainer.setVisibility(View.VISIBLE);
    }

    private void hideSearchResults() {
        searchResultsContainer.setVisibility(View.GONE);
    }

        private void animateToLocation(Point point, double zoom) {
        if (cameraAnimationsPlugin != null && mapboxMap != null) {
            try {
                // Create camera options for the target location
                CameraOptions cameraOptions = new CameraOptions.Builder()
                        .center(point)
                        .zoom(zoom)
                        .build();

                // Create animation options for smooth transition
                MapAnimationOptions animationOptions = new MapAnimationOptions.Builder()
                        .duration(3000) // 3 seconds animation for more visible movement
                        .build();

                // Show navigation start message
                Toast.makeText(this, "Starting navigation...", Toast.LENGTH_SHORT).show();

                // Log the navigation attempt
                android.util.Log.d("MapNavigation", "Navigating to: " + point.latitude() + ", " + point.longitude() + " with zoom: " + zoom);

                // Animate to the location
                cameraAnimationsPlugin.flyTo(cameraOptions, animationOptions, new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        // Animation started
                        runOnUiThread(() -> {
                            Toast.makeText(MapViewActivity.this, "Camera animation started", Toast.LENGTH_SHORT).show();
                        });
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        // Animation completed - show arrival message
                        runOnUiThread(() -> {
                            Toast.makeText(MapViewActivity.this,
                                    "Arrived at destination!", Toast.LENGTH_LONG).show();
                        });
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        // Animation cancelled
                        runOnUiThread(() -> {
                            Toast.makeText(MapViewActivity.this,
                                    "Navigation cancelled", Toast.LENGTH_SHORT).show();
                        });
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                        // Animation repeated
                    }
                });
            } catch (Exception e) {
                // Fallback to instant camera movement
                try {
                    CameraOptions cameraOptions = new CameraOptions.Builder()
                            .center(point)
                            .zoom(zoom)
                            .build();
                    mapboxMap.setCamera(cameraOptions);
                    Toast.makeText(this, "Moved to location instantly", Toast.LENGTH_SHORT).show();
                    android.util.Log.d("MapNavigation", "Used fallback navigation");
                } catch (Exception ex) {
                    Toast.makeText(this, "Error navigating to location: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                    android.util.Log.e("MapNavigation", "Navigation error: " + ex.getMessage());
                }
            }
        } else {
            Toast.makeText(this, "Map not ready for navigation", Toast.LENGTH_SHORT).show();
            android.util.Log.e("MapNavigation", "Map or camera plugin not available");
        }
    }

    private void setupClickListeners() {
        emergencyFab.setOnClickListener(v -> {
            Toast.makeText(this, "Emergency services contacted", Toast.LENGTH_LONG).show();
        });

        alertFab.setOnClickListener(v -> {
            Toast.makeText(this, "Alert sent", Toast.LENGTH_SHORT).show();
        });

        // Long press on emergency FAB to navigate to nearest hospital
        emergencyFab.setOnLongClickListener(v -> {
            SimpleSearchAdapter.SearchPlace nearestHospital = null;
            for (SimpleSearchAdapter.SearchPlace place : searchPlaces) {
                if (place.getName().toLowerCase().contains("hospital") ||
                        place.getName().toLowerCase().contains("medical")) {
                    nearestHospital = place;
                    break;
                }
            }
            if (nearestHospital != null) {
                onSearchResultSelected(nearestHospital);
                Toast.makeText(this, "Navigating to nearest medical facility", Toast.LENGTH_LONG).show();
            }
            return true;
        });

        // Long press on alert FAB to navigate to Lucban center
        alertFab.setOnLongClickListener(v -> {
            Point lucbanCenter = Point.fromLngLat(121.5564, 14.1136);
            animateToLocation(lucbanCenter, 15.0);
            Toast.makeText(this, "Navigated to Lucban Town Center", Toast.LENGTH_SHORT).show();
            return true;
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
    }

    private void setupBottomNavigation() {
        homeTab.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(this, MainDashboard.class);
                startActivity(intent);
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
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                finish();
            } catch (Exception e) {
                Toast.makeText(this, "Error navigating to Report", Toast.LENGTH_SHORT).show();
            }
        });

        mapTab.setOnClickListener(v -> {
            Toast.makeText(this, "You are already on the Map", Toast.LENGTH_SHORT).show();
        });

        alertsTab.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(this, AlertsActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                finish();
            } catch (Exception e) {
                Toast.makeText(this, "Error navigating to Alerts", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setMapTabAsSelected() {
        resetAllTabs();

        ImageView mapIcon = mapTab.findViewById(R.id.mapIcon);
        if (mapIcon != null) {
            // You can change the icon to selected state here if you have different icons
        }

        android.widget.TextView mapText = mapTab.findViewById(R.id.mapText);
        if (mapText != null) {
            mapText.setTextColor(getResources().getColor(android.R.color.holo_red_light));
        }
    }

    private void resetAllTabs() {
        int defaultColor = getResources().getColor(android.R.color.darker_gray);
        // You can add logic here to reset all tab text colors if needed
    }

    @Override
    public void onStart() {
        super.onStart();
        setMapTabAsSelected();
    }

    @Override
    public void onResume() {
        super.onResume();
        setMapTabAsSelected();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Remove any pending search callbacks
        if (searchRunnable != null) {
            searchHandler.removeCallbacks(searchRunnable);
        }
    }
}