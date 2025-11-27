package com.example.iq5.feature.multiplayer.model;

import java.util.Date;

/**
 * Model cho PhongChoi (PvPRooms)
 * Ánh xạ từ bảng PhongChoi trong CSDL
 */
public class PvPRoom {
    private int phongID;
    private String tenPhong;
    private String maPhong;
    private String trangThai; // "Đang chờ", "Đang chơi", "Kết thúc"
    private Date thoiGianTao;
    // Bạn có thể thêm danh sách người chơi trong phòng này
    // private List<NguoiDung> nguoiChoi;

    // Constructors
    public PvPRoom() {
    }

    public PvPRoom(int phongID, String tenPhong, String maPhong, String trangThai, Date thoiGianTao) {
        this.phongID = phongID;
        this.tenPhong = tenPhong;
        this.maPhong = maPhong;
        this.trangThai = trangThai;
        this.thoiGianTao = thoiGianTao;
    }

    // Getters and Setters
    public int getPhongID() {
        return phongID;
    }

    public void setPhongID(int phongID) {
        this.phongID = phongID;
    }

    public String getTenPhong() {
        return tenPhong;
    }

    public void setTenPhong(String tenPhong) {
        this.tenPhong = tenPhong;
    }

    public String getMaPhong() {
        return maPhong;
    }

    public void setMaPhong(String maPhong) {
        this.maPhong = maPhong;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public Date getThoiGianTao() {
        return thoiGianTao;
    }

    public void setThoiGianTao(Date thoiGianTao) {
        this.thoiGianTao = thoiGianTao;
    }
}