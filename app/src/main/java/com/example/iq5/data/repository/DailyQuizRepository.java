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
 * Repository để quản lý Daily Quiz (Quiz Ngày)
 */
public class DailyQuizRepository {
    
    private static final String TAG = "DailyQuizRepository";
    private final ApiService apiService;
    private final Context context;
    
    public DailyQuizRepository(Context context) {
        this.context = context;
        this.apiService = RetrofitClient.getApiService();
    }
    
    /**
     * Lấy quiz của ngày hôm nay
     */
    public void getTodayQuizAsync(TodayQuizCallback callback) {
        Log.d(TAG, "📅 Đang lấy quiz ngày hôm nay...");
        
        Call<QuizNgayDetailsDto> call = apiService.getTodayQuiz();
        call.enqueue(new Callback<QuizNgayDetailsDto>() {
            @Override
            public void onResponse(Call<QuizNgayDetailsDto> call, Response<QuizNgayDetailsDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "✅ Lấy quiz ngày thành công!");
                    callback.onSuccess(response.body());
                } else if (response.code() == 404) {
                    Log.d(TAG, "⚠️ Chưa có quiz ngày hôm nay");
                    callback.onNoQuizToday();
                } else {
                    Log.e(TAG, "❌ Lỗi lấy quiz ngày: " + response.code());
                    callback.onError("Không thể lấy quiz ngày. Mã lỗi: " + response.code());
                }
            }
            
            @Override
            public void onFailure(Call<QuizNgayDetailsDto> call, Throwable t) {
                Log.e(TAG, "❌ Lỗi kết nối: " + t.getMessage());
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }
    
    /**
     * Bắt đầu làm quiz ngày
     */
    public void startTodayQuizAsync(StartQuizCallback callback) {
        String token = "Bearer " + ApiHelper.getToken(context);
        
        Log.d(TAG, "🎮 Đang bắt đầu quiz ngày...");
        
        Call<StartQuizResponse> call = apiService.startTodayQuiz(token);
        call.enqueue(new Callback<StartQuizResponse>() {
            @Override
            public void onResponse(Call<StartQuizResponse> call, Response<StartQuizResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "✅ Bắt đầu quiz ngày thành công!");
                    callback.onSuccess(response.body());
                } else {
                    Log.e(TAG, "❌ Lỗi bắt đầu quiz ngày: " + response.code());
                    callback.onError("Không thể bắt đầu quiz ngày. Mã lỗi: " + response.code());
                }
            }
            
            @Override
            public void onFailure(Call<StartQuizResponse> call, Throwable t) {
                Log.e(TAG, "❌ Lỗi kết nối: " + t.getMessage());
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }
    
    /**
     * Nộp đáp án quiz ngày
     */
    public void submitTodayAnswerAsync(AnswerSubmitModel answer, SubmitCallback callback) {
        String token = "Bearer " + ApiHelper.getToken(context);
        
        Log.d(TAG, "📤 Đang nộp đáp án quiz ngày...");
        
        Call<SubmitAnswerResponse> call = apiService.submitTodayQuizAnswer(token, answer);
        call.enqueue(new Callback<SubmitAnswerResponse>() {
            @Override
            public void onResponse(Call<SubmitAnswerResponse> call, Response<SubmitAnswerResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "✅ Nộp đáp án thành công!");
                    callback.onSuccess(response.body().isCorrect());
                } else {
                    Log.e(TAG, "❌ Lỗi nộp đáp án: " + response.code());
                    callback.onError("Lỗi nộp đáp án: " + response.code());
                }
            }
            
            @Override
            public void onFailure(Call<SubmitAnswerResponse> call, Throwable t) {
                Log.e(TAG, "❌ Lỗi kết nối: " + t.getMessage());
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }
    
    /**
     * Kết thúc quiz ngày
     */
    public void endTodayQuizAsync(int attemptId, ResultCallback callback) {
        String token = "Bearer " + ApiHelper.getToken(context);
        
        Log.d(TAG, "🏁 Đang kết thúc quiz ngày...");
        
        Call<KetQuaModel> call = apiService.endTodayQuiz(attemptId, token);
        call.enqueue(new Callback<KetQuaModel>() {
            @Override
            public void onResponse(Call<KetQuaModel> call, Response<KetQuaModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "✅ Kết thúc quiz ngày thành công!");
                    callback.onSuccess(response.body());
                } else {
                    Log.e(TAG, "❌ Lỗi kết thúc quiz ngày: " + response.code());
                    callback.onError("Lỗi kết thúc quiz ngày: " + response.code());
                }
            }
            
            @Override
            public void onFailure(Call<KetQuaModel> call, Throwable t) {
                Log.e(TAG, "❌ Lỗi kết nối: " + t.getMessage());
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }
    
    // ============================================
    // CALLBACKS
    // ============================================
    
    public interface TodayQuizCallback {
        void onSuccess(QuizNgayDetailsDto quiz);
        void onNoQuizToday();
        void onError(String error);
    }
    
    public interface StartQuizCallback {
        void onSuccess(StartQuizResponse response);
        void onError(String error);
    }
    
    public interface SubmitCallback {
        void onSuccess(boolean isCorrect);
        void onError(String error);
    }
    
    public interface ResultCallback {
        void onSuccess(KetQuaModel result);
        void onError(String error);
    }
}
