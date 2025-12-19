package com.example.iq5.data.repository;

import android.content.Context;
import android.util.Log;

import com.example.iq5.core.network.ApiClient;
import com.example.iq5.core.network.AchievementApiService;
import com.example.iq5.core.prefs.PrefsManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Repository để xử lý các API calls liên quan đến Achievement
 */
public class AchievementApiRepository {
    
    private static final String TAG = "AchievementApiRepository";
    private final AchievementApiService apiService;
    private final Context context;
    
    public AchievementApiRepository(Context context) {
        this.context = context.getApplicationContext();
        PrefsManager prefsManager = new PrefsManager(context);
        Retrofit retrofit = ApiClient.getClient(prefsManager);
        this.apiService = ApiClient.createService(retrofit, AchievementApiService.class);
    }
    
    /**
     * Lấy danh sách thành tựu của user
     */
    public void getMyAchievements(final AchievementsCallback callback) {
        Log.d(TAG, "🏆 Đang gọi API Get My Achievements...");
        
        Call<List<AchievementApiService.Achievement>> call = apiService.getMyAchievements();
        
        call.enqueue(new Callback<List<AchievementApiService.Achievement>>() {
            @Override
            public void onResponse(Call<List<AchievementApiService.Achievement>> call, 
                                 Response<List<AchievementApiService.Achievement>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "✅ Get My Achievements thành công! Số thành tựu: " + response.body().size());
                    callback.onSuccess(response.body());
                } else if (response.code() == 401) {
                    Log.e(TAG, "❌ Unauthorized - Token hết hạn");
                    callback.onUnauthorized();
                } else {
                    Log.e(TAG, "❌ Get My Achievements lỗi: " + response.code());
                    callback.onError("Không thể lấy danh sách thành tựu. Mã lỗi: " + response.code());
                }
            }
            
            @Override
            public void onFailure(Call<List<AchievementApiService.Achievement>> call, Throwable t) {
                Log.e(TAG, "❌ Get My Achievements thất bại: " + t.getMessage());
                callback.onError("Không thể kết nối đến server: " + t.getMessage());
            }
        });
    }
    
    /**
     * Lấy thông tin chuỗi ngày chơi
     */
    public void getMyStreak(final StreakCallback callback) {
        Log.d(TAG, "🔥 Đang gọi API Get My Streak...");
        
        Call<AchievementApiService.StreakResponse> call = apiService.getMyStreak();
        
        call.enqueue(new Callback<AchievementApiService.StreakResponse>() {
            @Override
            public void onResponse(Call<AchievementApiService.StreakResponse> call, 
                                 Response<AchievementApiService.StreakResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "✅ Get My Streak thành công! Streak: " + response.body().getSoNgayLienTiep());
                    callback.onSuccess(response.body());
                } else if (response.code() == 401) {
                    Log.e(TAG, "❌ Unauthorized - Token hết hạn");
                    callback.onUnauthorized();
                } else {
                    Log.e(TAG, "❌ Get My Streak lỗi: " + response.code());
                    callback.onError("Không thể lấy thông tin streak. Mã lỗi: " + response.code());
                }
            }
            
            @Override
            public void onFailure(Call<AchievementApiService.StreakResponse> call, Throwable t) {
                Log.e(TAG, "❌ Get My Streak thất bại: " + t.getMessage());
                callback.onError("Không thể kết nối đến server: " + t.getMessage());
            }
        });
    }
    
    /**
     * Nhận thưởng hàng ngày
     */
    public void claimDailyReward(final DailyRewardCallback callback) {
        Log.d(TAG, "🎁 Đang gọi API Claim Daily Reward...");
        
        Call<AchievementApiService.DailyRewardResponse> call = apiService.claimDailyReward();
        
        call.enqueue(new Callback<AchievementApiService.DailyRewardResponse>() {
            @Override
            public void onResponse(Call<AchievementApiService.DailyRewardResponse> call, 
                                 Response<AchievementApiService.DailyRewardResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "✅ Claim Daily Reward thành công! Awarded: " + response.body().isAwarded());
                    callback.onSuccess(response.body().isAwarded(), response.body().getMessage());
                } else if (response.code() == 401) {
                    Log.e(TAG, "❌ Unauthorized - Token hết hạn");
                    callback.onUnauthorized();
                } else if (response.code() == 400) {
                    Log.e(TAG, "❌ Bad Request - Có thể đã nhận thưởng hôm nay");
                    callback.onAlreadyClaimed();
                } else {
                    Log.e(TAG, "❌ Claim Daily Reward lỗi: " + response.code());
                    callback.onError("Không thể nhận thưởng. Mã lỗi: " + response.code());
                }
            }
            
            @Override
            public void onFailure(Call<AchievementApiService.DailyRewardResponse> call, Throwable t) {
                Log.e(TAG, "❌ Claim Daily Reward thất bại: " + t.getMessage());
                callback.onError("Không thể kết nối đến server: " + t.getMessage());
            }
        });
    }
    
    // Callback interfaces
    public interface AchievementsCallback {
        void onSuccess(List<AchievementApiService.Achievement> achievements);
        void onUnauthorized();
        void onError(String error);
    }
    
    public interface StreakCallback {
        void onSuccess(AchievementApiService.StreakResponse streak);
        void onUnauthorized();
        void onError(String error);
    }
    
    public interface DailyRewardCallback {
        void onSuccess(boolean awarded, String message);
        void onAlreadyClaimed();
        void onUnauthorized();
        void onError(String error);
    }
}