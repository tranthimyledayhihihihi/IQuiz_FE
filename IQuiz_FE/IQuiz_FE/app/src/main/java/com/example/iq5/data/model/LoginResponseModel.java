package com.example.iq5.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Response model cho Login
 * Tương ứng với LoginResponseModel.cs
 */
public class LoginResponseModel {
    
    @SerializedName("token")
    private String token;
    
    @SerializedName("hoTen")
    private String hoTen;
    
    @SerializedName("vaiTro")
    private String vaiTro;

    // Constructors
    public LoginResponseModel() {}

    public LoginResponseModel(String token, String hoTen, String vaiTro) {
        this.token = token;
        this.hoTen = hoTen;
        this.vaiTro = vaiTro;
    }

    // Getters & Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getVaiTro() {
        return vaiTro;
    }

    public void setVaiTro(String vaiTro) {
        this.vaiTro = vaiTro;
    }
}
