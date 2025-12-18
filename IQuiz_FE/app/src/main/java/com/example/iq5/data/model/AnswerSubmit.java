package com.example.iq5.data.model;

public class AnswerSubmit {
    private int attemptID;
    private int cauHoiID;
    private String dapAnChon;
    private int userID; // Sẽ được set tự động từ JWT

    public AnswerSubmit(int attemptID, int cauHoiID, String dapAnChon) {
        this.attemptID = attemptID;
        this.cauHoiID = cauHoiID;
        this.dapAnChon = dapAnChon;
    }

    public int getAttemptID() {
        return attemptID;
    }

    public void setAttemptID(int attemptID) {
        this.attemptID = attemptID;
    }

    public int getCauHoiID() {
        return cauHoiID;
    }

    public void setCauHoiID(int cauHoiID) {
        this.cauHoiID = cauHoiID;
    }

    public String getDapAnChon() {
        return dapAnChon;
    }

    public void setDapAnChon(String dapAnChon) {
        this.dapAnChon = dapAnChon;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}