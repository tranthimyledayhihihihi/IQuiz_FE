package com.example.iq5.core.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface HistoryApiService {
    
    // Lấy lịch sử làm bài của tôi
    @GET("api/lichsuchoi/my")
    Call<HistoryResponse> getMyHistory(
        @Query("pageNumber") int pageNumber,
        @Query("pageSize") int pageSize
    );
    
    // Lấy chi tiết một lần làm bài
    @GET("api/lichsuchoi/{attemptId}")
    Call<HistoryDetail> getHistoryDetail(@Path("attemptId") int attemptId);
    
    // Lấy chuỗi ngày chơi từ lịch sử
    @GET("api/lichsuchoi/streak")
    Call<StreakInfo> getStreakFromHistory();
    
    // Lấy thành tựu từ lịch sử
    @GET("api/lichsuchoi/achievements")
    Call<java.util.List<AchievementApiService.Achievement>> getAchievementsFromHistory();
    
    // Response classes
    public static class HistoryResponse {
        private int tongSoKetQua;
        private int trangHienTai;
        private int kichThuocTrang;
        private int tongSoTrang;
        private java.util.List<HistoryItem> danhSach;
        private String message;
        
        public int getTongSoKetQua() { return tongSoKetQua; }
        public void setTongSoKetQua(int tongSoKetQua) { this.tongSoKetQua = tongSoKetQua; }
        
        public int getTrangHienTai() { return trangHienTai; }
        public void setTrangHienTai(int trangHienTai) { this.trangHienTai = trangHienTai; }
        
        public int getKichThuocTrang() { return kichThuocTrang; }
        public void setKichThuocTrang(int kichThuocTrang) { this.kichThuocTrang = kichThuocTrang; }
        
        public int getTongSoTrang() { return tongSoTrang; }
        public void setTongSoTrang(int tongSoTrang) { this.tongSoTrang = tongSoTrang; }
        
        public java.util.List<HistoryItem> getDanhSach() { return danhSach; }
        public void setDanhSach(java.util.List<HistoryItem> danhSach) { this.danhSach = danhSach; }
        
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
    
    public static class HistoryItem {
        private int quizAttemptID;
        private String ngayLam;
        private double diem;
        private int soCauDung;
        private int tongCauHoi;
        private String trangThaiKetQua;
        private String loaiQuiz;
        
        public int getQuizAttemptID() { return quizAttemptID; }
        public void setQuizAttemptID(int quizAttemptID) { this.quizAttemptID = quizAttemptID; }
        
        public String getNgayLam() { return ngayLam; }
        public void setNgayLam(String ngayLam) { this.ngayLam = ngayLam; }
        
        public double getDiem() { return diem; }
        public void setDiem(double diem) { this.diem = diem; }
        
        public int getSoCauDung() { return soCauDung; }
        public void setSoCauDung(int soCauDung) { this.soCauDung = soCauDung; }
        
        public int getTongCauHoi() { return tongCauHoi; }
        public void setTongCauHoi(int tongCauHoi) { this.tongCauHoi = tongCauHoi; }
        
        public String getTrangThaiKetQua() { return trangThaiKetQua; }
        public void setTrangThaiKetQua(String trangThaiKetQua) { this.trangThaiKetQua = trangThaiKetQua; }
        
        public String getLoaiQuiz() { return loaiQuiz; }
        public void setLoaiQuiz(String loaiQuiz) { this.loaiQuiz = loaiQuiz; }
    }
    
    public static class HistoryDetail {
        private int quizAttemptID;
        private String ngayLam;
        private double diem;
        private int soCauDung;
        private int tongCauHoi;
        private String trangThaiKetQua;
        private java.util.List<QuestionResult> chiTietCauHoi;
        
        public int getQuizAttemptID() { return quizAttemptID; }
        public void setQuizAttemptID(int quizAttemptID) { this.quizAttemptID = quizAttemptID; }
        
        public String getNgayLam() { return ngayLam; }
        public void setNgayLam(String ngayLam) { this.ngayLam = ngayLam; }
        
        public double getDiem() { return diem; }
        public void setDiem(double diem) { this.diem = diem; }
        
        public int getSoCauDung() { return soCauDung; }
        public void setSoCauDung(int soCauDung) { this.soCauDung = soCauDung; }
        
        public int getTongCauHoi() { return tongCauHoi; }
        public void setTongCauHoi(int tongCauHoi) { this.tongCauHoi = tongCauHoi; }
        
        public String getTrangThaiKetQua() { return trangThaiKetQua; }
        public void setTrangThaiKetQua(String trangThaiKetQua) { this.trangThaiKetQua = trangThaiKetQua; }
        
        public java.util.List<QuestionResult> getChiTietCauHoi() { return chiTietCauHoi; }
        public void setChiTietCauHoi(java.util.List<QuestionResult> chiTietCauHoi) { this.chiTietCauHoi = chiTietCauHoi; }
    }
    
    public static class QuestionResult {
        private String cauHoi;
        private String dapAnChon;
        private String dapAnDung;
        private boolean isCorrect;
        
        public String getCauHoi() { return cauHoi; }
        public void setCauHoi(String cauHoi) { this.cauHoi = cauHoi; }
        
        public String getDapAnChon() { return dapAnChon; }
        public void setDapAnChon(String dapAnChon) { this.dapAnChon = dapAnChon; }
        
        public String getDapAnDung() { return dapAnDung; }
        public void setDapAnDung(String dapAnDung) { this.dapAnDung = dapAnDung; }
        
        public boolean isCorrect() { return isCorrect; }
        public void setCorrect(boolean correct) { isCorrect = correct; }
    }
    
    public static class StreakInfo {
        private int chuoiID;
        private int userID;
        private int soNgayLienTiep;
        private String ngayCapNhatCuoi;
        private String message;
        
        public int getChuoiID() { return chuoiID; }
        public void setChuoiID(int chuoiID) { this.chuoiID = chuoiID; }
        
        public int getUserID() { return userID; }
        public void setUserID(int userID) { this.userID = userID; }
        
        public int getSoNgayLienTiep() { return soNgayLienTiep; }
        public void setSoNgayLienTiep(int soNgayLienTiep) { this.soNgayLienTiep = soNgayLienTiep; }
        
        public String getNgayCapNhatCuoi() { return ngayCapNhatCuoi; }
        public void setNgayCapNhatCuoi(String ngayCapNhatCuoi) { this.ngayCapNhatCuoi = ngayCapNhatCuoi; }
        
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
}