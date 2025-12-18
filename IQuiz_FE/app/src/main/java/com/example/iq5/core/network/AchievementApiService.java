package com.example.iq5.core.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AchievementApiService {
    
    // Lấy thành tựu của tôi
    @GET("user/achievement/me")
    Call<java.util.List<Achievement>> getMyAchievements();
    
    // Lấy chuỗi ngày chơi
    @GET("user/achievement/streak")
    Call<StreakResponse> getMyStreak();
    
    // Nhận thưởng hàng ngày
    @POST("user/achievement/daily-reward")
    Call<DailyRewardResponse> claimDailyReward();
    
    // Model classes
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
    
    public static class StreakResponse {
        private int soNgayLienTiep;
        private String ngayCapNhatCuoi;
        private String message;
        
        public int getSoNgayLienTiep() { return soNgayLienTiep; }
        public void setSoNgayLienTiep(int soNgayLienTiep) { this.soNgayLienTiep = soNgayLienTiep; }
        
        public String getNgayCapNhatCuoi() { return ngayCapNhatCuoi; }
        public void setNgayCapNhatCuoi(String ngayCapNhatCuoi) { this.ngayCapNhatCuoi = ngayCapNhatCuoi; }
        
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
    
    public static class DailyRewardResponse {
        private boolean awarded;
        private String message;
        
        public boolean isAwarded() { return awarded; }
        public void setAwarded(boolean awarded) { this.awarded = awarded; }
        
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
}