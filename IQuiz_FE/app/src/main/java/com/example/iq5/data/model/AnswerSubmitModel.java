package com.example.iq5.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Model để submit câu trả lời
 * Tương ứng với AnswerSubmitModel.cs
 */
public class AnswerSubmitModel {
    
    @SerializedName("quizAttemptID")
    private int quizAttemptID;
    
    @SerializedName("cauHoiID")
    private int cauHoiID;
    
    @SerializedName("dapAnDaChon")
    private String dapAnDaChon;
    
    @SerializedName("userID")
    private int userID;

    // Constructors
    public AnswerSubmitModel() {}

    public AnswerSubmitModel(int quizAttemptID, int cauHoiID, String dapAnDaChon, int userID) {
        this.quizAttemptID = quizAttemptID;
        this.cauHoiID = cauHoiID;
        this.dapAnDaChon = dapAnDaChon;
        this.userID = userID;
    }

    // Getters & Setters
    public int getQuizAttemptID() {
        return quizAttemptID;
    }

    public void setQuizAttemptID(int quizAttemptID) {
        this.quizAttemptID = quizAttemptID;
    }

    public int getCauHoiID() {
        return cauHoiID;
    }

    public void setCauHoiID(int cauHoiID) {
        this.cauHoiID = cauHoiID;
    }

    public String getDapAnDaChon() {
        return dapAnDaChon;
    }

    public void setDapAnDaChon(String dapAnDaChon) {
        this.dapAnDaChon = dapAnDaChon;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}
