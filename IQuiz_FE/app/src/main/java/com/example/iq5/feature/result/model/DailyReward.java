package com.example.iq5.feature.result.model;

public class DailyReward {
    private int dayNumber;     // Ngày 1..N (UI)
    private int reward;        // điểm/coins (giaTri)
    private boolean claimed;   // đã nhận
    private boolean today;     // hôm nay

    public int getDayNumber() { return dayNumber; }
    public int getReward() { return reward; }
    public boolean isClaimed() { return claimed; }
    public boolean isToday() { return today; }

    public void setDayNumber(int dayNumber) { this.dayNumber = dayNumber; }
    public void setReward(int reward) { this.reward = reward; }
    public void setClaimed(boolean claimed) { this.claimed = claimed; }
    public void setToday(boolean today) { this.today = today; }
}
