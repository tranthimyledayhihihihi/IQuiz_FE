package com.example.iq5.core.network.models;

public class GameResult {
    private String KetQua;
    private int Player1Score;
    private int Player2Score;
    private Integer WinnerId;

    // Getters and Setters
    public String getKetQua() { return KetQua; }
    public void setKetQua(String ketQua) { this.KetQua = ketQua; }

    public int getPlayer1Score() { return Player1Score; }
    public void setPlayer1Score(int player1Score) { this.Player1Score = player1Score; }

    public int getPlayer2Score() { return Player2Score; }
    public void setPlayer2Score(int player2Score) { this.Player2Score = player2Score; }

    public Integer getWinnerId() { return WinnerId; }
    public void setWinnerId(Integer winnerId) { this.WinnerId = winnerId; }
}