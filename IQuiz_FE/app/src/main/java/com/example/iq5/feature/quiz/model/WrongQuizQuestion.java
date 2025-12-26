package com.example.iq5.feature.quiz.model;

import com.google.gson.annotations.SerializedName;

public class WrongQuizQuestion {

    @SerializedName("cauHoiID")
    public int cauHoiID;

    @SerializedName("noiDung")
    public String noiDung;

    @SerializedName("dapAnA")
    public String dapAnA;

    @SerializedName("dapAnB")
    public String dapAnB;

    @SerializedName("dapAnC")
    public String dapAnC;

    @SerializedName("dapAnD")
    public String dapAnD;

    // BE trả "B" → FE phải map đúng
    @SerializedName("dapAnDung")
    public String dapAnDung;

    @SerializedName("chuDeID")
    public int chuDeID;
}
