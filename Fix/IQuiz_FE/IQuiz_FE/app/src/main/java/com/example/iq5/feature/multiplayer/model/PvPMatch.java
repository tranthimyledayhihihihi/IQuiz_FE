package com.example.iq5.feature.multiplayer.model;

import java.util.Date;

/**
 * Model cho TranDau (PvPMatches)
 * Ánh xạ từ bảng TranDau trong CSDL và bổ sung chi tiết UI.
 */
public class PvPMatch {
    private int tranDauID;
    private int phongID;
    private int user1ID;
    private int user2ID;

    // THÔNG TIN BỔ SUNG CỦA NGƯỜI CHƠI
    private String tenUser1;
    private String tenUser2;
    private String avatarUser1Url;
    private String avatarUser2Url;

    private int diemUser1;
    private int diemUser2;
    private String ketQua; // "User1 thắng", "User2 thắng", "Hòa"

    // THÔNG TIN TRẠNG THÁI GAMEPLAY
    private String trangThaiTranDau; // Ví dụ: "IN_PROGRESS"
    private int currentQuestionIndex; // Câu hỏi hiện tại

    private Date thoiGian;

    // Constructors
    public PvPMatch() {
    }

    // --- Getters and Setters (Đã có) ---

    public int getTranDauID() {
        return tranDauID;
    }

    public void setTranDauID(int tranDauID) {
        this.tranDauID = tranDauID;
    }

    public int getPhongID() {
        return phongID;
    }

    public void setPhongID(int phongID) {
        this.phongID = phongID;
    }

    public int getUser1ID() {
        return user1ID;
    }

    public void setUser1ID(int user1ID) {
        this.user1ID = user1ID;
    }

    public int getUser2ID() {
        return user2ID;
    }

    public void setUser2ID(int user2ID) {
        this.user2ID = user2ID;
    }

    // --- Getters và Setters BỔ SUNG ---

    public String getTenUser1() {
        return tenUser1;
    }

    public void setTenUser1(String tenUser1) {
        this.tenUser1 = tenUser1;
    }

    public String getTenUser2() {
        return tenUser2;
    }

    public void setTenUser2(String tenUser2) {
        this.tenUser2 = tenUser2;
    }

    public String getAvatarUser1Url() {
        return avatarUser1Url;
    }

    public void setAvatarUser1Url(String avatarUser1Url) {
        this.avatarUser1Url = avatarUser1Url;
    }

    public String getAvatarUser2Url() {
        return avatarUser2Url;
    }

    public void setAvatarUser2Url(String avatarUser2Url) {
        this.avatarUser2Url = avatarUser2Url;
    }

    public String getTrangThaiTranDau() {
        return trangThaiTranDau;
    }

    public void setTrangThaiTranDau(String trangThaiTranDau) {
        this.trangThaiTranDau = trangThaiTranDau;
    }

    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }

    public void setCurrentQuestionIndex(int currentQuestionIndex) {
        this.currentQuestionIndex = currentQuestionIndex;
    }

    // --- Getters and Setters (Tiếp tục) ---

    public int getDiemUser1() {
        return diemUser1;
    }

    public void setDiemUser1(int diemUser1) {
        this.diemUser1 = diemUser1;
    }

    public int getDiemUser2() {
        return diemUser2;
    }

    public void setDiemUser2(int diemUser2) {
        this.diemUser2 = diemUser2;
    }

    public String getKetQua() {
        return ketQua;
    }

    public void setKetQua(String ketQua) {
        this.ketQua = ketQua;
    }

    public Date getThoiGian() {
        return thoiGian;
    }

    public void setThoiGian(Date thoiGian) {
        this.thoiGian = thoiGian;
    }
}