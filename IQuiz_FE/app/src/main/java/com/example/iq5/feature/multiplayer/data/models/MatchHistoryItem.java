package com.example.iq5.feature.multiplayer.data.models;

public class MatchHistoryItem {
    private int tranDauID;
    private String matchCode;
    private String opponentName;
    private int myScore;
    private int opponentScore;
    private String result; // "WIN", "LOSE", "DRAW"
    private String thoiGianBatDau;
    private String thoiGianKetThuc;
    private String trangThai;

    public MatchHistoryItem() {}

    public int getTranDauID() { return tranDauID; }
    public void setTranDauID(int tranDauID) { this.tranDauID = tranDauID; }

    public String getMatchCode() { return matchCode; }
    public void setMatchCode(String matchCode) { this.matchCode = matchCode; }

    public String getOpponentName() { return opponentName; }
    public void setOpponentName(String opponentName) { this.opponentName = opponentName; }

    public int getMyScore() { return myScore; }
    public void setMyScore(int myScore) { this.myScore = myScore; }

    public int getOpponentScore() { return opponentScore; }
    public void setOpponentScore(int opponentScore) { this.opponentScore = opponentScore; }

    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }

    public String getThoiGianBatDau() { return thoiGianBatDau; }
    public void setThoiGianBatDau(String thoiGianBatDau) { this.thoiGianBatDau = thoiGianBatDau; }

    public String getThoiGianKetThuc() { return thoiGianKetThuc; }
    public void setThoiGianKetThuc(String thoiGianKetThuc) { this.thoiGianKetThuc = thoiGianKetThuc; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}