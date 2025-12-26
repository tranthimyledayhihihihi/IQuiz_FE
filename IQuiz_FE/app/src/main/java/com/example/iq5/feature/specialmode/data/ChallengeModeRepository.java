package com.example.iq5.feature.specialmode.data;

import android.content.Context;

import com.example.iq5.feature.specialmode.model.ChallengeModesResponse;
import com.google.gson.Gson;

import java.io.InputStream;

public class ChallengeModeRepository {

    private final Context context;
    private final Gson gson = new Gson();

    public ChallengeModeRepository(Context context) {
        this.context = context.getApplicationContext();
    }

    private String loadJson(String path) {
        try {
            InputStream is = context.getAssets().open(path);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            return new String(buffer);
        } catch (Exception e) {
            return null;
        }
    }

    public ChallengeModesResponse getChallengeModes() {
        String json = loadJson("specialmode/challenges.json");
        return gson.fromJson(json, ChallengeModesResponse.class);
    }
}
