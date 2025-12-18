package com.example.iq5.utils;

import android.util.Log;
import com.example.iq5.core.network.ApiClient;
import com.example.iq5.core.prefs.PrefsManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;

public class NetworkTestHelper {
    private static final String TAG = "NetworkTestHelper";

    // Interface để test API
    public interface TestApiService {
        @GET("swagger")
        Call<Object> getSwagger();
        
        @GET("Account/test")
        Call<Object> testAccount();
    }

    public static void testConnection(PrefsManager prefsManager) {
        try {
            Log.d(TAG, "🔄 Bắt đầu test kết nối đến server...");
            
            Retrofit retrofit = ApiClient.getClient(prefsManager);
            TestApiService testService = ApiClient.createService(retrofit, TestApiService.class);
            
            // Test endpoint đơn giản
            Call<Object> call = testService.getSwagger();
            call.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    Log.d(TAG, "✅ Nhận được phản hồi từ server!");
                    Log.d(TAG, "📊 Response code: " + response.code());
                    Log.d(TAG, "📊 Response message: " + response.message());
                    
                    if (response.isSuccessful()) {
                        Log.d(TAG, "🎉 Kết nối server thành công!");
                    } else {
                        Log.w(TAG, "⚠️ Server phản hồi nhưng có lỗi: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    Log.e(TAG, "❌ Lỗi kết nối: " + t.getClass().getSimpleName());
                    Log.e(TAG, "❌ Chi tiết lỗi: " + t.getMessage());
                    
                    if (t instanceof java.net.ConnectException) {
                        Log.e(TAG, "🔌 Lỗi kết nối - Kiểm tra server có đang chạy không");
                    } else if (t instanceof java.net.UnknownHostException) {
                        Log.e(TAG, "🌐 Lỗi DNS - Kiểm tra URL server");
                    } else if (t instanceof javax.net.ssl.SSLException) {
                        Log.e(TAG, "🔒 Lỗi SSL - Kiểm tra cấu hình HTTPS");
                    }
                    
                    t.printStackTrace();
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "💥 Exception khi khởi tạo: " + e.getMessage());
            e.printStackTrace();
        }
    }
}