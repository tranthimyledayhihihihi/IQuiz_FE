package com.example.iq5.data.model;

import com.google.gson.annotations.SerializedName;

public class StreakDayDto {

    @SerializedName("ngay")
    private String ngay;

    @SerializedName("hoanThanh")
    private boolean hoanThanh;

    public String getNgay() {
        return ngay;
    }

    public boolean isHoanThanh() {
        return hoanThanh;
    }
}
