package com.example.iq5.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Response cho chi tiết quiz submission
 */
public class QuizDetailResponse {
    
    @SerializedName("quizTuyChinhID")
    private int quizTuyChinhID;
    
    @SerializedName("tenQuiz")
    private String tenQuiz;
    
    @SerializedName("moTa")
    private String moTa;
    
    @SerializedName("trangThai")
    private String trangThai;
    
    @SerializedName("ngayTao")
    private String ngayTao;
    
    @SerializedName("cauHois")
    private List<CauHoiModel> cauHois;

    public QuizDetailResponse() {}

    public int getQuizTuyChinhID() {
        return quizTuyChinhID;
    }

    public void setQuizTuyChinhID(int quizTuyChinhID) {
        this.quizTuyChinhID = quizTuyChinhID;
    }

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

    public List<CauHoiModel> getCauHois() {
        return cauHois;
    }

    public void setCauHois(List<CauHoiModel> cauHois) {
        this.cauHois = cauHois;
    }
}
