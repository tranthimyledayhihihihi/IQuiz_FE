package com.example.iq5.feature.auth.model;

public class ProfileResponse {
    public int userId;
    public String name;
    public String email;
    public String avatarUrl;
    public Stats stats;

    public static class Stats {
        public int quizzesTaken;
        public int avgScore;
    }
}
