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
 * Repository implementation để quản lý Quiz Playing
 */
public class QuizRepositoryImpl {
    
    private static final String TAG = "QuizRepository";
    private final ApiService apiService;
    private final Context context;
    
    public QuizRepositoryImpl(Context context) {
        this.context = context;
        this.apiService = RetrofitClient.getApiService();
    }
    
    // ============================================
    // QUIZ PLAYING
    // ============================================
    
    /**
     * Bắt đầu chơi quiz
     */
    public void startQuizAsync(StartQuizRequest request, StartQuizCallback callback) {
        String token = "Bearer " + ApiHelper.getToken(context);
        
        Log.d(TAG, "🎮 Đang bắt đầu quiz...");
        
        Call<StartQuizResponse> call = apiService.startQuiz(token, request);
        call.enqueue(new Callback<StartQuizResponse>() {
            @Override
            public void onResponse(Call<StartQuizResponse> call, Response<StartQuizResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "✅ Bắt đầu quiz thành công! AttemptID: " + response.body().getAttemptID());
                    callback.onSuccess(response.body());
                } else {
                    Log.e(TAG, "❌ Lỗi bắt đầu quiz: " + response.code());
                    callback.onError("Không thể bắt đầu quiz. Mã lỗi: " + response.code());
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
     * Lấy câu hỏi tiếp theo
     */
    public void getNextQuestionAsync(int attemptId, QuestionCallback callback) {
        String token = "Bearer " + ApiHelper.getToken(context);
        
        Log.d(TAG, "📝 Đang lấy câu hỏi tiếp theo...");
        
        Call<CauHoiModel> call = apiService.getNextQuestion(attemptId, token);
        call.enqueue(new Callback<CauHoiModel>() {
            @Override
            public void onResponse(Call<CauHoiModel> call, Response<CauHoiModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "✅ Lấy câu hỏi thành công!");
                    callback.onSuccess(response.body());
                } else if (response.code() == 404) {
                    Log.d(TAG, "⚠️ Không còn câu hỏi");
                    callback.onNoMoreQuestions();
                } else {
                    Log.e(TAG, "❌ Lỗi lấy câu hỏi: " + response.code());
                    callback.onError("Lỗi lấy câu hỏi: " + response.code());
                }
            }
            
            @Override
            public void onFailure(Call<CauHoiModel> call, Throwable t) {
                Log.e(TAG, "❌ Lỗi kết nối: " + t.getMessage());
                callback.onError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }
    
    /**
     * Nộp đáp án
     */
    public void submitAnswerAsync(AnswerSubmitModel answer, SubmitCallback callback) {
        String token = "Bearer " + ApiHelper.getToken(context);
        
        Log.d(TAG, "📤 Đang nộp đáp án...");
        
        Call<SubmitAnswerResponse> call = apiService.submitAnswer(token, answer);
        call.enqueue(new Callback<SubmitAnswerResponse>() {
            @Override
            public void onResponse(Call<SubmitAnswerResponse> call, Response<SubmitAnswerResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "✅ Nộp đáp án thành công! Đúng: " + response.body().isCorrect());
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
     * Kết thúc quiz
     */
    public void endQuizAsync(int attemptId, ResultCallback callback) {
        String token = "Bearer " + ApiHelper.getToken(context);
        
        Log.d(TAG, "🏁 Đang kết thúc quiz...");
        
        Call<KetQuaModel> call = apiService.endQuiz(attemptId, token);
        call.enqueue(new Callback<KetQuaModel>() {
            @Override
            public void onResponse(Call<KetQuaModel> call, Response<KetQuaModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "✅ Kết thúc quiz thành công! Điểm: " + response.body().getDiem());
                    callback.onSuccess(response.body());
                } else {
                    Log.e(TAG, "❌ Lỗi kết thúc quiz: " + response.code());
                    callback.onError("Lỗi kết thúc quiz: " + response.code());
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
    
    public interface StartQuizCallback {
        void onSuccess(StartQuizResponse response);
        void onError(String error);
    }
    
    public interface QuestionCallback {
        void onSuccess(CauHoiModel question);
        void onNoMoreQuestions();
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
