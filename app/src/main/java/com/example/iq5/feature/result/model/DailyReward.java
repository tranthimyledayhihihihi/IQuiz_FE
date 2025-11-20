package com.example.iq5.feature.result.model;

import com.google.gson.annotations.SerializedName;

public class DailyReward {

    @SerializedName("day")
    private int dayNumber;        // map "day" từ JSON

    @SerializedName("reward")
    private int reward;

    @SerializedName("claimed")
    private boolean isClaimed;

    private boolean isToday;      // không có trong JSON, set sau khi load

    public DailyReward() {}

    // Getters
    public int getDayNumber() { return dayNumber; }
    public int getReward() { return reward; }
    public boolean isClaimed() { return isClaimed; }
    public boolean isToday() { return isToday; }

    // Setters
    public void setDayNumber(int dayNumber) { this.dayNumber = dayNumber; }
    public void setReward(int reward) { this.reward = reward; }
    public void setClaimed(boolean claimed) { this.isClaimed = claimed; }
    public void setToday(boolean today) { this.isToday = today; }
}
