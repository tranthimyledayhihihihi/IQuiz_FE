package com.example.iq5.data.model;

public class GameStartOptions {
    private int chuDeID;
    private int doKhoID;
    private int soCauHoi;
    private String loaiQuiz; // "random", "daily", "custom"

    public GameStartOptions(int chuDeID, int doKhoID, int soCauHoi, String loaiQuiz) {
        this.chuDeID = chuDeID;
        this.doKhoID = doKhoID;
        this.soCauHoi = soCauHoi;
        this.loaiQuiz = loaiQuiz;
    }

    public int getChuDeID() {
        return chuDeID;
    }

    public void setChuDeID(int chuDeID) {
        this.chuDeID = chuDeID;
    }

    public int getDoKhoID() {
        return doKhoID;
    }

    public void setDoKhoID(int doKhoID) {
        this.doKhoID = doKhoID;
    }

    public int getSoCauHoi() {
        return soCauHoi;
    }

    public void setSoCauHoi(int soCauHoi) {
        this.soCauHoi = soCauHoi;
    }

    public String getLoaiQuiz() {
        return loaiQuiz;
    }

    public void setLoaiQuiz(String loaiQuiz) {
        this.loaiQuiz = loaiQuiz;
    }
}