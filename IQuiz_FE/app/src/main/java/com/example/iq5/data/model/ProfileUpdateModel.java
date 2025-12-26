package com.example.iq5.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Model để cập nhật profile
 */
public class ProfileUpdateModel {
    
    @SerializedName("HoTen")
    public String hoTen;
    
    @SerializedName("Email")
    public String email;
    
    @SerializedName("AnhDaiDien")
    public String anhDaiDien;
    
    public ProfileUpdateModel() {}
    
    public ProfileUpdateModel(String hoTen, String email, String anhDaiDien) {
        this.hoTen = hoTen;
        this.email = email;
        this.anhDaiDien = anhDaiDien;
    }
}
