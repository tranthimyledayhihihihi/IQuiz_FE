package com.example.iq5.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Model cho KetQua (Result)
 * Tương ứng với KetQuaDto.cs
 */
public class KetQuaModel {
    
    @SerializedName("quizAttemptID")
    private int quizAttemptID;
    
    @SerializedName("diem")
    private int diem;
    
    @SerializedName("soCauDung")
    private int soCauDung;
    
    @SerializedName("tongCauHoi")
    private int tongCauHoi;
    
    @SerializedName("trangThaiKetQua")
    private String trangThaiKetQua;

    // Constructors
    public KetQuaModel() {}

    // Getters & Setters
    public int getQuizAttemptID() {
        return quizAttemptID;
    }

    public void setQuizAttemptID(int quizAttemptID) {
        this.quizAttemptID = quizAttemptID;
    }

    public int getDiem() {
        return diem;
    }

    public void setDiem(int diem) {
        this.diem = diem;
    }

    public int getSoCauDung() {
        return soCauDung;
    }

    public void setSoCauDung(int soCauDung) {
        this.soCauDung = soCauDung;
    }

    public int getTongCauHoi() {
        return tongCauHoi;
    }

    public void setTongCauHoi(int tongCauHoi) {
        this.tongCauHoi = tongCauHoi;
    }

    public String getTrangThaiKetQua() {
        return trangThaiKetQua;
    }

    public void setTrangThaiKetQua(String trangThaiKetQua) {
        this.trangThaiKetQua = trangThaiKetQua;
    }

    /**
     * Tính phần trăm đúng
     */
    public float getPhanTramDung() {
        if (tongCauHoi == 0) return 0;
        return (float) soCauDung / tongCauHoi * 100;
    }
}
