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

    // Thêm thông tin người dùng (UserID2) để hiển thị
    private String tenNguoiBan;
    private String anhDaiDienNguoiBan;
    private boolean isOnline; // Lấy từ NguoiDungOnline

    // Constructors
    public Friend() {
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
}
