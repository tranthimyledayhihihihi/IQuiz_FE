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
 * Repository để quản lý Ranking và Leaderboard
 */
public class RankingRepository {
    
    private static final String TAG = "RankingRepository";
    private final ApiService apiService;
    private final Context context;
    
    public RankingRepository(Context context) {
        this.context = context;
        this.apiService = RetrofitClient.getApiService();
    }
    
    /**
     * Lấy bảng xếp hạng
     * @param type "weekly" hoặc "monthly"
     * @param pageNumber Trang hiện tại (bắt đầu từ 1)
     * @param pageSize Số lượng mỗi trang
     */
    public void getLeaderboardAsync(String type, int pageNumber, int pageSize, LeaderboardCallback callback) {
        Log.d(TAG, "🏆 Đang lấy bảng xếp hạng: " + type);
        
        Call<LeaderboardResponse> call = apiService.getLeaderboard(type, pageNumber, pageSize);
        call.enqueue(new Callback<LeaderboardResponse>() {
            @Override
            public void onResponse(Call<LeaderboardResponse> call, Response<LeaderboardResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "✅ Lấy bảng xếp hạng thành công!");
                    callback.onSuccess(response.body());
                } else {
                    Log.e(TAG, "❌ Lỗi lấy bảng xếp hạng: " + response.code());
                    callback.onError("Không thể lấy bảng xếp hạng. Mã lỗi: " + response.code());
                }
            }
            
            @Override
            public void onFailure(Call<LeaderboardResponse> call, Throwable t) {
                Log.e(TAG, "❌ Lỗi kết nối: " + t.getMessage());
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }
    
    /**
     * Lấy thành tựu của tôi
     */
    public void getMyAchievementsAsync(AchievementsCallback callback) {
        String token = "Bearer " + ApiHelper.getToken(context);
        
        Log.d(TAG, "🏅 Đang lấy thành tựu...");
        
        Call<AchievementsResponse> call = apiService.getMyAchievements(token);
        call.enqueue(new Callback<AchievementsResponse>() {
            @Override
            public void onResponse(Call<AchievementsResponse> call, Response<AchievementsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "✅ Lấy thành tựu thành công!");
                    callback.onSuccess(response.body());
                } else {
                    Log.e(TAG, "❌ Lỗi lấy thành tựu: " + response.code());
                    callback.onError("Không thể lấy thành tựu. Mã lỗi: " + response.code());
                }
            }
            
            @Override
            public void onFailure(Call<AchievementsResponse> call, Throwable t) {
                Log.e(TAG, "❌ Lỗi kết nối: " + t.getMessage());
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }
    
    /**
     * Lấy số người online
     */
    public void getOnlineCountAsync(OnlineCountCallback callback) {
        Log.d(TAG, "👥 Đang lấy số người online...");
        
        Call<OnlineCountResponse> call = apiService.getOnlineCount();
        call.enqueue(new Callback<OnlineCountResponse>() {
            @Override
            public void onResponse(Call<OnlineCountResponse> call, Response<OnlineCountResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "✅ Lấy số người online thành công!");
                    callback.onSuccess(response.body().tongNguoiOnline);
                } else {
                    Log.e(TAG, "❌ Lỗi lấy số người online: " + response.code());
                    callback.onError("Không thể lấy số người online. Mã lỗi: " + response.code());
                }
            }
            
            @Override
            public void onFailure(Call<OnlineCountResponse> call, Throwable t) {
                Log.e(TAG, "❌ Lỗi kết nối: " + t.getMessage());
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }
    
    // ============================================
    // CALLBACKS
    // ============================================
    
    public interface LeaderboardCallback {
        void onSuccess(LeaderboardResponse response);
        void onError(String error);
    }
    
    public interface AchievementsCallback {
        void onSuccess(AchievementsResponse response);
        void onError(String error);
    }
    
    public interface OnlineCountCallback {
        void onSuccess(int count);
        void onError(String error);
    }
}
