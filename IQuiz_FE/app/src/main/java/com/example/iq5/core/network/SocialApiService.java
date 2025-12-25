package com.example.iq5.core.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SocialApiService {
    
    // Lấy bảng xếp hạng
    @GET("ranking/leaderboard")
    Call<LeaderboardResponse> getLeaderboard(
        @Query("type") String type, // "weekly", "monthly"
        @Query("pageNumber") int pageNumber,
        @Query("pageSize") int pageSize
    );
    
    // Lấy thành tựu của tôi
    @GET("ranking/achievements/my")
    Call<AchievementsResponse> getMyAchievements();
    
    // Lấy số người online
    @GET("ranking/online-count")
    Call<OnlineCountResponse> getOnlineCount();
    
    // Response classes
    public static class LeaderboardResponse {
        private String type;
        private int tongSoNguoi;
        private int trangHienTai;
        private int tongSoTrang;
        private java.util.List<RankingUser> danhSach;
        
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        
        public int getTongSoNguoi() { return tongSoNguoi; }
        public void setTongSoNguoi(int tongSoNguoi) { this.tongSoNguoi = tongSoNguoi; }
        
        public int getTrangHienTai() { return trangHienTai; }
        public void setTrangHienTai(int trangHienTai) { this.trangHienTai = trangHienTai; }
        
        public int getTongSoTrang() { return tongSoTrang; }
        public void setTongSoTrang(int tongSoTrang) { this.tongSoTrang = tongSoTrang; }
        
        public java.util.List<RankingUser> getDanhSach() { return danhSach; }
        public void setDanhSach(java.util.List<RankingUser> danhSach) { this.danhSach = danhSach; }
    }
    
    public static class RankingUser {
        private int rank;
        private String hoTen;
        private int totalScore;
        private int totalQuizzes;
        private String anhDaiDien;
        
        public int getRank() { return rank; }
        public void setRank(int rank) { this.rank = rank; }
        
        public String getHoTen() { return hoTen; }
        public void setHoTen(String hoTen) { this.hoTen = hoTen; }
        
        public int getTotalScore() { return totalScore; }
        public void setTotalScore(int totalScore) { this.totalScore = totalScore; }
        
        public int getTotalQuizzes() { return totalQuizzes; }
        public void setTotalQuizzes(int totalQuizzes) { this.totalQuizzes = totalQuizzes; }
        
        public String getAnhDaiDien() { return anhDaiDien; }
        public void setAnhDaiDien(String anhDaiDien) { this.anhDaiDien = anhDaiDien; }
    }
    
    public static class AchievementsResponse {
        private java.util.List<Achievement> achievements;
        private String message;
        
        public java.util.List<Achievement> getAchievements() { return achievements; }
        public void setAchievements(java.util.List<Achievement> achievements) { this.achievements = achievements; }
        
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
    
    public static class Achievement {
        private int thanhTuuID;
        private String tenThanhTuu;
        private String moTa;
        private String icon;
        private String ngayDat;
        
        public int getThanhTuuID() { return thanhTuuID; }
        public void setThanhTuuID(int thanhTuuID) { this.thanhTuuID = thanhTuuID; }
        
        public String getTenThanhTuu() { return tenThanhTuu; }
        public void setTenThanhTuu(String tenThanhTuu) { this.tenThanhTuu = tenThanhTuu; }
        
        public String getMoTa() { return moTa; }
        public void setMoTa(String moTa) { this.moTa = moTa; }
        
        public String getIcon() { return icon; }
        public void setIcon(String icon) { this.icon = icon; }
        
        public String getNgayDat() { return ngayDat; }
        public void setNgayDat(String ngayDat) { this.ngayDat = ngayDat; }
    }
    
    public static class OnlineCountResponse {
        private int tongNguoiOnline;
        
        public int getTongNguoiOnline() { return tongNguoiOnline; }
        public void setTongNguoiOnline(int tongNguoiOnline) { this.tongNguoiOnline = tongNguoiOnline; }
    }
}