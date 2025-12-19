package com.example.iq5.data.repository;

import android.content.Context;
import android.util.Log;

import com.example.iq5.core.network.ApiClient;
import com.example.iq5.core.network.DailyQuizApiService;
import com.example.iq5.core.prefs.PrefsManager;
import com.example.iq5.data.model.AnswerSubmit;
import com.example.iq5.data.model.Question;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Repository để xử lý các API calls liên quan đến Daily Quiz
 */
public class DailyQuizApiRepository {
    
    private static final String TAG = "DailyQuizApiRepository";
    private final DailyQuizApiService apiService;
    private final Context context;
    
    public DailyQuizApiRepository(Context context) {
        this.context = context.getApplicationContext();
        PrefsManager prefsManager = new PrefsManager(context);
        Retrofit retrofit = ApiClient.getClient(prefsManager);
        this.apiService = ApiClient.createService(retrofit, DailyQuizApiService.class);
    }
    
    /**
     * Lấy quiz hàng ngày
     */
    public void getTodayQuiz(final TodayQuizCallback callback) {
        Log.d(TAG, "📅 Đang gọi API Get Today Quiz...");
        
        Call<DailyQuizApiService.DailyQuizDetails> call = apiService.getTodayQuiz();
        
        call.enqueue(new Callback<DailyQuizApiService.DailyQuizDetails>() {
            @Override
            public void onResponse(Call<DailyQuizApiService.DailyQuizDetails> call, 
                                 Response<DailyQuizApiService.DailyQuizDetails> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "✅ Get Today Quiz thành công! ID: " + response.body().getQuizNgayID());
                    callback.onSuccess(response.body());
                } else if (response.code() == 404) {
                    Log.d(TAG, "ℹ️ Chưa có quiz ngày hôm nay");
                    callback.onNoQuizToday();
                } else {
                    Log.e(TAG, "❌ Get Today Quiz lỗi: " + response.code());
                    callback.onError("Không thể lấy quiz hôm nay. Mã lỗi: " + response.code());
                }
            }
            
            @Override
            public void onFailure(Call<DailyQuizApiService.DailyQuizDetails> call, Throwable t) {
                Log.e(TAG, "❌ Get Today Quiz thất bại: " + t.getMessage());
                callback.onError("Không thể kết nối đến server: " + t.getMessage());
            }
        });
    }
    
    /**
     * Bắt đầu quiz hàng ngày
     */
    public void startTodayQuiz(final DailyQuizStartCallback callback) {
        Log.d(TAG, "🎮 Đang gọi API Start Today Quiz...");
        
        Call<DailyQuizApiService.DailyQuizStartResponse> call = apiService.startTodayQuiz();
        
        call.enqueue(new Callback<DailyQuizApiService.DailyQuizStartResponse>() {
            @Override
            public void onResponse(Call<DailyQuizApiService.DailyQuizStartResponse> call, 
                                 Response<DailyQuizApiService.DailyQuizStartResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "✅ Start Today Quiz thành công! AttemptID: " + response.body().getAttemptID());
                    callback.onSuccess(response.body().getAttemptID(), 
                                     response.body().getQuestion(), 
                                     response.body().getMessage());
                } else {
                    Log.e(TAG, "❌ Start Today Quiz lỗi: " + response.code());
                    callback.onError("Không thể bắt đầu quiz hôm nay. Mã lỗi: " + response.code());
                }
            }
            
            @Override
            public void onFailure(Call<DailyQuizApiService.DailyQuizStartResponse> call, Throwable t) {
                Log.e(TAG, "❌ Start Today Quiz thất bại: " + t.getMessage());
                callback.onError("Không thể kết nối đến server: " + t.getMessage());
            }
        });
    }
    
    /**
     * Nộp đáp án quiz hàng ngày
     */
    public void submitDailyAnswer(AnswerSubmit answer, final DailyAnswerSubmitCallback callback) {
        Log.d(TAG, "📝 Đang gọi API Submit Daily Answer...");
        
        Call<DailyQuizApiService.DailyQuizAnswerResponse> call = apiService.submitDailyAnswer(answer);
        
        call.enqueue(new Callback<DailyQuizApiService.DailyQuizAnswerResponse>() {
            @Override
            public void onResponse(Call<DailyQuizApiService.DailyQuizAnswerResponse> call, 
                                 Response<DailyQuizApiService.DailyQuizAnswerResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "✅ Submit Daily Answer thành công! Correct: " + response.body().isCorrect());
                    callback.onSuccess(response.body().isCorrect(), response.body().getMessage());
                } else {
                    Log.e(TAG, "❌ Submit Daily Answer lỗi: " + response.code());
                    callback.onError("Không thể nộp đáp án. Mã lỗi: " + response.code());
                }
            }
            
            @Override
            public void onFailure(Call<DailyQuizApiService.DailyQuizAnswerResponse> call, Throwable t) {
                Log.e(TAG, "❌ Submit Daily Answer thất bại: " + t.getMessage());
                callback.onError("Không thể kết nối đến server: " + t.getMessage());
            }
        });
    }
    
    /**
     * Kết thúc quiz hàng ngày
     */
    public void endTodayQuiz(int attemptId, final DailyQuizEndCallback callback) {
        Log.d(TAG, "🏁 Đang gọi API End Today Quiz...");
        
        Call<DailyQuizApiService.DailyQuizResult> call = apiService.endTodayQuiz(attemptId);
        
        call.enqueue(new Callback<DailyQuizApiService.DailyQuizResult>() {
            @Override
            public void onResponse(Call<DailyQuizApiService.DailyQuizResult> call, 
                                 Response<DailyQuizApiService.DailyQuizResult> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "✅ End Today Quiz thành công! Điểm: " + response.body().getDiem());
                    callback.onSuccess(response.body());
                } else {
                    Log.e(TAG, "❌ End Today Quiz lỗi: " + response.code());
                    callback.onError("Không thể kết thúc quiz hôm nay. Mã lỗi: " + response.code());
                }
            }
            
            @Override
            public void onFailure(Call<DailyQuizApiService.DailyQuizResult> call, Throwable t) {
                Log.e(TAG, "❌ End Today Quiz thất bại: " + t.getMessage());
                callback.onError("Không thể kết nối đến server: " + t.getMessage());
            }
        });
    }
    
    // Callback interfaces
    public interface TodayQuizCallback {
        void onSuccess(DailyQuizApiService.DailyQuizDetails quizDetails);
        void onNoQuizToday();
        void onError(String error);
    }
    
    public interface DailyQuizStartCallback {
        void onSuccess(int attemptId, Question question, String message);
        void onError(String error);
    }
    
    public interface DailyAnswerSubmitCallback {
        void onSuccess(boolean isCorrect, String message);
        void onError(String error);
    }
    
    public interface DailyQuizEndCallback {
        void onSuccess(DailyQuizApiService.DailyQuizResult result);
        void onError(String error);
    }
}