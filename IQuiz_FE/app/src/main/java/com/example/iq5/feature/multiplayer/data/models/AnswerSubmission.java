package com.example.iq5.feature.multiplayer.data.models;

public class AnswerSubmission {
    private int tranDauID;
    private int cauHoiID;
    private int userID;
    private String dapAnNguoiChoi;
    private boolean dungHaySai;
    private String thoiGianTraLoi;
    private float thoiGianGiaiQuyet;
    private int diemNhanDuoc;

    // Getters and Setters
    public int getTranDauID() { return tranDauID; }
    public void setTranDauID(int tranDauID) { this.tranDauID = tranDauID; }

    public int getCauHoiID() { return cauHoiID; }
    public void setCauHoiID(int cauHoiID) { this.cauHoiID = cauHoiID; }

    public int getUserID() { return userID; }
    public void setUserID(int userID) { this.userID = userID; }

    public String getDapAnNguoiChoi() { return dapAnNguoiChoi; }
    public void setDapAnNguoiChoi(String dapAnNguoiChoi) { this.dapAnNguoiChoi = dapAnNguoiChoi; }

    public boolean isDungHaySai() { return dungHaySai; }
    public void setDungHaySai(boolean dungHaySai) { this.dungHaySai = dungHaySai; }

    public String getThoiGianTraLoi() { return thoiGianTraLoi; }
    public void setThoiGianTraLoi(String thoiGianTraLoi) { this.thoiGianTraLoi = thoiGianTraLoi; }

    public float getThoiGianGiaiQuyet() { return thoiGianGiaiQuyet; }
    public void setThoiGianGiaiQuyet(float thoiGianGiaiQuyet) { this.thoiGianGiaiQuyet = thoiGianGiaiQuyet; }

    public int getDiemNhanDuoc() { return diemNhanDuoc; }
    public void setDiemNhanDuoc(int diemNhanDuoc) { this.diemNhanDuoc = diemNhanDuoc; }
}