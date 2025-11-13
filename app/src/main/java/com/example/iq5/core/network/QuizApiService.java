package com.example.iq5.core.network;

import com.example.iq5.data.model.QuestionDto;
import com.example.iq5.data.model.TopicDto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface QuizApiService {
    @GET("chude")
    Call<List<TopicDto>> getTopics();

    @GET("chude/{id}/cauhoi")
    Call<List<QuestionDto>> getQuestionsByTopic(@Path("id") int chuDeId);
}