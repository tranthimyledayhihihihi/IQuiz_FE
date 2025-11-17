package com.example.iq5.feature.multiplayer.model;

import java.util.Date;

/**
 * Model cho TranDau (PvPMatches)
 * Ánh xạ từ bảng TranDau trong CSDL
 */
public class PvPMatch {
    private int tranDauID;
    private int phongID;
    private int user1ID;
    private int user2ID;
    private int diemUser1;
    private int diemUser2;
    private String ketQua; // "User1 thắng", "User2 thắng", "Hòa"
    private Date thoiGian;

    // Constructors
    public PvPMatch() {
    }

    // Getters and Setters
    public int getTranDauID() {
        return tranDauID;
    }

    public void setTranDauID(int tranDauID) {
        this.tranDauID = tranDauID;
    }

    public int getPhongID() {
        return phongID;
    }

    public void setPhongID(int phongID) {
        this.phongID = phongID;
    }

    public int getUser1ID() {
        return user1ID;
    }

    public void setUser1ID(int user1ID) {
        this.user1ID = user1ID;
    }

    public int getUser2ID() {
        return user2ID;
    }

    public void setUser2ID(int user2ID) {
        this.user2ID = user2ID;
    }

    public int getDiemUser1() {
        return diemUser1;
    }

    public void setDiemUser1(int diemUser1) {
        this.diemUser1 = diemUser1;
    }

    public int getDiemUser2() {
        return diemUser2;
    }

    public void setDiemUser2(int diemUser2) {
        this.diemUser2 = diemUser2;
    }

    public String getKetQua() {
        return ketQua;
    }

    public void setKetQua(String ketQua) {
        this.ketQua = ketQua;
    }

    public Date getThoiGian() {
        return thoiGian;
    }

    public void setThoiGian(Date thoiGian) {
        this.thoiGian = thoiGian;
    }
}
