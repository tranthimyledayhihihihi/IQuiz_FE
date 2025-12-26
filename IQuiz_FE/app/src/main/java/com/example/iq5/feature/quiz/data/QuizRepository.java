package com.example.iq5.feature.quiz.data;

import android.content.Context;
import android.util.Log;

import com.example.iq5.data.api.ApiService;
import com.example.iq5.data.api.RetrofitClient;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizRepository {

    private final ApiService apiService;

    public QuizRepository(Context context) {
        this.apiService = RetrofitClient.getApiService();
    }

    public void getFeaturedQuiz(Callback<List<FeaturedQuiz>> callback) {
        Call<List<FeaturedQuiz>> call = apiService.getFeaturedQuiz();
        call.enqueue(new Callback<List<FeaturedQuiz>>() {
            @Override
            public void onResponse(Call<List<FeaturedQuiz>> call, Response<List<FeaturedQuiz>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("API_RESPONSE", "Received: " + response.body().size() + " items");
                    if (!response.body().isEmpty()) {
                        Log.d("API_RESPONSE", "First item: " + new Gson().toJson(response.body().get(0)));
                    }
                    // Forward the response to the original callback
                    callback.onResponse(call, response);
                } else {
                    Log.e("API_RESPONSE", "Response not successful: " + response.code());
                    callback.onResponse(call, response);
                }
            }

            @Override
            public void onFailure(Call<List<FeaturedQuiz>> call, Throwable t) {
                Log.e("API_RESPONSE", "API call failed: " + t.getMessage());
                callback.onFailure(call, t);
            }
        });
    }
}