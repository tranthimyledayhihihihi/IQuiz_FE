package com.example.iq5.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Generic API Response
 * Dùng cho các response đơn giản chỉ có message
 */
public class ApiResponse {
    
    @SerializedName("message")
    private String message;
    
    @SerializedName("success")
    private boolean success;

    public ApiResponse() {}

    public ApiResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
