package com.example.iq5.feature.multiplayer.model;

public class LeaderboardEntry {
    private int bxhID;
    private int userID;
    private int diemTuan;
    private int diemThang;
    private int hangTuan;
    private int hangThang;

    // Thêm thông tin người dùng để hiển thị
    private String tenNguoiDung;
    private String anhDaiDien;

    // Constructors
    public LeaderboardEntry() {
    }

    // Getters and Setters
    public int getBxhID() {
        return bxhID;
    }

    public void setBxhID(int bxhID) {
        this.bxhID = bxhID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getDiemTuan() {
        return diemTuan;
    }

    public void setDiemTuan(int diemTuan) {
        this.diemTuan = diemTuan;
    }

    public int getDiemThang() {
        return diemThang;
    }

    public void setDiemThang(int diemThang) {
        this.diemThang = diemThang;
    }

    public int getHangTuan() {
        return hangTuan;
    }

    public void setHangTuan(int hangTuan) {
        this.hangTuan = hangTuan;
    }

    public int getHangThang() {
        return hangThang;
    }

    public void setHangThang(int hangThang) {
        this.hangThang = hangThang;
    }

    public String getTenNguoiDung() {
        return tenNguoiDung;
    }

    public void setTenNguoiDung(String tenNguoiDung) {
        this.tenNguoiDung = tenNguoiDung;
    }

    public String getAnhDaiDien() {
        return anhDaiDien;
    }

    public void setAnhDaiDien(String anhDaiDien) {
        this.anhDaiDien = anhDaiDien;
    }
}