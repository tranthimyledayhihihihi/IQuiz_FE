package com.example.iq5.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Response model cho Quiz Ngày (Daily Quiz)
 * Tương ứng với QuizNgayDetailsDto từ backend
 */
public class QuizNgayResponse {
    
    @SerializedName("quizNgayID")
    private int quizNgayID;
    
    @SerializedName("cauHoiID")
    private int cauHoiID;
    
    @SerializedName("ngayHienThi")
    private String ngayHienThi;
    
    @SerializedName("noiDung")
    private String noiDung;
    
    @SerializedName("dapAnA")
    private String dapAnA;
    
    @SerializedName("dapAnB")
    private String dapAnB;
    
    @SerializedName("dapAnC")
    private String dapAnC;
    
    @SerializedName("dapAnD")
    private String dapAnD;

    // Constructors
    public QuizNgayResponse() {}

    // Getters & Setters
    public int getQuizNgayID() {
        return quizNgayID;
    }

    public void setQuizNgayID(int quizNgayID) {
        this.quizNgayID = quizNgayID;
    }

    public int getCauHoiID() {
        return cauHoiID;
    }

    public void setCauHoiID(int cauHoiID) {
        this.cauHoiID = cauHoiID;
    }

    public String getNgayHienThi() {
        return ngayHienThi;
    }

    public void setNgayHienThi(String ngayHienThi) {
        this.ngayHienThi = ngayHienThi;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public String getDapAnA() {
        return dapAnA;
    }

    public void setDapAnA(String dapAnA) {
        this.dapAnA = dapAnA;
    }

    public String getDapAnB() {
        return dapAnB;
    }

    public void setDapAnB(String dapAnB) {
        this.dapAnB = dapAnB;
    }

    public String getDapAnC() {
        return dapAnC;
    }

    public void setDapAnC(String dapAnC) {
        this.dapAnC = dapAnC;
    }

    public String getDapAnD() {
        return dapAnD;
    }

    public void setDapAnD(String dapAnD) {
        this.dapAnD = dapAnD;
    }
}
