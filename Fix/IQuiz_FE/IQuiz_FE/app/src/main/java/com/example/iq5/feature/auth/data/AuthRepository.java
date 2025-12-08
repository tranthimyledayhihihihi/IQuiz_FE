package com.example.iq5.feature.auth.data;

import android.content.Context;
import android.util.Log;

import com.example.iq5.data.api.ApiService;
import com.example.iq5.data.api.RetrofitClient;
import com.example.iq5.feature.auth.model.HomeResponse;
import com.example.iq5.feature.auth.model.LoginResponse;
import com.example.iq5.feature.auth.model.ProfileResponse;
import com.example.iq5.feature.auth.model.RegisterResponse;
import com.example.iq5.feature.auth.model.SettingsResponse;
import com.example.iq5.feature.auth.model.SplashResponse;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {

    private static final String TAG = "AuthRepository";
    private final Context context;
    private final Gson gson = new Gson();
    private final ApiService apiService;

    public AuthRepository(Context context) {
        this.context = context.getApplicationContext();
        this.apiService = RetrofitClient.getApiService();
    }

    private String loadJsonFromAssets(String path) {
        try {
            InputStream is = context.getAssets().open(path);
            int size = is.available();
            byte[] buffer = new byte[size];
            int read = is.read(buffer);
            is.close();
            if (read <= 0) return null;
            return new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public SplashResponse getSplashData() {
        String json = loadJsonFromAssets("auth/splash.json");
        return gson.fromJson(json, SplashResponse.class);
    }

    public LoginResponse getLoginData() {
        String json = loadJsonFromAssets("auth/login.json");
        return gson.fromJson(json, LoginResponse.class);
    }

    public RegisterResponse getRegisterData() {
        String json = loadJsonFromAssets("auth/register.json");
        return gson.fromJson(json, RegisterResponse.class);
    }

    public ProfileResponse getProfileData() {
        String json = loadJsonFromAssets("auth/profile.json");
        return gson.fromJson(json, ProfileResponse.class);
    }

    public SettingsResponse getSettingsData() {
        String json = loadJsonFromAssets("auth/settings.json");
        return gson.fromJson(json, SettingsResponse.class);
    }

    /**
     * ⚠️ DEPRECATED: Đọc từ JSON file (fallback)
     * Sử dụng getHomeDataAsync() để call API thay thế
     */
    @Deprecated
    public HomeResponse getHomeData() {
        Log.w(TAG, "⚠️ Đang dùng fallback JSON. Nên dùng getHomeDataAsync() để call API");
        String json = loadJsonFromAssets("auth/home.json");
        return gson.fromJson(json, HomeResponse.class);
    }

    /**
     * ✅ MỚI: Lấy dữ liệu Home từ API (Async)
     * Call API từ backend port 7092
     */
    public void getHomeDataAsync(final HomeDataCallback callback) {
        Log.d(TAG, "🌐 Đang gọi API Home từ backend...");
        
        Call<com.example.iq5.data.model.HomeResponse> call = apiService.getHomeData();
        
        call.enqueue(new Callback<com.example.iq5.data.model.HomeResponse>() {
            @Override
            public void onResponse(Call<com.example.iq5.data.model.HomeResponse> call, 
                                 Response<com.example.iq5.data.model.HomeResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "✅ API Home thành công!");
                    
                    // Convert từ API model sang local model
                    com.example.iq5.data.model.HomeResponse apiResponse = response.body();
                    HomeResponse localResponse = convertToLocalHomeResponse(apiResponse);
                    
                    callback.onSuccess(localResponse);
                } else {
                    Log.e(TAG, "❌ API Home lỗi: " + response.code());
                    // ⚠️ KHÔNG FALLBACK - Hiển thị lỗi
                    HomeResponse errorResponse = new HomeResponse();
                    errorResponse.welcomeMessage = "⚠️ Lỗi API: " + response.code();
                    errorResponse.featuredQuizzes = new java.util.ArrayList<>();
                    callback.onSuccess(errorResponse);
                }
            }

            @Override
            public void onFailure(Call<com.example.iq5.data.model.HomeResponse> call, Throwable t) {
                Log.e(TAG, "❌ API Home thất bại: " + t.getMessage());
                // ⚠️ KHÔNG FALLBACK - BẮT BUỘC PHẢI CÓ API
                // Trả về dữ liệu rỗng hoặc báo lỗi
                HomeResponse emptyResponse = new HomeResponse();
                emptyResponse.welcomeMessage = "⚠️ Không thể kết nối API";
                emptyResponse.featuredQuizzes = new java.util.ArrayList<>();
                callback.onSuccess(emptyResponse);
            }
        });
    }

    /**
     * Convert từ API HomeResponse sang local HomeResponse
     */
    private HomeResponse convertToLocalHomeResponse(com.example.iq5.data.model.HomeResponse apiResponse) {
        HomeResponse local = new HomeResponse();
        local.welcomeMessage = apiResponse.getWelcomeMessage();
        
        if (apiResponse.getFeaturedQuizzes() != null) {
            local.featuredQuizzes = new java.util.ArrayList<>();
            for (com.example.iq5.data.model.HomeResponse.FeaturedQuiz apiQuiz : apiResponse.getFeaturedQuizzes()) {
                HomeResponse.QuizItem localQuiz = new HomeResponse.QuizItem();
                localQuiz.id = apiQuiz.getId();
                localQuiz.title = apiQuiz.getTitle();
                localQuiz.difficulty = apiQuiz.getDifficulty();
                local.featuredQuizzes.add(localQuiz);
            }
        }
        
        return local;
    }

    /**
     * ✅ MỚI: Login với API
     * Call API POST /api/Account/login
     */
    public void loginAsync(String username, String password, final LoginCallback callback) {
        Log.d(TAG, "🔐 Đang gọi API Login...");
        
        com.example.iq5.data.model.LoginRequestModel request = 
            new com.example.iq5.data.model.LoginRequestModel(username, password);
        
        Call<com.example.iq5.data.model.LoginResponseModel> call = apiService.login(request);
        
        call.enqueue(new Callback<com.example.iq5.data.model.LoginResponseModel>() {
            @Override
            public void onResponse(Call<com.example.iq5.data.model.LoginResponseModel> call, 
                                 Response<com.example.iq5.data.model.LoginResponseModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "✅ API Login thành công!");
                    
                    com.example.iq5.data.model.LoginResponseModel apiResponse = response.body();
                    
                    // Lưu token vào SharedPreferences
                    com.example.iq5.utils.ApiHelper.saveToken(context, apiResponse.getToken());
                    
                    callback.onSuccess(
                        apiResponse.getToken(),
                        apiResponse.getHoTen(),
                        apiResponse.getVaiTro()
                    );
                } else {
                    Log.e(TAG, "❌ API Login lỗi: " + response.code());
                    String errorMsg = "Đăng nhập thất bại. Mã lỗi: " + response.code();
                    
                    // Parse error message từ response body nếu có
                    try {
                        if (response.errorBody() != null) {
                            errorMsg = response.errorBody().string();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    
                    callback.onError(errorMsg);
                }
            }

            @Override
            public void onFailure(Call<com.example.iq5.data.model.LoginResponseModel> call, Throwable t) {
                Log.e(TAG, "❌ API Login thất bại: " + t.getMessage());
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    /**
     * Callback interface cho async API call
     */
    public interface HomeDataCallback {
        void onSuccess(HomeResponse data);
    }
    
    /**
     * ✅ MỚI: Register với API
     * Call API POST /api/Account/register
     */
    public void registerAsync(String username, String hoTen, String email, String password, final RegisterCallback callback) {
        Log.d(TAG, "📝 Đang gọi API Register...");
        
        com.example.iq5.data.model.RegisterRequestModel request = 
            new com.example.iq5.data.model.RegisterRequestModel(username, email, password, password, hoTen);
        
        Call<com.example.iq5.data.model.ApiResponse> call = apiService.register(request);
        
        call.enqueue(new Callback<com.example.iq5.data.model.ApiResponse>() {
            @Override
            public void onResponse(Call<com.example.iq5.data.model.ApiResponse> call, 
                                 Response<com.example.iq5.data.model.ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "✅ API Register thành công!");
                    callback.onSuccess(response.body().getMessage());
                } else if (response.code() == 409) {
                    // Conflict - Username hoặc Email đã tồn tại
                    Log.e(TAG, "❌ API Register lỗi: 409 Conflict");
                    callback.onError("Tên đăng nhập hoặc Email đã được sử dụng");
                } else {
                    Log.e(TAG, "❌ API Register lỗi: " + response.code());
                    String errorMsg = "Đăng ký thất bại. Mã lỗi: " + response.code();
                    
                    try {
                        if (response.errorBody() != null) {
                            errorMsg = response.errorBody().string();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    
                    callback.onError(errorMsg);
                }
            }

            @Override
            public void onFailure(Call<com.example.iq5.data.model.ApiResponse> call, Throwable t) {
                Log.e(TAG, "❌ API Register thất bại: " + t.getMessage());
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    /**
     * Callback interface cho Login API
     */
    public interface LoginCallback {
        void onSuccess(String token, String hoTen, String vaiTro);
        void onError(String error);
    }
    
    /**
     * Callback interface cho Register API
     */
    public interface RegisterCallback {
        void onSuccess(String message);
        void onError(String error);
    }
}
