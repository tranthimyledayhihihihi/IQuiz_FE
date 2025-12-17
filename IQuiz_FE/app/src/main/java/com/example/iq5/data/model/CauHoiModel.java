package com.example.iq5.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.Date;

/**
 * Model cho CauHoi (Question) từ backend
 * Tương ứng với CauHoi.cs và CauHoiPlayDto.cs
 */
public class CauHoiModel {
    
    @SerializedName("cauHoiID")
    private int cauHoiID;
    
    @SerializedName("chuDeID")
    private int chuDeID;
    
    @SerializedName("doKhoID")
    private int doKhoID;
    
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
    
    @SerializedName("dapAnDung")
    private String dapAnDung;
    
    @SerializedName("hinhAnh")
    private String hinhAnh;
    
    @SerializedName("ngayTao")
    private Date ngayTao;
    
    @SerializedName("quizTuyChinhID")
    private Integer quizTuyChinhID;

    // Constructors
    public CauHoiModel() {}

    // Getters & Setters
    public int getCauHoiID() {
        return cauHoiID;
    }

    public void setCauHoiID(int cauHoiID) {
        this.cauHoiID = cauHoiID;
    }

    public int getChuDeID() {
        return chuDeID;
    }

    public void setChuDeID(int chuDeID) {
        this.chuDeID = chuDeID;
    }

    public int getDoKhoID() {
        return doKhoID;
    }

    public void setDoKhoID(int doKhoID) {
        this.doKhoID = doKhoID;
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

    public String getDapAnDung() {
        return dapAnDung;
    }

    public void setDapAnDung(String dapAnDung) {
        this.dapAnDung = dapAnDung;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public Date getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(Date ngayTao) {
        this.ngayTao = ngayTao;
    }

    public Integer getQuizTuyChinhID() {
        return quizTuyChinhID;
    }

    public void setQuizTuyChinhID(Integer quizTuyChinhID) {
        this.quizTuyChinhID = quizTuyChinhID;
    }
}
