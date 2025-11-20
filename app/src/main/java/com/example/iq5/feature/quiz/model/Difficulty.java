package com.example.iq5.feature.quiz.model;

import com.google.gson.annotations.SerializedName;

public class Difficulty {
    @SerializedName("id")
    private String id; // Đổi sang String
    @SerializedName("name") // Khớp với trường JSON: "name"
    private String name;
    @SerializedName("description") // Thêm trường bị thiếu trong JSON mock
    private String description;

    // QUAN TRỌNG: Thêm constructor rỗng cho Gson
    public Difficulty() {}

    public Difficulty(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }

    public String getContent() {
        return name; // Hiển thị tên (Dễ, Trung bình, Khó)
    }
}