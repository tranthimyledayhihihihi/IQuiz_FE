package com.example.iq5.feature.reward.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.iq5.core.network.ApiClient;
import com.example.iq5.core.network.DailyRewardApiService;
import com.example.iq5.data.model.DailyRewardResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiDailyRewardActivity extends AppCompatActivity {
    
    private LinearLayout containerLayout;
    private DailyRewardApiService dailyRewardService;
    
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
        dailyRewardService = ApiClient.createService(
            ApiClient.getClient(new com.example.iq5.core.prefs.PrefsManager(this)), 
            DailyRewardApiService.class
        );
        
        // Add buttons
        addButtons();
        
        // Check today's reward status
        checkTodayReward();
    }
    
    private void addButtons() {
        // Title
        TextView titleText = new TextView(this);
        titleText.setText("🎁 Daily Rewards");
        titleText.setTextSize(20);
        titleText.setTextColor(0xFF2196F3);
        titleText.setPadding(0, 0, 0, 30);
        containerLayout.addView(titleText);
        
        // Check Today button
        Button checkTodayBtn = new Button(this);
        checkTodayBtn.setText("📅 Check Today's Reward");
        checkTodayBtn.setOnClickListener(v -> checkTodayReward());
        containerLayout.addView(checkTodayBtn);
        
        // Claim Reward button
        Button claimBtn = new Button(this);
        claimBtn.setText("🎁 Claim Daily Reward");
        claimBtn.setOnClickListener(v -> claimDailyReward());
        containerLayout.addView(claimBtn);
        
        // My Rewards button
        Button myRewardsBtn = new Button(this);
        myRewardsBtn.setText("📜 My Reward History");
        myRewardsBtn.setOnClickListener(v -> loadUserRewards());
        containerLayout.addView(myRewardsBtn);
        
        // Separator
        TextView separatorText = new TextView(this);
        separatorText.setText("─────────────────────");
        separatorText.setTextSize(16);
        separatorText.setTextColor(0xFF757575);
        separatorText.setPadding(0, 20, 0, 20);
        containerLayout.addView(separatorText);
    }
    
    private void checkTodayReward() {
        clearResults();
        
        TextView loadingText = new TextView(this);
        loadingText.setText("🔄 Checking today's reward...");
        loadingText.setTextSize(16);
        loadingText.setPadding(0, 20, 0, 20);
        containerLayout.addView(loadingText);
        
        // Call API (using test user ID = 2)
        dailyRewardService.checkTodayReward(2).enqueue(new Callback<com.example.iq5.data.model.ApiResponse>() {
            @Override
            public void onResponse(Call<com.example.iq5.data.model.ApiResponse> call, Response<com.example.iq5.data.model.ApiResponse> response) {
                containerLayout.removeView(loadingText);
                
                if (response.isSuccessful() && response.body() != null) {
                    com.example.iq5.data.model.ApiResponse apiResponse = response.body();
                    
                    if (apiResponse.success) {
                        displayTodayStatus(apiResponse);
                    } else {
                        showError("Failed to check today's reward");
                    }
                } else {
                    showError("Failed to check today's reward");
                }
            }
            
            @Override
            public void onFailure(Call<com.example.iq5.data.model.ApiResponse> call, Throwable t) {
                containerLayout.removeView(loadingText);
                showError("Network error: " + t.getMessage());
            }
        });
    }
    
    private void claimDailyReward() {
        DailyRewardApiService.ClaimRewardRequest request = 
            new DailyRewardApiService.ClaimRewardRequest(
                2, // User ID
                "Coins",
                100,
                "Daily login reward"
            );
        
        dailyRewardService.claimDailyReward(request).enqueue(new Callback<com.example.iq5.data.model.ApiResponse>() {
            @Override
            public void onResponse(Call<com.example.iq5.data.model.ApiResponse> call, Response<com.example.iq5.data.model.ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    com.example.iq5.data.model.ApiResponse apiResponse = response.body();
                    
                    if (apiResponse.success) {
                        Toast.makeText(ApiDailyRewardActivity.this, "🎉 Daily reward claimed successfully!", Toast.LENGTH_SHORT).show();
                        checkTodayReward(); // Refresh status
                    } else {
                        showError(apiResponse.message != null ? apiResponse.message : "Failed to claim reward");
                    }
                } else {
                    showError("Failed to claim reward");
                }
            }
            
            @Override
            public void onFailure(Call<com.example.iq5.data.model.ApiResponse> call, Throwable t) {
                showError("Network error: " + t.getMessage());
            }
        });
    }
    
    private void loadUserRewards() {
        clearResults();
        
        TextView loadingText = new TextView(this);
        loadingText.setText("🔄 Loading your rewards...");
        loadingText.setTextSize(16);
        loadingText.setPadding(0, 20, 0, 20);
        containerLayout.addView(loadingText);
        
        dailyRewardService.getUserDailyRewards(2).enqueue(new Callback<DailyRewardResponse>() {
            @Override
            public void onResponse(Call<DailyRewardResponse> call, Response<DailyRewardResponse> response) {
                containerLayout.removeView(loadingText);
                
                if (response.isSuccessful() && response.body() != null) {
                    DailyRewardResponse rewardResponse = response.body();
                    
                    if (rewardResponse.success && rewardResponse.data != null) {
                        displayRewards(rewardResponse.data);
                    } else {
                        showError("No rewards found");
                    }
                } else {
                    showError("Failed to load rewards");
                }
            }
            
            @Override
            public void onFailure(Call<DailyRewardResponse> call, Throwable t) {
                containerLayout.removeView(loadingText);
                showError("Network error: " + t.getMessage());
            }
        });
    }
    
    private void displayTodayStatus(com.example.iq5.data.model.ApiResponse response) {
        // Status card
        LinearLayout statusCard = new LinearLayout(this);
        statusCard.setOrientation(LinearLayout.VERTICAL);
        statusCard.setPadding(20, 20, 20, 20);
        statusCard.setBackgroundColor(0xFFF5F5F5);
        
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(0, 0, 0, 20);
        statusCard.setLayoutParams(cardParams);
        
        // Title
        TextView titleText = new TextView(this);
        titleText.setText("📅 Today's Reward Status");
        titleText.setTextSize(18);
        titleText.setTextColor(0xFF2196F3);
        titleText.setPadding(0, 0, 0, 15);
        statusCard.addView(titleText);
        
        // Status message
        TextView statusText = new TextView(this);
        if (response.message != null && response.message.contains("Already claimed")) {
            statusText.setText("✅ You have already claimed today's reward!");
            statusText.setTextColor(0xFF4CAF50);
        } else {
            statusText.setText("🎁 Daily reward is available to claim!");
            statusText.setTextColor(0xFFFF9800);
        }
        statusText.setTextSize(16);
        statusText.setPadding(0, 0, 0, 10);
        statusCard.addView(statusText);
        
        // Reward info
        TextView rewardText = new TextView(this);
        rewardText.setText("💰 Reward: 100 Coins");
        rewardText.setTextSize(14);
        rewardText.setTextColor(0xFF666666);
        statusCard.addView(rewardText);
        
        containerLayout.addView(statusCard);
    }
    
    private void displayRewards(java.util.List<DailyRewardResponse.DailyRewardData> rewards) {
        // Title
        TextView titleText = new TextView(this);
        titleText.setText("📜 Your Reward History");
        titleText.setTextSize(18);
        titleText.setTextColor(0xFF2196F3);
        titleText.setPadding(0, 0, 0, 20);
        containerLayout.addView(titleText);
        
        if (rewards.isEmpty()) {
            TextView emptyText = new TextView(this);
            emptyText.setText("🎁 No rewards claimed yet!\nClaim your first daily reward!");
            emptyText.setTextSize(16);
            emptyText.setTextColor(0xFF757575);
            emptyText.setPadding(0, 20, 0, 20);
            containerLayout.addView(emptyText);
            return;
        }
        
        // Display each reward
        for (DailyRewardResponse.DailyRewardData reward : rewards) {
            LinearLayout rewardCard = new LinearLayout(this);
            rewardCard.setOrientation(LinearLayout.VERTICAL);
            rewardCard.setPadding(20, 20, 20, 20);
            rewardCard.setBackgroundColor(0xFFF5F5F5);
            
            LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            );
            cardParams.setMargins(0, 0, 0, 15);
            rewardCard.setLayoutParams(cardParams);
            
            // Reward type and value
            TextView rewardText = new TextView(this);
            String rewardEmoji = getRewardEmoji(reward.LoaiThuong);
            rewardText.setText(rewardEmoji + " " + reward.GiaTri + " " + reward.LoaiThuong);
            rewardText.setTextSize(16);
            rewardText.setTextColor(0xFF333333);
            rewardText.setPadding(0, 0, 0, 10);
            rewardCard.addView(rewardText);
            
            // Description
            if (reward.MoTa != null && !reward.MoTa.isEmpty()) {
                TextView descText = new TextView(this);
                descText.setText("📝 " + reward.MoTa);
                descText.setTextSize(14);
                descText.setTextColor(0xFF666666);
                descText.setPadding(0, 0, 0, 10);
                rewardCard.addView(descText);
            }
            
            // Date
            TextView dateText = new TextView(this);
            dateText.setText("📅 " + reward.NgayNhan.substring(0, 10));
            dateText.setTextSize(12);
            dateText.setTextColor(0xFF757575);
            rewardCard.addView(dateText);
            
            containerLayout.addView(rewardCard);
        }
        
        // Summary
        int totalValue = rewards.stream().mapToInt(r -> r.GiaTri).sum();
        TextView summaryText = new TextView(this);
        summaryText.setText("📊 Total rewards: " + rewards.size() + " • Total value: " + totalValue + " coins");
        summaryText.setTextSize(14);
        summaryText.setTextColor(0xFF2196F3);
        summaryText.setPadding(0, 20, 0, 0);
        containerLayout.addView(summaryText);
    }
    
    private String getRewardEmoji(String rewardType) {
        switch (rewardType.toLowerCase()) {
            case "coins": return "💰";
            case "gems": return "💎";
            case "points": return "⭐";
            default: return "🎁";
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