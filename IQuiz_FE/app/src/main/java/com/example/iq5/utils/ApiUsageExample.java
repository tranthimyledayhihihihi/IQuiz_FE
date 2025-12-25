package com.example.iq5.utils;

import android.util.Log;
import com.example.iq5.core.network.ApiServiceFactory;
import com.example.iq5.core.network.AuthApiService;
import com.example.iq5.core.network.QuizApiService;
import com.example.iq5.core.network.UserApiService;
import com.example.iq5.core.prefs.PrefsManager;
import com.example.iq5.data.model.LoginRequest;
import com.example.iq5.data.model.LoginResponse;
import com.example.iq5.data.model.RegisterRequest;
import com.example.iq5.data.model.ApiResponse;
import com.example.iq5.data.model.UserProfileModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Ví dụ cách sử dụng các API services
 * Copy code này vào Activity của bạn
 */
public class ApiUsageExample {
    private static final String TAG = "ApiUsageExample";
    
    // ===============================================
    // 1. ĐĂNG NHẬP
    // ===============================================
    public static void loginExample(PrefsManager prefsManager, String username, String password) {
        AuthApiService authService = ApiServiceFactory.getAuthService(prefsManager);
        
        LoginRequest request = new LoginRequest(username, password);
        Call<LoginResponse> call = authService.login(request);
        
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();
                    
                    // Lưu token vào SharedPreferences
                    prefsManager.saveAuthToken(loginResponse.getToken());
                    
                    Log.d(TAG, "✅ Đăng nhập thành công!");
                    Log.d(TAG, "👤 Họ tên: " + loginResponse.getHoTen());
                    Log.d(TAG, "🔑 Vai trò: " + loginResponse.getVaiTro());
                    
                    // Chuyển đến MainActivity hoặc HomeActivity
                    
                } else {
                    Log.e(TAG, "❌ Đăng nhập thất bại: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e(TAG, "❌ Lỗi kết nối khi đăng nhập: " + t.getMessage());
            }
        });
    }
    
    // ===============================================
    // 2. ĐĂNG KÝ
    // ===============================================
    public static void registerExample(PrefsManager prefsManager, String username, String email, 
                                     String password, String confirmPassword, String fullName) {
        AuthApiService authService = ApiServiceFactory.getAuthService(prefsManager);
        
        RegisterRequest request = new RegisterRequest(username, email, password, confirmPassword, fullName);
        Call<ApiResponse> call = authService.register(request);
        
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "✅ Đăng ký thành công!");
                    Log.d(TAG, "📝 " + response.body().getMessage());
                    
                    // Chuyển về LoginActivity
                    
                } else {
                    Log.e(TAG, "❌ Đăng ký thất bại: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e(TAG, "❌ Lỗi kết nối khi đăng ký: " + t.getMessage());
            }
        });
    }
    
    // ===============================================
    // 3. LẤY THÔNG TIN PROFILE
    // ===============================================
    public static void getProfileExample(PrefsManager prefsManager) {
        UserApiService userService = ApiServiceFactory.getUserService(prefsManager);
        
        Call<UserProfileModel> call = userService.getMyProfile();
        
        call.enqueue(new Callback<UserProfileModel>() {
            @Override
            public void onResponse(Call<UserProfileModel> call, Response<UserProfileModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserProfileModel profile = response.body();
                    
                    Log.d(TAG, "✅ Lấy profile thành công!");
                    Log.d(TAG, "👤 Tên: " + profile.getHoTen());
                    Log.d(TAG, "📧 Email: " + profile.getEmail());
                    Log.d(TAG, "🔑 Vai trò: " + profile.getVaiTro());
                    
                    // Cập nhật UI với thông tin profile
                    
                } else if (response.code() == 401) {
                    Log.e(TAG, "❌ Token hết hạn, cần đăng nhập lại");
                    // Chuyển về LoginActivity
                } else {
                    Log.e(TAG, "❌ Lỗi lấy profile: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<UserProfileModel> call, Throwable t) {
                Log.e(TAG, "❌ Lỗi kết nối khi lấy profile: " + t.getMessage());
            }
        });
    }
    
    // ===============================================
    // 4. BẮT ĐẦU QUIZ
    // ===============================================
    public static void startQuizExample(PrefsManager prefsManager) {
        QuizApiService quizService = ApiServiceFactory.getQuizService(prefsManager);
        
        // Tạo options cho quiz (có thể lấy từ UI)
        com.example.iq5.data.model.GameStartOptions options = 
            new com.example.iq5.data.model.GameStartOptions(1, 1, 10, "random");
        
        Call<QuizApiService.GameStartResponse> call = quizService.startQuiz(options);
        
        call.enqueue(new Callback<QuizApiService.GameStartResponse>() {
            @Override
            public void onResponse(Call<QuizApiService.GameStartResponse> call, Response<QuizApiService.GameStartResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    QuizApiService.GameStartResponse gameStart = response.body();
                    
                    Log.d(TAG, "✅ Bắt đầu quiz thành công!");
                    Log.d(TAG, "🎯 Attempt ID: " + gameStart.getAttemptID());
                    Log.d(TAG, "❓ Câu hỏi đầu tiên: " + gameStart.getQuestion().getNoiDung());
                    
                    // Chuyển đến QuizActivity với attemptID và question
                    
                } else {
                    Log.e(TAG, "❌ Lỗi bắt đầu quiz: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<QuizApiService.GameStartResponse> call, Throwable t) {
                Log.e(TAG, "❌ Lỗi kết nối khi bắt đầu quiz: " + t.getMessage());
            }
        });
    }
    
    // ===============================================
    // 5. ĐĂNG XUẤT
    // ===============================================
    public static void logoutExample(PrefsManager prefsManager) {
        AuthApiService authService = ApiServiceFactory.getAuthService(prefsManager);
        
        Call<ApiResponse> call = authService.logout();
        
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {

                prefsManager.clearAuthToken();
                ApiServiceFactory.resetServices();
                
                Log.d(TAG, "✅ Đăng xuất thành công!");
                

            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

                prefsManager.clearAuthToken();
                ApiServiceFactory.resetServices();
                
                Log.d(TAG, "✅ Đăng xuất (offline)");
            }
        });
    }
}