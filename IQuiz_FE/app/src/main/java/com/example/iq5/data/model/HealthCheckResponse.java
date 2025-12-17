package com.example.iq5.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Response model cho Health Check API
 */
public class HealthCheckResponse {
    
    @SerializedName("DatabaseStatus")
    private String databaseStatus;
    
    @SerializedName("Message")
    private String message;

    public HealthCheckResponse() {}

    public String getDatabaseStatus() {
        return databaseStatus;
    }

    public void setDatabaseStatus(String databaseStatus) {
        this.databaseStatus = databaseStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
