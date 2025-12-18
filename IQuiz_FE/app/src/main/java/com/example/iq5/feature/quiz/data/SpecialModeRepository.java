package com.example.iq5.feature.quiz.data;

import android.content.Context;

// *CẦN THAY THẾ CÁC MODEL CƠ BẢN BẰNG CÁC RESPONSE MODEL WRAPPER*
import com.example.iq5.feature.quiz.model.SelectionScreenResponse;
import com.example.iq5.feature.quiz.model.CurrentQuestionResponse;
import com.example.iq5.feature.quiz.model.HelpOptionsResponse;
import com.example.iq5.feature.quiz.model.QuizReviewResponse;
import com.example.iq5.feature.quiz.model.ErrorResponse; // Cho màn hình báo lỗi

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

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

    // ===============================================
    //               PHẦN HOÀN CHỈNH (KHỚP TÊN FILE GỐC)
    // ===============================================

    // 1. Màn hình Chọn chủ đề + Độ khó
    // SỬA: Dùng file gốc 'selection_screen.json' và trả về Response Wrapper
    public SelectionScreenResponse getSelectionScreenData() {
        // SỬA: Dùng file gốc 'selection_screen.json'
        String json = loadJsonFromAssets("quiz/selection_screen.json");
        return gson.fromJson(json, SelectionScreenResponse.class);
    }

    // 2. Màn hình Câu hỏi và trả lời
    // SỬA: Dùng file gốc 'current_question.json' và trả về Response Wrapper
    public CurrentQuestionResponse getCurrentQuestionData() {
        String json = loadJsonFromAssets("quiz/current_question.json");
        return gson.fromJson(json, CurrentQuestionResponse.class);
    }

    // 3. Màn hình Quyền trợ giúp
    // SỬA: Dùng file gốc 'help_options.json' và trả về Response Wrapper
    public HelpOptionsResponse getLifelineOptions() {
        String json = loadJsonFromAssets("quiz/help_options.json");
        return gson.fromJson(json, HelpOptionsResponse.class);
    }

    // 4. Màn hình Xem lại câu hỏi
    // SỬA: Dùng file gốc 'quiz_review.json' và trả về Response Wrapper
    public QuizReviewResponse getQuizReviewData() {
        String json = loadJsonFromAssets("quiz/quiz_review.json");
        return gson.fromJson(json, QuizReviewResponse.class);
    }

    // 5. Màn hình Đang tải + Báo lỗi (Mock cho trạng thái lỗi mạng)
    // SỬA: Dùng file gốc 'error_network.json' và trả về Response Wrapper
    public ErrorResponse getNetworkErrorMock() {
        String json = loadJsonFromAssets("quiz/error_network.json");
        return gson.fromJson(json, ErrorResponse.class);
    }

    // *Các phương thức cũ trả về List<T> đã bị loại bỏ hoặc thay thế.*
}