package com.example.iq5.feature.result.model;

public class StreakDay {
    private final int dayNumber;
    private final String date;
    private final int rewardPoints;
    private final boolean completed;

    public StreakDay(int dayNumber, String date, int rewardPoints, boolean completed) {
        this.dayNumber = dayNumber;
        this.date = date;
        this.rewardPoints = rewardPoints;
        this.completed = completed;
    }

    public int getDayNumber() { return dayNumber; }
    public String getDate() { return date; }
    public int getRewardPoints() { return rewardPoints; }
    public boolean isCompleted() { return completed; }
}