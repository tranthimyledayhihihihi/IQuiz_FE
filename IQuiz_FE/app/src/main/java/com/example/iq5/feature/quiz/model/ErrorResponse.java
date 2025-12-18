package com.example.iq5.feature.quiz.model;

import com.google.gson.annotations.SerializedName;

public class ErrorResponse {
    @SerializedName("status")
    private String status;
    @SerializedName("error_code")
    private int errorCode;
    @SerializedName("error_type")
    private String errorType;
    @SerializedName("title")
    private String title;
    @SerializedName("message")
    private String message;
    @SerializedName("action_text")
    private String actionText;

    // Thêm constructors, getters và setters...
}