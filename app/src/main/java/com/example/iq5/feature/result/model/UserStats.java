package com.example.iq5.feature.result.model;

public class UserStats {
    private String statName;
    private String description;
    private String value;
    private String iconResId; // <-- THÊM MỚI

    public UserStats() {}

    public UserStats(String statName, String description, String value, String iconResId) {
        this.statName = statName;
        this.description = description;
        this.value = value;
        this.iconResId = iconResId;
    }

    public String getStatName() { return statName; }
    public String getDescription() { return description; }
    public String getValue() { return value; }
    public String getIconResId() { return iconResId; }

    public void setStatName(String statName) { this.statName = statName; }
    public void setDescription(String description) { this.description = description; }
    public void setValue(String value) { this.value = value; }
    public void setIconResId(String iconResId) { this.iconResId = iconResId; }
}
