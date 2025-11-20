package com.example.iq5.feature.quiz.model;

public class Lifeline {
    private String type;   // 5050 / hint / skip
    private boolean used;

    public Lifeline(String type, boolean used) {
        this.type = type;
        this.used = used;
    }

    public String getType() { return type; }
    public boolean isUsed() { return used; }
    public void setUsed(boolean used) { this.used = used; }
}
