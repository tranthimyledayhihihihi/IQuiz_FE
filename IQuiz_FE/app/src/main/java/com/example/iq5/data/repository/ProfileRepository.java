package com.example.iq5.data.repository;

import android.content.Context;
import android.util.Log;

import com.example.iq5.data.api.RetrofitClient;
import com.example.iq5.data.api.ApiService;
import com.example.iq5.data.model.*;
import com.example.iq5.utils.ApiHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repository để quản lý Profile và Settings
 */
public class ProfileRepository {
    
    private static final String TAG = "ProfileRepository";
    private final ApiService apiService;
    private final Context context;
    
    public ProfileRepository(Context context) {
        this.context = context;
        this.apiService = RetrofitClient.getApiService();
    }
    
    /**
     * Lấy thông tin profile của user hiện tại
     */
    public void getMyProfileAsync(ProfileCallback callback) {
        String token = "Bearer " + ApiHelper.getToken(context);
        
        Log.d(TAG, "👤 Đang lấy thông tin profile...");
        
        Call<UserProfileModel> call = apiService.getMyProfile(token);
        call.enqueue(new Callback<UserProfileModel>() {
            @Override
            public void onResponse(Call<UserProfileModel> call, Response<UserProfileModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "✅ Lấy profile thành công!");
                    callback.onSuccess(response.body());
                } else {
                    Log.e(TAG, "❌ Lỗi lấy profile: " + response.code());
                    callback.onError("Không thể lấy thông tin profile. Mã lỗi: " + response.code());
                }
            }
            
            @Override
            public void onFailure(Call<UserProfileModel> call, Throwable t) {
                Log.e(TAG, "❌ Lỗi kết nối: " + t.getMessage());
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }
    
    /**
     * Cập nhật profile
     */
    public void updateProfileAsync(ProfileUpdateModel profile, UpdateCallback callback) {
        String token = "Bearer " + ApiHelper.getToken(context);
        
        Log.d(TAG, "✏️ Đang cập nhật profile...");
        Log.d(TAG, "📤 Request data: HoTen=" + profile.hoTen + ", Email=" + profile.email + ", AnhDaiDien=" + profile.anhDaiDien);
        
        Call<ApiResponse> call = apiService.updateMyProfile(token, profile);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Log.d(TAG, "📥 Response code: " + response.code());
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "✅ Cập nhật profile thành công!");
                    callback.onSuccess(response.body().getMessage());
                } else {
                    Log.e(TAG, "❌ Lỗi cập nhật profile: " + response.code());
                    String errorBody = "";
                    try {
                        if (response.errorBody() != null) {
                            errorBody = response.errorBody().string();
                            Log.e(TAG, "📄 Error body: " + errorBody);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Không thể đọc error body: " + e.getMessage());
                    }
                    callback.onError("Không thể cập nhật profile. Mã lỗi: " + response.code() + (errorBody.isEmpty() ? "" : " - " + errorBody));
                }
            }
            
            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e(TAG, "❌ Lỗi kết nối: " + t.getMessage());
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }
    
    /**
     * Cập nhật cài đặt
     */
    public void updateSettingsAsync(CaiDatModel settings, UpdateCallback callback) {
        String token = "Bearer " + ApiHelper.getToken(context);
        
        Log.d(TAG, "⚙️ Đang cập nhật cài đặt...");
        
        Call<ApiResponse> call = apiService.updateSettings(token, settings);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "✅ Cập nhật cài đặt thành công!");
                    callback.onSuccess(response.body().getMessage());
                } else {
                    Log.e(TAG, "❌ Lỗi cập nhật cài đặt: " + response.code());
                    callback.onError("Không thể cập nhật cài đặt. Mã lỗi: " + response.code());
                }
            }
            
            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e(TAG, "❌ Lỗi kết nối: " + t.getMessage());
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }
    
    /**
     * Đăng xuất
     */
    public void logoutAsync(LogoutCallback callback) {
        String token = "Bearer " + ApiHelper.getToken(context);
        
        Log.d(TAG, "🚪 Đang đăng xuất...");
        
        Call<ApiResponse> call = apiService.logout(token);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                // Xóa token local dù API có thành công hay không
                ApiHelper.clearToken(context);
                
                if (response.isSuccessful()) {
                    Log.d(TAG, "✅ Đăng xuất thành công!");
                    callback.onSuccess("Đăng xuất thành công");
                } else {
                    Log.w(TAG, "⚠️ Đăng xuất với cảnh báo: " + response.code());
                    callback.onSuccess("Đăng xuất thành công");
                }
            }
            
            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                // Vẫn xóa token local khi lỗi
                ApiHelper.clearToken(context);
                Log.w(TAG, "⚠️ Đăng xuất offline: " + t.getMessage());
                callback.onSuccess("Đăng xuất thành công");
            }
        });
    }
    
    // ============================================
    // CALLBACKS
    // ============================================
    
    public interface ProfileCallback {
        void onSuccess(UserProfileModel profile);
        void onError(String error);
    }
    
    public interface UpdateCallback {
        void onSuccess(String message);
        void onError(String error);
    }
    
    public interface LogoutCallback {
        void onSuccess(String message);
    }
    
    /**
     * Đổi mật khẩu
     */
    public void changePasswordAsync(String currentPassword, String newPassword, String confirmNewPassword, ChangePasswordCallback callback) {
        String token = "Bearer " + ApiHelper.getToken(context);
        
        Log.d(TAG, "🔐 Đang đổi mật khẩu...");
        
        ChangePasswordModel request = new ChangePasswordModel(currentPassword, newPassword, confirmNewPassword);
        Call<ApiResponse> call = apiService.changePassword(token, request);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "✅ Đổi mật khẩu thành công!");
                    callback.onSuccess(response.body().getMessage());
                } else if (response.code() == 401) {
                    Log.e(TAG, "❌ Mật khẩu hiện tại không đúng");
                    callback.onError("Mật khẩu hiện tại không đúng");
                } else {
                    Log.e(TAG, "❌ Lỗi đổi mật khẩu: " + response.code());
                    callback.onError("Không thể đổi mật khẩu. Mã lỗi: " + response.code());
                }
            }
            
            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e(TAG, "❌ Lỗi kết nối: " + t.getMessage());
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }
    
    public interface ChangePasswordCallback {
        void onSuccess(String message);
        void onError(String error);
    }
}
