package com.example.iq5.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Request model cho Login
 * Tương ứng với DangNhapModel.cs
 */
public class LoginRequestModel {
    
    @SerializedName("tenDangNhap")
    private String tenDangNhap;
    
    @SerializedName("matKhau")
    private String matKhau;

    // Constructors
    public LoginRequestModel() {}

    public LoginRequestModel(String tenDangNhap, String matKhau) {
        this.tenDangNhap = tenDangNhap;
        this.matKhau = matKhau;
    }

    // Getters & Setters
    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }
}
