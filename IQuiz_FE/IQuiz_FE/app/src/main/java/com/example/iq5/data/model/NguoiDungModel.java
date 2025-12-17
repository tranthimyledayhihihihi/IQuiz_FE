package com.example.iq5.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.Date;

/**
 * Model cho NguoiDung (User) từ backend
 * Tương ứng với NguoiDung.cs
 */
public class NguoiDungModel {
    
    @SerializedName("userID")
    private int userID;
    
    @SerializedName("tenDangNhap")
    private String tenDangNhap;
    
    @SerializedName("email")
    private String email;
    
    @SerializedName("hoTen")
    private String hoTen;
    
    @SerializedName("anhDaiDien")
    private String anhDaiDien;
    
    @SerializedName(value = "ngayDangKy", alternate = {"ngayDangKyfix"})
    private Date ngayDangKy;
    
    @SerializedName("lanDangNhapCuoi")
    private Date lanDangNhapCuoi;
    
    @SerializedName("trangThai")
    private boolean trangThai;
    
    @SerializedName("vaiTroID")
    private int vaiTroID;

    // Constructors
    public NguoiDungModel() {}

    // Getters & Setters
    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getAnhDaiDien() {
        return anhDaiDien;
    }

    public void setAnhDaiDien(String anhDaiDien) {
        this.anhDaiDien = anhDaiDien;
    }

    public Date getNgayDangKy() {
        return ngayDangKy;
    }

    public void setNgayDangKy(Date ngayDangKy) {
        this.ngayDangKy = ngayDangKy;
    }

    public Date getLanDangNhapCuoi() {
        return lanDangNhapCuoi;
    }

    public void setLanDangNhapCuoi(Date lanDangNhapCuoi) {
        this.lanDangNhapCuoi = lanDangNhapCuoi;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }

    public int getVaiTroID() {
        return vaiTroID;
    }

    public void setVaiTroID(int vaiTroID) {
        this.vaiTroID = vaiTroID;
    }
}
