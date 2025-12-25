package com.example.iq5.feature.multiplayer.data.api;

import android.content.Context;
import android.util.Log;
import com.example.iq5.feature.multiplayer.data.models.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApiService {
    private static final String TAG = "ApiService";
    private static ApiService instance;

    private final OkHttpClient client;
    private final Gson gson;
    private final String baseUrl;

    private ApiService(Context context) {
        ApiClient apiClient = ApiClient.getInstance(context);
        this.client = apiClient.getClient();
        this.gson = apiClient.getGson();
        this.baseUrl = ApiEndpoints.BASE_URL;
    }

    public static synchronized ApiService getInstance(Context context) {
        if (instance == null) {
            instance = new ApiService(context);
        }
        return instance;
    }

    // ===============================
    // CALLBACK INTERFACES
    // ===============================
    public interface ApiCallback<T> {
        void onSuccess(T result);
        void onError(String error);
    }

    // ===============================
    // GET ONLINE COUNT
    // ===============================
    public void getOnlineCount(ApiCallback<Integer> callback) {
        String url = baseUrl + ApiEndpoints.ONLINE_COUNT;

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "getOnlineCount failed: " + e.getMessage());
                callback.onError(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String body = response.body().string();
                    try {
                        Map<String, Object> map = gson.fromJson(body, Map.class);
                        int count = ((Double) map.get("onlineUsers")).intValue();
                        callback.onSuccess(count);
                    } catch (Exception e) {
                        callback.onError("Parse error: " + e.getMessage());
                    }
                } else {
                    callback.onError("Error: " + response.code());
                }
            }
        });
    }

    // ===============================
    // GET MATCH INFO
    // ===============================
    public void getMatchInfo(String matchCode, ApiCallback<MatchData> callback) {
        String url = baseUrl + ApiEndpoints.GET_MATCH_INFO.replace("{matchCode}", matchCode);

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "getMatchInfo failed: " + e.getMessage());
                callback.onError(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String body = response.body().string();
                    try {
                        MatchData matchData = gson.fromJson(body, MatchData.class);
                        callback.onSuccess(matchData);
                    } catch (Exception e) {
                        callback.onError("Parse error: " + e.getMessage());
                    }
                } else {
                    callback.onError("Error: " + response.code());
                }
            }
        });
    }

    // ===============================
    // GET QUESTIONS FOR MATCH
    // ===============================
    public void getQuestionsForMatch(String matchCode, ApiCallback<List<Question>> callback) {
        String url = baseUrl + ApiEndpoints.GET_QUESTIONS.replace("{matchCode}", matchCode);

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "getQuestionsForMatch failed: " + e.getMessage());
                callback.onError(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String body = response.body().string();
                    try {
                        Type listType = new TypeToken<List<Question>>(){}.getType();
                        List<Question> questions = gson.fromJson(body, listType);
                        callback.onSuccess(questions);
                    } catch (Exception e) {
                        callback.onError("Parse error: " + e.getMessage());
                    }
                } else {
                    callback.onError("Error: " + response.code());
                }
            }
        });
    }

    // ===============================
    // SUBMIT ANSWER (REST API)
    // ===============================
    public void submitAnswer(String matchCode, int questionId, String answer, ApiCallback<AnswerSubmission> callback) {
        String url = baseUrl + ApiEndpoints.SUBMIT_ANSWER;

        Map<String, Object> data = new HashMap<>();
        data.put("matchCode", matchCode);
        data.put("questionId", questionId);
        data.put("selectedAnswer", answer);

        String json = gson.toJson(data);
        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "submitAnswer failed: " + e.getMessage());
                callback.onError(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    try {
                        AnswerSubmission result = gson.fromJson(responseBody, AnswerSubmission.class);
                        callback.onSuccess(result);
                    } catch (Exception e) {
                        callback.onError("Parse error: " + e.getMessage());
                    }
                } else {
                    callback.onError("Error: " + response.code());
                }
            }
        });
    }

    // ===============================
    // END MATCH
    // ===============================
    public void endMatch(String matchCode, ApiCallback<GameResult> callback) {
        String url = baseUrl + ApiEndpoints.END_MATCH.replace("{matchCode}", matchCode);

        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create("", null))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "endMatch failed: " + e.getMessage());
                callback.onError(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String body = response.body().string();
                    try {
                        GameResult result = gson.fromJson(body, GameResult.class);
                        callback.onSuccess(result);
                    } catch (Exception e) {
                        callback.onError("Parse error: " + e.getMessage());
                    }
                } else {
                    callback.onError("Error: " + response.code());
                }
            }
        });
    }

    // ===============================
    // LOGIN
    // ===============================
    public void login(String username, String password, ApiCallback<Map<String, String>> callback) {
        String url = baseUrl + ApiEndpoints.LOGIN;

        Map<String, String> data = new HashMap<>();
        data.put("username", username);
        data.put("password", password);

        String json = gson.toJson(data);
        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "login failed: " + e.getMessage());
                callback.onError(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    try {
                        Type type = new TypeToken<Map<String, String>>(){}.getType();
                        Map<String, String> result = gson.fromJson(responseBody, type);
                        callback.onSuccess(result);
                    } catch (Exception e) {
                        callback.onError("Parse error: " + e.getMessage());
                    }
                } else {
                    callback.onError("Login failed: " + response.code());
                }
            }
        });
    }
}