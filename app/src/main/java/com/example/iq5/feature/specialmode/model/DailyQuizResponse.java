package com.example.iq5.feature.specialmode.model;

import java.util.List;

public class DailyQuizResponse {
    public String date;
    public int streakDays;
    public int totalRewardsToday;
    public List<DailyQuizItem> items;
}
