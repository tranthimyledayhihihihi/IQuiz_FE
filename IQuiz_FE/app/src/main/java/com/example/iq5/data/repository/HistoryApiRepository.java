package com.example.iq5.data.repository;

import android.content.Context;
import android.util.Log;

import com.example.iq5.core.network.AchievementApiService;
import com.example.iq5.core.network.ApiClient;
import com.example.iq5.core.network.HistoryApiService;
import com.example.iq5.core.prefs.PrefsManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Repository để xử lý các API calls liên quan đến Quiz History
 */
public class HistoryApiRepository {
    
    private static final String TAG = "HistoryApiRepository";
    private final HistoryApiService apiService;
    private final Context context;
    
    public HistoryApiRepository(Context context) {
        this.context = context.getApplicationContext();
        PrefsManager prefsManager = new PrefsManager(context);
        Retrofit retrofit = ApiClient.getClient(prefsManager);
        this.apiService = ApiClient.createService(retrofit, HistoryApiService.class);
    }
    
    /**
     * Lấy lịch sử làm bài của user
     */
    public void getMyHistory(int pageNumber, int pageSize, final HistoryCallback callback) {
        Log.d(TAG, "📚 Đang gọi API Get My History...");
        
        Call<HistoryApiService.HistoryResponse> call = 
            apiService.getMyHistory(pageNumber, pageSize);
        
        call.enqueue(new Callback<HistoryApiService.HistoryResponse>() {
            @Override
            public void onResponse(Call<HistoryApiService.HistoryResponse> call, 
                                 Response<HistoryApiService.HistoryResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "✅ Get My History thành công! Số kết quả: " + 
                          response.body().getTongSoKetQua());
                    callback.onSuccess(response.body());
                } else if (response.code() == 401) {
                    Log.e(TAG, "❌ Unauthorized - Token hết hạn");
                    callback.onUnauthorized();
                } else {
                    Log.e(TAG, "❌ Get My History lỗi: " + response.code());
                    callback.onError("Không thể lấy lịch sử. Mã lỗi: " + response.code());
                }
            }
            
            @Override
            public void onFailure(Call<HistoryApiService.HistoryResponse> call, Throwable t) {
                Log.e(TAG, "❌ Get My History thất bại: " + t.getMessage());
                callback.onError("Không thể kết nối đến server: " + t.getMessage());
            }
        });
    }
    
    /**
     * Lấy chi tiết một lần làm bài
     */
    public void getHistoryDetail(int attemptId, final HistoryDetailCallback callback) {
        Log.d(TAG, "📖 Đang gọi API Get History Detail...");
        
        Call<HistoryApiService.HistoryDetail> call = apiService.getHistoryDetail(attemptId);
        
        call.enqueue(new Callback<HistoryApiService.HistoryDetail>() {
            @Override
            public void onResponse(Call<HistoryApiService.HistoryDetail> call, 
                                 Response<HistoryApiService.HistoryDetail> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "✅ Get History Detail thành công!");
                    callback.onSuccess(response.body());
                } else if (response.code() == 401) {
                    Log.e(TAG, "❌ Unauthorized - Token hết hạn");
                    callback.onUnauthorized();
                } else if (response.code() == 404) {
                    Log.e(TAG, "❌ Not Found - Không tìm thấy lịch sử");
                    callback.onNotFound();
                } else {
                    Log.e(TAG, "❌ Get History Detail lỗi: " + response.code());
                    callback.onError("Không thể lấy chi tiết lịch sử. Mã lỗi: " + response.code());
                }
            }
            
            @Override
            public void onFailure(Call<HistoryApiService.HistoryDetail> call, Throwable t) {
                Log.e(TAG, "❌ Get History Detail thất bại: " + t.getMessage());
                callback.onError("Không thể kết nối đến server: " + t.getMessage());
            }
        });
    }
    
    /**
     * Lấy thông tin streak từ lịch sử
     */
    public void getStreakFromHistory(final StreakCallback callback) {
        Log.d(TAG, "🔥 Đang gọi API Get Streak From History...");
        
        Call<HistoryApiService.StreakInfo> call = apiService.getStreakFromHistory();
        
        call.enqueue(new Callback<HistoryApiService.StreakInfo>() {
            @Override
            public void onResponse(Call<HistoryApiService.StreakInfo> call, 
                                 Response<HistoryApiService.StreakInfo> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "✅ Get Streak From History thành công! Streak: " + 
                          response.body().getSoNgayLienTiep());
                    callback.onSuccess(response.body());
                } else if (response.code() == 401) {
                    Log.e(TAG, "❌ Unauthorized - Token hết hạn");
                    callback.onUnauthorized();
                } else {
                    Log.e(TAG, "❌ Get Streak From History lỗi: " + response.code());
                    callback.onError("Không thể lấy streak. Mã lỗi: " + response.code());
                }
            }
            
            @Override
            public void onFailure(Call<HistoryApiService.StreakInfo> call, Throwable t) {
                Log.e(TAG, "❌ Get Streak From History thất bại: " + t.getMessage());
                callback.onError("Không thể kết nối đến server: " + t.getMessage());
            }
        });
    }
    
    /**
     * Lấy thành tựu từ lịch sử
     */
    public void getAchievementsFromHistory(final AchievementsCallback callback) {
        Log.d(TAG, "🏆 Đang gọi API Get Achievements From History...");
        
        Call<List<AchievementApiService.Achievement>> call = 
            apiService.getAchievementsFromHistory();
        
        call.enqueue(new Callback<List<AchievementApiService.Achievement>>() {
            @Override
            public void onResponse(Call<List<AchievementApiService.Achievement>> call, 
                                 Response<List<AchievementApiService.Achievement>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "✅ Get Achievements From History thành công! Số thành tựu: " + 
                          response.body().size());
                    callback.onSuccess(response.body());
                } else if (response.code() == 401) {
                    Log.e(TAG, "❌ Unauthorized - Token hết hạn");
                    callback.onUnauthorized();
                } else {
                    Log.e(TAG, "❌ Get Achievements From History lỗi: " + response.code());
                    callback.onError("Không thể lấy thành tựu. Mã lỗi: " + response.code());
                }
            }
            
            @Override
            public void onFailure(Call<List<AchievementApiService.Achievement>> call, Throwable t) {
                Log.e(TAG, "❌ Get Achievements From History thất bại: " + t.getMessage());
                callback.onError("Không thể kết nối đến server: " + t.getMessage());
            }
        });
    }
    
    // Callback interfaces
    public interface HistoryCallback {
        void onSuccess(HistoryApiService.HistoryResponse history);
        void onUnauthorized();
        void onError(String error);
    }
    
    public interface HistoryDetailCallback {
        void onSuccess(HistoryApiService.HistoryDetail detail);
        void onNotFound();
        void onUnauthorized();
        void onError(String error);
    }
    
    public interface StreakCallback {
        void onSuccess(HistoryApiService.StreakInfo streak);
        void onUnauthorized();
        void onError(String error);
    }
    
    public interface AchievementsCallback {
        void onSuccess(List<AchievementApiService.Achievement> achievements);
        void onUnauthorized();
        void onError(String error);
    }
}