package com.example.iq5.data.model;

import com.google.gson.annotations.SerializedName;

public class ClaimRewardRequest {
    @SerializedName("userID")
    public int userID;

    @SerializedName("loaiThuong")
    public String loaiThuong = "Coins";

    @SerializedName("giaTri")
    public int giaTri = 100;

    @SerializedName("moTa")
    public String moTa = "Daily login reward";
}
