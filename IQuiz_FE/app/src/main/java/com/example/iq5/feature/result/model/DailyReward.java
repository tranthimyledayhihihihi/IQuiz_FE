package com.example.iq5.feature.result.model;

import com.google.gson.annotations.SerializedName;

public class DailyReward {

    // Các trường dữ liệu
    private int dayNumber;        // Số thứ tự ngày: 1, 2, 3, ...
    private int reward;           // Giá trị phần thưởng (coin/điểm)
    private boolean isClaimed;    // Đã nhận thưởng hay chưa
    private boolean isToday;      // Có phải ngày hôm nay không

    // 🔥 QUAN TRỌNG: THÊM GETTER METHODS
    public int getDayNumber() {
        return dayNumber;
    }

    public int getReward() {
        return reward;
    }

    public boolean isClaimed() {
        return isClaimed;
    }

    public boolean isToday() {
        return isToday;
    }

    // 🔥 QUAN TRỌNG: THÊM SETTER METHODS
    public void setDayNumber(int dayNumber) {
        this.dayNumber = dayNumber;
    }

    public void setReward(int reward) {
        this.reward = reward;
    }

    public void setClaimed(boolean claimed) {
        this.isClaimed = claimed;
    }

    public void setToday(boolean today) {
        this.isToday = today;
    }

    // Constructor
    public DailyReward() {}

    public DailyReward(int dayNumber, int reward, boolean isClaimed, boolean isToday) {
        this.dayNumber = dayNumber;
        this.reward = reward;
        this.isClaimed = isClaimed;
        this.isToday = isToday;
    }
}