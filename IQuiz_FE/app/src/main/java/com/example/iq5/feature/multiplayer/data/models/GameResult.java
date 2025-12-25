package com.example.iq5.feature.multiplayer.data.models;

import com.google.gson.annotations.SerializedName;

public class GameResult {

    private String ketQua;        // "Player1Win", "Player2Win", "Draw"
    private int player1Score;
    private int player2Score;

    // 🔥 SỬA DUY NHẤT Ở ĐÂY
    @SerializedName("winnerUserID")  // ← Tên trong JSON
    private Integer winnerUserId;     // ← Tên trong Java
    private String trangThai;     // "HoanThanh"

    public GameResult() {}

    public String getKetQua() {
        return ketQua;
    }

    public void setKetQua(String ketQua) {
        this.ketQua = ketQua;
    }

    public int getPlayer1Score() {
        return player1Score;
    }

    public void setPlayer1Score(int player1Score) {
        this.player1Score = player1Score;
    }

    public int getPlayer2Score() {
        return player2Score;
    }

    public void setPlayer2Score(int player2Score) {
        this.player2Score = player2Score;
    }

    public Integer getWinnerUserId() {
        return winnerUserId;
    }

    public void setWinnerUserId(Integer winnerUserId) {
        this.winnerUserId = winnerUserId;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}
