package com.example.quiz.model;

import java.util.List;

public class Question {
    public final String id;
    public final String text;
    public final List<String> options;
    public final int answerIndex;
    public final String category;

    public Question(String id, String text, List<String> options, int answerIndex, String category) {
        this.id = id;
        this.text = text;
        this.options = options;
        this.answerIndex = answerIndex;
        this.category = category;
    }
}