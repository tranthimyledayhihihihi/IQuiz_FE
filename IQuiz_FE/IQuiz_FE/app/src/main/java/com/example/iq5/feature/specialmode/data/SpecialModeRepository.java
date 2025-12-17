package com.example.iq5.feature.specialmode.data;

import android.content.Context;

import com.example.iq5.feature.specialmode.model.ChallengeModesResponse;
import com.example.iq5.feature.specialmode.model.CustomQuizResponse;
import com.example.iq5.feature.specialmode.model.DailyQuizResponse;
import com.example.iq5.feature.specialmode.model.PlayerSearchResponse;
import com.example.iq5.feature.specialmode.model.WrongAnswersResponse;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;

public class SpecialModeRepository {

    private final Context context;
    private final Gson gson = new Gson();

    public SpecialModeRepository(Context context) {
        this.context = context.getApplicationContext();
    }

    private String loadJsonFromAssets(String path) {
        try {
            InputStream is = context.getAssets().open(path);
            int size = is.available();
            byte[] buffer = new byte[size];
            int read = is.read(buffer);
            is.close();
            if (read <= 0) return null;
            return new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public DailyQuizResponse getDailyQuiz() {
        String json = loadJsonFromAssets("specialmode/daily_quiz.json");
        return gson.fromJson(json, DailyQuizResponse.class);
    }

    public WrongAnswersResponse getWrongAnswers() {
        String json = loadJsonFromAssets("specialmode/wrong_answers.json");
        return gson.fromJson(json, WrongAnswersResponse.class);
    }

    public ChallengeModesResponse getChallengeModes() {
        String json = loadJsonFromAssets("specialmode/challenges.json");
        return gson.fromJson(json, ChallengeModesResponse.class);
    }

    public CustomQuizResponse getCustomQuizzes() {
        String json = loadJsonFromAssets("specialmode/custom_quiz.json");
        return gson.fromJson(json, CustomQuizResponse.class);
    }

    public PlayerSearchResponse searchPlayers(String keyword) {
        // Hiện tại chỉ mock, chưa filter theo keyword
        String json = loadJsonFromAssets("specialmode/players_search.json");
        return gson.fromJson(json, PlayerSearchResponse.class);
    }
}
