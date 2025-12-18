package com.example.iq5.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Request để submit custom quiz
 */
public class QuizSubmissionRequest {
    
    @SerializedName("tenQuiz")
    private String tenQuiz;
    
    @SerializedName("moTa")
    private String moTa;
    
    @SerializedName("chuDeID")
    private int chuDeID;
    
    @SerializedName("doKhoID")
    private int doKhoID;
    
    @SerializedName("cauHois")
    private List<CauHoiSubmission> cauHois;

    public QuizSubmissionRequest() {}

    public String getTenQuiz() {
        return tenQuiz;
    }

    public void setTenQuiz(String tenQuiz) {
        this.tenQuiz = tenQuiz;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
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

    public List<CauHoiSubmission> getCauHois() {
        return cauHois;
    }

    public void setCauHois(List<CauHoiSubmission> cauHois) {
        this.cauHois = cauHois;
    }

    /**
     * Inner class cho câu hỏi trong submission
     */
    public static class CauHoiSubmission {
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

        public CauHoiSubmission() {}

        // Getters & Setters
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
    }
}
