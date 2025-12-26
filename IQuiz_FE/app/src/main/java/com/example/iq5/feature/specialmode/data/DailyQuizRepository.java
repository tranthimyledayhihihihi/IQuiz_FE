package com.example.iq5.feature.specialmode.data;

import android.content.Context;

import com.example.iq5.feature.specialmode.model.DailyQuizResponse;
import com.google.gson.Gson;

import java.io.InputStream;

/**
 * Repository MOCK cho Quiz mỗi ngày
 * - Dữ liệu local (assets)
 * - Không gọi API
 */
public class DailyQuizRepository {

    private final Context context;
    private final Gson gson = new Gson();

    public DailyQuizRepository(Context context) {
        this.context = context.getApplicationContext();
    }

    private String loadJsonFromAssets(String path) {
        try {
            InputStream is = context.getAssets().open(path);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            return new String(buffer, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public DailyQuizResponse getDailyQuiz() {
        String json = loadJsonFromAssets("specialmode/daily_quiz.json");
        if (json == null) return null;
        return gson.fromJson(json, DailyQuizResponse.class);
    }
}
