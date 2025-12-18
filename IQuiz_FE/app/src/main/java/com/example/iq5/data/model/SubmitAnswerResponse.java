package com.example.iq5.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Response khi submit câu trả lời
 */
public class SubmitAnswerResponse {
    
    @SerializedName("isCorrect")
    private boolean isCorrect;
    
    @SerializedName("message")
    private String message;

    public SubmitAnswerResponse() {}

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
