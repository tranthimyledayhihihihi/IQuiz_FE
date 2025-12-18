package com.example.iq5.core.network;

import com.example.iq5.core.prefs.PrefsManager;
import retrofit2.Retrofit;

/**
 * Factory class để tạo các API service instances
 * Sử dụng Singleton pattern để tối ưu performance
 */
public class ApiServiceFactory {
    
    private static AuthApiService authService;
    private static QuizApiService quizService;
    private static UserApiService userService;
    
    public static AuthApiService getAuthService(PrefsManager prefsManager) {
        if (authService == null) {
            Retrofit retrofit = ApiClient.getClient(prefsManager);
            authService = ApiClient.createService(retrofit, AuthApiService.class);
        }
        return authService;
    }
    
    public static QuizApiService getQuizService(PrefsManager prefsManager) {
        if (quizService == null) {
            Retrofit retrofit = ApiClient.getClient(prefsManager);
            quizService = ApiClient.createService(retrofit, QuizApiService.class);
        }
        return quizService;
    }
    
    public static UserApiService getUserService(PrefsManager prefsManager) {
        if (userService == null) {
            Retrofit retrofit = ApiClient.getClient(prefsManager);
            userService = ApiClient.createService(retrofit, UserApiService.class);
        }
        return userService;
    }
    
    /**
     * Reset tất cả services (dùng khi đăng xuất hoặc thay đổi token)
     */
    public static void resetServices() {
        authService = null;
        quizService = null;
        userService = null;
    }
}