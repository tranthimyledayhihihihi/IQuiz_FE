package com.example.iq5.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Response model cho API streak từ backend
 * Mapping với response từ /api/user/achievement/streak
 */
public class UserStreakResponse {
    
    @SerializedName("soNgayLienTiep")
    private int soNgayLienTiep;
    
    @SerializedName("ngayCapNhatCuoi")
    private String ngayCapNhatCuoi;
    
    // Constructors
    public UserStreakResponse() {}
    
    public UserStreakResponse(int soNgayLienTiep, String ngayCapNhatCuoi) {
        this.soNgayLienTiep = soNgayLienTiep;
        this.ngayCapNhatCuoi = ngayCapNhatCuoi;
    }
    
    // Getters and Setters
    public int getSoNgayLienTiep() {
        return soNgayLienTiep;
    }
    
    public void setSoNgayLienTiep(int soNgayLienTiep) {
        this.soNgayLienTiep = soNgayLienTiep;
    }
    
    public String getNgayCapNhatCuoi() {
        return ngayCapNhatCuoi;
    }
    
    public void setNgayCapNhatCuoi(String ngayCapNhatCuoi) {
        this.ngayCapNhatCuoi = ngayCapNhatCuoi;
    }
    
    @Override
    public String toString() {
        return "UserStreakResponse{" +
                "soNgayLienTiep=" + soNgayLienTiep +
                ", ngayCapNhatCuoi='" + ngayCapNhatCuoi + '\'' +
                '}';
    }
}