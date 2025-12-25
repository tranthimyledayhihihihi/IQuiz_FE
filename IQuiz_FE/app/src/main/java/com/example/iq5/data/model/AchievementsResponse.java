package com.example.iq5.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Model thành tựu KHỚP BE
 * BE trả về: List<Achievement>
 */
public class AchievementsResponse {

    public static class Achievement {

        @SerializedName("thanhTuuID")
        public int thanhTuuID;

        @SerializedName("tenThanhTuu")
        public String tenThanhTuu;

        @SerializedName("moTa")
        public String moTa;

        @SerializedName("icon")
        public String icon;

        @SerializedName("ngayDatDuoc")
        public String ngayDatDuoc;

        @SerializedName("diemThuong")
        public int diemThuong;
    }
}
