package com.example.iq5.data.repository;

import android.content.Context;
import android.util.Log;

import com.example.iq5.core.network.ApiClient;
import com.example.iq5.core.network.UserApiService;
import com.example.iq5.core.prefs.PrefsManager;
import com.example.iq5.data.model.ApiResponse;
import com.example.iq5.data.model.ChangePasswordModel;
import com.example.iq5.data.model.ProfileUpdateModel;
import com.example.iq5.data.model.UserProfileModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Repository để xử lý các API calls liên quan đến User Profile
 */
public class UserProfileApiRepository {
    
    private static final String TAG = "UserProfileApiRepository";
    private final UserApiService apiService;
    private final Context context;
    
    public UserProfileApiRepository(Context context) {
        this.context = context.getApplicationContext();
        PrefsManager prefsManager = new PrefsManager(context);
        Retrofit retrofit = ApiClient.getClient(prefsManager);
        this.apiService = ApiClient.createService(retrofit, UserApiService.class);
    }
    
    /**
     * Lấy thông tin profile của user hiện tại
     */
    public void getMyProfile(final ProfileCallback callback) {
        Log.d(TAG, "👤 Đang gọi API Get My Profile...");
        
        Call<UserProfileModel> call = apiService.getMyProfile();
        
        call.enqueue(new Callback<UserProfileModel>() {
            @Override
            public void onResponse(Call<UserProfileModel> call, Response<UserProfileModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "✅ Get My Profile thành công!");
                    callback.onSuccess(response.body());
                } else if (response.code() == 401) {
                    Log.e(TAG, "❌ Unauthorized - Token hết hạn");
                    callback.onUnauthorized();
                } else {
                    Log.e(TAG, "❌ Get My Profile lỗi: " + response.code());
                    callback.onError("Không thể lấy thông tin profile. Mã lỗi: " + response.code());
                }
            }
            
            @Override
            public void onFailure(Call<UserProfileModel> call, Throwable t) {
                Log.e(TAG, "❌ Get My Profile thất bại: " + t.getMessage());
                callback.onError("Không thể kết nối đến server: " + t.getMessage());
            }
        });
    }
    
    /**
     * Cập nhật thông tin profile
     */
    public void updateProfile(ProfileUpdateModel profile, final UpdateCallback callback) {
        Log.d(TAG, "✏️ Đang gọi API Update Profile...");
        
        Call<ApiResponse> call = apiService.updateProfile(profile);
        
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "✅ Update Profile thành công!");
                    callback.onSuccess(response.body().getMessage());
                } else if (response.code() == 401) {
                    Log.e(TAG, "❌ Unauthorized - Token hết hạn");
                    callback.onUnauthorized();
                } else if (response.code() == 400) {
                    Log.e(TAG, "❌ Bad Request - Dữ liệu không hợp lệ");
                    callback.onError("Thông tin cập nhật không hợp lệ");
                } else {
                    Log.e(TAG, "❌ Update Profile lỗi: " + response.code());
                    callback.onError("Không thể cập nhật profile. Mã lỗi: " + response.code());
                }
            }
            
            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e(TAG, "❌ Update Profile thất bại: " + t.getMessage());
                callback.onError("Không thể kết nối đến server: " + t.getMessage());
            }
        });
    }
    
    /**
     * Cập nhật cài đặt người dùng
     */
    public void updateSettings(boolean amThanh, boolean nhacNen, boolean thongBao, 
                              String ngonNgu, final UpdateCallback callback) {
        Log.d(TAG, "⚙️ Đang gọi API Update Settings...");
        
        UserApiService.UserSettingsModel settings = 
            new UserApiService.UserSettingsModel(amThanh, nhacNen, thongBao, ngonNgu);
        
        Call<ApiResponse> call = apiService.updateSettings(settings);
        
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "✅ Update Settings thành công!");
                    callback.onSuccess(response.body().getMessage());
                } else if (response.code() == 401) {
                    Log.e(TAG, "❌ Unauthorized - Token hết hạn");
                    callback.onUnauthorized();
                } else {
                    Log.e(TAG, "❌ Update Settings lỗi: " + response.code());
                    callback.onError("Không thể cập nhật cài đặt. Mã lỗi: " + response.code());
                }
            }
            
            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e(TAG, "❌ Update Settings thất bại: " + t.getMessage());
                callback.onError("Không thể kết nối đến server: " + t.getMessage());
            }
        });
    }
    
    /**
     * Cập nhật thống kê quiz sau khi hoàn thành
     */
    public void updateQuizStats(int correctAnswers, int totalQuestions, double score, 
                               String category, final UpdateCallback callback) {
        Log.d(TAG, "📊 Đang gọi API Update Quiz Stats...");
        Log.d(TAG, "   ✅ Correct: " + correctAnswers + "/" + totalQuestions);
        Log.d(TAG, "   💯 Score: " + score + "%");
        Log.d(TAG, "   📚 Category: " + category);
        
        UserApiService.QuizStatsUpdateModel stats = 
            new UserApiService.QuizStatsUpdateModel(correctAnswers, totalQuestions, score, category);
        
        Call<ApiResponse> call = apiService.updateQuizStats(stats);
        
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "✅ Update Quiz Stats thành công!");
                    callback.onSuccess(response.body().getMessage());
                } else if (response.code() == 401) {
                    Log.e(TAG, "❌ Unauthorized - Token hết hạn");
                    callback.onUnauthorized();
                } else {
                    Log.e(TAG, "❌ Update Quiz Stats lỗi: " + response.code());
                    callback.onError("Không thể cập nhật thống kê. Mã lỗi: " + response.code());
                }
            }
            
            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e(TAG, "❌ Update Quiz Stats thất bại: " + t.getMessage());
                callback.onError("Không thể kết nối đến server: " + t.getMessage());
            }
        });
    }
    
    /**
     * Đổi mật khẩu
     */
    public void changePassword(String currentPassword, String newPassword, final UpdateCallback callback) {
        Log.d(TAG, "🔐 Đang gọi API Change Password...");
        
        ChangePasswordModel changePasswordModel = new ChangePasswordModel(currentPassword, newPassword);
        
        Call<ApiResponse> call = apiService.changePassword(changePasswordModel);
        
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "✅ Change Password thành công!");
                    callback.onSuccess(response.body().getMessage());
                } else if (response.code() == 401) {
                    Log.e(TAG, "❌ Unauthorized - Mật khẩu hiện tại không đúng");
                    callback.onError("Mật khẩu hiện tại không đúng");
                } else {
                    Log.e(TAG, "❌ Change Password lỗi: " + response.code());
                    callback.onError("Không thể đổi mật khẩu. Mã lỗi: " + response.code());
                }
            }
            
            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e(TAG, "❌ Change Password thất bại: " + t.getMessage());
                callback.onError("Không thể kết nối đến server: " + t.getMessage());
            }
        });
    }
    
    // Callback interfaces
    public interface ProfileCallback {
        void onSuccess(UserProfileModel profile);
        void onUnauthorized();
        void onError(String error);
    }
    
    public interface UpdateCallback {
        void onSuccess(String message);
        void onUnauthorized();
        void onError(String error);
    }
}