package com.example.iq5.data.repository;

import android.util.Log;

import com.example.iq5.data.api.ApiService;
import com.example.iq5.data.api.RetrofitClient;
import com.example.iq5.data.model.HomeResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repository để lấy dữ liệu Home từ API
 */
public class HomeRepository {
    
    private static final String TAG = "HomeRepository";
    private final ApiService apiService;

    public HomeRepository() {
        this.apiService = RetrofitClient.getApiService();
    }

    /**
     * Lấy dữ liệu trang chủ từ API
     * @param callback Callback để xử lý kết quả
     */
    public void getHomeData(final HomeDataCallback callback) {
        Call<HomeResponse> call = apiService.getHomeData();
        
        call.enqueue(new Callback<HomeResponse>() {
            @Override
            public void onResponse(Call<HomeResponse> call, Response<HomeResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "✅ Lấy dữ liệu Home thành công");
                    callback.onSuccess(response.body());
                } else {
                    Log.e(TAG, "❌ Lỗi response: " + response.code());
                    callback.onError("Lỗi: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<HomeResponse> call, Throwable t) {
                Log.e(TAG, "❌ Lỗi kết nối: " + t.getMessage());
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    /**
     * Interface callback cho việc lấy dữ liệu Home
     */
    public interface HomeDataCallback {
        void onSuccess(HomeResponse data);
        void onError(String error);
    }
}
