package com.example.iq5.debug;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iq5.core.network.ApiClient;
import com.example.iq5.core.network.DailyRewardApiService;
import com.example.iq5.core.network.UserApiService;
import com.example.iq5.core.prefs.PrefsManager;
import com.example.iq5.data.model.ApiResponse;
import com.example.iq5.data.model.UserProfileModel;
import com.example.iq5.feature.auth.ui.ApiProfileActivity;
import com.example.iq5.feature.reward.ui.ApiDailyRewardActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Test activity for Daily Reward and User Stats
 */
public class TestRewardAndStatsActivity extends AppCompatActivity {

    private static final String TAG = "TestRewardAndStats";
    
    private TextView tvResult;
    private DailyRewardApiService dailyRewardService;
    private UserApiService userApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        createLayout();
        initApiServices();
    }

    private void createLayout() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(32, 32, 32, 32);
        layout.setBackgroundColor(0xFFF5F5F5);
        
        // Title
        TextView title = new TextView(this);
        title.setText("🎁📊 TEST REWARD & STATS");
        title.setTextSize(20);
        title.setTextColor(0xFF333333);
        title.setPadding(0, 0, 0, 24);
        layout.addView(title);
        
        // Daily Reward Tests
        TextView rewardTitle = new TextView(this);
        rewardTitle.setText("🎁 DAILY REWARD TESTS:");
        rewardTitle.setTextSize(16);
        rewardTitle.setTextColor(0xFF2196F3);
        rewardTitle.setPadding(0, 0, 0, 16);
        layout.addView(rewardTitle);
        
        Button btnCheckReward = new Button(this);
        btnCheckReward.setText("📅 Check Today's Reward");
        btnCheckReward.setOnClickListener(v -> testCheckTodayReward());
        layout.addView(btnCheckReward);
        
        Button btnClaimReward = new Button(this);
        btnClaimReward.setText("🎁 Claim Daily Reward");
        btnClaimReward.setOnClickListener(v -> testClaimReward());
        layout.addView(btnClaimReward);
        
        Button btnOpenRewardActivity = new Button(this);
        btnOpenRewardActivity.setText("🔗 Open Reward Activity");
        btnOpenRewardActivity.setOnClickListener(v -> openRewardActivity());
        layout.addView(btnOpenRewardActivity);
        
        // User Stats Tests
        TextView statsTitle = new TextView(this);
        statsTitle.setText("📊 USER STATS TESTS:");
        statsTitle.setTextSize(16);
        statsTitle.setTextColor(0xFF4CAF50);
        statsTitle.setPadding(0, 24, 0, 16);
        layout.addView(statsTitle);
        
        Button btnTestProfile = new Button(this);
        btnTestProfile.setText("👤 Test Profile API");
        btnTestProfile.setOnClickListener(v -> testProfileApi());
        layout.addView(btnTestProfile);
        
        Button btnOpenProfile = new Button(this);
        btnOpenProfile.setText("🔗 Open Profile Activity");
        btnOpenProfile.setOnClickListener(v -> openProfileActivity());
        layout.addView(btnOpenProfile);
        
        // Result
        tvResult = new TextView(this);
        tvResult.setText("Nhấn button để test...");
        tvResult.setTextSize(12);
        tvResult.setPadding(0, 24, 0, 0);
        tvResult.setTextColor(0xFF666666);
        layout.addView(tvResult);
        
        setContentView(layout);
    }

    private void initApiServices() {
        try {
            PrefsManager prefsManager = new PrefsManager(this);
            Retrofit retrofit = ApiClient.getClient(prefsManager);
            
            dailyRewardService = retrofit.create(DailyRewardApiService.class);
            userApiService = retrofit.create(UserApiService.class);
            
            updateResult("✅ API Services initialized");
            Log.d(TAG, "✅ API Services initialized");
        } catch (Exception e) {
            updateResult("❌ Failed to init API services: " + e.getMessage());
            Log.e(TAG, "❌ Failed to init API services", e);
        }
    }

    private void testCheckTodayReward() {
        updateResult("🔄 Testing check today's reward...");
        
        if (dailyRewardService == null) {
            updateResult("❌ Daily Reward Service not initialized");
            return;
        }

        try {
            Call<ApiResponse> call = dailyRewardService.checkTodayReward(2);
            
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        ApiResponse apiResponse = response.body();
                        String result = "✅ CHECK REWARD SUCCESS!\n\n" +
                                "Success: " + apiResponse.success + "\n" +
                                "Message: " + apiResponse.message + "\n" +
                                "Response code: " + response.code();
                        
                        updateResult(result);
                        Toast.makeText(TestRewardAndStatsActivity.this, 
                            "✅ " + apiResponse.message, Toast.LENGTH_SHORT).show();
                        
                        Log.d(TAG, "✅ Check reward success: " + apiResponse.message);
                    } else {
                        handleApiError("Check Reward", response);
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    handleNetworkError("Check Reward", t);
                }
            });
            
        } catch (Exception e) {
            updateResult("❌ Exception in check reward: " + e.getMessage());
            Log.e(TAG, "❌ Exception in check reward", e);
        }
    }

    private void testClaimReward() {
        updateResult("🔄 Testing claim daily reward...");
        
        if (dailyRewardService == null) {
            updateResult("❌ Daily Reward Service not initialized");
            return;
        }

        try {
            DailyRewardApiService.ClaimRewardRequest request = 
                new DailyRewardApiService.ClaimRewardRequest(2, "Coins", 100, "Daily login reward");
            
            Call<ApiResponse> call = dailyRewardService.claimDailyReward(request);
            
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        ApiResponse apiResponse = response.body();
                        String result = "✅ CLAIM REWARD SUCCESS!\n\n" +
                                "Success: " + apiResponse.success + "\n" +
                                "Message: " + apiResponse.message + "\n" +
                                "Response code: " + response.code();
                        
                        updateResult(result);
                        Toast.makeText(TestRewardAndStatsActivity.this, 
                            "🎉 " + apiResponse.message, Toast.LENGTH_SHORT).show();
                        
                        Log.d(TAG, "✅ Claim reward success: " + apiResponse.message);
                    } else {
                        handleApiError("Claim Reward", response);
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    handleNetworkError("Claim Reward", t);
                }
            });
            
        } catch (Exception e) {
            updateResult("❌ Exception in claim reward: " + e.getMessage());
            Log.e(TAG, "❌ Exception in claim reward", e);
        }
    }

    private void testProfileApi() {
        updateResult("🔄 Testing profile API...");
        
        if (userApiService == null) {
            updateResult("❌ User API Service not initialized");
            return;
        }

        try {
            Call<UserProfileModel> call = userApiService.getMyProfile();
            
            call.enqueue(new Callback<UserProfileModel>() {
                @Override
                public void onResponse(Call<UserProfileModel> call, Response<UserProfileModel> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        UserProfileModel profile = response.body();
                        String result = "✅ PROFILE API SUCCESS!\n\n" +
                                "Name: " + profile.getHoTen() + "\n" +
                                "Email: " + profile.getEmail() + "\n" +
                                "Role: " + profile.getVaiTro() + "\n";
                        
                        if (profile.getThongKe() != null) {
                            UserProfileModel.ThongKeModel stats = profile.getThongKe();
                            result += "\n📊 STATS:\n" +
                                    "Quiz completed: " + stats.getSoBaiQuizHoanThanh() + "\n" +
                                    "Average score: " + String.format("%.1f", stats.getDiemTrungBinh()) + "\n" +
                                    "Accuracy: " + String.format("%.1f%%", stats.getTyLeDung());
                        }
                        
                        updateResult(result);
                        Toast.makeText(TestRewardAndStatsActivity.this, 
                            "✅ Profile loaded: " + profile.getHoTen(), Toast.LENGTH_SHORT).show();
                        
                        Log.d(TAG, "✅ Profile API success: " + profile.getHoTen());
                    } else {
                        handleApiError("Profile API", response);
                    }
                }

                @Override
                public void onFailure(Call<UserProfileModel> call, Throwable t) {
                    handleNetworkError("Profile API", t);
                }
            });
            
        } catch (Exception e) {
            updateResult("❌ Exception in profile API: " + e.getMessage());
            Log.e(TAG, "❌ Exception in profile API", e);
        }
    }

    private void openRewardActivity() {
        try {
            Intent intent = new Intent(this, ApiDailyRewardActivity.class);
            startActivity(intent);
            Toast.makeText(this, "🎁 Opening Daily Reward Activity...", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            updateResult("❌ Error opening Reward Activity: " + e.getMessage());
            Log.e(TAG, "❌ Error opening reward activity", e);
        }
    }

    private void openProfileActivity() {
        try {
            Intent intent = new Intent(this, ApiProfileActivity.class);
            startActivity(intent);
            Toast.makeText(this, "👤 Opening Profile Activity...", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            updateResult("❌ Error opening Profile Activity: " + e.getMessage());
            Log.e(TAG, "❌ Error opening profile activity", e);
        }
    }

    private void handleApiError(String apiName, Response<?> response) {
        String result = "❌ " + apiName.toUpperCase() + " FAILED!\n\n" +
                "Response code: " + response.code() + "\n" +
                "Message: " + response.message() + "\n\n";
        
        if (response.code() == 401) {
            result += "❌ 401 UNAUTHORIZED\n" +
                    "Token không hợp lệ hoặc hết hạn.\n" +
                    "Hãy đăng nhập lại.";
        }
        
        try {
            if (response.errorBody() != null) {
                result += "Error body: " + response.errorBody().string();
            }
        } catch (Exception e) {
            result += "Error reading error body: " + e.getMessage();
        }
        
        updateResult(result);
        Toast.makeText(this, "❌ " + apiName + " Error: " + response.code(), Toast.LENGTH_LONG).show();
        Log.e(TAG, "❌ " + apiName + " failed: " + response.code());
    }

    private void handleNetworkError(String apiName, Throwable t) {
        String result = "❌ " + apiName.toUpperCase() + " NETWORK ERROR!\n\n" +
                "Error: " + t.getMessage() + "\n" +
                "Type: " + t.getClass().getSimpleName() + "\n\n" +
                "Có thể:\n" +
                "- Backend chưa chạy (http://localhost:5048)\n" +
                "- Không có kết nối mạng\n" +
                "- URL sai (emulator cần 10.0.2.2)";
        
        updateResult(result);
        Toast.makeText(this, "❌ " + apiName + " Network Error", Toast.LENGTH_LONG).show();
        Log.e(TAG, "❌ " + apiName + " network error", t);
    }

    private void updateResult(String text) {
        runOnUiThread(() -> {
            if (tvResult != null) {
                tvResult.setText(text);
            }
        });
    }
}