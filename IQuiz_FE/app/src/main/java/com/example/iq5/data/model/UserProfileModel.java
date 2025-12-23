package com.example.iq5.data.model;

import com.google.gson.annotations.SerializedName;

/**
 * Model cho User Profile
 * Tương ứng với response từ ProfileController backend
 */
public class UserProfileModel {
    
    @SerializedName("userID")
    private int userID;
    
    @SerializedName("tenDangNhap")
    private String tenDangNhap;
    
    @SerializedName("email")
    private String email;
    
    @SerializedName("hoTen")
    private String hoTen;
    
    @SerializedName("anhDaiDien")
    private String anhDaiDien;
    
    @SerializedName("ngayDangKy")
    private String ngayDangKy;
    
    @SerializedName("lanDangNhapCuoi")
    private String lanDangNhapCuoi;
    
    @SerializedName("vaiTro")
    private String vaiTro;
    
    @SerializedName("caiDat")
    private CaiDatModel caiDat;
    
    @SerializedName("thongKe")
    private ThongKeModel thongKe;
    
    // Nested CaiDat class
    public static class CaiDatModel {
        @SerializedName("amThanh")
        private boolean amThanh;
        
        @SerializedName("nhacNen")
        private boolean nhacNen;
        
        @SerializedName("thongBao")
        private boolean thongBao;
        
        @SerializedName("ngonNgu")
        private String ngonNgu;
        
        // Getters & Setters
        public boolean isAmThanh() { return amThanh; }
        public void setAmThanh(boolean amThanh) { this.amThanh = amThanh; }
        
        public boolean isNhacNen() { return nhacNen; }
        public void setNhacNen(boolean nhacNen) { this.nhacNen = nhacNen; }
        
        public boolean isThongBao() { return thongBao; }
        public void setThongBao(boolean thongBao) { this.thongBao = thongBao; }
        
        public String getNgonNgu() { return ngonNgu; }
        public void setNgonNgu(String ngonNgu) { this.ngonNgu = ngonNgu; }
    }
    
    // Nested ThongKe class
    public static class ThongKeModel {
        @SerializedName("soBaiQuizHoanThanh")
        private int soBaiQuizHoanThanh;
        
        @SerializedName("diemTrungBinh")
        private double diemTrungBinh;
        
        @SerializedName("tongSoCauDung")
        private int tongSoCauDung;
        
        @SerializedName("tongSoCauHoi")
        private int tongSoCauHoi;
        
        @SerializedName("tyLeDung")
        private double tyLeDung;
        
        // Getters & Setters
        public int getSoBaiQuizHoanThanh() { return soBaiQuizHoanThanh; }
        public void setSoBaiQuizHoanThanh(int soBaiQuizHoanThanh) { this.soBaiQuizHoanThanh = soBaiQuizHoanThanh; }
        
        public double getDiemTrungBinh() { return diemTrungBinh; }
        public void setDiemTrungBinh(double diemTrungBinh) { this.diemTrungBinh = diemTrungBinh; }
        
        public int getTongSoCauDung() { return tongSoCauDung; }
        public void setTongSoCauDung(int tongSoCauDung) { this.tongSoCauDung = tongSoCauDung; }
        
        public int getTongSoCauHoi() { return tongSoCauHoi; }
        public void setTongSoCauHoi(int tongSoCauHoi) { this.tongSoCauHoi = tongSoCauHoi; }
        
        public double getTyLeDung() { return tyLeDung; }
        public void setTyLeDung(double tyLeDung) { this.tyLeDung = tyLeDung; }
    }

    // Constructors
    public UserProfileModel() {}

    // Getters & Setters
    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getTenDangNhap() {
        return tenDangNhap;
    }

    public void setTenDangNhap(String tenDangNhap) {
        this.tenDangNhap = tenDangNhap;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getAnhDaiDien() {
        return anhDaiDien;
    }

    public void setAnhDaiDien(String anhDaiDien) {
        this.anhDaiDien = anhDaiDien;
    }

    public String getNgayDangKy() {
        return ngayDangKy;
    }

    public void setNgayDangKy(String ngayDangKy) {
        this.ngayDangKy = ngayDangKy;
    }

    public String getLanDangNhapCuoi() {
        return lanDangNhapCuoi;
    }

    public void setLanDangNhapCuoi(String lanDangNhapCuoi) {
        this.lanDangNhapCuoi = lanDangNhapCuoi;
    }

    public String getVaiTro() {
        return vaiTro;
    }

    public void setVaiTro(String vaiTro) {
        this.vaiTro = vaiTro;
    }

    public CaiDatModel getCaiDat() {
        return caiDat;
    }

    public void setCaiDat(CaiDatModel caiDat) {
        this.caiDat = caiDat;
    }
    
    public ThongKeModel getThongKe() {
        return thongKe;
    }

    public void setThongKe(ThongKeModel thongKe) {
        this.thongKe = thongKe;
    }
}
