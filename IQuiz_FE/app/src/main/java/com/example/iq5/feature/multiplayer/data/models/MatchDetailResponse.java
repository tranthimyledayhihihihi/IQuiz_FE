package com.example.iq5.feature.multiplayer.data.models;

import java.util.List;

public class MatchDetailResponse {
    private int tranDauID;
    private String matchCode;
    private int player1ID;
    private int player2ID;
    private String player1Name;
    private String player2Name;
    private int diemPlayer1;
    private int diemPlayer2;
    private String trangThai;
    private String thoiGianBatDau;
    private String thoiGianKetThuc;
    private List<Question> questions;

    public MatchDetailResponse() {}

    public int getTranDauID() { return tranDauID; }
    public void setTranDauID(int tranDauID) { this.tranDauID = tranDauID; }

    public String getMatchCode() { return matchCode; }
    public void setMatchCode(String matchCode) { this.matchCode = matchCode; }

    public int getPlayer1ID() { return player1ID; }
    public void setPlayer1ID(int player1ID) { this.player1ID = player1ID; }

    public int getPlayer2ID() { return player2ID; }
    public void setPlayer2ID(int player2ID) { this.player2ID = player2ID; }

    public String getPlayer1Name() { return player1Name; }
    public void setPlayer1Name(String player1Name) { this.player1Name = player1Name; }

    public String getPlayer2Name() { return player2Name; }
    public void setPlayer2Name(String player2Name) { this.player2Name = player2Name; }

    public int getDiemPlayer1() { return diemPlayer1; }
    public void setDiemPlayer1(int diemPlayer1) { this.diemPlayer1 = diemPlayer1; }

    public int getDiemPlayer2() { return diemPlayer2; }
    public void setDiemPlayer2(int diemPlayer2) { this.diemPlayer2 = diemPlayer2; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public String getThoiGianBatDau() { return thoiGianBatDau; }
    public void setThoiGianBatDau(String thoiGianBatDau) { this.thoiGianBatDau = thoiGianBatDau; }

    public String getThoiGianKetThuc() { return thoiGianKetThuc; }
    public void setThoiGianKetThuc(String thoiGianKetThuc) { this.thoiGianKetThuc = thoiGianKetThuc; }

    public List<Question> getQuestions() { return questions; }
    public void setQuestions(List<Question> questions) { this.questions = questions; }
}