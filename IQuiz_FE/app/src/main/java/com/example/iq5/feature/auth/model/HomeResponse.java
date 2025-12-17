package com.example.iq5.feature.auth.model;

import java.util.List;

public class HomeResponse {

    public String welcomeMessage;
    public List<QuizItem> featuredQuizzes;

    public static class QuizItem {
        public int id;
        public String title;
        public String difficulty;
    }
}
