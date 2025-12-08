package com.example.iq5.feature.quiz.model;

import com.google.gson.annotations.SerializedName;

public class Lifeline {
    @SerializedName("type")
    private String type; // 50:50 / hint / skip
    @SerializedName("name")
    private String name;
    @SerializedName("count_remaining")
    private int countRemaining;
    @SerializedName("is_used_on_current_question")
    private boolean isUsedOnCurrentQuestion;

    public Lifeline() {} // constructor rỗng cho Gson

    public Lifeline(String type, String name, int countRemaining, boolean isUsedOnCurrentQuestion) {
        this.type = type;
        this.name = name;
        this.countRemaining = countRemaining;
        this.isUsedOnCurrentQuestion = isUsedOnCurrentQuestion;
    }

    public String getType() { return type; }
    public String getName() { return name; }
    public int getCountRemaining() { return countRemaining; }
    public boolean isUsedOnCurrentQuestion() { return isUsedOnCurrentQuestion; }

    public void setCountRemaining(int countRemaining) { this.countRemaining = countRemaining; }
    public void setUsedOnCurrentQuestion(boolean used) { this.isUsedOnCurrentQuestion = used; }
}
