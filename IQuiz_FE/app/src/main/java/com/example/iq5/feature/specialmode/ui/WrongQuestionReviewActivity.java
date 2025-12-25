package com.example.iq5.feature.specialmode.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iq5.R;
import com.example.iq5.feature.quiz.ui.ApiQuizActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Activity để xem lại các câu hỏi đã trả lời sai
 */
public class WrongQuestionReviewActivity extends AppCompatActivity {

    private static final String TAG = "WrongQuestionReview";
    
    private LinearLayout layoutQuestions;
    private TextView tvTitle;
    private Button btnClearHistory;
    private Button btnBack;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        createLayout();
        loadWrongQuestions();
    }
    
    private void createLayout() {
        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setPadding(32, 32, 32, 32);
        
        // Title
        tvTitle = new TextView(this);
        tvTitle.setText("🔍 XEM LẠI CÂU SAI");
        tvTitle.setTextSize(20);
        tvTitle.setPadding(0, 0, 0, 16);
        mainLayout.addView(tvTitle);
        
        // Buttons
        LinearLayout buttonLayout = new LinearLayout(this);
        buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
        
        btnBack = new Button(this);
        btnBack.setText("← Quay lại");
        btnBack.setOnClickListener(v -> finish());
        buttonLayout.addView(btnBack);
        
        btnClearHistory = new Button(this);
        btnClearHistory.setText("🗑️ Xóa lịch sử");
        btnClearHistory.setOnClickListener(v -> clearWrongQuestions());
        buttonLayout.addView(btnClearHistory);
        
        mainLayout.addView(buttonLayout);
        
        // Questions container
        layoutQuestions = new LinearLayout(this);
        layoutQuestions.setOrientation(LinearLayout.VERTICAL);
        mainLayout.addView(layoutQuestions);
        
        setContentView(mainLayout);
    }
    
    private void loadWrongQuestions() {
        try {
            android.content.SharedPreferences prefs = getSharedPreferences("wrong_questions", MODE_PRIVATE);
            String wrongQuestionsJson = prefs.getString("wrong_questions_list", "[]");
            
            com.google.gson.Gson gson = new com.google.gson.Gson();
            java.lang.reflect.Type listType = new com.google.gson.reflect.TypeToken<List<ApiQuizActivity.WrongQuestionData>>(){}.getType();
            List<ApiQuizActivity.WrongQuestionData> wrongQuestions = gson.fromJson(wrongQuestionsJson, listType);
            
            if (wrongQuestions == null || wrongQuestions.isEmpty()) {
                showEmptyState();
                return;
            }
            
            tvTitle.setText(String.format("🔍 XEM LẠI CÂU SAI (%d câu)", wrongQuestions.size()));
            
            // Display wrong questions
            for (int i = 0; i < wrongQuestions.size(); i++) {
                ApiQuizActivity.WrongQuestionData wrongData = wrongQuestions.get(i);
                addWrongQuestionView(wrongData, i + 1);
            }
            
            Log.d(TAG, "✅ Loaded " + wrongQuestions.size() + " wrong questions");
            
        } catch (Exception e) {
            Log.e(TAG, "❌ Error loading wrong questions: " + e.getMessage());
            Toast.makeText(this, "❌ Lỗi tải câu sai: " + e.getMessage(), Toast.LENGTH_LONG).show();
            showEmptyState();
        }
    }
    
    private void addWrongQuestionView(ApiQuizActivity.WrongQuestionData wrongData, int questionNumber) {
        // Container for this question
        LinearLayout questionContainer = new LinearLayout(this);
        questionContainer.setOrientation(LinearLayout.VERTICAL);
        questionContainer.setPadding(16, 16, 16, 16);
        questionContainer.setBackgroundColor(0xFFF5F5F5);
        
        // Question number and timestamp
        TextView tvHeader = new TextView(this);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM HH:mm", Locale.getDefault());
        String timeStr = sdf.format(new Date(wrongData.timestamp));
        tvHeader.setText(String.format("❌ Câu %d - %s", questionNumber, timeStr));
        tvHeader.setTextSize(14);
        tvHeader.setTextColor(0xFF666666);
        questionContainer.addView(tvHeader);
        
        // Question text
        TextView tvQuestion = new TextView(this);
        tvQuestion.setText("❓ " + wrongData.questionText);
        tvQuestion.setTextSize(16);
        tvQuestion.setPadding(0, 8, 0, 8);
        questionContainer.addView(tvQuestion);
        
        // Options
        addOptionView(questionContainer, "A", wrongData.optionA, wrongData.userAnswer, wrongData.correctAnswer);
        addOptionView(questionContainer, "B", wrongData.optionB, wrongData.userAnswer, wrongData.correctAnswer);
        addOptionView(questionContainer, "C", wrongData.optionC, wrongData.userAnswer, wrongData.correctAnswer);
        addOptionView(questionContainer, "D", wrongData.optionD, wrongData.userAnswer, wrongData.correctAnswer);
        
        // Summary
        TextView tvSummary = new TextView(this);
        tvSummary.setText(String.format("👤 Bạn chọn: %s | ✅ Đáp án đúng: %s", 
            wrongData.userAnswer, wrongData.correctAnswer));
        tvSummary.setTextSize(12);
        tvSummary.setTextColor(0xFF333333);
        tvSummary.setPadding(0, 8, 0, 0);
        questionContainer.addView(tvSummary);
        
        // Add to main layout
        layoutQuestions.addView(questionContainer);
        
        // Add margin
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) questionContainer.getLayoutParams();
        params.setMargins(0, 0, 0, 16);
        questionContainer.setLayoutParams(params);
    }
    
    private void addOptionView(LinearLayout parent, String optionId, String optionText, 
                              String userAnswer, String correctAnswer) {
        TextView tvOption = new TextView(this);
        
        String prefix = optionId + ". ";
        String fullText = prefix + optionText;
        
        // Color coding
        if (optionId.equals(correctAnswer)) {
            // Correct answer - green
            tvOption.setBackgroundColor(0xFF4CAF50);
            tvOption.setTextColor(0xFFFFFFFF);
            fullText = "✅ " + fullText;
        } else if (optionId.equals(userAnswer)) {
            // User's wrong answer - red
            tvOption.setBackgroundColor(0xFFF44336);
            tvOption.setTextColor(0xFFFFFFFF);
            fullText = "❌ " + fullText;
        } else {
            // Other options - gray
            tvOption.setBackgroundColor(0xFFE0E0E0);
            tvOption.setTextColor(0xFF666666);
        }
        
        tvOption.setText(fullText);
        tvOption.setTextSize(14);
        tvOption.setPadding(12, 8, 12, 8);
        
        parent.addView(tvOption);
        
        // Add margin
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tvOption.getLayoutParams();
        params.setMargins(0, 4, 0, 4);
        tvOption.setLayoutParams(params);
    }
    
    private void showEmptyState() {
        layoutQuestions.removeAllViews();
        
        TextView tvEmpty = new TextView(this);
        tvEmpty.setText("🎉 Tuyệt vời!\n\nBạn chưa có câu nào trả lời sai.\nHãy tiếp tục chơi quiz để thử thách bản thân!");
        tvEmpty.setTextSize(16);
        tvEmpty.setTextColor(0xFF666666);
        tvEmpty.setPadding(32, 64, 32, 64);
        
        layoutQuestions.addView(tvEmpty);
        
        tvTitle.setText("🔍 XEM LẠI CÂU SAI (0 câu)");
        btnClearHistory.setVisibility(View.GONE);
    }
    
    private void clearWrongQuestions() {
        android.content.SharedPreferences prefs = getSharedPreferences("wrong_questions", MODE_PRIVATE);
        prefs.edit().clear().apply();
        
        Toast.makeText(this, "🗑️ Đã xóa lịch sử câu sai", Toast.LENGTH_SHORT).show();
        
        // Reload
        layoutQuestions.removeAllViews();
        loadWrongQuestions();
        
        Log.d(TAG, "🗑️ Wrong questions history cleared");
    }
}