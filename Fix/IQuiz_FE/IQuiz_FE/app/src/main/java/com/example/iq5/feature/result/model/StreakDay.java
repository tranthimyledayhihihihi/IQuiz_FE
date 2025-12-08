package com.example.iq5.feature.result.model;

public class StreakDay {
    private int dayNumber;
    private String date;
    private int rewardPoints;
    private boolean completed;

    public StreakDay() {}  // BẮT BUỘC: cần constructor rỗng để Gson gọi

    public int getDayNumber() { return dayNumber; }
    public String getDate() { return date; }
    public int getRewardPoints() { return rewardPoints; }
    public boolean isCompleted() { return completed; }
}
