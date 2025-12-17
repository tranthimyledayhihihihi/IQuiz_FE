package com.example.iq5.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Request model cho Register
 * Tương ứng với DangKyModel.cs
 */
public class RegisterRequestModel {
    
    @SerializedName("tenDangNhap")
    private String tenDangNhap;
    
    @SerializedName("email")
    private String email;
    
    @SerializedName("matKhau")
    private String matKhau;
    
    @SerializedName("xacNhanMatKhau")
    private String xacNhanMatKhau;
    
    @SerializedName("hoTen")
    private String hoTen;

    // Constructors
    public RegisterRequestModel() {}

    public RegisterRequestModel(String tenDangNhap, String email, String matKhau, 
                               String xacNhanMatKhau, String hoTen) {
        this.tenDangNhap = tenDangNhap;
        this.email = email;
        this.matKhau = matKhau;
        this.xacNhanMatKhau = xacNhanMatKhau;
        this.hoTen = hoTen;
    }

    // Getters & Setters
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

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    public String getXacNhanMatKhau() {
        return xacNhanMatKhau;
    }

    public void setXacNhanMatKhau(String xacNhanMatKhau) {
        this.xacNhanMatKhau = xacNhanMatKhau;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }
}
