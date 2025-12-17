package com.example.iq5.data.model;

public class MatchmakingRequest {
    private int chuDeId;
    private int doKhoLevel;
    private int soLuongCauHoi;

    // Constructor
    public MatchmakingRequest(int chuDeId, int doKhoLevel, int soLuongCauHoi) {
        this.chuDeId = chuDeId;
        this.doKhoLevel = doKhoLevel;
        this.soLuongCauHoi = soLuongCauHoi;
    }

    // Getters and Setters (bỏ qua cho ngắn gọn)
    // ...
}