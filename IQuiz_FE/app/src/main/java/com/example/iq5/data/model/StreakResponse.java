package com.example.iq5.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class StreakResponse {

    @SerializedName("soNgayLienTiep")
    private int soNgayLienTiep;

    @SerializedName("ngayCapNhatCuoi")
    private String ngayCapNhatCuoi;

    @SerializedName("message")
    private String message;

    public int getSoNgayLienTiep() {
        return soNgayLienTiep;
    }

    public String getNgayCapNhatCuoi() {
        return ngayCapNhatCuoi;
    }

    public String getMessage() {
        return message;
    }
}
