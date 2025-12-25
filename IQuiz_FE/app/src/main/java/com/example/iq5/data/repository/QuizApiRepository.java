package com.example.iq5.data.repository;

import android.content.Context;
import android.util.Log;

import com.example.iq5.core.network.ApiClient;
import com.example.iq5.core.network.QuizApiService;
import com.example.iq5.core.prefs.PrefsManager;
import com.example.iq5.data.model.AnswerSubmit;
import com.example.iq5.data.model.GameStartOptions;
import com.example.iq5.data.model.Question;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Repository để xử lý các API calls liên quan đến Quiz
 */
public class QuizApiRepository {
    
    private static final String TAG = "QuizApiRepository";
    private final QuizApiService apiService;
    private final Context context;
    
    public QuizApiRepository(Context context) {
        this.context = context.getApplicationContext();
        PrefsManager prefsManager = new PrefsManager(context);
        Retrofit retrofit = ApiClient.getClient(prefsManager);
        this.apiService = ApiClient.createService(retrofit, QuizApiService.class);
    }
    
    /**
     * Bắt đầu quiz mới
     */
    public void startQuiz(GameStartOptions options, final QuizStartCallback callback) {
        Log.d(TAG, "🎮 Đang gọi API Start Quiz...");
        
        Call<QuizApiService.GameStartResponse> call = apiService.startQuiz(options);
        
        call.enqueue(new Callback<QuizApiService.GameStartResponse>() {
            @Override
            public void onResponse(Call<QuizApiService.GameStartResponse> call, 
                                 Response<QuizApiService.GameStartResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "✅ Start Quiz thành công! AttemptID: " + response.body().getAttemptID());
                    callback.onSuccess(response.body().getAttemptID(), response.body().getQuestion());
                } else {
                    Log.e(TAG, "❌ Start Quiz lỗi: " + response.code());
                    callback.onError("Không thể bắt đầu quiz. Mã lỗi: " + response.code());
                }
            }
            
            @Override
            public void onFailure(Call<QuizApiService.GameStartResponse> call, Throwable t) {
                Log.e(TAG, "❌ Start Quiz thất bại: " + t.getMessage());
                callback.onError("Không thể kết nối đến server: " + t.getMessage());
            }
        });
    }
    
    /**
     * Nộp đáp án
     */
    public void submitAnswer(AnswerSubmit answer, final AnswerSubmitCallback callback) {
        Log.d(TAG, "📝 Đang gọi API Submit Answer...");
        
        Call<QuizApiService.AnswerResponse> call = apiService.submitAnswer(answer);
        
        call.enqueue(new Callback<QuizApiService.AnswerResponse>() {
            @Override
            public void onResponse(Call<QuizApiService.AnswerResponse> call, 
                                 Response<QuizApiService.AnswerResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "✅ Submit Answer thành công! Correct: " + response.body().isCorrect());
                    callback.onSuccess(response.body().isCorrect(), response.body().getMessage());
                } else {
                    Log.e(TAG, "❌ Submit Answer lỗi: " + response.code());
                    callback.onError("Không thể nộp đáp án. Mã lỗi: " + response.code());
                }
            }
            
            @Override
            public void onFailure(Call<QuizApiService.AnswerResponse> call, Throwable t) {
                Log.e(TAG, "❌ Submit Answer thất bại: " + t.getMessage());
                callback.onError("Không thể kết nối đến server: " + t.getMessage());
            }
        });
    }
    
    /**
     * Lấy câu hỏi tiếp theo
     */
    public void getNextQuestion(int attemptId, final NextQuestionCallback callback) {
        Log.d(TAG, "➡️ Đang gọi API Get Next Question...");
        
        Call<Question> call = apiService.getNextQuestion(attemptId);
        
        call.enqueue(new Callback<Question>() {
            @Override
            public void onResponse(Call<Question> call, Response<Question> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "✅ Get Next Question thành công!");
                    callback.onSuccess(response.body());
                } else if (response.code() == 404) {
                    Log.d(TAG, "ℹ️ Không còn câu hỏi nào");
                    callback.onNoMoreQuestions();
                } else {
                    Log.e(TAG, "❌ Get Next Question lỗi: " + response.code());
                    callback.onError("Không thể lấy câu hỏi. Mã lỗi: " + response.code());
                }
            }
            
            @Override
            public void onFailure(Call<Question> call, Throwable t) {
                Log.e(TAG, "❌ Get Next Question thất bại: " + t.getMessage());
                callback.onError("Không thể kết nối đến server: " + t.getMessage());
            }
        });
    }
    
    /**
     * Kết thúc quiz
     */
    public void endQuiz(int attemptId, final QuizEndCallback callback) {
        Log.d(TAG, "🏁 Đang gọi API End Quiz...");
        
        Call<QuizApiService.QuizResult> call = apiService.endQuiz(attemptId);
        
        call.enqueue(new Callback<QuizApiService.QuizResult>() {
            @Override
            public void onResponse(Call<QuizApiService.QuizResult> call, 
                                 Response<QuizApiService.QuizResult> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "✅ End Quiz thành công! Điểm: " + response.body().getDiem());
                    callback.onSuccess(response.body());
                } else {
                    Log.e(TAG, "❌ End Quiz lỗi: " + response.code());
                    callback.onError("Không thể kết thúc quiz. Mã lỗi: " + response.code());
                }
            }
            
            @Override
            public void onFailure(Call<QuizApiService.QuizResult> call, Throwable t) {
                Log.e(TAG, "❌ End Quiz thất bại: " + t.getMessage());
                callback.onError("Không thể kết nối đến server: " + t.getMessage());
            }
        });
    }
    
    /**
     * Lấy danh sách câu hỏi sai để ôn tập
     */
    public void getIncorrectQuestions(final IncorrectQuestionsCallback callback) {
        Log.d(TAG, "📚 Đang gọi API Get Incorrect Questions...");
        
        Call<QuizApiService.IncorrectQuestionsResponse> call = apiService.getIncorrectQuestions();
        
        call.enqueue(new Callback<QuizApiService.IncorrectQuestionsResponse>() {
            @Override
            public void onResponse(Call<QuizApiService.IncorrectQuestionsResponse> call, 
                                 Response<QuizApiService.IncorrectQuestionsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "✅ Get Incorrect Questions thành công! Số câu: " + 
                          response.body().getTongSoCauHoiSai());
                    callback.onSuccess(response.body().getDanhSach(), 
                                     response.body().getTongSoCauHoiSai());
                } else {
                    Log.e(TAG, "❌ Get Incorrect Questions lỗi: " + response.code());
                    callback.onError("Không thể lấy danh sách câu hỏi sai. Mã lỗi: " + response.code());
                }
            }
            
            @Override
            public void onFailure(Call<QuizApiService.IncorrectQuestionsResponse> call, Throwable t) {
                Log.e(TAG, "❌ Get Incorrect Questions thất bại: " + t.getMessage());
                callback.onError("Không thể kết nối đến server: " + t.getMessage());
            }
        });
    }
    
    // Callback interfaces
    public interface QuizStartCallback {
        void onSuccess(int attemptId, Question firstQuestion);
        void onError(String error);
    }
    
    public interface AnswerSubmitCallback {
        void onSuccess(boolean isCorrect, String message);
        void onError(String error);
    }
    
    public interface NextQuestionCallback {
        void onSuccess(Question question);
        void onNoMoreQuestions();
        void onError(String error);
    }
    
    public interface QuizEndCallback {
        void onSuccess(QuizApiService.QuizResult result);
        void onError(String error);
    }
    
    public interface IncorrectQuestionsCallback {
        void onSuccess(java.util.List<Question> questions, int totalCount);
        void onError(String error);
    }
}
