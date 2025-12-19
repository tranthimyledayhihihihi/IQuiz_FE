package com.example.iq5.feature.multiplayer.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.iq5.core.network.ApiClient;
import com.example.iq5.core.network.PvPApiService;
import com.example.iq5.data.model.PvPBattleResponse;
import com.example.iq5.data.model.PvPAnswerResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiPvPBattleActivity extends AppCompatActivity {
    
    private LinearLayout containerLayout;
    private PvPApiService pvpService;
    
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
        pvpService = ApiClient.createService(
            ApiClient.getClient(new com.example.iq5.core.prefs.PrefsManager(this)), 
            PvPApiService.class
        );
        
        // Add buttons
        addButtons();
        
        // Load battles
        loadUserBattles();
    }
    
    private void addButtons() {
        // Title
        TextView titleText = new TextView(this);
        titleText.setText("⚔️ PvP Battle Manager");
        titleText.setTextSize(20);
        titleText.setTextColor(0xFF2196F3);
        titleText.setPadding(0, 0, 0, 30);
        containerLayout.addView(titleText);
        
        // My Battles button
        Button myBattlesBtn = new Button(this);
        myBattlesBtn.setText("🏆 My Battles");
        myBattlesBtn.setOnClickListener(v -> loadUserBattles());
        containerLayout.addView(myBattlesBtn);
        
        // Create Battle button
        Button createBtn = new Button(this);
        createBtn.setText("➕ Create New Battle");
        createBtn.setOnClickListener(v -> createNewBattle());
        containerLayout.addView(createBtn);
        
        // View Battle Details button
        Button detailsBtn = new Button(this);
        detailsBtn.setText("🔍 View Battle #1 Details");
        detailsBtn.setOnClickListener(v -> viewBattleDetails(1));
        containerLayout.addView(detailsBtn);
        
        // Separator
        TextView separatorText = new TextView(this);
        separatorText.setText("─────────────────────");
        separatorText.setTextSize(16);
        separatorText.setTextColor(0xFF757575);
        separatorText.setPadding(0, 20, 0, 20);
        containerLayout.addView(separatorText);
    }
    
    private void loadUserBattles() {
        clearResults();
        
        TextView loadingText = new TextView(this);
        loadingText.setText("🔄 Loading your battles...");
        loadingText.setTextSize(16);
        loadingText.setPadding(0, 20, 0, 20);
        containerLayout.addView(loadingText);
        
        // Call API (using test user ID = 2)
        pvpService.getUserBattles(2).enqueue(new Callback<PvPBattleResponse>() {
            @Override
            public void onResponse(Call<PvPBattleResponse> call, Response<PvPBattleResponse> response) {
                containerLayout.removeView(loadingText);
                
                if (response.isSuccessful() && response.body() != null) {
                    PvPBattleResponse battleResponse = response.body();
                    
                    if (battleResponse.success && battleResponse.data != null) {
                        displayBattles(battleResponse.data);
                    } else {
                        showError("No battles found");
                    }
                } else {
                    showError("Failed to load battles");
                }
            }
            
            @Override
            public void onFailure(Call<PvPBattleResponse> call, Throwable t) {
                containerLayout.removeView(loadingText);
                showError("Network error: " + t.getMessage());
            }
        });
    }
    
    private void createNewBattle() {
        PvPApiService.CreateBattleRequest request = new PvPApiService.CreateBattleRequest(2); // User ID = 2
        
        pvpService.createBattle(request).enqueue(new Callback<com.example.iq5.data.model.ApiResponse>() {
            @Override
            public void onResponse(Call<com.example.iq5.data.model.ApiResponse> call, Response<com.example.iq5.data.model.ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(ApiPvPBattleActivity.this, "✅ Battle created successfully!", Toast.LENGTH_SHORT).show();
                    loadUserBattles(); // Refresh the list
                } else {
                    showError("Failed to create battle");
                }
            }
            
            @Override
            public void onFailure(Call<com.example.iq5.data.model.ApiResponse> call, Throwable t) {
                showError("Network error: " + t.getMessage());
            }
        });
    }
    
    private void viewBattleDetails(int battleId) {
        clearResults();
        
        TextView loadingText = new TextView(this);
        loadingText.setText("🔄 Loading battle details...");
        loadingText.setTextSize(16);
        loadingText.setPadding(0, 20, 0, 20);
        containerLayout.addView(loadingText);
        
        pvpService.getBattleAnswers(battleId).enqueue(new Callback<PvPAnswerResponse>() {
            @Override
            public void onResponse(Call<PvPAnswerResponse> call, Response<PvPAnswerResponse> response) {
                containerLayout.removeView(loadingText);
                
                if (response.isSuccessful() && response.body() != null) {
                    PvPAnswerResponse answerResponse = response.body();
                    
                    if (answerResponse.success && answerResponse.data != null) {
                        displayBattleAnswers(answerResponse.data, battleId);
                    } else {
                        showError("No battle details found");
                    }
                } else {
                    showError("Failed to load battle details");
                }
            }
            
            @Override
            public void onFailure(Call<PvPAnswerResponse> call, Throwable t) {
                containerLayout.removeView(loadingText);
                showError("Network error: " + t.getMessage());
            }
        });
    }
    
    private void displayBattles(java.util.List<PvPBattleResponse.PvPBattleData> battles) {
        // Title
        TextView titleText = new TextView(this);
        titleText.setText("🏆 Your PvP Battles");
        titleText.setTextSize(18);
        titleText.setTextColor(0xFF2196F3);
        titleText.setPadding(0, 0, 0, 20);
        containerLayout.addView(titleText);
        
        if (battles.isEmpty()) {
            TextView emptyText = new TextView(this);
            emptyText.setText("⚔️ No battles yet!\nCreate your first battle!");
            emptyText.setTextSize(16);
            emptyText.setTextColor(0xFF757575);
            emptyText.setPadding(0, 20, 0, 20);
            containerLayout.addView(emptyText);
            return;
        }
        
        // Display each battle
        for (PvPBattleResponse.PvPBattleData battle : battles) {
            LinearLayout battleCard = new LinearLayout(this);
            battleCard.setOrientation(LinearLayout.VERTICAL);
            battleCard.setPadding(20, 20, 20, 20);
            battleCard.setBackgroundColor(0xFFF5F5F5);
            
            LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            );
            cardParams.setMargins(0, 0, 0, 15);
            battleCard.setLayoutParams(cardParams);
            
            // Battle ID and Status
            TextView idStatusText = new TextView(this);
            String statusEmoji = getStatusEmoji(battle.TrangThai);
            idStatusText.setText(statusEmoji + " Battle #" + battle.TranDauID + " - " + battle.TrangThai);
            idStatusText.setTextSize(16);
            idStatusText.setTextColor(0xFF333333);
            idStatusText.setPadding(0, 0, 0, 10);
            battleCard.addView(idStatusText);
            
            // Players
            TextView playersText = new TextView(this);
            String player2 = (battle.TenNguoiChoi2 != null && !battle.TenNguoiChoi2.isEmpty()) 
                ? battle.TenNguoiChoi2 : "Waiting for opponent";
            playersText.setText("👥 " + battle.TenNguoiChoi1 + " vs " + player2);
            playersText.setTextSize(14);
            playersText.setTextColor(0xFF666666);
            playersText.setPadding(0, 0, 0, 10);
            battleCard.addView(playersText);
            
            // Scores (if available)
            if (battle.DiemNguoiChoi1 != null || battle.DiemNguoiChoi2 != null) {
                TextView scoresText = new TextView(this);
                String score1 = battle.DiemNguoiChoi1 != null ? battle.DiemNguoiChoi1.toString() : "0";
                String score2 = battle.DiemNguoiChoi2 != null ? battle.DiemNguoiChoi2.toString() : "0";
                scoresText.setText("🏆 Score: " + score1 + " - " + score2);
                scoresText.setTextSize(14);
                scoresText.setTextColor(0xFF4CAF50);
                scoresText.setPadding(0, 0, 0, 10);
                battleCard.addView(scoresText);
            }
            
            // Winner (if available)
            if (battle.NguoiThangID != null) {
                TextView winnerText = new TextView(this);
                String winner = (battle.NguoiThangID.equals(battle.NguoiChoi1ID)) 
                    ? battle.TenNguoiChoi1 : battle.TenNguoiChoi2;
                winnerText.setText("🥇 Winner: " + winner);
                winnerText.setTextSize(14);
                winnerText.setTextColor(0xFFFF9800);
                winnerText.setPadding(0, 0, 0, 10);
                battleCard.addView(winnerText);
            }
            
            // Date
            TextView dateText = new TextView(this);
            dateText.setText("📅 " + battle.NgayBatDau.substring(0, 10));
            dateText.setTextSize(12);
            dateText.setTextColor(0xFF757575);
            battleCard.addView(dateText);
            
            containerLayout.addView(battleCard);
        }
        
        // Summary
        TextView summaryText = new TextView(this);
        summaryText.setText("📊 Total battles: " + battles.size());
        summaryText.setTextSize(14);
        summaryText.setTextColor(0xFF2196F3);
        summaryText.setPadding(0, 20, 0, 0);
        containerLayout.addView(summaryText);
    }
    
    private void displayBattleAnswers(java.util.List<PvPAnswerResponse.PvPAnswerData> answers, int battleId) {
        // Title
        TextView titleText = new TextView(this);
        titleText.setText("🔍 Battle #" + battleId + " Details");
        titleText.setTextSize(18);
        titleText.setTextColor(0xFF2196F3);
        titleText.setPadding(0, 0, 0, 20);
        containerLayout.addView(titleText);
        
        if (answers.isEmpty()) {
            TextView emptyText = new TextView(this);
            emptyText.setText("📝 No answers recorded yet");
            emptyText.setTextSize(16);
            emptyText.setTextColor(0xFF757575);
            emptyText.setPadding(0, 20, 0, 20);
            containerLayout.addView(emptyText);
            return;
        }
        
        // Display each answer
        for (PvPAnswerResponse.PvPAnswerData answer : answers) {
            LinearLayout answerCard = new LinearLayout(this);
            answerCard.setOrientation(LinearLayout.VERTICAL);
            answerCard.setPadding(20, 20, 20, 20);
            answerCard.setBackgroundColor(0xFFF5F5F5);
            
            LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            );
            cardParams.setMargins(0, 0, 0, 15);
            answerCard.setLayoutParams(cardParams);
            
            // Player and result
            TextView playerText = new TextView(this);
            String resultEmoji = answer.LaDapAnDung ? "✅" : "❌";
            playerText.setText(resultEmoji + " " + answer.TenNguoiDung + " - " + answer.ThoiGianTraLoi + "s");
            playerText.setTextSize(16);
            playerText.setTextColor(answer.LaDapAnDung ? 0xFF4CAF50 : 0xFFE91E63);
            playerText.setPadding(0, 0, 0, 10);
            answerCard.addView(playerText);
            
            // Question
            TextView questionText = new TextView(this);
            questionText.setText("❓ " + answer.CauHoi);
            questionText.setTextSize(14);
            questionText.setTextColor(0xFF333333);
            questionText.setPadding(0, 0, 0, 10);
            answerCard.addView(questionText);
            
            // Chosen answer
            TextView chosenText = new TextView(this);
            chosenText.setText("📝 Chosen: " + answer.DapAnChon);
            chosenText.setTextSize(14);
            chosenText.setTextColor(0xFF666666);
            chosenText.setPadding(0, 0, 0, 5);
            answerCard.addView(chosenText);
            
            // Correct answer
            TextView correctText = new TextView(this);
            correctText.setText("✅ Correct: " + answer.DapAnDung);
            correctText.setTextSize(14);
            correctText.setTextColor(0xFF4CAF50);
            answerCard.addView(correctText);
            
            containerLayout.addView(answerCard);
        }
        
        // Summary
        TextView summaryText = new TextView(this);
        summaryText.setText("📊 Total answers: " + answers.size());
        summaryText.setTextSize(14);
        summaryText.setTextColor(0xFF2196F3);
        summaryText.setPadding(0, 20, 0, 0);
        containerLayout.addView(summaryText);
    }
    
    private String getStatusEmoji(String status) {
        switch (status) {
            case "Waiting": return "⏳";
            case "Playing": return "🎮";
            case "Finished": return "🏁";
            default: return "❓";
        }
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