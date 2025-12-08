package com.example.iq5.feature.multiplayer.model;

public class User {
    private String id;
    private String name;
    private String avatar;
    private int level;
    private int score;

    public User(String id, String name, String avatar, int level, int score) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.level = level;
        this.score = score;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getAvatar() { return avatar; }
    public int getLevel() { return level; }
    public int getScore() { return score; }
}
