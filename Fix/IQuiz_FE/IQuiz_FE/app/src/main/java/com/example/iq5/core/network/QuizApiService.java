package com.example.iq5.core.network;

import com.example.iq5.data.model.QuestionDto;
import com.example.iq5.data.model.TopicDto;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query; // Cần thiết để gửi tham số truy vấn

public interface QuizApiService {

    @GET("chude")
    Call<List<TopicDto>> getTopics();

    @GET("chude/{id}/cauhoi")
    Call<List<QuestionDto>> getQuestionsByTopic(@Path("id") int chuDeId);

    // KHẮC PHỤC LỖI: Thêm @GET và @Query annotations
    @GET("questions/random") // Địa chỉ API để lấy câu hỏi ngẫu nhiên
    Call<ResponseBody> getRandomQuestions(
            @Query("categoryId") int categoryId,   // Tham số sẽ được gửi dưới dạng ?categoryId=...
            @Query("difficultyId") int difficultyId // Tham số sẽ được gửi dưới dạng &difficultyId=...
    );
}