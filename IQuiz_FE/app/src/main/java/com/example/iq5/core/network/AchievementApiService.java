package com.example.iq5.core.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AchievementApiService {
    
    // Lấy thành tựu của tôi
    @GET("api/user/achievement/me")
    Call<AchievementResponse> getMyAchievements();
    
    // Lấy chuỗi ngày chơi
    @GET("api/user/achievement/streak")
    Call<StreakResponse> getMyStreak();
    
    // Nhận thưởng hàng ngày
    @POST("api/user/achievement/daily-reward")
    Call<DailyRewardResponse> claimDailyReward();
    
    // Model classes
    public static class Achievement {
        private int id;
        private int thanhTuuID;
        private String tenThanhTuu;
        private String moTa;
        private String icon;
        private String ngayDat;
        private boolean isUnlocked;
        private int progress;
        private String requirement;
        
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        
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
        
        public boolean isUnlocked() { return isUnlocked; }
        public void setUnlocked(boolean unlocked) { isUnlocked = unlocked; }
        
        public int getProgress() { return progress; }
        public void setProgress(int progress) { this.progress = progress; }
        
        public String getRequirement() { return requirement; }
        public void setRequirement(String requirement) { this.requirement = requirement; }
    }
    
    public static class AchievementResponse {
        private boolean success;
        private int totalAchievements;
        private int unlockedCount;
        private java.util.List<Achievement> achievements;
        
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        
        public int getTotalAchievements() { return totalAchievements; }
        public void setTotalAchievements(int totalAchievements) { this.totalAchievements = totalAchievements; }
        
        public int getUnlockedCount() { return unlockedCount; }
        public void setUnlockedCount(int unlockedCount) { this.unlockedCount = unlockedCount; }
        
        public java.util.List<Achievement> getAchievements() { return achievements; }
        public void setAchievements(java.util.List<Achievement> achievements) { this.achievements = achievements; }
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