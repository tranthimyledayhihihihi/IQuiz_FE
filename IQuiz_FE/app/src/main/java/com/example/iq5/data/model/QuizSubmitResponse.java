package com.example.iq5.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Response khi submit custom quiz
 */
public class QuizSubmitResponse {
    
    @SerializedName("quizId")
    private int quizId;
    
    @SerializedName("message")
    private String message;
    
    @SerializedName("trangThai")
    private String trangThai;

    public QuizSubmitResponse() {}

    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}
