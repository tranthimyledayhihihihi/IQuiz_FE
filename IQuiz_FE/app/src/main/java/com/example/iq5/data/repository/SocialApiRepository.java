package com.example.iq5.data.repository;

import android.content.Context;
import android.util.Log;

import com.example.iq5.core.network.ApiClient;
import com.example.iq5.core.network.SocialApiService;
import com.example.iq5.core.prefs.PrefsManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Repository để xử lý các API calls liên quan đến Social features
 */
public class SocialApiRepository {
    
    private static final String TAG = "SocialApiRepository";
    private final SocialApiService apiService;
    private final Context context;
    
    public SocialApiRepository(Context context) {
        this.context = context.getApplicationContext();
        PrefsManager prefsManager = new PrefsManager(context);
        Retrofit retrofit = ApiClient.getClient(prefsManager);
        this.apiService = ApiClient.createService(retrofit, SocialApiService.class);
    }
    
    /**
     * Lấy bảng xếp hạng
     */
    public void getLeaderboard(String type, int pageNumber, int pageSize, 
                              final LeaderboardCallback callback) {
        Log.d(TAG, "🏅 Đang gọi API Get Leaderboard...");
        
        Call<SocialApiService.LeaderboardResponse> call = 
            apiService.getLeaderboard(type, pageNumber, pageSize);
        
        call.enqueue(new Callback<SocialApiService.LeaderboardResponse>() {
            @Override
            public void onResponse(Call<SocialApiService.LeaderboardResponse> call, 
                                 Response<SocialApiService.LeaderboardResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "✅ Get Leaderboard thành công! Số người: " + 
                          response.body().getTongSoNguoi());
                    callback.onSuccess(response.body());
                } else {
                    Log.e(TAG, "❌ Get Leaderboard lỗi: " + response.code());
                    callback.onError("Không thể lấy bảng xếp hạng. Mã lỗi: " + response.code());
                }
            }
            
            @Override
            public void onFailure(Call<SocialApiService.LeaderboardResponse> call, Throwable t) {
                Log.e(TAG, "❌ Get Leaderboard thất bại: " + t.getMessage());
                callback.onError("Không thể kết nối đến server: " + t.getMessage());
            }
        });
    }
    
    /**
     * Lấy bảng xếp hạng tuần
     */
    public void getWeeklyLeaderboard(int pageNumber, int pageSize, 
                                   final LeaderboardCallback callback) {
        getLeaderboard("weekly", pageNumber, pageSize, callback);
    }
    
    /**
     * Lấy bảng xếp hạng tháng
     */
    public void getMonthlyLeaderboard(int pageNumber, int pageSize, 
                                    final LeaderboardCallback callback) {
        getLeaderboard("monthly", pageNumber, pageSize, callback);
    }
    
    /**
     * Lấy thành tựu của tôi (từ social API)
     */
    public void getMyAchievements(final AchievementsCallback callback) {
        Log.d(TAG, "🏆 Đang gọi API Get My Achievements (Social)...");
        
        Call<SocialApiService.AchievementsResponse> call = apiService.getMyAchievements();
        
        call.enqueue(new Callback<SocialApiService.AchievementsResponse>() {
            @Override
            public void onResponse(Call<SocialApiService.AchievementsResponse> call, 
                                 Response<SocialApiService.AchievementsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "✅ Get My Achievements (Social) thành công!");
                    callback.onSuccess(response.body());
                } else if (response.code() == 401) {
                    Log.e(TAG, "❌ Unauthorized - Token hết hạn");
                    callback.onUnauthorized();
                } else {
                    Log.e(TAG, "❌ Get My Achievements (Social) lỗi: " + response.code());
                    callback.onError("Không thể lấy thành tựu. Mã lỗi: " + response.code());
                }
            }
            
            @Override
            public void onFailure(Call<SocialApiService.AchievementsResponse> call, Throwable t) {
                Log.e(TAG, "❌ Get My Achievements (Social) thất bại: " + t.getMessage());
                callback.onError("Không thể kết nối đến server: " + t.getMessage());
            }
        });
    }
    
    /**
     * Lấy số người đang online
     */
    public void getOnlineCount(final OnlineCountCallback callback) {
        Log.d(TAG, "👥 Đang gọi API Get Online Count...");
        
        Call<SocialApiService.OnlineCountResponse> call = apiService.getOnlineCount();
        
        call.enqueue(new Callback<SocialApiService.OnlineCountResponse>() {
            @Override
            public void onResponse(Call<SocialApiService.OnlineCountResponse> call, 
                                 Response<SocialApiService.OnlineCountResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "✅ Get Online Count thành công! Online: " + 
                          response.body().getTongNguoiOnline());
                    callback.onSuccess(response.body().getTongNguoiOnline());
                } else {
                    Log.e(TAG, "❌ Get Online Count lỗi: " + response.code());
                    callback.onError("Không thể lấy số người online. Mã lỗi: " + response.code());
                }
            }
            
            @Override
            public void onFailure(Call<SocialApiService.OnlineCountResponse> call, Throwable t) {
                Log.e(TAG, "❌ Get Online Count thất bại: " + t.getMessage());
                callback.onError("Không thể kết nối đến server: " + t.getMessage());
            }
        });
    }
    
    // Callback interfaces
    public interface LeaderboardCallback {
        void onSuccess(SocialApiService.LeaderboardResponse leaderboard);
        void onError(String error);
    }
    
    public interface AchievementsCallback {
        void onSuccess(SocialApiService.AchievementsResponse achievements);
        void onUnauthorized();
        void onError(String error);
    }
    
    public interface OnlineCountCallback {
        void onSuccess(int onlineCount);
        void onError(String error);
    }
}