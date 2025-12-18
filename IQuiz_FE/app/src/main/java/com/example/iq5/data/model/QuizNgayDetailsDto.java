package com.example.iq5.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Model cho Quiz Ngày Details
 */
public class QuizNgayDetailsDto {
    
    @SerializedName("quizNgayID")
    private int quizNgayID;
    
    @SerializedName("cauHoiID")
    private int cauHoiID;
    
    @SerializedName("ngay")
    private String ngay;
    
    @SerializedName("cauHoi")
    private CauHoiModel cauHoi;

    public QuizNgayDetailsDto() {}

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

    public String getNgay() {
        return ngay;
    }

    public void setNgay(String ngay) {
        this.ngay = ngay;
    }

    public CauHoiModel getCauHoi() {
        return cauHoi;
    }

    public void setCauHoi(CauHoiModel cauHoi) {
        this.cauHoi = cauHoi;
    }
}
