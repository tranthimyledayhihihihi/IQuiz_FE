package com.example.iq5.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Model cho Quiz Submission Request
 */
public class QuizSubmissionModel {
    
    @SerializedName("tieuDe")
    private String tieuDe;
    
    @SerializedName("moTa")
    private String moTa;
    
    @SerializedName("cauHois")
    private List<CauHoiSubmission> cauHois;

    public QuizSubmissionModel(String tieuDe, String moTa, List<CauHoiSubmission> cauHois) {
        this.tieuDe = tieuDe;
        this.moTa = moTa;
        this.cauHois = cauHois;
    }

    public String getTieuDe() {
        return tieuDe;
    }

    public void setTieuDe(String tieuDe) {
        this.tieuDe = tieuDe;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public List<CauHoiSubmission> getCauHois() {
        return cauHois;
    }

    public void setCauHois(List<CauHoiSubmission> cauHois) {
        this.cauHois = cauHois;
    }

    /**
     * Câu hỏi trong submission
     */
    public static class CauHoiSubmission {
        @SerializedName("noiDung")
        private String noiDung;
        
        @SerializedName("dapAn1")
        private String dapAn1;
        
        @SerializedName("dapAn2")
        private String dapAn2;
        
        @SerializedName("dapAn3")
        private String dapAn3;
        
        @SerializedName("dapAn4")
        private String dapAn4;
        
        @SerializedName("dapAnDung")
        private int dapAnDung;

        public CauHoiSubmission(String noiDung, String dapAn1, String dapAn2, 
                               String dapAn3, String dapAn4, int dapAnDung) {
            this.noiDung = noiDung;
            this.dapAn1 = dapAn1;
            this.dapAn2 = dapAn2;
            this.dapAn3 = dapAn3;
            this.dapAn4 = dapAn4;
            this.dapAnDung = dapAnDung;
        }

        // Getters and Setters
        public String getNoiDung() { return noiDung; }
        public void setNoiDung(String noiDung) { this.noiDung = noiDung; }
        
        public String getDapAn1() { return dapAn1; }
        public void setDapAn1(String dapAn1) { this.dapAn1 = dapAn1; }
        
        public String getDapAn2() { return dapAn2; }
        public void setDapAn2(String dapAn2) { this.dapAn2 = dapAn2; }
        
        public String getDapAn3() { return dapAn3; }
        public void setDapAn3(String dapAn3) { this.dapAn3 = dapAn3; }
        
        public String getDapAn4() { return dapAn4; }
        public void setDapAn4(String dapAn4) { this.dapAn4 = dapAn4; }
        
        public int getDapAnDung() { return dapAnDung; }
        public void setDapAnDung(int dapAnDung) { this.dapAnDung = dapAnDung; }
    }
}
