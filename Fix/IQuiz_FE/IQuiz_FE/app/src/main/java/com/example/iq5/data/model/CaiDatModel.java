package com.example.iq5.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Model cài đặt người dùng
 */
public class CaiDatModel {
    
    @SerializedName("amThanh")
    public boolean amThanh = true;
    
    @SerializedName("nhacNen")
    public boolean nhacNen = true;
    
    @SerializedName("thongBao")
    public boolean thongBao = true;
    
    @SerializedName("ngonNgu")
    public String ngonNgu = "vi";
    
    public CaiDatModel() {}
    
    public CaiDatModel(boolean amThanh, boolean nhacNen, boolean thongBao, String ngonNgu) {
        this.amThanh = amThanh;
        this.nhacNen = nhacNen;
        this.thongBao = thongBao;
        this.ngonNgu = ngonNgu;
    }
}
