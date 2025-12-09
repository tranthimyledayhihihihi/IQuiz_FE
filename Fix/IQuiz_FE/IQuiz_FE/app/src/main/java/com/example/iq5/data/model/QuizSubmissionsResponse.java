package com.example.iq5.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Response cho danh sách Quiz Submissions
 */
public class QuizSubmissionsResponse {
    
    @SerializedName("tongSoDeXuat")
    private int tongSoDeXuat;
    
    @SerializedName("trangHienTai")
    private int trangHienTai;
    
    @SerializedName("tongSoTrang")
    private int tongSoTrang;
    
    @SerializedName("danhSach")
    private List<QuizSubmissionItem> danhSach;

    public QuizSubmissionsResponse() {}

    public int getTongSoDeXuat() {
        return tongSoDeXuat;
    }

    public void setTongSoDeXuat(int tongSoDeXuat) {
        this.tongSoDeXuat = tongSoDeXuat;
    }

    public int getTrangHienTai() {
        return trangHienTai;
    }

    public void setTrangHienTai(int trangHienTai) {
        this.trangHienTai = trangHienTai;
    }

    public int getTongSoTrang() {
        return tongSoTrang;
    }

    public void setTongSoTrang(int tongSoTrang) {
        this.tongSoTrang = tongSoTrang;
    }

    public List<QuizSubmissionItem> getDanhSach() {
        return danhSach;
    }

    public void setDanhSach(List<QuizSubmissionItem> danhSach) {
        this.danhSach = danhSach;
    }

    /**
     * Item trong danh sách submissions
     */
    public static class QuizSubmissionItem {
        @SerializedName("quizTuyChinhID")
        private int quizTuyChinhID;
        
        @SerializedName("tieuDe")
        private String tieuDe;
        
        @SerializedName("moTa")
        private String moTa;
        
        @SerializedName("trangThai")
        private String trangThai;
        
        @SerializedName("ngayTao")
        private String ngayTao;

        public int getQuizTuyChinhID() {
            return quizTuyChinhID;
        }

        public void setQuizTuyChinhID(int quizTuyChinhID) {
            this.quizTuyChinhID = quizTuyChinhID;
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

        public String getTrangThai() {
            return trangThai;
        }

        public void setTrangThai(String trangThai) {
            this.trangThai = trangThai;
        }

        public String getNgayTao() {
            return ngayTao;
        }

        public void setNgayTao(String ngayTao) {
            this.ngayTao = ngayTao;
        }
    }
}
