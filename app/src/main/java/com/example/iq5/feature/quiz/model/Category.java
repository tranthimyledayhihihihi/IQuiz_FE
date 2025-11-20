package com.example.iq5.feature.quiz.model;

import com.google.gson.annotations.SerializedName;

public class Category {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("icon")
    private String icon;
    @SerializedName("quiz_count") // Thêm trường bị thiếu trong JSON mock
    private int quizCount;
    @SerializedName("progress_percent") // Thêm trường bị thiếu trong JSON mock
    private int progressPercent;

    // QUAN TRỌNG: Thêm constructor rỗng cho Gson
    public Category() {}

    public Category(int id, String name, String icon, int quizCount, int progressPercent) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.quizCount = quizCount;
        this.progressPercent = progressPercent;
    }

    // Các Getters đã có...
    public int getId() { return id; }
    public String getName() { return name; }
    public String getIcon() { return icon; }
    public int getQuizCount() { return quizCount; }
    public int getProgressPercent() { return progressPercent; }

    public String getContent() {
        return name;
    }
}