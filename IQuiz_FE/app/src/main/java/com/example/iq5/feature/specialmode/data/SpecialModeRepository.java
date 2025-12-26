package com.example.iq5.feature.specialmode.data;

import android.content.Context;

import com.example.iq5.core.network.ApiClient;
import com.example.iq5.core.prefs.PrefsManager;
import com.example.iq5.data.api.ApiService;
import com.example.iq5.data.model.ApiResponse;
import com.example.iq5.data.model.CustomQuizResponse;
import com.example.iq5.data.model.QuizSubmissionModel;
import com.example.iq5.data.model.QuizSubmitResponse;
import com.example.iq5.feature.specialmode.model.WrongAnswersResponse;

import retrofit2.Call;

public class SpecialModeRepository {

    private final ApiService apiService;
    private final PrefsManager prefsManager;

    public SpecialModeRepository(Context context) {
        prefsManager = new PrefsManager(context);
        apiService = ApiClient
                .getClient(prefsManager)
                .create(ApiService.class);
    }

    // ================================
    // QUIZ TÙY CHỈNH (API THẬT)
    // ================================

    public Call<CustomQuizResponse> getCustomQuizzes() {
        return apiService.getMyCustomQuizzes(
                "Bearer " + prefsManager.getToken(),
                1,
                20
        );
    }

    public Call<QuizSubmitResponse> submitCustomQuiz(QuizSubmissionModel body) {
        return apiService.submitCustomQuiz(
                "Bearer " + prefsManager.getToken(),
                body
        );
    }



    public Call<ApiResponse> deleteCustomQuiz(int quizId) {
        return apiService.deleteCustomQuiz(
                quizId,
                "Bearer " + prefsManager.getToken()
        );
    }



    public Call<WrongAnswersResponse> getWrongAnswers() {
        return apiService.getWrongQuestions("Bearer " + prefsManager.getToken()
        );
    }

}
