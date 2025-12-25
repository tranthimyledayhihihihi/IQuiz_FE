package com.example.iq5.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class DailyRewardResponse {
    @SerializedName("success")
    public boolean success;

    @SerializedName("data")
    public List<DailyRewardData> data;

    @SerializedName("message")
    public String message;

    public static class DailyRewardData {
        @SerializedName("thuongID")
        public int thuongID;

        @SerializedName("userID")
        public int userID;

        @SerializedName("ngayNhan")
        public String ngayNhan;

        @SerializedName("loaiThuong")
        public String loaiThuong;

        @SerializedName("giaTri")
        public int giaTri;

        @SerializedName("moTa")
        public String moTa;
    }
}
