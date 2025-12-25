package com.example.iq5.feature.multiplayer.data.models;

import com.google.gson.annotations.SerializedName;

/**
 * DTO để hiển thị câu hỏi cho người chơi (KHÔNG gồm đáp án đúng),
 * map 1:1 với QUIZ_GAME_WEB.Models.ViewModels.CauHoiDisplayModel (C#).
 */
public class CauHoiDisplayModel {

    @SerializedName(value = "CauHoiID", alternate = {"cauHoiID", "cauHoiId", "CauHoiId"})
    private int cauHoiID;

    @SerializedName(value = "NoiDung", alternate = {"noiDung"})
    private String noiDung;

    /**
     * Chuỗi JSON chứa A/B/C/D giống backend trả về:
     * {"A":"...","B":"...","C":"...","D":"..."}
     */
    @SerializedName(value = "CacLuaChon", alternate = {"cacLuaChon"})
    private String cacLuaChon;

    @SerializedName(value = "ThuTuTrongTranDau", alternate = {"thuTuTrongTranDau"})
    private int thuTuTrongTranDau;

    @SerializedName(value = "ThoiGianToiDa", alternate = {"thoiGianToiDa"})
    private double thoiGianToiDa = 15.0;

    @SerializedName(value = "ChuDeID", alternate = {"chuDeID", "chuDeId", "ChuDeId"})
    private Integer chuDeID; // nullable

    // ===== Getters/Setters =====
    public int getCauHoiID() {
        return cauHoiID;
    }

    public void setCauHoiID(int cauHoiID) {
        this.cauHoiID = cauHoiID;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public String getCacLuaChon() {
        return cacLuaChon;
    }

    public void setCacLuaChon(String cacLuaChon) {
        this.cacLuaChon = cacLuaChon;
    }

    public int getThuTuTrongTranDau() {
        return thuTuTrongTranDau;
    }

    public void setThuTuTrongTranDau(int thuTuTrongTranDau) {
        this.thuTuTrongTranDau = thuTuTrongTranDau;
    }

    public double getThoiGianToiDa() {
        return thoiGianToiDa;
    }

    public void setThoiGianToiDa(double thoiGianToiDa) {
        this.thoiGianToiDa = thoiGianToiDa;
    }

    public Integer getChuDeID() {
        return chuDeID;
    }

    public void setChuDeID(Integer chuDeID) {
        this.chuDeID = chuDeID;
    }
}
