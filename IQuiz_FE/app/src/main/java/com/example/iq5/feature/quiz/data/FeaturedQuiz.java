package com.example.iq5.feature.quiz.data;

import com.google.gson.annotations.SerializedName;

/**
 * Model ánh xạ dữ liệu từ API Featured Quiz (Quiz nổi bật)
 * Dựa trên cấu trúc trả về từ TestQuizController của Backend.
 */
public class FeaturedQuiz {

    @SerializedName("quizID")
    private int id;

    @SerializedName("tieuDe")
    private String title;

    @SerializedName("moTa")
    private String description;

    @SerializedName("anhBia")
    private String imageUrl;

    @SerializedName("luotChoi")
    private int playCount;

    // Các trường bổ sung nếu bạn muốn giữ lại cho logic khác
    private int questionCount;
    private String category;
    private String difficulty;

    // --- Getter và Setter ---

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public int getPlayCount() { return playCount; }
    public void setPlayCount(int playCount) { this.playCount = playCount; }

    public int getQuestionCount() { return questionCount; }
    public void setQuestionCount(int questionCount) { this.questionCount = questionCount; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }
}