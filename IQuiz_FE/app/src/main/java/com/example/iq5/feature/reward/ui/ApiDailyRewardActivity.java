package com.example.iq5.feature.reward.ui;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.iq5.core.network.ApiClient;
import com.example.iq5.core.network.DailyRewardApiService;
import com.example.iq5.core.prefs.PrefsManager;
import com.example.iq5.data.model.ApiResponse;
import com.example.iq5.data.model.DailyRewardResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ApiDailyRewardActivity extends AppCompatActivity {
    
    private static final String TAG = "ApiDailyRewardActivity";
    
    private LinearLayout containerLayout;
    private DailyRewardApiService dailyRewardService;
    private TextView statusText;
    private Button claimButton;
    private boolean isRewardClaimed = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        createBeautifulUI();
        initApiService();
        checkTodayReward();
    }

    private void createBeautifulUI() {
        // Main container
        ScrollView scrollView = new ScrollView(this);
        scrollView.setBackgroundColor(0xFFF5F5F5);
        
        containerLayout = new LinearLayout(this);
        containerLayout.setOrientation(LinearLayout.VERTICAL);
        containerLayout.setPadding(24, 24, 24, 24);
        
        // Back button (simple text)
        TextView backButton = new TextView(this);
        backButton.setText("← Phần Thưởng Hàng Ngày");
        backButton.setTextSize(18);
        backButton.setTextColor(0xFF333333);
        backButton.setPadding(0, 0, 0, 32);
        backButton.setOnClickListener(v -> finish());
        containerLayout.addView(backButton);
        
        // Main reward card
        createMainRewardCard();
        
        // Reward history section
        createRewardHistorySection();
        
        scrollView.addView(containerLayout);
        setContentView(scrollView);
    }

    private void createMainRewardCard() {
        // Main card container
        LinearLayout mainCard = new LinearLayout(this);
        mainCard.setOrientation(LinearLayout.VERTICAL);
        mainCard.setPadding(32, 40, 32, 40);
        
        // Create rounded background
        GradientDrawable cardBackground = new GradientDrawable();
        cardBackground.setColor(Color.WHITE);
        cardBackground.setCornerRadius(24);
        cardBackground.setStroke(1, 0xFFE0E0E0);
        mainCard.setBackground(cardBackground);
        
        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(0, 0, 0, 32);
        mainCard.setLayoutParams(cardParams);
        
        // Title with gradient background
        LinearLayout titleContainer = new LinearLayout(this);
        titleContainer.setOrientation(LinearLayout.HORIZONTAL);
        titleContainer.setGravity(Gravity.CENTER);
        titleContainer.setPadding(32, 16, 32, 16);
        
        // Create gradient background for title
        GradientDrawable titleBackground = new GradientDrawable(
            GradientDrawable.Orientation.LEFT_RIGHT,
            new int[]{0xFF9C27B0, 0xFFE91E63, 0xFFFF5722}
        );
        titleBackground.setCornerRadius(25);
        titleContainer.setBackground(titleBackground);
        
        TextView titleText = new TextView(this);
        titleText.setText("THƯỞNG HÀNG NGÀY");
        titleText.setTextSize(18);
        titleText.setTextColor(Color.WHITE);
        titleText.setGravity(Gravity.CENTER);
        titleContainer.addView(titleText);
        
        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        titleParams.setMargins(0, 0, 0, 32);
        titleContainer.setLayoutParams(titleParams);
        
        mainCard.addView(titleContainer);
        
        // Reward info section
        LinearLayout rewardInfo = new LinearLayout(this);
        rewardInfo.setOrientation(LinearLayout.VERTICAL);
        rewardInfo.setGravity(Gravity.CENTER);
        rewardInfo.setPadding(0, 20, 0, 32);
        
        // Reward icon and amount
        TextView rewardIcon = new TextView(this);
        rewardIcon.setText("💰");
        rewardIcon.setTextSize(48);
        rewardIcon.setGravity(Gravity.CENTER);
        rewardInfo.addView(rewardIcon);
        
        TextView rewardAmount = new TextView(this);
        rewardAmount.setText("100 Coins");
        rewardAmount.setTextSize(24);
        rewardAmount.setTextColor(0xFF333333);
        rewardAmount.setGravity(Gravity.CENTER);
        rewardAmount.setPadding(0, 8, 0, 0);
        rewardInfo.addView(rewardAmount);
        
        // Status text
        statusText = new TextView(this);
        statusText.setText("Đang kiểm tra...");
        statusText.setTextSize(16);
        statusText.setTextColor(0xFF666666);
        statusText.setGravity(Gravity.CENTER);
        statusText.setPadding(0, 16, 0, 0);
        rewardInfo.addView(statusText);
        
        mainCard.addView(rewardInfo);
        
        // Claim button
        claimButton = new Button(this);
        claimButton.setText("ĐÃ NHẬN HÔM NAY");
        claimButton.setTextSize(16);
        claimButton.setTextColor(Color.WHITE);
        claimButton.setPadding(32, 16, 32, 16);
        
        // Create gradient background for button
        GradientDrawable buttonBackground = new GradientDrawable();
        buttonBackground.setColor(0xFF6C63FF);
        buttonBackground.setCornerRadius(25);
        claimButton.setBackground(buttonBackground);
        
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        buttonParams.setMargins(0, 0, 0, 0);
        claimButton.setLayoutParams(buttonParams);
        
        claimButton.setOnClickListener(v -> claimDailyReward());
        
        mainCard.addView(claimButton);
        containerLayout.addView(mainCard);
    }

    private void createRewardHistorySection() {
        // History title
        TextView historyTitle = new TextView(this);
        historyTitle.setText("📜 Lịch sử nhận thưởng");
        historyTitle.setTextSize(18);
        historyTitle.setTextColor(0xFF333333);
        historyTitle.setPadding(16, 0, 0, 16);
        containerLayout.addView(historyTitle);
        
        // Load history button
        Button historyButton = new Button(this);
        historyButton.setText("Xem lịch sử nhận thưởng");
        historyButton.setTextSize(14);
        historyButton.setTextColor(0xFF6C63FF);
        historyButton.setBackgroundColor(Color.TRANSPARENT);
        historyButton.setPadding(16, 12, 16, 12);
        
        GradientDrawable historyBg = new GradientDrawable();
        historyBg.setStroke(2, 0xFF6C63FF);
        historyBg.setCornerRadius(20);
        historyButton.setBackground(historyBg);
        
        historyButton.setOnClickListener(v -> loadUserRewards());
        containerLayout.addView(historyButton);
    }

    private void initApiService() {
        try {
            PrefsManager prefsManager = new PrefsManager(this);
            Retrofit retrofit = ApiClient.getClient(prefsManager);
            dailyRewardService = retrofit.create(DailyRewardApiService.class);
            Log.d(TAG, "✅ API Service initialized");
        } catch (Exception e) {
            Log.e(TAG, "❌ Failed to init API service", e);
            showError("Không thể khởi tạo dịch vụ API");
        }
    }
    
    private void checkTodayReward() {
        Log.d(TAG, "🔄 Checking today's reward...");
        statusText.setText("Đang kiểm tra...");
        
        if (dailyRewardService == null) {
            updateRewardStatus(false, "Lỗi kết nối");
            return;
        }
        
        // Get current user ID from token (simplified to 2 for now)
        dailyRewardService.checkTodayReward(2).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse apiResponse = response.body();
                    
                    if (apiResponse.success) {
                        // Check if already claimed
                        boolean claimed = apiResponse.message != null && 
                                         apiResponse.message.contains("Already claimed");
                        updateRewardStatus(claimed, apiResponse.message);
                        Log.d(TAG, "✅ Reward status: " + (claimed ? "Claimed" : "Available"));
                    } else {
                        updateRewardStatus(false, "Có thể nhận thưởng");
                        Log.d(TAG, "⚠️ API returned success=false");
                    }
                } else {
                    updateRewardStatus(false, "Lỗi kiểm tra thưởng");
                    Log.e(TAG, "❌ Failed to check reward: " + response.code());
                }
            }
            
            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                updateRewardStatus(false, "Lỗi kết nối");
                Log.e(TAG, "❌ Network error checking reward", t);
            }
        });
    }
    
    private void updateRewardStatus(boolean claimed, String message) {
        isRewardClaimed = claimed;
        
        if (claimed) {
            statusText.setText("✅ Bạn đã nhận thưởng hôm nay!");
            statusText.setTextColor(0xFF4CAF50);
            
            claimButton.setText("ĐÃ NHẬN HÔM NAY");
            claimButton.setEnabled(false);
            
            GradientDrawable disabledBg = new GradientDrawable();
            disabledBg.setColor(0xFF9E9E9E);
            disabledBg.setCornerRadius(25);
            claimButton.setBackground(disabledBg);
        } else {
            statusText.setText("🎁 Thưởng hàng ngày có sẵn!");
            statusText.setTextColor(0xFFFF9800);
            
            claimButton.setText("NHẬN THƯỞNG");
            claimButton.setEnabled(true);
            
            GradientDrawable enabledBg = new GradientDrawable();
            enabledBg.setColor(0xFF6C63FF);
            enabledBg.setCornerRadius(25);
            claimButton.setBackground(enabledBg);
        }
    }
    
    private void claimDailyReward() {
        if (isRewardClaimed) {
            Toast.makeText(this, "Bạn đã nhận thưởng hôm nay rồi!", Toast.LENGTH_SHORT).show();
            return;
        }
        
        Log.d(TAG, "🎁 Claiming daily reward...");
        claimButton.setText("ĐANG NHẬN...");
        claimButton.setEnabled(false);
        
        DailyRewardApiService.ClaimRewardRequest request = 
            new DailyRewardApiService.ClaimRewardRequest(2, "Coins", 100, "Daily login reward");
        
        dailyRewardService.claimDailyReward(request).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse apiResponse = response.body();
                    
                    if (apiResponse.success) {
                        updateRewardStatus(true, "Đã nhận thưởng thành công!");
                        Toast.makeText(ApiDailyRewardActivity.this, 
                            "🎉 Nhận thưởng thành công! +100 coins", Toast.LENGTH_LONG).show();
                        Log.d(TAG, "✅ Reward claimed successfully");
                    } else {
                        updateRewardStatus(false, "Có thể nhận thưởng");
                        showError(apiResponse.message != null ? apiResponse.message : "Không thể nhận thưởng");
                    }
                } else {
                    updateRewardStatus(false, "Có thể nhận thưởng");
                    showError("Lỗi server: " + response.code());
                }
            }
            
            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                updateRewardStatus(false, "Có thể nhận thưởng");
                showError("Lỗi kết nối: " + t.getMessage());
                Log.e(TAG, "❌ Network error claiming reward", t);
            }
        });
    }
    
    private void loadUserRewards() {
        Log.d(TAG, "📜 Loading reward history...");
        
        // Create history display area
        LinearLayout historyContainer = new LinearLayout(this);
        historyContainer.setOrientation(LinearLayout.VERTICAL);
        historyContainer.setPadding(16, 16, 16, 16);
        
        GradientDrawable historyBg = new GradientDrawable();
        historyBg.setColor(Color.WHITE);
        historyBg.setCornerRadius(16);
        historyBg.setStroke(1, 0xFFE0E0E0);
        historyContainer.setBackground(historyBg);
        
        TextView loadingText = new TextView(this);
        loadingText.setText("🔄 Đang tải lịch sử...");
        loadingText.setTextSize(14);
        loadingText.setGravity(Gravity.CENTER);
        loadingText.setPadding(0, 20, 0, 20);
        historyContainer.addView(loadingText);
        
        LinearLayout.LayoutParams historyParams = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        historyParams.setMargins(0, 16, 0, 0);
        historyContainer.setLayoutParams(historyParams);
        
        containerLayout.addView(historyContainer);
        
        dailyRewardService.getUserDailyRewards(2).enqueue(new Callback<DailyRewardResponse>() {
            @Override
            public void onResponse(Call<DailyRewardResponse> call, Response<DailyRewardResponse> response) {
                historyContainer.removeView(loadingText);
                
                if (response.isSuccessful() && response.body() != null) {
                    DailyRewardResponse rewardResponse = response.body();
                    
                    if (rewardResponse.success && rewardResponse.data != null && !rewardResponse.data.isEmpty()) {
                        displayRewardHistory(historyContainer, rewardResponse.data);
                    } else {
                        TextView emptyText = new TextView(ApiDailyRewardActivity.this);
                        emptyText.setText("📝 Chưa có lịch sử nhận thưởng\nHãy nhận thưởng đầu tiên!");
                        emptyText.setTextSize(14);
                        emptyText.setTextColor(0xFF757575);
                        emptyText.setGravity(Gravity.CENTER);
                        emptyText.setPadding(0, 20, 0, 20);
                        historyContainer.addView(emptyText);
                    }
                } else {
                    TextView errorText = new TextView(ApiDailyRewardActivity.this);
                    errorText.setText("❌ Không thể tải lịch sử");
                    errorText.setTextSize(14);
                    errorText.setTextColor(0xFFE91E63);
                    errorText.setGravity(Gravity.CENTER);
                    historyContainer.addView(errorText);
                }
            }
            
            @Override
            public void onFailure(Call<DailyRewardResponse> call, Throwable t) {
                historyContainer.removeView(loadingText);
                TextView errorText = new TextView(ApiDailyRewardActivity.this);
                errorText.setText("❌ Lỗi kết nối");
                errorText.setTextSize(14);
                errorText.setTextColor(0xFFE91E63);
                errorText.setGravity(Gravity.CENTER);
                historyContainer.addView(errorText);
            }
        });
    }
    
    private void displayRewardHistory(LinearLayout container, java.util.List<DailyRewardResponse.DailyRewardData> rewards) {
        for (DailyRewardResponse.DailyRewardData reward : rewards) {
            LinearLayout rewardItem = new LinearLayout(this);
            rewardItem.setOrientation(LinearLayout.HORIZONTAL);
            rewardItem.setPadding(16, 12, 16, 12);
            rewardItem.setGravity(Gravity.CENTER_VERTICAL);
            
            // Reward icon
            TextView icon = new TextView(this);
            icon.setText(getRewardEmoji(reward.LoaiThuong));
            icon.setTextSize(20);
            icon.setPadding(0, 0, 16, 0);
            rewardItem.addView(icon);
            
            // Reward info
            LinearLayout infoLayout = new LinearLayout(this);
            infoLayout.setOrientation(LinearLayout.VERTICAL);
            infoLayout.setLayoutParams(new LinearLayout.LayoutParams(0, 
                LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
            
            TextView amountText = new TextView(this);
            amountText.setText(reward.GiaTri + " " + reward.LoaiThuong);
            amountText.setTextSize(16);
            amountText.setTextColor(0xFF333333);
            infoLayout.addView(amountText);
            
            TextView dateText = new TextView(this);
            dateText.setText(reward.NgayNhan.substring(0, 10));
            dateText.setTextSize(12);
            dateText.setTextColor(0xFF757575);
            infoLayout.addView(dateText);
            
            rewardItem.addView(infoLayout);
            container.addView(rewardItem);
            
            // Separator
            if (rewards.indexOf(reward) < rewards.size() - 1) {
                TextView separator = new TextView(this);
                separator.setHeight(1);
                separator.setBackgroundColor(0xFFE0E0E0);
                container.addView(separator);
            }
        }
    }
    
    private String getRewardEmoji(String rewardType) {
        switch (rewardType.toLowerCase()) {
            case "coins": return "💰";
            case "gems": return "💎";
            case "points": return "⭐";
            default: return "🎁";
        }
    }
    
    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        Log.e(TAG, "Error: " + message);
    }
}