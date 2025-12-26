package com.example.iq5.core.network;

import com.example.iq5.feature.specialmode.model.WrongAnswersResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface WrongQuestionApiService {

    /**
     * Lấy danh sách câu sai gần đây của user hiện tại
     * BE tự lấy userId từ JWT
     *
     * GET /api/quiz/causai/recent?limit=20
     */
    @GET("api/quiz/causai/recent")
    Call<WrongAnswersResponse> getRecentWrongAnswers(
            @Header("Authorization") String token,
            @Query("limit") int limit
    );
}
