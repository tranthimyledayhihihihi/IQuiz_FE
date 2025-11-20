package com.example.iq5.feature.quiz.model;

import java.util.List;

public class Question {
    private int id;
    private String content;
    private List<String> options;
    private String correctAnswer;
    private String explanation;

    public Question(int id, String content, List<String> options, String correctAnswer, String explanation) {
        this.id = id;
        this.content = content;
        this.options = options;
        this.correctAnswer = correctAnswer;
        this.explanation = explanation;
    }

    public int getId() { return id; }
    public String getContent() { return content; }
    public List<String> getOptions() { return options; }
    public String getCorrectAnswer() { return correctAnswer; }
    public String getExplanation() { return explanation; }

    // KHẮC PHỤC LỖI: Thêm lệnh return
    // Phương thức này có thể được dùng thay thế cho getCorrectAnswer()
    public String getCorrect() {
        return this.correctAnswer;
    }
}