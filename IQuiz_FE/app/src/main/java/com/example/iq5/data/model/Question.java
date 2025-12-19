package com.example.iq5.data.model;

import java.util.List;

public class Question {
    private int question_id;
    private int id; // Alias for question_id
    private String question_text;
    private List<Option> options;
    private String correct_answer_id;
    private String correct_answer; // For simple A/B/C/D format
    private String user_selected_answer_id;
    private String explanation;
    private String category;
    private String difficulty;
    
    // Individual options for simple format
    private String option_a;
    private String option_b;
    private String option_c;
    private String option_d;
    
    // Track if answer was correct
    private boolean answeredCorrectly = false;

    // Nested Option class
    public static class Option {
        private String optionId;
        private String optionText;
        
        public Option() {}
        
        public Option(String optionId, String optionText) {
            this.optionId = optionId;
            this.optionText = optionText;
        }
        
        public String getOptionId() {
            return optionId;
        }
        
        public void setOptionId(String optionId) {
            this.optionId = optionId;
        }
        
        public String getOptionText() {
            return optionText;
        }
        
        public void setOptionText(String optionText) {
            this.optionText = optionText;
        }
    }

    // Getters and Setters
    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }

    public String getQuestion_text() {
        return question_text;
    }

    public void setQuestion_text(String question_text) {
        this.question_text = question_text;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    public String getCorrect_answer_id() {
        return correct_answer_id;
    }

    public void setCorrect_answer_id(String correct_answer_id) {
        this.correct_answer_id = correct_answer_id;
    }

    public String getUser_selected_answer_id() {
        return user_selected_answer_id;
    }

    public void setUser_selected_answer_id(String user_selected_answer_id) {
        this.user_selected_answer_id = user_selected_answer_id;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
    
    // ID getters/setters
    public int getId() {
        return id != 0 ? id : question_id;
    }
    
    public void setId(int id) {
        this.id = id;
        this.question_id = id;
    }
    
    // Simple option getters/setters
    public String getOption_a() {
        return option_a;
    }
    
    public void setOption_a(String option_a) {
        this.option_a = option_a;
    }
    
    public String getOption_b() {
        return option_b;
    }
    
    public void setOption_b(String option_b) {
        this.option_b = option_b;
    }
    
    public String getOption_c() {
        return option_c;
    }
    
    public void setOption_c(String option_c) {
        this.option_c = option_c;
    }
    
    public String getOption_d() {
        return option_d;
    }
    
    public void setOption_d(String option_d) {
        this.option_d = option_d;
    }
    
    // Correct answer getters/setters
    public String getCorrect_answer() {
        return correct_answer;
    }
    
    public void setCorrect_answer(String correct_answer) {
        this.correct_answer = correct_answer;
    }
    
    // Answer tracking
    public boolean isAnsweredCorrectly() {
        return answeredCorrectly;
    }
    
    public void setAnsweredCorrectly(boolean answeredCorrectly) {
        this.answeredCorrectly = answeredCorrectly;
    }
    
    // Helper methods
    public boolean isUserAnswerCorrect() {
        return user_selected_answer_id != null && 
               user_selected_answer_id.equals(correct_answer_id);
    }
    
    public void clearUserSelection() {
        this.user_selected_answer_id = null;
    }
    
    // Vietnamese method aliases for compatibility
    public String getNoiDung() {
        return question_text;
    }
    
    public void setNoiDung(String noiDung) {
        this.question_text = noiDung;
    }
    
    // Create options list from individual options
    public void createOptionsFromIndividual() {
        if (option_a != null && option_b != null && option_c != null && option_d != null) {
            options = new java.util.ArrayList<>();
            options.add(new Option("A", option_a));
            options.add(new Option("B", option_b));
            options.add(new Option("C", option_c));
            options.add(new Option("D", option_d));
        }
    }
}