package com.example.iq5.feature.specialmode.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.iq5.core.network.ApiClient;
import com.example.iq5.core.network.CustomQuizApiService;
import com.example.iq5.data.model.CustomQuizResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiCustomQuizActivity extends AppCompatActivity {
    
    private LinearLayout containerLayout;
    private CustomQuizApiService customQuizService;
    
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
        customQuizService = ApiClient.createService(
            ApiClient.getClient(new com.example.iq5.core.prefs.PrefsManager(this)), 
            CustomQuizApiService.class
        );
        
        // Add buttons
        addButtons();
        
        // Load custom quizzes
        loadAllCustomQuizzes();
    }
    
    private void addButtons() {
        // Title
        TextView titleText = new TextView(this);
        titleText.setText("🎯 Custom Quiz Manager");
        titleText.setTextSize(20);
        titleText.setTextColor(0xFF2196F3);
        titleText.setPadding(0, 0, 0, 30);
        containerLayout.addView(titleText);
        
        // My Quizzes button
        Button myQuizzesBtn = new Button(this);
        myQuizzesBtn.setText("📝 My Custom Quizzes");
        myQuizzesBtn.setOnClickListener(v -> loadUserCustomQuizzes());
        containerLayout.addView(myQuizzesBtn);
        
        // All Quizzes button
        Button allQuizzesBtn = new Button(this);
        allQuizzesBtn.setText("🌍 All Custom Quizzes");
        allQuizzesBtn.setOnClickListener(v -> loadAllCustomQuizzes());
        containerLayout.addView(allQuizzesBtn);
        
        // Create Quiz button
        Button createBtn = new Button(this);
        createBtn.setText("➕ Create New Quiz");
        createBtn.setOnClickListener(v -> createSampleQuiz());
        containerLayout.addView(createBtn);
        
        // Separator
        TextView separatorText = new TextView(this);
        separatorText.setText("─────────────────────");
        separatorText.setTextSize(16);
        separatorText.setTextColor(0xFF757575);
        separatorText.setPadding(0, 20, 0, 20);
        containerLayout.addView(separatorText);
    }
    
    private void loadUserCustomQuizzes() {
        clearResults();
        
        TextView loadingText = new TextView(this);
        loadingText.setText("🔄 Loading your custom quizzes...");
        loadingText.setTextSize(16);
        loadingText.setPadding(0, 20, 0, 20);
        containerLayout.addView(loadingText);
        
        // Call API (using test user ID = 2)
        customQuizService.getUserCustomQuizzes(2).enqueue(new Callback<CustomQuizResponse>() {
            @Override
            public void onResponse(Call<CustomQuizResponse> call, Response<CustomQuizResponse> response) {
                containerLayout.removeView(loadingText);
                
                if (response.isSuccessful() && response.body() != null) {
                    CustomQuizResponse quizResponse = response.body();
                    
                    if (quizResponse.success && quizResponse.data != null) {
                        displayCustomQuizzes(quizResponse.data, "📝 Your Custom Quizzes");
                    } else {
                        showError("No custom quizzes found");
                    }
                } else {
                    showError("Failed to load custom quizzes");
                }
            }
            
            @Override
            public void onFailure(Call<CustomQuizResponse> call, Throwable t) {
                containerLayout.removeView(loadingText);
                showError("Network error: " + t.getMessage());
            }
        });
    }
    
    private void loadAllCustomQuizzes() {
        clearResults();
        
        TextView loadingText = new TextView(this);
        loadingText.setText("🔄 Loading all custom quizzes...");
        loadingText.setTextSize(16);
        loadingText.setPadding(0, 20, 0, 20);
        containerLayout.addView(loadingText);
        
        customQuizService.getAllCustomQuizzes().enqueue(new Callback<CustomQuizResponse>() {
            @Override
            public void onResponse(Call<CustomQuizResponse> call, Response<CustomQuizResponse> response) {
                containerLayout.removeView(loadingText);
                
                if (response.isSuccessful() && response.body() != null) {
                    CustomQuizResponse quizResponse = response.body();
                    
                    if (quizResponse.success && quizResponse.data != null) {
                        displayCustomQuizzes(quizResponse.data, "🌍 All Custom Quizzes");
                    } else {
                        showError("No custom quizzes found");
                    }
                } else {
                    showError("Failed to load custom quizzes");
                }
            }
            
            @Override
            public void onFailure(Call<CustomQuizResponse> call, Throwable t) {
                containerLayout.removeView(loadingText);
                showError("Network error: " + t.getMessage());
            }
        });
    }
    
    private void createSampleQuiz() {
        CustomQuizApiService.CreateCustomQuizRequest request = 
            new CustomQuizApiService.CreateCustomQuizRequest(
                2, // User ID
                "Sample Quiz " + System.currentTimeMillis(),
                "A sample quiz created from the app",
                5, // 5 questions
                300 // 5 minutes
            );
        
        customQuizService.createCustomQuiz(request).enqueue(new Callback<com.example.iq5.data.model.ApiResponse>() {
            @Override
            public void onResponse(Call<com.example.iq5.data.model.ApiResponse> call, Response<com.example.iq5.data.model.ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(ApiCustomQuizActivity.this, "✅ Quiz created successfully!", Toast.LENGTH_SHORT).show();
                    loadUserCustomQuizzes(); // Refresh the list
                } else {
                    showError("Failed to create quiz");
                }
            }
            
            @Override
            public void onFailure(Call<com.example.iq5.data.model.ApiResponse> call, Throwable t) {
                showError("Network error: " + t.getMessage());
            }
        });
    }
    
    private void displayCustomQuizzes(java.util.List<CustomQuizResponse.CustomQuizData> quizzes, String title) {
        // Title
        TextView titleText = new TextView(this);
        titleText.setText(title);
        titleText.setTextSize(18);
        titleText.setTextColor(0xFF2196F3);
        titleText.setPadding(0, 0, 0, 20);
        containerLayout.addView(titleText);
        
        if (quizzes.isEmpty()) {
            TextView emptyText = new TextView(this);
            emptyText.setText("📝 No custom quizzes yet!\nCreate your first quiz!");
            emptyText.setTextSize(16);
            emptyText.setTextColor(0xFF757575);
            emptyText.setPadding(0, 20, 0, 20);
            containerLayout.addView(emptyText);
            return;
        }
        
        // Display each quiz
        for (CustomQuizResponse.CustomQuizData quiz : quizzes) {
            LinearLayout quizCard = new LinearLayout(this);
            quizCard.setOrientation(LinearLayout.VERTICAL);
            quizCard.setPadding(20, 20, 20, 20);
            quizCard.setBackgroundColor(0xFFF5F5F5);
            
            LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            );
            cardParams.setMargins(0, 0, 0, 15);
            quizCard.setLayoutParams(cardParams);
            
            // Quiz name
            TextView nameText = new TextView(this);
            nameText.setText("🎯 " + quiz.TenQuiz);
            nameText.setTextSize(16);
            nameText.setTextColor(0xFF333333);
            nameText.setPadding(0, 0, 0, 10);
            quizCard.addView(nameText);
            
            // Description
            if (quiz.MoTa != null && !quiz.MoTa.isEmpty()) {
                TextView descText = new TextView(this);
                descText.setText("📝 " + quiz.MoTa);
                descText.setTextSize(14);
                descText.setTextColor(0xFF666666);
                descText.setPadding(0, 0, 0, 10);
                quizCard.addView(descText);
            }
            
            // Creator (if available)
            if (quiz.TenNguoiDung != null) {
                TextView creatorText = new TextView(this);
                creatorText.setText("👤 Created by: " + quiz.TenNguoiDung);
                creatorText.setTextSize(12);
                creatorText.setTextColor(0xFF9C27B0);
                creatorText.setPadding(0, 0, 0, 5);
                quizCard.addView(creatorText);
            }
            
            // Details
            TextView detailsText = new TextView(this);
            detailsText.setText("📊 " + quiz.SoLuongCauHoi + " questions • ⏱️ " + (quiz.ThoiGianGioiHan / 60) + " minutes");
            detailsText.setTextSize(12);
            detailsText.setTextColor(0xFF4CAF50);
            detailsText.setPadding(0, 0, 0, 5);
            quizCard.addView(detailsText);
            
            // Date
            TextView dateText = new TextView(this);
            dateText.setText("📅 " + quiz.NgayTao.substring(0, 10));
            dateText.setTextSize(12);
            dateText.setTextColor(0xFF757575);
            quizCard.addView(dateText);
            
            containerLayout.addView(quizCard);
        }
        
        // Summary
        TextView summaryText = new TextView(this);
        summaryText.setText("📊 Total: " + quizzes.size() + " custom quizzes");
        summaryText.setTextSize(14);
        summaryText.setTextColor(0xFF2196F3);
        summaryText.setPadding(0, 20, 0, 0);
        containerLayout.addView(summaryText);
    }
    
    private void clearResults() {
        // Remove all views except the first 5 (title + 4 buttons)
        while (containerLayout.getChildCount() > 5) {
            containerLayout.removeViewAt(containerLayout.getChildCount() - 1);
        }
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