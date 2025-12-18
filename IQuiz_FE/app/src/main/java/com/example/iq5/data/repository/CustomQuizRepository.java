package com.example.iq5.data.repository;

import android.content.Context;
import android.util.Log;

import com.example.iq5.data.api.RetrofitClient;
import com.example.iq5.data.api.ApiService;
import com.example.iq5.data.model.*;
import com.example.iq5.utils.ApiHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repository để quản lý Custom Quiz (Quiz Tùy Chỉnh)
 */
public class CustomQuizRepository {
    
    private static final String TAG = "CustomQuizRepository";
    private final ApiService apiService;
    private final Context context;
    
    public CustomQuizRepository(Context context) {
        this.context = context;
        this.apiService = RetrofitClient.getApiService();
    }
    
    /**
     * Lấy danh sách đề xuất của tôi
     */
    public void getMySubmissionsAsync(int pageNumber, int pageSize, SubmissionsCallback callback) {
        String token = "Bearer " + ApiHelper.getToken(context);
        
        Log.d(TAG, "📋 Đang lấy danh sách đề xuất...");
        
        Call<QuizSubmissionsResponse> call = apiService.getMyQuizSubmissions(token, pageNumber, pageSize);
        call.enqueue(new Callback<QuizSubmissionsResponse>() {
            @Override
            public void onResponse(Call<QuizSubmissionsResponse> call, Response<QuizSubmissionsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "✅ Lấy danh sách đề xuất thành công!");
                    callback.onSuccess(response.body());
                } else if (response.code() == 404) {
                    Log.d(TAG, "⚠️ Không có đề xuất nào");
                    callback.onEmpty();
                } else {
                    Log.e(TAG, "❌ Lỗi lấy danh sách: " + response.code());
                    callback.onError("Không thể lấy danh sách đề xuất. Mã lỗi: " + response.code());
                }
            }
            
            @Override
            public void onFailure(Call<QuizSubmissionsResponse> call, Throwable t) {
                Log.e(TAG, "❌ Lỗi kết nối: " + t.getMessage());
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }
    
    /**
     * Gửi đề xuất quiz mới
     */
    public void submitCustomQuizAsync(QuizSubmissionModel submission, SubmitCallback callback) {
        String token = "Bearer " + ApiHelper.getToken(context);
        
        Log.d(TAG, "📤 Đang gửi đề xuất quiz...");
        
        Call<QuizSubmitResponse> call = apiService.submitCustomQuiz(token, submission);
        call.enqueue(new Callback<QuizSubmitResponse>() {
            @Override
            public void onResponse(Call<QuizSubmitResponse> call, Response<QuizSubmitResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "✅ Gửi đề xuất thành công!");
                    callback.onSuccess(response.body());
                } else {
                    Log.e(TAG, "❌ Lỗi gửi đề xuất: " + response.code());
                    callback.onError("Không thể gửi đề xuất. Mã lỗi: " + response.code());
                }
            }
            
            @Override
            public void onFailure(Call<QuizSubmitResponse> call, Throwable t) {
                Log.e(TAG, "❌ Lỗi kết nối: " + t.getMessage());
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }
    
    /**
     * Xóa đề xuất
     */
    public void deleteSubmissionAsync(int quizId, DeleteCallback callback) {
        String token = "Bearer " + ApiHelper.getToken(context);
        
        Log.d(TAG, "🗑️ Đang xóa đề xuất...");
        
        Call<ApiResponse> call = apiService.deleteQuizSubmission(quizId, token);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "✅ Xóa đề xuất thành công!");
                    callback.onSuccess(response.body().getMessage());
                } else if (response.code() == 404) {
                    Log.e(TAG, "❌ Không tìm thấy đề xuất");
                    callback.onError("Đề xuất không tồn tại hoặc bạn không có quyền xóa");
                } else {
                    Log.e(TAG, "❌ Lỗi xóa đề xuất: " + response.code());
                    callback.onError("Không thể xóa đề xuất. Mã lỗi: " + response.code());
                }
            }
            
            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e(TAG, "❌ Lỗi kết nối: " + t.getMessage());
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }
    
    /**
     * Lấy chi tiết đề xuất
     */
    public void getSubmissionDetailsAsync(int quizId, DetailsCallback callback) {
        String token = "Bearer " + ApiHelper.getToken(context);
        
        Log.d(TAG, "🔍 Đang lấy chi tiết đề xuất...");
        
        Call<QuizDetailResponse> call = apiService.getQuizSubmissionDetails(quizId, token);
        call.enqueue(new Callback<QuizDetailResponse>() {
            @Override
            public void onResponse(Call<QuizDetailResponse> call, Response<QuizDetailResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "✅ Lấy chi tiết thành công!");
                    callback.onSuccess(response.body());
                } else if (response.code() == 404) {
                    Log.e(TAG, "❌ Không tìm thấy đề xuất");
                    callback.onError("Đề xuất không tồn tại hoặc bạn không có quyền xem");
                } else {
                    Log.e(TAG, "❌ Lỗi lấy chi tiết: " + response.code());
                    callback.onError("Không thể lấy chi tiết. Mã lỗi: " + response.code());
                }
            }
            
            @Override
            public void onFailure(Call<QuizDetailResponse> call, Throwable t) {
                Log.e(TAG, "❌ Lỗi kết nối: " + t.getMessage());
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }
    
    // ============================================
    // CALLBACKS
    // ============================================
    
    public interface SubmissionsCallback {
        void onSuccess(QuizSubmissionsResponse response);
        void onEmpty();
        void onError(String error);
    }
    
    public interface SubmitCallback {
        void onSuccess(QuizSubmitResponse response);
        void onError(String error);
    }
    
    public interface DeleteCallback {
        void onSuccess(String message);
        void onError(String error);
    }
    
    public interface DetailsCallback {
        void onSuccess(QuizDetailResponse details);
        void onError(String error);
    }
}
