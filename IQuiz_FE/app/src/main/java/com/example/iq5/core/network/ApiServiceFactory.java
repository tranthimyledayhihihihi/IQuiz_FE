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
    private static SocialApiService socialService;
    private static DailyQuizApiService dailyQuizService;
    private static AchievementApiService achievementService;
    private static HistoryApiService historyService;
    
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
    
    public static SocialApiService getSocialService(PrefsManager prefsManager) {
        if (socialService == null) {
            Retrofit retrofit = ApiClient.getClient(prefsManager);
            socialService = ApiClient.createService(retrofit, SocialApiService.class);
        }
        return socialService;
    }
    
    public static DailyQuizApiService getDailyQuizService(PrefsManager prefsManager) {
        if (dailyQuizService == null) {
            Retrofit retrofit = ApiClient.getClient(prefsManager);
            dailyQuizService = ApiClient.createService(retrofit, DailyQuizApiService.class);
        }
        return dailyQuizService;
    }
    
    public static AchievementApiService getAchievementService(PrefsManager prefsManager) {
        if (achievementService == null) {
            Retrofit retrofit = ApiClient.getClient(prefsManager);
            achievementService = ApiClient.createService(retrofit, AchievementApiService.class);
        }
        return achievementService;
    }
    
    public static HistoryApiService getHistoryService(PrefsManager prefsManager) {
        if (historyService == null) {
            Retrofit retrofit = ApiClient.getClient(prefsManager);
            historyService = ApiClient.createService(retrofit, HistoryApiService.class);
        }
        return historyService;
    }
    
    /**
     * Reset tất cả services (dùng khi đăng xuất hoặc thay đổi token)
     */
    public static void resetServices() {
        authService = null;
        quizService = null;
        userService = null;
        socialService = null;
        dailyQuizService = null;
        achievementService = null;
        historyService = null;
    }
}