package com.example.iq5.core.network;

import com.example.iq5.core.prefs.PrefsManager;
import retrofit2.Retrofit;

public final class ApiServiceFactory {

    private static volatile AuthApiService authService;
    private static volatile QuizApiService quizService;
    private static volatile UserApiService userService;

    private ApiServiceFactory() {}

    public static AuthApiService getAuthService(PrefsManager prefsManager) {
        if (authService == null) {
            synchronized (ApiServiceFactory.class) {
                if (authService == null) {
                    Retrofit retrofit = ApiClient.getClient(prefsManager);
                    authService = ApiClient.createService(retrofit, AuthApiService.class);
                }
            }
        }
        return authService;
    }

    public static QuizApiService getQuizService(PrefsManager prefsManager) {
        if (quizService == null) {
            synchronized (ApiServiceFactory.class) {
                if (quizService == null) {
                    Retrofit retrofit = ApiClient.getClient(prefsManager);
                    quizService = ApiClient.createService(retrofit, QuizApiService.class);
                }
            }
        }
        return quizService;
    }

    public static UserApiService getUserService(PrefsManager prefsManager) {
        if (userService == null) {
            synchronized (ApiServiceFactory.class) {
                if (userService == null) {
                    Retrofit retrofit = ApiClient.getClient(prefsManager);
                    userService = ApiClient.createService(retrofit, UserApiService.class);
                }
            }
        }
        return userService;
    }

    public static void resetServices() {
        authService = null;
        quizService = null;
        userService = null;
    }
}
