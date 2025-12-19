package com.example.iq5.feature.specialmode.ui;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.iq5.core.network.ApiClient;
import com.example.iq5.core.network.WrongQuestionApiService;
import com.example.iq5.data.model.WrongQuestionResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiWrongHistoryActivity extends AppCompatActivity {
    
    private LinearLayout containerLayout;
    private WrongQuestionApiService wrongQuestionService;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Create dynamic UI
        ScrollView scrollView = new ScrollView(this);
        containerLayout = new LinearLayout(this);
        containerLayout.setOrientation(LinearLayout.VERTICAL);
        containerLayout.setPadding(32, 32, 32, 32);
        scrollView.addView(containerLayout);
        setContentView(scrollView);
        
        // Initialize API service
        wrongQuestionService = ApiClient.createService(
            ApiClient.getClient(new com.example.iq5.core.prefs.PrefsManager(this)), 
            WrongQuestionApiService.class
        );
        
        // Load wrong questions
        loadWrongQuestions();
    }
    
    private void loadWrongQuestions() {
        // Show loading
        TextView loadingText = new TextView(this);
        loadingText.setText("🔄 Loading wrong questions...");
        loadingText.setTextSize(16);
        loadingText.setPadding(0, 20, 0, 20);
        containerLayout.addView(loadingText);
        
        // Call API (using test user ID = 2)
        wrongQuestionService.getUserWrongQuestions(2).enqueue(new Callback<WrongQuestionResponse>() {
            @Override
            public void onResponse(Call<WrongQuestionResponse> call, Response<WrongQuestionResponse> response) {
                containerLayout.removeView(loadingText);
                
                if (response.isSuccessful() && response.body() != null) {
                    WrongQuestionResponse wrongResponse = response.body();
                    
                    if (wrongResponse.success && wrongResponse.data != null) {
                        displayWrongQuestions(wrongResponse.data);
                    } else {
                        showError("No wrong questions found");
                    }
                } else {
                    showError("Failed to load wrong questions");
                }
            }
            
            @Override
            public void onFailure(Call<WrongQuestionResponse> call, Throwable t) {
                containerLayout.removeView(loadingText);
                showError("Network error: " + t.getMessage());
            }
        });
    }
    
    private void displayWrongQuestions(java.util.List<WrongQuestionResponse.WrongQuestionData> wrongQuestions) {
        // Title
        TextView titleText = new TextView(this);
        titleText.setText("❌ Wrong Questions History");
        titleText.setTextSize(20);
        titleText.setTextColor(0xFF2196F3);
        titleText.setPadding(0, 0, 0, 30);
        containerLayout.addView(titleText);
        
        if (wrongQuestions.isEmpty()) {
            TextView emptyText = new TextView(this);
            emptyText.setText("🎉 No wrong questions yet!\nKeep practicing!");
            emptyText.setTextSize(16);
            emptyText.setTextColor(0xFF4CAF50);
            emptyText.setPadding(0, 20, 0, 20);
            containerLayout.addView(emptyText);
            return;
        }
        
        // Display each wrong question
        for (WrongQuestionResponse.WrongQuestionData wrongQuestion : wrongQuestions) {
            LinearLayout questionCard = new LinearLayout(this);
            questionCard.setOrientation(LinearLayout.VERTICAL);
            questionCard.setPadding(20, 20, 20, 20);
            questionCard.setBackgroundColor(0xFFF5F5F5);
            
            LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            );
            cardParams.setMargins(0, 0, 0, 20);
            questionCard.setLayoutParams(cardParams);
            
            // Category
            TextView categoryText = new TextView(this);
            categoryText.setText("📚 " + wrongQuestion.TenChuDe);
            categoryText.setTextSize(14);
            categoryText.setTextColor(0xFF9C27B0);
            categoryText.setPadding(0, 0, 0, 10);
            questionCard.addView(categoryText);
            
            // Question
            TextView questionText = new TextView(this);
            questionText.setText("❓ " + wrongQuestion.CauHoi);
            questionText.setTextSize(16);
            questionText.setTextColor(0xFF333333);
            questionText.setPadding(0, 0, 0, 15);
            questionCard.addView(questionText);
            
            // Your wrong answer
            TextView wrongAnswerText = new TextView(this);
            wrongAnswerText.setText("❌ Your answer: " + wrongQuestion.DapAnSai);
            wrongAnswerText.setTextSize(14);
            wrongAnswerText.setTextColor(0xFFE91E63);
            wrongAnswerText.setPadding(0, 0, 0, 5);
            questionCard.addView(wrongAnswerText);
            
            // Correct answer
            TextView correctAnswerText = new TextView(this);
            correctAnswerText.setText("✅ Correct answer: " + wrongQuestion.DapAnDung);
            correctAnswerText.setTextSize(14);
            correctAnswerText.setTextColor(0xFF4CAF50);
            wrongAnswerText.setPadding(0, 0, 0, 10);
            questionCard.addView(correctAnswerText);
            
            // Date
            TextView dateText = new TextView(this);
            dateText.setText("📅 " + wrongQuestion.NgayTao.substring(0, 10));
            dateText.setTextSize(12);
            dateText.setTextColor(0xFF757575);
            questionCard.addView(dateText);
            
            containerLayout.addView(questionCard);
        }
        
        // Summary
        TextView summaryText = new TextView(this);
        summaryText.setText("📊 Total wrong questions: " + wrongQuestions.size());
        summaryText.setTextSize(16);
        summaryText.setTextColor(0xFF2196F3);
        summaryText.setPadding(0, 30, 0, 0);
        containerLayout.addView(summaryText);
    }
    
    private void showError(String message) {
        TextView errorText = new TextView(this);
        errorText.setText("❌ " + message);
        errorText.setTextSize(16);
        errorText.setTextColor(0xFFE91E63);
        errorText.setPadding(0, 20, 0, 20);
        containerLayout.addView(errorText);
        
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}