package com.example.iq5.feature.multiplayer.data.models;


import com.google.gson.annotations.SerializedName;

public class Question {

    @SerializedName(value = "CauHoiID", alternate = {"cauHoiID", "cauHoiId", "CauHoiId"})
    private int cauHoiID;

    @SerializedName(value = "ChuDeID", alternate = {"chuDeID", "chuDeId", "ChuDeId"})
    private int chuDeID;

    @SerializedName(value = "DoKhoID", alternate = {"doKhoID", "doKhoId", "DoKhoId"})
    private int doKhoID;

    @SerializedName(value = "NoiDung", alternate = {"noiDung"})
    private String noiDung;

    @SerializedName(value = "DapAnA", alternate = {"dapAnA"})
    private String dapAnA;

    @SerializedName(value = "DapAnB", alternate = {"dapAnB"})
    private String dapAnB;

    @SerializedName(value = "DapAnC", alternate = {"dapAnC"})
    private String dapAnC;

    @SerializedName(value = "DapAnD", alternate = {"dapAnD"})
    private String dapAnD;

    @SerializedName(value = "DapAnDung", alternate = {"dapAnDung"})
    private String dapAnDung;

    @SerializedName(value = "HinhAnh", alternate = {"hinhAnh"})
    private String hinhAnh;

    @SerializedName(value = "TrangThaiDuyet", alternate = {"trangThaiDuyet"})
    private String trangThaiDuyet;

    // getters/setters giữ nguyên...

    // Getters and Setters
    public int getCauHoiID() { return cauHoiID; }
    public void setCauHoiID(int cauHoiID) { this.cauHoiID = cauHoiID; }

    public int getChuDeID() { return chuDeID; }
    public void setChuDeID(int chuDeID) { this.chuDeID = chuDeID; }

    public int getDoKhoID() { return doKhoID; }
    public void setDoKhoID(int doKhoID) { this.doKhoID = doKhoID; }

    public String getNoiDung() { return noiDung; }
    public void setNoiDung(String noiDung) { this.noiDung = noiDung; }

    public String getDapAnA() { return dapAnA; }
    public void setDapAnA(String dapAnA) { this.dapAnA = dapAnA; }

    public String getDapAnB() { return dapAnB; }
    public void setDapAnB(String dapAnB) { this.dapAnB = dapAnB; }

    public String getDapAnC() { return dapAnC; }
    public void setDapAnC(String dapAnC) { this.dapAnC = dapAnC; }

    public String getDapAnD() { return dapAnD; }
    public void setDapAnD(String dapAnD) { this.dapAnD = dapAnD; }

    public String getDapAnDung() { return dapAnDung; }
    public void setDapAnDung(String dapAnDung) { this.dapAnDung = dapAnDung; }

    public String getHinhAnh() { return hinhAnh; }
    public void setHinhAnh(String hinhAnh) { this.hinhAnh = hinhAnh; }

    public String getTrangThaiDuyet() { return trangThaiDuyet; }
    public void setTrangThaiDuyet(String trangThaiDuyet) { this.trangThaiDuyet = trangThaiDuyet; }
}