package com.example.iq5.data.model;

import java.util.List;

public class SimpleQuizResponse {
    public boolean success;
    public List<SimpleQuestionData> data;
    public String message;
    
    public static class SimpleQuestionData {
        public int id;
        public String question;
        public String option_a;
        public String option_b;
        public String option_c;
        public String option_d;
        public String correct_answer;
        public int difficulty_id;
        public int category_id;
    }
}