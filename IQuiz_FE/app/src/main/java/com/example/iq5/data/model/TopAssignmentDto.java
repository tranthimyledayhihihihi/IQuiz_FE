package com.example.iq5.data.model;

public class TopAssignmentDto {
    private int doAnID;
    private String tieuDe;
    private int soNopBai; // Số lượng nộp bài (dựa trên sp_TopDoAnTheoNopBai)

    // Constructor
    public TopAssignmentDto(int doAnID, String tieuDe, int soNopBai) {
        this.doAnID = doAnID;
        this.tieuDe = tieuDe;
        this.soNopBai = soNopBai;
    }

    // Getters and Setters (bắt buộc)
    public int getDoAnID() { return doAnID; }
    public void setDoAnID(int doAnID) { this.doAnID = doAnID; }

    public String getTieuDe() { return tieuDe; }
    public void setTieuDe(String tieuDe) { this.tieuDe = tieuDe; }

    public int getSoNopBai() { return soNopBai; }
    public void setSoNopBai(int soNopBai) { this.soNopBai = soNopBai; }
}