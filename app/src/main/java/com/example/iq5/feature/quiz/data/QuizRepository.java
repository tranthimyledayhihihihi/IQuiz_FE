package com.example.iq5.feature.quiz.data;

import android.util.Log;

import com.example.iq5.core.network.ApiClient;
import com.example.iq5.core.network.QuizApiService;
import com.example.iq5.feature.quiz.model.Question;
import com.example.iq5.feature.quiz.data.mapper.QuestionMapper;
import com.example.iq5.core.prefs.PrefsManager;

import org.json.JSONArray;
import org.json.JSONObject; // Cần thiết để đọc JSON Object
import org.json.JSONException; // Cần thiết để bắt lỗi JSON

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class QuizRepository {

    private static final String TAG = "QuizRepository";

    public interface Callback {
        void onSuccess(List<Question> list);
        void onError(String msg);
    }

    private final PrefsManager prefsManager;
    private final QuizApiService quizApiService;

    public QuizRepository(PrefsManager prefsManager) {
        this.prefsManager = prefsManager;
        Retrofit retrofit = ApiClient.getClient(prefsManager);
        this.quizApiService = retrofit.create(QuizApiService.class);
    }

    public void loadQuestions(int categoryId, int difficultyId, Callback callback) {

        Call<ResponseBody> call = quizApiService.getRandomQuestions(categoryId, difficultyId);

        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> res) {

                if (!res.isSuccessful() || res.body() == null) {
                    Log.e(TAG, "Server error: " + res.code());
                    callback.onError("Server error: Code " + res.code());
                    return;
                }

                try {
                    String json = res.body().string();
                    Log.d(TAG, "JSON Response: " + json); // Log response để debug

                    JSONArray arr = null;

                    // Thử phân tích JSON. Giả sử API trả về Array trực tiếp,
                    // hoặc Object chứa Array dưới key "data" hoặc "questions"

                    if (json.trim().startsWith("[")) {
                        // Trường hợp 1: Array trực tiếp
                        arr = new JSONArray(json);
                    } else if (json.trim().startsWith("{")) {
                        // Trường hợp 2: Object bao bọc
                        JSONObject root = new JSONObject(json);

                        // Cần thay đổi key "data" này cho phù hợp với API của bạn
                        if (root.has("data")) {
                            arr = root.getJSONArray("data");
                        } else if (root.has("questions")) {
                            arr = root.getJSONArray("questions");
                        } else {
                            throw new JSONException("Response is an Object but missing Array key.");
                        }
                    } else {
                        throw new JSONException("Response is not valid JSON.");
                    }

                    if (arr == null) {
                        throw new JSONException("Could not find array of questions in response.");
                    }

                    List<Question> list = new ArrayList<>();
                    for (int i = 0; i < arr.length(); i++) {
                        // Đảm bảo QuestionMapper.fromJson xử lý JSON Object an toàn
                        Question q = QuestionMapper.fromJson(arr.getJSONObject(i));
                        if (q != null) list.add(q);
                    }

                    callback.onSuccess(list);

                } catch (JSONException e) {
                    Log.e(TAG, "JSON Parsing Failed: ", e);
                    callback.onError("Parsing error: Invalid JSON format.");
                } catch (Exception e) {
                    Log.e(TAG, "General Parsing error", e);
                    callback.onError("General Parsing error");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "Network error", t);
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }
}