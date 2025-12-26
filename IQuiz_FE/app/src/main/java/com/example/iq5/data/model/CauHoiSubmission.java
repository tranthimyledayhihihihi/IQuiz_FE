package com.example.iq5.data.model;

import com.google.gson.annotations.SerializedName;

public class CauHoiSubmission {

    @SerializedName("ChuDeID")
    public int chuDeID;

    @SerializedName("DoKhoID")
    public int doKhoID;

    @SerializedName("NoiDung")
    public String noiDung;

    @SerializedName("DapAnA")
    public String dapAnA;

    @SerializedName("DapAnB")
    public String dapAnB;

    @SerializedName("DapAnC")
    public String dapAnC;

    @SerializedName("DapAnD")
    public String dapAnD;

    @SerializedName("DapAnDung")
    public String dapAnDung;

    @SerializedName("HinhAnh")
    public String hinhAnh;

    public CauHoiSubmission(
            int chuDeID,
            int doKhoID,
            String noiDung,
            String dapAnA,
            String dapAnB,
            String dapAnC,
            String dapAnD,
            String dapAnDung
    ) {
        this.chuDeID = chuDeID;
        this.doKhoID = doKhoID;
        this.noiDung = noiDung;
        this.dapAnA = dapAnA;
        this.dapAnB = dapAnB;
        this.dapAnC = dapAnC;
        this.dapAnD = dapAnD;
        this.dapAnDung = dapAnDung;
        this.hinhAnh = null;
    }
}
