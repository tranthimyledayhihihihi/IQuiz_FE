package com.example.iq5.feature.result.model;

public class DailyReward {
    private int dayNumber;
    private boolean isClaimed;
    private boolean isToday;

    public DailyReward() {}

    public DailyReward(int dayNumber, boolean isClaimed, boolean isToday) {
        this.dayNumber = dayNumber;
        this.isClaimed = isClaimed;
        this.isToday = isToday;
    }

    public int getDayNumber() { return dayNumber; }
    public boolean isClaimed() { return isClaimed; }
    public boolean isToday() { return isToday; }
    // ...
}