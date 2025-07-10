package com.example.accizardlucban.models;

import java.util.HashMap;
import java.util.Map;

public class Report {
    private String reportId;
    private String userId;
    private String title;
    private String description;
    private String location;
    private String priority; // low, medium, high, emergency
    private String category; // fire, flood, earthquake, landslide, volcanic, health, police, other
    private String status; // pending, ongoing, responded, resolved
    private String imageUrl;
    private long timestamp;
    private String adminResponse;
    private long responseTimestamp;

    // Default constructor required for Firestore
    public Report() {}

    public Report(String userId, String title, String description, String location, 
                  String priority, String category) {
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.priority = priority;
        this.category = category;
        this.status = "pending";
        this.timestamp = System.currentTimeMillis();
    }

    // Getters and Setters
    public String getReportId() { return reportId; }
    public void setReportId(String reportId) { this.reportId = reportId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public String getAdminResponse() { return adminResponse; }
    public void setAdminResponse(String adminResponse) { this.adminResponse = adminResponse; }

    public long getResponseTimestamp() { return responseTimestamp; }
    public void setResponseTimestamp(long responseTimestamp) { this.responseTimestamp = responseTimestamp; }

    // Convert to Map for Firestore
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("title", title);
        map.put("description", description);
        map.put("location", location);
        map.put("priority", priority);
        map.put("category", category);
        map.put("status", status);
        map.put("imageUrl", imageUrl != null ? imageUrl : "");
        map.put("timestamp", timestamp);
        map.put("adminResponse", adminResponse != null ? adminResponse : "");
        map.put("responseTimestamp", responseTimestamp);
        return map;
    }

    // Priority constants
    public static final String PRIORITY_LOW = "low";
    public static final String PRIORITY_MEDIUM = "medium";
    public static final String PRIORITY_HIGH = "high";
    public static final String PRIORITY_EMERGENCY = "emergency";

    // Category constants
    public static final String CATEGORY_FIRE = "fire";
    public static final String CATEGORY_FLOOD = "flood";
    public static final String CATEGORY_EARTHQUAKE = "earthquake";
    public static final String CATEGORY_LANDSLIDE = "landslide";
    public static final String CATEGORY_VOLCANIC = "volcanic";
    public static final String CATEGORY_HEALTH = "health";
    public static final String CATEGORY_POLICE = "police";
    public static final String CATEGORY_OTHER = "other";

    // Status constants
    public static final String STATUS_PENDING = "pending";
    public static final String STATUS_ONGOING = "ongoing";
    public static final String STATUS_RESPONDED = "responded";
    public static final String STATUS_RESOLVED = "resolved";
} 