package com.example.iq5.feature.multiplayer.model;


import java.util.List;

public class Question {
    private String content;
    private String correctAnswerKey; // Đáp án đúng (ví dụ: "A", "B", "C", "D")
    private List<String> options; // Danh sách 4 đáp án (có tiền tố A., B., ...)

    public Question(String content, String correctAnswerKey, List<String> options) {
        this.content = content;
        this.correctAnswerKey = correctAnswerKey;
        this.options = options;
    }

    // Getters
    public String getContent() { return content; }
    public String getCorrectAnswerKey() { return correctAnswerKey; }
    public List<String> getOptions() { return options; }
}