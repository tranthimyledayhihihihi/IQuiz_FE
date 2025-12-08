package com.example.iq5.data.model;

import java.util.List;

public class GameSessionDto {
    private String sessionId;
    private String trangThai; // "waiting", "in_progress", "finished"
    private List<UserDto> players;
    private QuestionDto currentQuestion;

    // Constructor
    public GameSessionDto(String sessionId, String trangThai, List<UserDto> players, QuestionDto currentQuestion) {
        this.sessionId = sessionId;
        this.trangThai = trangThai;
        this.players = players;
        this.currentQuestion = currentQuestion;
    }

    // Getters and Setters (bỏ qua cho ngắn gọn)
    // ...
}