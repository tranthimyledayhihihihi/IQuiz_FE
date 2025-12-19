package com.example.iq5.core.network;

import com.example.iq5.data.model.ApiResponse;
import com.example.iq5.data.model.UserProfileModel;
import com.example.iq5.data.model.ProfileUpdateModel;
import com.example.iq5.data.model.ChangePasswordModel;
import com.example.iq5.data.model.UserStreakResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.POST;

public interface UserApiService {
    
    @GET("user/profile/me")
    Call<com.example.iq5.data.model.UserProfileModel> getMyProfile();
    
    @PUT("user/profile/me")
    Call<ApiResponse> updateProfile(@Body ProfileUpdateModel profile);
    
    @PUT("user/profile/settings")
    Call<ApiResponse> updateSettings(@Body UserSettingsModel settings);
    
    @POST("Account/change-password")
    Call<ApiResponse> changePassword(@Body ChangePasswordModel changePassword);
    
    @GET("user/achievement/streak")
    Call<UserStreakResponse> getMyStreak();
    
    // Model cho cài đặt người dùng
    public static class UserSettingsModel {
        private boolean amThanh;
        private boolean nhacNen;
        private boolean thongBao;
        private String ngonNgu;
        
        public UserSettingsModel(boolean amThanh, boolean nhacNen, boolean thongBao, String ngonNgu) {
            this.amThanh = amThanh;
            this.nhacNen = nhacNen;
            this.thongBao = thongBao;
            this.ngonNgu = ngonNgu;
        }
        
        public boolean isAmThanh() { return amThanh; }
        public void setAmThanh(boolean amThanh) { this.amThanh = amThanh; }
        
        public boolean isNhacNen() { return nhacNen; }
        public void setNhacNen(boolean nhacNen) { this.nhacNen = nhacNen; }
        
        public boolean isThongBao() { return thongBao; }
        public void setThongBao(boolean thongBao) { this.thongBao = thongBao; }
        
        public String getNgonNgu() { return ngonNgu; }
        public void setNgonNgu(String ngonNgu) { this.ngonNgu = ngonNgu; }
    }

    // Response classes (deprecated - use UserProfileModel instead)
    @Deprecated
    public static class UserProfile {
        private int userID;
        private String tenDangNhap;
        private String email;
        private String hoTen;
        private String anhDaiDien;
        private String ngayDangKy;
        private String lanDangNhapCuoi;
        private String vaiTro;
        private UserSettings caiDat;
        
        // Getters and Setters
        public int getUserID() { return userID; }
        public void setUserID(int userID) { this.userID = userID; }
        
        public String getTenDangNhap() { return tenDangNhap; }
        public void setTenDangNhap(String tenDangNhap) { this.tenDangNhap = tenDangNhap; }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getHoTen() { return hoTen; }
        public void setHoTen(String hoTen) { this.hoTen = hoTen; }
        
        public String getAnhDaiDien() { return anhDaiDien; }
        public void setAnhDaiDien(String anhDaiDien) { this.anhDaiDien = anhDaiDien; }
        
        public String getNgayDangKy() { return ngayDangKy; }
        public void setNgayDangKy(String ngayDangKy) { this.ngayDangKy = ngayDangKy; }
        
        public String getLanDangNhapCuoi() { return lanDangNhapCuoi; }
        public void setLanDangNhapCuoi(String lanDangNhapCuoi) { this.lanDangNhapCuoi = lanDangNhapCuoi; }
        
        public String getVaiTro() { return vaiTro; }
        public void setVaiTro(String vaiTro) { this.vaiTro = vaiTro; }
        
        public UserSettings getCaiDat() { return caiDat; }
        public void setCaiDat(UserSettings caiDat) { this.caiDat = caiDat; }
    }
    
    public static class UserSettings {
        private boolean amThanh;
        private boolean nhacNen;
        private boolean thongBao;
        private String ngonNgu;
        
        public boolean isAmThanh() { return amThanh; }
        public void setAmThanh(boolean amThanh) { this.amThanh = amThanh; }
        
        public boolean isNhacNen() { return nhacNen; }
        public void setNhacNen(boolean nhacNen) { this.nhacNen = nhacNen; }
        
        public boolean isThongBao() { return thongBao; }
        public void setThongBao(boolean thongBao) { this.thongBao = thongBao; }
        
        public String getNgonNgu() { return ngonNgu; }
        public void setNgonNgu(String ngonNgu) { this.ngonNgu = ngonNgu; }
    }
    
    public static class ProfileUpdateRequest {
        private String email;
        private String hoTen;
        
        public ProfileUpdateRequest(String email, String hoTen) {
            this.email = email;
            this.hoTen = hoTen;
        }
        
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        
        public String getHoTen() { return hoTen; }
        public void setHoTen(String hoTen) { this.hoTen = hoTen; }
    }
    
    public static class SettingsUpdateRequest {
        private boolean amThanh;
        private boolean nhacNen;
        private boolean thongBao;
        private String ngonNgu;
        
        public SettingsUpdateRequest(boolean amThanh, boolean nhacNen, boolean thongBao, String ngonNgu) {
            this.amThanh = amThanh;
            this.nhacNen = nhacNen;
            this.thongBao = thongBao;
            this.ngonNgu = ngonNgu;
        }
        
        public boolean isAmThanh() { return amThanh; }
        public void setAmThanh(boolean amThanh) { this.amThanh = amThanh; }
        
        public boolean isNhacNen() { return nhacNen; }
        public void setNhacNen(boolean nhacNen) { this.nhacNen = nhacNen; }
        
        public boolean isThongBao() { return thongBao; }
        public void setThongBao(boolean thongBao) { this.thongBao = thongBao; }
        
        public String getNgonNgu() { return ngonNgu; }
        public void setNgonNgu(String ngonNgu) { this.ngonNgu = ngonNgu; }
    }
}