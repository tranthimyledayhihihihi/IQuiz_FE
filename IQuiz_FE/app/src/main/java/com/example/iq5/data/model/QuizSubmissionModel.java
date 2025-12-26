package com.example.iq5.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Model gửi lên API: POST /api/QuizTuyChinh/submit
 * KHỚP DTO BACKEND
 */
public class QuizSubmissionModel {

    @SerializedName("TenQuiz")
    private String tenQuiz;

    @SerializedName("MoTa")
    private String moTa;

    @SerializedName("Questions")
    private List<CauHoiSubmission> questions;

    public QuizSubmissionModel(String tenQuiz,
                               String moTa,
                               List<CauHoiSubmission> questions) {
        this.tenQuiz = tenQuiz;
        this.moTa = moTa;
        this.questions = questions;
    }

    // ================== INNER CLASS ==================
    public static class CauHoiSubmission {

        // ===== BẮT BUỘC THEO DB / BE =====
        @SerializedName("ChuDeID")
        private int chuDeID;

        @SerializedName("DoKhoID")
        private int doKhoID;

        // ===== NỘI DUNG =====
        @SerializedName("NoiDung")
        private String noiDung;

        @SerializedName("DapAnA")
        private String dapAnA;

        @SerializedName("DapAnB")
        private String dapAnB;

        @SerializedName("DapAnC")
        private String dapAnC;

        @SerializedName("DapAnD")
        private String dapAnD;

        // "DapAnA" | "DapAnB" | "DapAnC" | "DapAnD"
        @SerializedName("DapAnDung")
        private String dapAnDung;

        @SerializedName("HinhAnh")
        private String hinhAnh;

        // =================================================
        // CONSTRUCTOR CHUẨN – DÙNG CHO CUSTOM QUIZ
        // =================================================
        public CauHoiSubmission(int chuDeID,
                                int doKhoID,
                                String noiDung,
                                String dapAnA,
                                String dapAnB,
                                String dapAnC,
                                String dapAnD,
                                String dapAnDung) {

            this.chuDeID = chuDeID;
            this.doKhoID = doKhoID;
            this.noiDung = noiDung;
            this.dapAnA = dapAnA;
            this.dapAnB = dapAnB;
            this.dapAnC = dapAnC;
            this.dapAnD = dapAnD;
            this.dapAnDung = dapAnDung;
            this.hinhAnh = null; // chưa dùng ảnh
        }

        // =================================================
        // GETTER (nếu Adapter cần)
        // =================================================
        public int getChuDeID() { return chuDeID; }
        public int getDoKhoID() { return doKhoID; }
        public String getNoiDung() { return noiDung; }
        public String getDapAnA() { return dapAnA; }
        public String getDapAnB() { return dapAnB; }
        public String getDapAnC() { return dapAnC; }
        public String getDapAnD() { return dapAnD; }
        public String getDapAnDung() { return dapAnDung; }
    }
}
