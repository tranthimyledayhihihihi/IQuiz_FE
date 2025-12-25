package com.example.iq5.data.api;

import com.example.iq5.data.model.*;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.*;

/**
 * API Service interface cho Quiz Game Backend
 * Base URL: http://localhost:7092 (hoặc IP máy backend)
 */
public interface ApiService {

    // ============================================
    // HOME & HEALTH CHECK
    // ============================================
    
    /**
     * Lấy dữ liệu trang chủ (featured quizzes)
     * Endpoint: GET /api/Home
     */
    @GET("api/Home")
    Call<HomeResponse> getHomeData();

    /**
     * Health check - kiểm tra API có hoạt động không
     * Endpoint: GET /api/Home/HealthCheck
     */
    @GET("api/Home/HealthCheck")
    Call<HealthCheckResponse> healthCheck();

    // ============================================
    // AUTHENTICATION (Account Controller)
    // ============================================
    
    /**
     * Đăng nhập
     * Endpoint: POST /api/Account/login
     */
    @POST("api/Account/login")
    Call<LoginResponseModel> login(@Body LoginRequestModel request);

    /**
     * Đăng ký
     * Endpoint: POST /api/Account/register
     */
    @POST("api/Account/register")
    Call<ApiResponse> register(@Body RegisterRequestModel request);

    /**
     * Đăng xuất
     * Endpoint: POST /api/Account/logout
     */
    @POST("api/Account/logout")
    Call<ApiResponse> logout(@Header("Authorization") String token);

    /**
     * Đổi mật khẩu
     * Endpoint: POST /api/Account/change-password
     */
    @POST("api/Account/change-password")
    Call<ApiResponse> changePassword(
        @Header("Authorization") String token,
        @Body ChangePasswordModel request
    );

    // ============================================
    // USER PROFILE
    // ============================================
    
    /**
     * Lấy thông tin profile của user
     * Endpoint: GET /api/User/profile/{userId}
     */
    @GET("api/User/profile/{userId}")
    Call<UserProfileModel> getUserProfile(
        @Path("userId") int userId,
        @Header("Authorization") String token
    );

    /**
     * Cập nhật profile
     * Endpoint: PUT /api/User/profile
     */
    @PUT("api/User/profile")
    Call<ApiResponse> updateProfile(
        @Header("Authorization") String token,
        @Body UserProfileModel profile
    );

    // ============================================
    // CHƠI QUIZ (Quiz Attempt)
    // ============================================
    
    /**
     * Bắt đầu chơi quiz
     * Endpoint: POST /api/Choi/start
     */
    @POST("api/Choi/start")
    Call<StartQuizResponse> startQuiz(
        @Header("Authorization") String token,
        @Body StartQuizRequest request
    );

    /**
     * Lấy câu hỏi tiếp theo
     * Endpoint: GET /api/Choi/next/{attemptId}
     */
    @GET("api/Choi/next/{attemptId}")
    Call<CauHoiModel> getNextQuestion(
        @Path("attemptId") int attemptId,
        @Header("Authorization") String token
    );

    /**
     * Nộp đáp án
     * Endpoint: POST /api/Choi/submit
     */
    @POST("api/Choi/submit")
    Call<SubmitAnswerResponse> submitAnswer(
        @Header("Authorization") String token,
        @Body AnswerSubmitModel answer
    );

    /**
     * Kết thúc quiz
     * Endpoint: POST /api/Choi/end/{attemptId}
     */
    @POST("api/Choi/end/{attemptId}")
    Call<KetQuaModel> endQuiz(
        @Path("attemptId") int attemptId,
        @Header("Authorization") String token
    );

    // ============================================
    // PROFILE APIs (User/Profile Controller)
    // ============================================
    
    /**
     * Lấy thông tin profile của tôi
     * Endpoint: GET /api/user/profile/me
     */
    @GET("api/user/profile/me")
    Call<UserProfileModel> getMyProfile(@Header("Authorization") String token);

    /**
     * Cập nhật profile
     * Endpoint: PUT /api/user/profile/me
     */
    @PUT("api/user/profile/me")
    Call<ApiResponse> updateMyProfile(
        @Header("Authorization") String token,
        @Body ProfileUpdateModel profile
    );

    /**
     * Cập nhật cài đặt
     * Endpoint: PUT /api/user/profile/settings
     */
    @PUT("api/user/profile/settings")
    Call<ApiResponse> updateSettings(
        @Header("Authorization") String token,
        @Body CaiDatModel settings
    );

    // ============================================
    // RANKING & LEADERBOARD APIs
    // ============================================
    
    /**
     * Lấy bảng xếp hạng
     * Endpoint: GET /api/Ranking/leaderboard
     */
    @GET("api/Ranking/leaderboard")
    Call<LeaderboardResponse> getLeaderboard(
        @Query("type") String type,
        @Query("pageNumber") int pageNumber,
        @Query("pageSize") int pageSize
    );

    /**
     * Lấy thành tựu của tôi
     * Endpoint: GET /api/Ranking/achievements/my
     */

    /**

     * Lấy số người online
     * Endpoint: GET /api/Ranking/online-count
     */
    @GET("api/user/Achievement/me")
    Call<List<AchievementsResponse.Achievement>> getMyAchievements(
            @Header("Authorization") String token
    );

    @GET("api/Ranking/online-count")
    Call<OnlineCountResponse> getOnlineCount();

    // ============================================
    // DAILY QUIZ APIs (Quiz Ngày)
    // ============================================
    
    /**
     * Lấy quiz của ngày hôm nay
     * Endpoint: GET /api/QuizNgay/today
     */
    @GET("api/QuizNgay/today")
    Call<QuizNgayDetailsDto> getTodayQuiz();

    /**
     * Bắt đầu làm quiz ngày
     * Endpoint: POST /api/QuizNgay/start
     */
    @POST("api/QuizNgay/start")
    Call<StartQuizResponse> startTodayQuiz(@Header("Authorization") String token);

    /**
     * Nộp đáp án quiz ngày
     * Endpoint: POST /api/QuizNgay/submit
     */
    @POST("api/QuizNgay/submit")
    Call<SubmitAnswerResponse> submitTodayQuizAnswer(
        @Header("Authorization") String token,
        @Body AnswerSubmitModel answer
    );

    /**
     * Kết thúc quiz ngày
     * Endpoint: POST /api/QuizNgay/end/{attemptId}
     */
    @POST("api/QuizNgay/end/{attemptId}")
    Call<KetQuaModel> endTodayQuiz(
        @Path("attemptId") int attemptId,
        @Header("Authorization") String token
    );

    // ============================================
    // CUSTOM QUIZ APIs (Quiz Tùy Chỉnh)
    // ============================================
    
    /**
     * Lấy danh sách đề xuất của tôi
     * Endpoint: GET /api/QuizTuyChinh/my-submissions
     */
    @GET("api/QuizTuyChinh/my-submissions")
    Call<QuizSubmissionsResponse> getMyQuizSubmissions(
        @Header("Authorization") String token,
        @Query("pageNumber") int pageNumber,
        @Query("pageSize") int pageSize
    );

    /**
     * Gửi đề xuất quiz mới
     * Endpoint: POST /api/QuizTuyChinh/submit
     */
    @POST("api/QuizTuyChinh/submit")
    Call<QuizSubmitResponse> submitCustomQuiz(
        @Header("Authorization") String token,
        @Body QuizSubmissionModel submission
    );

    /**
     * Xóa đề xuất
     * Endpoint: DELETE /api/QuizTuyChinh/{quizId}
     */
    @DELETE("api/QuizTuyChinh/{quizId}")
    Call<ApiResponse> deleteQuizSubmission(
        @Path("quizId") int quizId,
        @Header("Authorization") String token
    );

    /**
     * Lấy chi tiết đề xuất
     * Endpoint: GET /api/QuizTuyChinh/{quizId}
     */
    @GET("api/QuizTuyChinh/{quizId}")
    Call<QuizDetailResponse> getQuizSubmissionDetails(
        @Path("quizId") int quizId,
        @Header("Authorization") String token
    );
// ============================================
// DAILY REWARD APIs
// ============================================

    @POST("api/user/Achievement/daily-reward")
    Call<DailyRewardClaimResponse> claimDailyReward(
            @Header("Authorization") String token
    );
    // ============================================
    // DAILY STREAK
    // ============================================
    @GET("api/user/achievement/streak")
    Call<StreakResponse> getDailyStreak(
            @Header("Authorization") String token
    );

}
