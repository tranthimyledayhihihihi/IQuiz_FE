package com.example.iq5.core.network;

import com.example.iq5.core.prefs.PrefsManager;

import retrofit2.Retrofit;

/**
 * Factory tạo các API Service (Singleton)
 * Dùng chung ApiClient + BuildConfig.BASE_URL
 */
public final class ApiServiceFactory {

    private static volatile AuthApiService authService;
    private static volatile QuizApiService quizService;
    private static volatile UserApiService userService;
    private static volatile SocialApiService socialService;
    private static volatile DailyQuizApiService dailyQuizService;
    private static volatile AchievementApiService achievementService;
    private static volatile HistoryApiService historyService;
    private static volatile WrongQuestionApiService wrongQuestionService;
    private static volatile CustomQuizApiService customQuizService;
    private static volatile PvPApiService pvpService;
    private static volatile DailyRewardApiService dailyRewardService;

    private ApiServiceFactory() {
        // no instance
    }

    /* ===================== AUTH ===================== */
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

    /* ===================== QUIZ ===================== */
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

    /* ===================== USER ===================== */
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

    /* ===================== SOCIAL ===================== */
    public static SocialApiService getSocialService(PrefsManager prefsManager) {
        if (socialService == null) {
            synchronized (ApiServiceFactory.class) {
                if (socialService == null) {
                    Retrofit retrofit = ApiClient.getClient(prefsManager);
                    socialService = ApiClient.createService(retrofit, SocialApiService.class);
                }
            }
        }
        return socialService;
    }

    /* ===================== DAILY QUIZ ===================== */
    public static DailyQuizApiService getDailyQuizService(PrefsManager prefsManager) {
        if (dailyQuizService == null) {
            synchronized (ApiServiceFactory.class) {
                if (dailyQuizService == null) {
                    Retrofit retrofit = ApiClient.getClient(prefsManager);
                    dailyQuizService = ApiClient.createService(retrofit, DailyQuizApiService.class);
                }
            }
        }
        return dailyQuizService;
    }

    /* ===================== ACHIEVEMENT ===================== */
    public static AchievementApiService getAchievementService(PrefsManager prefsManager) {
        if (achievementService == null) {
            synchronized (ApiServiceFactory.class) {
                if (achievementService == null) {
                    Retrofit retrofit = ApiClient.getClient(prefsManager);
                    achievementService = ApiClient.createService(retrofit, AchievementApiService.class);
                }
            }
        }
        return achievementService;
    }

    /* ===================== HISTORY ===================== */
    public static HistoryApiService getHistoryService(PrefsManager prefsManager) {
        if (historyService == null) {
            synchronized (ApiServiceFactory.class) {
                if (historyService == null) {
                    Retrofit retrofit = ApiClient.getClient(prefsManager);
                    historyService = ApiClient.createService(retrofit, HistoryApiService.class);
                }
            }
        }
        return historyService;
    }

    /* ===================== WRONG QUESTION ===================== */
    public static WrongQuestionApiService getWrongQuestionService(PrefsManager prefsManager) {
        if (wrongQuestionService == null) {
            synchronized (ApiServiceFactory.class) {
                if (wrongQuestionService == null) {
                    Retrofit retrofit = ApiClient.getClient(prefsManager);
                    wrongQuestionService = ApiClient.createService(retrofit, WrongQuestionApiService.class);
                }
            }
        }
        return wrongQuestionService;
    }

    /* ===================== CUSTOM QUIZ ===================== */
    public static CustomQuizApiService getCustomQuizService(PrefsManager prefsManager) {
        if (customQuizService == null) {
            synchronized (ApiServiceFactory.class) {
                if (customQuizService == null) {
                    Retrofit retrofit = ApiClient.getClient(prefsManager);
                    customQuizService = ApiClient.createService(retrofit, CustomQuizApiService.class);
                }
            }
        }
        return customQuizService;
    }

    /* ===================== PVP ===================== */
    public static PvPApiService getPvPService(PrefsManager prefsManager) {
        if (pvpService == null) {
            synchronized (ApiServiceFactory.class) {
                if (pvpService == null) {
                    Retrofit retrofit = ApiClient.getClient(prefsManager);
                    pvpService = ApiClient.createService(retrofit, PvPApiService.class);
                }
            }
        }
        return pvpService;
    }

    /* ===================== DAILY REWARD ===================== */
    public static DailyRewardApiService getDailyRewardService(PrefsManager prefsManager) {
        if (dailyRewardService == null) {
            synchronized (ApiServiceFactory.class) {
                if (dailyRewardService == null) {
                    Retrofit retrofit = ApiClient.getClient(prefsManager);
                    dailyRewardService = ApiClient.createService(retrofit, DailyRewardApiService.class);
                }
            }
        }
        return dailyRewardService;
    }

    /* ===================== RESET ===================== */
    /**
     * Reset tất cả service khi logout / đổi token / đổi môi trường
     */
    public static void resetServices() {
        authService = null;
        quizService = null;
        userService = null;
        socialService = null;
        dailyQuizService = null;
        achievementService = null;
        historyService = null;
        wrongQuestionService = null;
        customQuizService = null;
        pvpService = null;
        dailyRewardService = null;

        ApiClient.reset(); // 🔥 reset luôn Retrofit
    }
}
