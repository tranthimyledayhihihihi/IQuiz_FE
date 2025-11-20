package com.example.iq5.feature.quiz.model;

public class Category {
    private int id;
    private String name;
    private String icon;

    public Category(int id, String name, String icon) {
        this.id = id;
        this.name = name;
        this.icon = icon;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getIcon() { return icon; }

    // KHẮC PHỤC LỖI: Thêm lệnh return name
    // Phương thức này thường được sử dụng trong các Adapter để lấy nội dung hiển thị
    public String getContent() {
        return name;
    }
}