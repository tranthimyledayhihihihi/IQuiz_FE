package com.example.iq5.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.Date;

/**
 * Model cho User Profile
 * Tương ứng với UserProfileDto.cs
 */
public class UserProfileModel {
    
    @SerializedName("userID")
    private int userID;
    
    @SerializedName("tenDangNhap")
    private String tenDangNhap;
    
    @SerializedName("hoTen")
    private String hoTen;
    
    @SerializedName("anhDaiDien")
    private String anhDaiDien;
    
    @SerializedName(value = "ngayDangKy", alternate = {"ngayDangKyfix"})
    private Date ngayDangKy;
    
    @SerializedName("tongSoQuizDaLam")
    private int tongSoQuizDaLam;
    
    @SerializedName("tongSoCauHoiDung")
    private int tongSoCauHoiDung;
    
    @SerializedName("tongSoDiem")
    private int tongSoDiem;
    
    @SerializedName("soNguoiTheoDoi")
    private int soNguoiTheoDoi;
    
    @SerializedName("dangTheoDoi")
    private int dangTheoDoi;
    
    @SerializedName("isFollowing")
    private boolean isFollowing;

    // Constructors
    public UserProfileModel() {}

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

    public int getTongSoQuizDaLam() {
        return tongSoQuizDaLam;
    }

    public void setTongSoQuizDaLam(int tongSoQuizDaLam) {
        this.tongSoQuizDaLam = tongSoQuizDaLam;
    }

    public int getTongSoCauHoiDung() {
        return tongSoCauHoiDung;
    }

    public void setTongSoCauHoiDung(int tongSoCauHoiDung) {
        this.tongSoCauHoiDung = tongSoCauHoiDung;
    }

    public int getTongSoDiem() {
        return tongSoDiem;
    }

    public void setTongSoDiem(int tongSoDiem) {
        this.tongSoDiem = tongSoDiem;
    }

    public int getSoNguoiTheoDoi() {
        return soNguoiTheoDoi;
    }

    public void setSoNguoiTheoDoi(int soNguoiTheoDoi) {
        this.soNguoiTheoDoi = soNguoiTheoDoi;
    }

    public int getDangTheoDoi() {
        return dangTheoDoi;
    }

    public void setDangTheoDoi(int dangTheoDoi) {
        this.dangTheoDoi = dangTheoDoi;
    }

    public boolean isFollowing() {
        return isFollowing;
    }

    public void setFollowing(boolean following) {
        isFollowing = following;
    }
}
