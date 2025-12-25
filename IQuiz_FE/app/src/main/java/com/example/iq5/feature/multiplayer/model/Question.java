package com.example.iq5.feature.multiplayer.model;

public class Question {
    private int cauHoiID;
    private int chuDeID;
    private int doKhoID;
    private String noiDung;
    private String dapAnA;
    private String dapAnB;
    private String dapAnC;
    private String dapAnD;
    private String dapAnDung;
    private String hinhAnh;
    private String trangThaiDuyet;

    // ✅ THÊM FIELD NÀY
    private String cacLuaChon; // JSON string: {"A": "...", "B": "...", "C": "...", "D": "..."}

    // Getters and Setters
    public int getCauHoiID() { return cauHoiID; }
    public void setCauHoiID(int cauHoiID) { this.cauHoiID = cauHoiID; }

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

    public String getCacLuaChon() { return cacLuaChon; }
    public void setCacLuaChon(String cacLuaChon) { this.cacLuaChon = cacLuaChon; }
}