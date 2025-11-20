package com.example.iq5.feature.multiplayer.model;

import java.util.Date;

/**
 * Model cho BanBe (Friends)
 * Ánh xạ từ bảng BanBe trong CSDL
 */
public class Friend {
    private int banBeID;
    private int userID1; // Người gửi yêu cầu
    private int userID2; // Người nhận yêu cầu
    private String trangThai; // "Chờ xác nhận", "Bạn bè", "Đã hủy"
    private Date ngayKetBan;

    // --- THÔNG TIN BỔ SUNG ĐỂ HIỂN THỊ TRÊN UI ---
    private String tenNguoiBan;
    private String anhDaiDienNguoiBan;
    private boolean isOnline; // Lấy từ NguoiDungOnline

    // THÊM: Điểm số và Level của người bạn (CẦN THIẾT cho item_friend.xml)
    private int diemSo;
    private int level;
    // ---------------------------------------------

    // Constructors
    public Friend() {
    }

    // Constructor đầy đủ cho mục đích Mock Data hoặc khởi tạo từ API
    public Friend(int banBeID, String tenNguoiBan, String anhDaiDienNguoiBan, int diemSo, int level, boolean isOnline, String trangThai) {
        this.banBeID = banBeID;
        this.tenNguoiBan = tenNguoiBan;
        this.anhDaiDienNguoiBan = anhDaiDienNguoiBan;
        this.diemSo = diemSo;
        this.level = level;
        this.isOnline = isOnline;
        this.trangThai = trangThai;
    }


    // Getters and Setters
    public int getBanBeID() {
        return banBeID;
    }

    public void setBanBeID(int banBeID) {
        this.banBeID = banBeID;
    }

    public int getUserID1() {
        return userID1;
    }

    public void setUserID1(int userID1) {
        this.userID1 = userID1;
    }

    public int getUserID2() {
        return userID2;
    }

    public void setUserID2(int userID2) {
        this.userID2 = userID2;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public Date getNgayKetBan() {
        return ngayKetBan;
    }

    public void setNgayKetBan(Date ngayKetBan) {
        this.ngayKetBan = ngayKetBan;
    }

    public String getTenNguoiBan() {
        return tenNguoiBan;
    }

    public void setTenNguoiBan(String tenNguoiBan) {
        this.tenNguoiBan = tenNguoiBan;
    }

    public String getAnhDaiDienNguoiBan() {
        return anhDaiDienNguoiBan;
    }

    public void setAnhDaiDienNguoiBan(String anhDaiDienNguoiBan) {
        this.anhDaiDienNguoiBan = anhDaiDienNguoiBan;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    // THÊM: Getter và Setter cho Điểm số
    public int getDiemSo() {
        return diemSo;
    }

    public void setDiemSo(int diemSo) {
        this.diemSo = diemSo;
    }

    // THÊM: Getter và Setter cho Level
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}