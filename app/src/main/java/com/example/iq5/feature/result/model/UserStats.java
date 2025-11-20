package com.example.iq5.feature.result.model;

public class UserStats {
    private String statName;
    private String description;
    private String value; // Ví dụ: "4,100", "820", "5/7"

    public UserStats() {}

    public UserStats(String statName, String description, String value) {
        this.statName = statName;
        this.description = description;
        this.value = value;
    }

    public String getStatName() { return statName; }
    public String getDescription() { return description; }
    public String getValue() { return value; }
    // ...
}