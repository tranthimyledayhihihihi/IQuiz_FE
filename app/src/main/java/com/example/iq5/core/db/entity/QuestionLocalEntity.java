package com.example.iq5.core.db.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "questions")
public class QuestionLocalEntity {
    @PrimaryKey
    private int cauHoiId;
    private int chuDeId;
    private String noiDung;
    private String luaChonA;
    private String luaChonB;
    private String luaChonC;
    private String luaChonD;
    private String dapAn;
    private String doKho;

    // KHẮC PHỤC LỖI: Thêm Constructor rỗng (No-arg constructor)
    public QuestionLocalEntity() {
    }

    // Constructor đầy đủ tham số (Giữ nguyên)
    public QuestionLocalEntity(int cauHoiId, int chuDeId, String noiDung, String luaChonA, String luaChonB, String luaChonC, String luaChonD, String dapAn, String doKho) {
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

    // Getters and Setters (Giữ nguyên)
    public int getCauHoiId() { return cauHoiId; }
    public int getChuDeId() { return chuDeId; }
    public String getNoiDung() { return noiDung; }
    public String getLuaChonA() { return luaChonA; }
    public String getLuaChonB() { return luaChonB; }
    public String getLuaChonC() { return luaChonC; }
    public String getLuaChonD() { return luaChonD; }
    public String getDapAn() { return dapAn; }
    public String getDoKho() { return doKho; }
    public void setCauHoiId(int cauHoiId) { this.cauHoiId = cauHoiId; }
    public void setChuDeId(int chuDeId) { this.chuDeId = chuDeId; }
    public void setNoiDung(String noiDung) { this.noiDung = noiDung; }
    public void setLuaChonA(String luaChonA) { this.luaChonA = luaChonA; }
    public void setLuaChonB(String luaChonB) { this.luaChonB = luaChonB; }
    public void setLuaChonC(String luaChonC) { this.luaChonC = luaChonC; }
    public void setLuaChonD(String luaChonD) { this.luaChonD = luaChonD; }
    public void setDapAn(String dapAn) { this.dapAn = dapAn; }
    public void setDoKho(String doKho) { this.doKho = doKho; }

}