package com.example.iq5.feature.multiplayer.data.models;

public class MatchData {
    private int tranDauID;
    private String matchCode;
    private int player1ID;
    private int player2ID;
    private int soCauHoi;
    private int diemPlayer1;
    private int diemPlayer2;
    private Integer winnerUserID;
    private String trangThai;
    private String thoiGianBatDau;
    private String thoiGianKetThuc;

    // Getters and Setters
    public int getTranDauID() { return tranDauID; }
    public void setTranDauID(int tranDauID) { this.tranDauID = tranDauID; }

    public String getMatchCode() { return matchCode; }
    public void setMatchCode(String matchCode) { this.matchCode = matchCode; }

    public int getPlayer1ID() { return player1ID; }
    public void setPlayer1ID(int player1ID) { this.player1ID = player1ID; }

    public int getPlayer2ID() { return player2ID; }
    public void setPlayer2ID(int player2ID) { this.player2ID = player2ID; }

    public int getSoCauHoi() { return soCauHoi; }
    public void setSoCauHoi(int soCauHoi) { this.soCauHoi = soCauHoi; }

    public int getDiemPlayer1() { return diemPlayer1; }
    public void setDiemPlayer1(int diemPlayer1) { this.diemPlayer1 = diemPlayer1; }

    public int getDiemPlayer2() { return diemPlayer2; }
    public void setDiemPlayer2(int diemPlayer2) { this.diemPlayer2 = diemPlayer2; }

    public Integer getWinnerUserID() { return winnerUserID; }
    public void setWinnerUserID(Integer winnerUserID) { this.winnerUserID = winnerUserID; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public String getThoiGianBatDau() { return thoiGianBatDau; }
    public void setThoiGianBatDau(String thoiGianBatDau) { this.thoiGianBatDau = thoiGianBatDau; }

    public String getThoiGianKetThuc() { return thoiGianKetThuc; }
    public void setThoiGianKetThuc(String thoiGianKetThuc) { this.thoiGianKetThuc = thoiGianKetThuc; }
}