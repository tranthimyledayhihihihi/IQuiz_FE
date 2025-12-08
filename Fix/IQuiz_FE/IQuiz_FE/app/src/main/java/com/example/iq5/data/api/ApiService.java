package com.example.iq5.data.api;

import com.example.iq5.data.model.*;

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
    // QUIZ NGÀY (Daily Quiz)
    // ============================================
    
    /**
     * Lấy Quiz của ngày hôm nay
     * Endpoint: GET /api/QuizNgay/today
     */
    @GET("api/QuizNgay/today")
    Call<QuizNgayResponse> getTodayQuiz(@Header("Authorization") String token);

    /**
     * Bắt đầu làm Quiz Ngày
     * Endpoint: POST /api/QuizNgay/start
     */
    @POST("api/QuizNgay/start")
    Call<StartQuizResponse> startTodayQuiz(@Header("Authorization") String token);

    /**
     * Nộp đáp án Quiz Ngày
     * Endpoint: POST /api/QuizNgay/submit
     */
    @POST("api/QuizNgay/submit")
    Call<SubmitAnswerResponse> submitTodayQuizAnswer(
        @Header("Authorization") String token,
        @Body AnswerSubmitModel answer
    );

    /**
     * Kết thúc Quiz Ngày
     * Endpoint: POST /api/QuizNgay/end/{attemptId}
     */
    @POST("api/QuizNgay/end/{attemptId}")
    Call<KetQuaModel> endTodayQuiz(
        @Path("attemptId") int attemptId,
        @Header("Authorization") String token
    );

    // ============================================
    // QUIZ TÙY CHỈNH (Custom Quiz)
    // ============================================
    
    /**
     * Lấy danh sách đề xuất của tôi
     * Endpoint: GET /api/QuizTuyChinh/my-submissions
     */
    @GET("api/QuizTuyChinh/my-submissions")
    Call<QuizSubmissionsResponse> getMySubmissions(
        @Header("Authorization") String token,
        @Query("pageNumber") int pageNumber,
        @Query("pageSize") int pageSize
    );

    /**
     * Gửi đề xuất Quiz mới
     * Endpoint: POST /api/QuizTuyChinh/submit
     */
    @POST("api/QuizTuyChinh/submit")
    Call<ApiResponse> submitCustomQuiz(
        @Header("Authorization") String token,
        @Body QuizSubmissionRequest request
    );

    /**
     * Xóa đề xuất
     * Endpoint: DELETE /api/QuizTuyChinh/{quizId}
     */
    @DELETE("api/QuizTuyChinh/{quizId}")
    Call<ApiResponse> deleteSubmission(
        @Path("quizId") int quizId,
        @Header("Authorization") String token
    );

    /**
     * Lấy chi tiết đề xuất
     * Endpoint: GET /api/QuizTuyChinh/{quizId}
     */
    @GET("api/QuizTuyChinh/{quizId}")
    Call<QuizDetailResponse> getSubmissionDetails(
        @Path("quizId") int quizId,
        @Header("Authorization") String token
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
}
