package com.example.iq5.feature.specialmode.model;

import java.util.List;

public class ChallengeMode {

    public String modeId;          // SURVIVAL | SPEED
    public String name;
    public String description;

    // SURVIVAL
    public Integer maxLives;

    // SPEED
    public Integer totalTime;
    public Integer comboMultiplierMax;

    // COMMON
    public Integer timePerQuestion;
    public Integer baseReward;
    public Integer bonusPerStreak;
    public List<String> difficultyRange;
}
