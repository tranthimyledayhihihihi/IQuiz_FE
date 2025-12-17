package com.example.iq5.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Request để bắt đầu quiz
 */
public class StartQuizRequest {
    
    @SerializedName("chuDeID")
    private Integer chuDeID;
    
    @SerializedName("doKhoID")
    private Integer doKhoID;
    
    @SerializedName("soLuongCauHoi")
    private int soLuongCauHoi;

    public StartQuizRequest() {}

    public StartQuizRequest(Integer chuDeID, Integer doKhoID, int soLuongCauHoi) {
        this.chuDeID = chuDeID;
        this.doKhoID = doKhoID;
        this.soLuongCauHoi = soLuongCauHoi;
    }

    public Integer getChuDeID() {
        return chuDeID;
    }

    public void setChuDeID(Integer chuDeID) {
        this.chuDeID = chuDeID;
    }

    public Integer getDoKhoID() {
        return doKhoID;
    }

    public void setDoKhoID(Integer doKhoID) {
        this.doKhoID = doKhoID;
    }

    public int getSoLuongCauHoi() {
        return soLuongCauHoi;
    }

    public void setSoLuongCauHoi(int soLuongCauHoi) {
        this.soLuongCauHoi = soLuongCauHoi;
    }
}
