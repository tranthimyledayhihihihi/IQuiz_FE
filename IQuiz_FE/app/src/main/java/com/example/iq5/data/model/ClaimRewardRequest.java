package com.example.iq5.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Model cho request claim daily reward
 * Khớp với bảng ThuongNgay trong SQL: UserID, PhanThuong, DiemThuong
 */
public class ClaimRewardRequest {
    @SerializedName("userID")
    public int UserID;

    // Hỗ trợ cả PhanThuong (SQL) và LoaiThuong (API cũ)
    @SerializedName(value = "phanThuong", alternate = {"loaiThuong", "LoaiThuong"})
    public String PhanThuong;

    // Hỗ trợ cả DiemThuong (SQL) và GiaTri (API cũ)
    @SerializedName(value = "diemThuong", alternate = {"giaTri", "GiaTri"})
    public int DiemThuong;

    @SerializedName(value = "moTa", alternate = {"MoTa"})
    public String MoTa;

    public ClaimRewardRequest() {}

    public ClaimRewardRequest(int userId, String phanThuong, int diemThuong, String moTa) {
        this.UserID = userId;
        this.PhanThuong = phanThuong;
        this.DiemThuong = diemThuong;
        this.MoTa = moTa;
    }

    // Constructor tương thích với code cũ
    public ClaimRewardRequest(int userId, String loaiThuong, int giaTri, String moTa, boolean useOldNames) {
        this.UserID = userId;
        this.PhanThuong = loaiThuong;
        this.DiemThuong = giaTri;
        this.MoTa = moTa;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        this.UserID = userID;
    }

    // Getter/Setter cho PhanThuong
    public String getPhanThuong() {
        return PhanThuong;
    }

    public void setPhanThuong(String phanThuong) {
        this.PhanThuong = phanThuong;
    }

    // Getter/Setter tương thích với code cũ
    public String getLoaiThuong() {
        return PhanThuong;
    }

    public void setLoaiThuong(String loaiThuong) {
        this.PhanThuong = loaiThuong;
    }

    // Getter/Setter cho DiemThuong
    public int getDiemThuong() {
        return DiemThuong;
    }

    public void setDiemThuong(int diemThuong) {
        this.DiemThuong = diemThuong;
    }

    // Getter/Setter tương thích với code cũ
    public int getGiaTri() {
        return DiemThuong;
    }

    public void setGiaTri(int giaTri) {
        this.DiemThuong = giaTri;
    }

    public String getMoTa() {
        return MoTa;
    }

    public void setMoTa(String moTa) {
        this.MoTa = moTa;
    }
}

