package com.example.accizardlucban;

public class Report {
    private String reportType;
    private String description;
    private String location;
    private String timestamp;
    private String status;

    public Report(String reportType, String description, String location, String timestamp) {
        this.reportType = reportType;
        this.description = description;
        this.location = location;
        this.timestamp = timestamp;
        this.status = "Submitted"; // Default status
    }

    // Getters
    public String getReportType() {
        return reportType;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getStatus() {
        return status;
    }

    // Setters
    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}