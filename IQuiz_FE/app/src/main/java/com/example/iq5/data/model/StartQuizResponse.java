package com.example.iq5.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Response khi bắt đầu quiz
 */
public class StartQuizResponse {
    
    @SerializedName("attemptID")
    private int attemptID;
    
    @SerializedName("question")
    private CauHoiModel question;
    
    @SerializedName("message")
    private String message;

    public StartQuizResponse() {}

    public int getAttemptID() {
        return attemptID;
    }

    public void setAttemptID(int attemptID) {
        this.attemptID = attemptID;
    }

    public CauHoiModel getQuestion() {
        return question;
    }

    public void setQuestion(CauHoiModel question) {
        this.question = question;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
