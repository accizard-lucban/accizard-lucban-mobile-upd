package com.example.accizardlucban.models;

import java.util.HashMap;
import java.util.Map;

public class User {
    private String userId;
    private String email;
    private String fullName;
    private String phoneNumber;
    private String address;
    private String profilePictureUrl;
    private long createdAt;
    private boolean isVerified;
    private String validIdUrl;

    // Default constructor required for Firestore
    public User() {}

    public User(String userId, String email, String fullName, String phoneNumber, String address) {
        this.userId = userId;
        this.email = email;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.createdAt = System.currentTimeMillis();
        this.isVerified = false;
    }

    // Getters and Setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getProfilePictureUrl() { return profilePictureUrl; }
    public void setProfilePictureUrl(String profilePictureUrl) { this.profilePictureUrl = profilePictureUrl; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

    public boolean isVerified() { return isVerified; }
    public void setVerified(boolean verified) { isVerified = verified; }

    public String getValidIdUrl() { return validIdUrl; }
    public void setValidIdUrl(String validIdUrl) { this.validIdUrl = validIdUrl; }

    // Convert to Map for Firestore
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        map.put("email", email);
        map.put("fullName", fullName);
        map.put("phoneNumber", phoneNumber);
        map.put("address", address);
        map.put("profilePictureUrl", profilePictureUrl != null ? profilePictureUrl : "");
        map.put("createdAt", createdAt);
        map.put("isVerified", isVerified);
        map.put("validIdUrl", validIdUrl != null ? validIdUrl : "");
        return map;
    }
} 