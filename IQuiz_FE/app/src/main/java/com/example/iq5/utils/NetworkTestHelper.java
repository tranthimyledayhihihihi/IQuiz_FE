package com.example.iq5.utils;

import android.util.Log;
import com.example.iq5.core.network.ApiClient;
import com.example.iq5.core.network.AuthApiService;
import com.example.iq5.core.network.UserApiService;
import com.example.iq5.core.prefs.PrefsManager;
import com.example.iq5.data.model.LoginRequest;
import com.example.iq5.data.model.LoginResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;

public class NetworkTestHelper {
    private static final String TAG = "NetworkTestHelper";

    // Interface để test API cơ bản
    public interface TestApiService {
        @GET("swagger")
        Call<Object> getSwagger();
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
                        // Test thêm API thực tế
                        testRealApis(prefsManager);
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
    
    private static void testRealApis(PrefsManager prefsManager) {
        Log.d(TAG, "🧪 Testing real APIs...");
        
        Retrofit retrofit = ApiClient.getClient(prefsManager);
        
        // Test Auth API
        AuthApiService authService = ApiClient.createService(retrofit, AuthApiService.class);
        
        // Test với thông tin đăng nhập test (không thực sự đăng nhập)
        LoginRequest testLogin = new LoginRequest("test", "test");
        Call<LoginResponse> loginCall = authService.login(testLogin);
        
        loginCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.code() == 401) {
                    Log.d(TAG, "✅ Auth API hoạt động (401 Unauthorized như mong đợi)");
                } else {
                    Log.d(TAG, "📊 Auth API response: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e(TAG, "❌ Auth API test failed: " + t.getMessage());
            }
        });
        
        // Test User Profile API (sẽ fail vì chưa có token)
        UserApiService userService = ApiClient.createService(retrofit, UserApiService.class);
        Call<UserApiService.UserProfile> profileCall = userService.getMyProfile();
        
        profileCall.enqueue(new Callback<UserApiService.UserProfile>() {
            @Override
            public void onResponse(Call<UserApiService.UserProfile> call, Response<UserApiService.UserProfile> response) {
                if (response.code() == 401) {
                    Log.d(TAG, "✅ User API hoạt động (401 Unauthorized như mong đợi)");
                } else {
                    Log.d(TAG, "📊 User API response: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<UserApiService.UserProfile> call, Throwable t) {
                Log.e(TAG, "❌ User API test failed: " + t.getMessage());
            }
        });
    }
}