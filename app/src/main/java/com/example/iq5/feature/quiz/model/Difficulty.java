package com.example.iq5.feature.quiz.model;

public class Difficulty {
    private int id;
    private String level;   // easy / medium / hard

    public Difficulty(int id, String level) {
        this.id = id;
        this.level = level;
    }

    public int getId() { return id; }
    public String getLevel() { return level; }

    // FIX: Added return statement
    public String getContent() {
        return level;
    }
}