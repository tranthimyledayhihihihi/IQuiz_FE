package com.example.iq5.feature.result.model;

public class Achievement {
    private int id;
    private String title;
    private String description;
    private boolean isUnlocked;
    private String iconResId;  // Lưu tên icon dạng String
    private int currentProgress;
    private int targetProgress;

    // Constructor rỗng
    public Achievement() {}

    // Constructor đầy đủ
    public Achievement(int id, String title, String description, boolean isUnlocked,
                       String iconResId, int currentProgress, int targetProgress) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.isUnlocked = isUnlocked;
        this.iconResId = iconResId;
        this.currentProgress = currentProgress;
        this.targetProgress = targetProgress;
    }

    // Getters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public boolean isUnlocked() { return isUnlocked; }
    public String getIconResId() { return iconResId; }
    public int getCurrentProgress() { return currentProgress; }
    public int getTargetProgress() { return targetProgress; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setUnlocked(boolean unlocked) { isUnlocked = unlocked; }
    public void setIconResId(String iconResId) { this.iconResId = iconResId; }
    public void setCurrentProgress(int currentProgress) { this.currentProgress = currentProgress; }
    public void setTargetProgress(int targetProgress) { this.targetProgress = targetProgress; }
}