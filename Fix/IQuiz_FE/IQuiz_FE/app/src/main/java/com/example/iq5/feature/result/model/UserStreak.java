package com.example.iq5.feature.result.model;

public class UserStreak {
    private int currentStreak;
    private int longestStreak;
    private String lastPlayedDate;

    public UserStreak() {}

    public UserStreak(int currentStreak, int longestStreak, String lastPlayedDate) {
        this.currentStreak = currentStreak;
        this.longestStreak = longestStreak;
        this.lastPlayedDate = lastPlayedDate;
    }

    public int getCurrentStreak() { return currentStreak; }
    public int getLongestStreak() { return longestStreak; }
    public String getLastPlayedDate() { return lastPlayedDate; }
    // ...
}