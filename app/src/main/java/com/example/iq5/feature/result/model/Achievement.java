package com.example.iq5.feature.result.model;

public class Achievement {
    private int id;
    private String title;
    private String description;
    private boolean isUnlocked;
    private int iconResId;
    private int currentProgress;
    private int targetProgress;

    public Achievement() {}

    // Constructor Hoàn chỉnh (Dành cho Thành tựu Đã/Chưa mở khóa)
    public Achievement(int id, String title, String description, boolean isUnlocked, int iconResId, int currentProgress, int targetProgress) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.isUnlocked = isUnlocked;
        this.iconResId = iconResId;
        this.currentProgress = currentProgress;
        this.targetProgress = targetProgress;
    }

    // Constructor đơn giản (Dùng tạm nếu bạn không cần progress bar)
    public Achievement(int id, String title, String description, boolean isUnlocked, int iconResId) {
        this(id, title, description, isUnlocked, iconResId, isUnlocked ? 100 : 0, 100);
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public boolean isUnlocked() { return isUnlocked; }
    public int getIconResId() { return iconResId; }
    public int getCurrentProgress() { return currentProgress; }
    public int getTargetProgress() { return targetProgress; }
}