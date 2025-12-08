package com.example.iq5.feature.result.model;

public class MatchResult {
    private int score;
    private int correctAnswers;
    private int totalQuestions;
    private boolean isWin;

    public MatchResult() {}

    public MatchResult(int score, int correctAnswers, int totalQuestions, boolean isWin) {
        this.score = score;
        this.correctAnswers = correctAnswers;
        this.totalQuestions = totalQuestions;
        this.isWin = isWin;
    }

    public int getScore() { return score; }
    public int getCorrectAnswers() { return correctAnswers; }
    public int getTotalQuestions() { return totalQuestions; }
    public boolean isWin() { return isWin; }
    // ...
}