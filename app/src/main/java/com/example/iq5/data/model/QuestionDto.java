package com.example.iq5.data.model;

public class QuestionDto {
    private int cauHoiId;
    private int chuDeId;
    private String noiDung;
    private String luaChonA;
    private String luaChonB; // Thiếu trong file gốc nhưng cần thiết
    private String luaChonC; // Thiếu trong file gốc nhưng cần thiết
    private String luaChonD; // Thiếu trong file gốc nhưng cần thiết
    private String dapAn;
    private String doKho; // Thiếu trong file gốc nhưng cần thiết

    // 1. CONSTRUCTOR ĐẦY ĐỦ
    public QuestionDto(int cauHoiId, int chuDeId, String noiDung, String luaChonA, String luaChonB, String luaChonC, String luaChonD, String dapAn, String doKho) {
        this.cauHoiId = cauHoiId;
        this.chuDeId = chuDeId;
        this.noiDung = noiDung;
        this.luaChonA = luaChonA;
        this.luaChonB = luaChonB;
        this.luaChonC = luaChonC;
        this.luaChonD = luaChonD;
        this.dapAn = dapAn;
        this.doKho = doKho;
    }

    // 2. GETTERS (ĐÃ BỔ SUNG RETURN)

    public int getCauHoiId() {
        return cauHoiId;
    }

    public int getChuDeId() {
        return chuDeId;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public String getLuaChonA() {
        return luaChonA;
    }

    public String getLuaChonB() {
        return luaChonB;
    }

    public String getLuaChonC() {
        return luaChonC;
    }

    public String getLuaChonD() {
        return luaChonD;
    }

    public String getDapAn() {
        return dapAn;
    }

    public String getDoKho() {
        return doKho;
    }

    // 3. SETTERS (Bổ sung để hoàn thiện DTO)

    public void setCauHoiId(int cauHoiId) {
        this.cauHoiId = cauHoiId;
    }

    public void setChuDeId(int chuDeId) {
        this.chuDeId = chuDeId;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public void setLuaChonA(String luaChonA) {
        this.luaChonA = luaChonA;
    }

    public void setLuaChonB(String luaChonB) {
        this.luaChonB = luaChonB;
    }

    public void setLuaChonC(String luaChonC) {
        this.luaChonC = luaChonC;
    }

    public void setLuaChonD(String luaChonD) {
        this.luaChonD = luaChonD;
    }

    public void setDapAn(String dapAn) {
        this.dapAn = dapAn;
    }

    public void setDoKho(String doKho) {
        this.doKho = doKho;
    }
}