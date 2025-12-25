package com.example.iq5.feature.result.model;

public class StreakDay {
    private int dayNumber;
    private String date;
    private int rewardPoints;
    private boolean completed;

    public StreakDay() {}  // BẮT BUỘC: cần constructor rỗng để Gson gọi

    public int getDayNumber() { return dayNumber; }
    public void setDayNumber(int dayNumber) { this.dayNumber = dayNumber; }
    
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    
    public int getRewardPoints() { return rewardPoints; }
    public void setRewardPoints(int rewardPoints) { this.rewardPoints = rewardPoints; }
    
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
}
