package com.example.iq5.data.model;

public class GameStartOptions {
    private int chuDeID;
    private int doKhoID;
    private int soCauHoi;
    private String loaiQuiz; // "random", "daily", "custom"

    // Default constructor
    public GameStartOptions() {
        this.chuDeID = 1; // Default general category
        this.doKhoID = 1; // Default easy difficulty
        this.soCauHoi = 10; // Default 10 questions
        this.loaiQuiz = "random";
    }

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
    
    // English method aliases for compatibility
    public void setDifficulty(String difficulty) {
        // Map difficulty to doKhoID (1=easy, 2=medium, 3=hard)
        switch (difficulty.toLowerCase()) {
            case "easy":
                this.doKhoID = 1;
                break;
            case "medium":
                this.doKhoID = 2;
                break;
            case "hard":
                this.doKhoID = 3;
                break;
            default:
                this.doKhoID = 1;
        }
    }
    
    public void setCategory(String category) {
        // Map category to chuDeID (default to 1 for general)
        this.chuDeID = 1; // Default general category
    }
    
    public void setQuestionCount(int count) {
        this.soCauHoi = count;
    }
}