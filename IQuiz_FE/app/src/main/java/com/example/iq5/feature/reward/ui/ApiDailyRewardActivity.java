package com.example.iq5.feature.reward.ui;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
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
import com.example.iq5.data.model.ClaimRewardRequest;
import com.example.iq5.data.model.DailyRewardResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

// THÊM CÁC IMPORT CẦN THIẾT
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

public class ApiDailyRewardActivity extends AppCompatActivity {

    private static final String TAG = "ApiDailyRewardActivity";

    private LinearLayout containerLayout;
    private DailyRewardApiService dailyRewardService;
    private TextView statusText;
    private Button claimButton;
    private boolean isRewardClaimed = false;

    // THÊM BIẾN CHO TEST BUTTONS
    private LinearLayout testButtonsContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "🚀 Activity created");

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

        // THÊM PHẦN TEST API
        createTestSection();

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

    // THÊM PHẦN TẠO SECTION TEST
    private void createTestSection() {
        // Test section title
        TextView testTitle = new TextView(this);
        testTitle.setText("🧪 Test API");
        testTitle.setTextSize(16);
        testTitle.setTextColor(0xFF666666);
        testTitle.setPadding(16, 32, 0, 8);
        containerLayout.addView(testTitle);

        // Container cho các nút test
        testButtonsContainer = new LinearLayout(this);
        testButtonsContainer.setOrientation(LinearLayout.VERTICAL);
        testButtonsContainer.setPadding(16, 0, 16, 0);

        LinearLayout.LayoutParams testContainerParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        testContainerParams.setMargins(0, 0, 0, 32);
        testButtonsContainer.setLayoutParams(testContainerParams);

        // Nút test đơn giản
        addTestButton("Test với UserID = 2", () -> testWithFixedUserId(2));
        addTestButton("Test với UserID = 3", () -> testWithFixedUserId(3));
        addTestButton("Test với UserID = 999 (không tồn tại)", () -> testWithFixedUserId(999));
        addTestButton("Test Token Parse", this::testTokenParse);
        addTestButton("Test Base URL", this::testBaseUrl);
        addTestButton("Test API Check Today (tất cả user)", this::testAllUsers);

        containerLayout.addView(testButtonsContainer);
    }

    private void addTestButton(String text, Runnable action) {
        Button testButton = new Button(this);
        testButton.setText(text);
        testButton.setTextSize(12);
        testButton.setTextColor(0xFF795548);
        testButton.setBackgroundColor(Color.TRANSPARENT);
        testButton.setPadding(16, 8, 16, 8);

        GradientDrawable testBg = new GradientDrawable();
        testBg.setStroke(1, 0xFF795548);
        testBg.setCornerRadius(10);
        testButton.setBackground(testBg);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 4, 0, 4);
        testButton.setLayoutParams(params);

        testButton.setOnClickListener(v -> {
            Toast.makeText(this, "Testing: " + text, Toast.LENGTH_SHORT).show();
            action.run();
        });

        testButtonsContainer.addView(testButton);
    }

    private void initApiService() {
        try {
            PrefsManager prefsManager = new PrefsManager(this);

            // Dùng ApiClient.getClient8084() với auth interceptor
            Retrofit retrofit = ApiClient.getClient8084(prefsManager);
            dailyRewardService = retrofit.create(DailyRewardApiService.class);

            Log.d(TAG, "✅ API Service initialized for port 8084");
            String baseUrl = ApiClient.getBaseUrl8084();
            Log.d(TAG, "🌐 Base URL: " + baseUrl);

            // Test URL connection
            testUrlConnection(baseUrl);

        } catch (Exception e) {
            Log.e(TAG, "❌ Failed to init API service", e);
            showError("Không thể khởi tạo dịch vụ API");
        }
    }

    private void testUrlConnection(String baseUrl) {
        new Thread(() -> {
            try {
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(5, TimeUnit.SECONDS)
                        .readTimeout(5, TimeUnit.SECONDS)
                        .build();

                Request request = new Request.Builder()
                        .url(baseUrl)
                        .head()
                        .build();

                okhttp3.Response response = client.newCall(request).execute();
                Log.d(TAG, "🔗 URL Connection Test: " +
                        (response.isSuccessful() ? "✅ Connected" : "❌ Failed: " + response.code()));
                response.close();
            } catch (Exception e) {
                Log.e(TAG, "🔗 URL Connection Test Failed: " + e.getMessage());
            }
        }).start();
    }

    private void checkTodayReward() {
        Log.d(TAG, "🔍 ========== CHECK TODAY REWARD ==========");

        PrefsManager prefsManager = new PrefsManager(this);
        String token = prefsManager.getAuthToken();

        if (token == null || token.isEmpty()) {
            Log.e(TAG, "❌ Token is NULL or EMPTY");
            updateRewardStatus(false, "Vui lòng đăng nhập");
            return;
        }

        Log.d(TAG, "✅ Token exists, length: " + token.length());

        String authHeader = "Bearer " + token;
        Log.d(TAG, "Auth Header: " + authHeader.substring(0, Math.min(30, authHeader.length())) + "...");

        // Lấy userId
        int userId = getUserIdFromToken(token);
        Log.d(TAG, "📋 UserID to use: " + userId);

        if (userId == 0) {
            Log.e(TAG, "❌ UserID is 0, can't proceed");
            updateRewardStatus(false, "Không tìm thấy user. Vui lòng đăng nhập lại");
            return;
        }

        // DEBUG: Log URL và parameters
        String baseUrl = ApiClient.getBaseUrl8084();
        Log.d(TAG, "🌐 Base URL: " + baseUrl);
        Log.d(TAG, "📤 API Call: checkTodayReward");
        Log.d(TAG, "📝 Parameters: userId=" + userId);

        // Gọi API
        dailyRewardService.checkTodayReward(userId, authHeader).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Log.d(TAG, "📥 Response received");
                Log.d(TAG, "📊 Response Code: " + response.code());
                Log.d(TAG, "📊 Response Successful: " + response.isSuccessful());

                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse apiResponse = response.body();
                    Log.d(TAG, "📄 Response Body Success: " + apiResponse.success);
                    Log.d(TAG, "📄 Response Message: " + apiResponse.message);

                    if (apiResponse.success) {
                        boolean claimed = apiResponse.message != null &&
                                (apiResponse.message.contains("Already claimed") ||
                                        apiResponse.message.contains("đã nhận"));
                        updateRewardStatus(claimed, apiResponse.message);
                        Log.d(TAG, "✅ Reward check: " + (claimed ? "Claimed" : "Available"));
                    } else {
                        updateRewardStatus(false, "Thưởng có sẵn!");
                        Log.w(TAG, "⚠️ API check returned success=false");
                    }
                } else {
                    Log.e(TAG, "❌ Response NOT successful or body is null");
                    if (response.errorBody() != null) {
                        try {
                            String errorBody = response.errorBody().string();
                            Log.e(TAG, "❌ Error Body: " + errorBody);
                        } catch (Exception e) {
                            Log.e(TAG, "❌ Can't read error body", e);
                        }
                    }
                    updateRewardStatus(false, "Thưởng có sẵn!");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e(TAG, "❌ Network failure", t);
                updateRewardStatus(false, "Lỗi kết nối");
            }
        });
    }

    // ==================== TEST METHODS ====================

    private void testWithFixedUserId(int userId) {
        Log.d(TAG, "🧪 ========== TEST WITH FIXED USERID = " + userId + " ==========");

        PrefsManager prefsManager = new PrefsManager(this);
        String token = prefsManager.getAuthToken();

        if (token == null || token.isEmpty()) {
            Toast.makeText(this, "Chưa đăng nhập", Toast.LENGTH_SHORT).show();
            return;
        }

        String authHeader = "Bearer " + token;

        // Gọi API với userID cố định
        dailyRewardService.checkTodayReward(userId, authHeader).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                String result = "UserID " + userId + ": Code=" + response.code();

                if (response.isSuccessful() && response.body() != null) {
                    result += ", Success=" + response.body().success;
                    result += ", Msg=" + response.body().message;
                } else {
                    result += ", Error";
                }

                Log.d(TAG, "🧪 " + result);
                Toast.makeText(ApiDailyRewardActivity.this,
                        "Test " + userId + ": " + response.code(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e(TAG, "🧪 UserID " + userId + " FAILED: " + t.getMessage());
                Toast.makeText(ApiDailyRewardActivity.this,
                        "Test " + userId + " failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void testTokenParse() {
        Log.d(TAG, "🧪 ========== TEST TOKEN PARSE ==========");

        PrefsManager prefsManager = new PrefsManager(this);
        String token = prefsManager.getAuthToken();

        if (token == null || token.isEmpty()) {
            Log.e(TAG, "❌ Token is null");
            Toast.makeText(this, "Token is null", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d(TAG, "🧪 Token length: " + token.length());
        Log.d(TAG, "🧪 Token preview: " + token.substring(0, Math.min(50, token.length())) + "...");

        // Test parse
        int userId = getUserIdFromToken(token);
        Log.d(TAG, "🧪 Parsed UserID: " + userId);

        Toast.makeText(this, "Token parsed, UserID=" + userId, Toast.LENGTH_SHORT).show();
    }

    private void testBaseUrl() {
        Log.d(TAG, "🧪 ========== TEST BASE URL ==========");

        String baseUrl = ApiClient.getBaseUrl8084();
        Log.d(TAG, "🧪 Base URL: " + baseUrl);

        // Test endpoint
        String testUrl = baseUrl + "api/DailyReward/check-today?userId=2";
        Log.d(TAG, "🧪 Full Test URL: " + testUrl);

        Toast.makeText(this, "Base URL: " + baseUrl, Toast.LENGTH_LONG).show();
    }

    private void testAllUsers() {
        Log.d(TAG, "🧪 ========== TEST ALL USERS ==========");

        int[] testUserIds = {1, 2, 3, 999}; // Bao gồm user không tồn tại

        for (int userId : testUserIds) {
            testSingleUser(userId);
        }
    }

    private void testSingleUser(int userId) {
        PrefsManager prefsManager = new PrefsManager(this);
        String token = prefsManager.getAuthToken();

        if (token == null || token.isEmpty()) {
            return;
        }

        String authHeader = "Bearer " + token;

        // Tạo client mới cho test
        OkHttpClient testClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Content-Type", "application/json")
                            .header("Authorization", authHeader);
                    return chain.proceed(requestBuilder.build());
                })
                .build();

        Retrofit testRetrofit = new Retrofit.Builder()
                .baseUrl(ApiClient.getBaseUrl8084())
                .addConverterFactory(GsonConverterFactory.create())
                .client(testClient)
                .build();

        DailyRewardApiService testService = testRetrofit.create(DailyRewardApiService.class);

        testService.checkTodayReward(userId, authHeader).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                String result = "🧪 UserID " + userId + " -> Code: " + response.code();

                if (response.isSuccessful() && response.body() != null) {
                    result += ", Success: " + response.body().success;
                    result += ", Message: " + response.body().message;
                }

                Log.d(TAG, result);
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e(TAG, "🧪 UserID " + userId + " -> FAILED: " + t.getMessage());
            }
        });
    }

    // ==================== END TEST METHODS ====================

    // THÊM PHƯƠNG THỨC: Lấy userId từ JWT token
    private int getUserIdFromToken(String token) {
        try {
            Log.d(TAG, "🔍 Parsing JWT token...");

            // Kiểm tra token đơn giản trước
            if (token.contains(".")) {
                String[] parts = token.split("\\.");
                if (parts.length < 2) {
                    Log.e(TAG, "❌ Invalid JWT token format");
                    return 0;
                }

                String payload = parts[1];
                // Thêm padding nếu cần
                while (payload.length() % 4 != 0) {
                    payload += "=";
                }

                byte[] decodedBytes = android.util.Base64.decode(payload, android.util.Base64.DEFAULT);
                String decodedPayload = new String(decodedBytes, "UTF-8");

                Log.d(TAG, "🔍 Full JWT Payload: " + decodedPayload);

                // THỬ CÁC TRƯỜNG KHÁC NHAU
                String[] possibleFields = {"nameid", "sub", "userId", "uid", "user_id", "id"};

                for (String field : possibleFields) {
                    if (decodedPayload.contains("\"" + field + "\":")) {
                        // Tìm giá trị dạng số hoặc chuỗi
                        String pattern = "\"" + field + "\":\"?(\\d+)\"?";
                        java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
                        java.util.regex.Matcher m = p.matcher(decodedPayload);

                        if (m.find()) {
                            try {
                                int userId = Integer.parseInt(m.group(1));
                                Log.d(TAG, "✅ Found UserID in field '" + field + "': " + userId);
                                return userId;
                            } catch (NumberFormatException e) {
                                Log.e(TAG, "❌ Field '" + field + "' value is not a number");
                            }
                        }
                    }
                }

                Log.e(TAG, "❌ No UserID found in JWT token");

            } else {
                Log.e(TAG, "❌ Token doesn't appear to be JWT format");
            }

            // FALLBACK: Nếu không parse được, thử userID cố định để test
            Log.w(TAG, "⚠️ Falling back to test UserID = 2");
            return 2; // Dùng player01 để test

        } catch (Exception e) {
            Log.e(TAG, "❌ Error parsing JWT token", e);
            // FALLBACK
            return 2;
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

        // Get token
        PrefsManager prefsManager = new PrefsManager(this);
        String token = prefsManager.getAuthToken();
        if (token == null || token.isEmpty()) {
            showError("Chưa đăng nhập");
            resetClaimButton();
            return;
        }

        // SỬA: Lấy userId từ token
        int userId = getUserIdFromToken(token);

        // DEBUG LOG
        Log.d(TAG, "=== DEBUG CLAIM REWARD ===");
        Log.d(TAG, "User ID for claim: " + userId);

        if (userId == 0) {
            showError("Không tìm thấy user. Vui lòng đăng nhập lại");
            resetClaimButton();
            return;
        }

        ClaimRewardRequest request = new ClaimRewardRequest(
                userId,
                "Coins",
                100,
                "Daily login reward"
        );

        String authHeader = "Bearer " + token;

        // Gọi API claim
        dailyRewardService.claimDailyReward(authHeader, request).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse apiResponse = response.body();

                    if (apiResponse.success) {
                        updateRewardStatus(true, "Đã nhận thưởng thành công!");
                        Toast.makeText(ApiDailyRewardActivity.this,
                                "🎉 Nhận thưởng thành công! +100 coins", Toast.LENGTH_LONG).show();
                        Log.d(TAG, "✅ Reward claimed successfully");

                        // Tự động load lại lịch sử
                        loadUserRewards();
                    } else {
                        updateRewardStatus(false, "Có thể nhận thưởng");
                        showError(apiResponse.message != null ? apiResponse.message : "Không thể nhận thưởng");
                        resetClaimButton();
                    }
                } else {
                    updateRewardStatus(false, "Có thể nhận thưởng");
                    // Parse error message từ server
                    if (response.errorBody() != null) {
                        try {
                            String errorBody = response.errorBody().string();
                            if (errorBody.contains("Không tìm thấy user") || errorBody.contains("User not found")) {
                                showError("Không tìm thấy user trong hệ thống");
                            } else {
                                showError("Lỗi server: " + response.code());
                            }
                        } catch (Exception e) {
                            showError("Lỗi server: " + response.code());
                        }
                    } else {
                        showError("Lỗi server: " + response.code());
                    }
                    resetClaimButton();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                updateRewardStatus(false, "Có thể nhận thưởng");
                showError("Lỗi kết nối: " + t.getMessage());
                Log.e(TAG, "❌ Network error claiming reward", t);
                resetClaimButton();
            }
        });
    }

    private void resetClaimButton() {
        runOnUiThread(() -> {
            claimButton.setText("NHẬN THƯỞNG");
            claimButton.setEnabled(true);
        });
    }

    private void updateRewardStatus(boolean claimed, String message) {
        isRewardClaimed = claimed;

        runOnUiThread(() -> {
            if (claimed) {
                statusText.setText("✅ " + message);
                statusText.setTextColor(0xFF4CAF50);

                claimButton.setText("ĐÃ NHẬN HÔM NAY");
                claimButton.setEnabled(false);

                GradientDrawable disabledBg = new GradientDrawable();
                disabledBg.setColor(0xFF9E9E9E);
                disabledBg.setCornerRadius(25);
                claimButton.setBackground(disabledBg);
            } else {
                statusText.setText("🎁 " + message);
                statusText.setTextColor(0xFFFF9800);

                claimButton.setText("NHẬN THƯỞNG");
                claimButton.setEnabled(true);

                GradientDrawable enabledBg = new GradientDrawable();
                enabledBg.setColor(0xFF6C63FF);
                enabledBg.setCornerRadius(25);
                claimButton.setBackground(enabledBg);
            }
        });
    }

    private void loadUserRewards() {
        Log.d(TAG, "📜 Loading reward history...");

        // Get token
        PrefsManager prefsManager = new PrefsManager(this);
        String token = prefsManager.getAuthToken();
        if (token == null || token.isEmpty()) {
            Toast.makeText(this, "Chưa đăng nhập", Toast.LENGTH_SHORT).show();
            return;
        }
        String authHeader = "Bearer " + token;

        // SỬA: Lấy userId từ token
        int userId = getUserIdFromToken(token);

        if (userId == 0) {
            Toast.makeText(this, "Không tìm thấy user", Toast.LENGTH_SHORT).show();
            return;
        }

        dailyRewardService.getUserDailyRewards(userId, authHeader).enqueue(new Callback<DailyRewardResponse>() {
            @Override
            public void onResponse(Call<DailyRewardResponse> call, Response<DailyRewardResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    DailyRewardResponse rewardResponse = response.body();

                    if (rewardResponse.success && rewardResponse.data != null && !rewardResponse.data.isEmpty()) {
                        // Tạo container hiển thị lịch sử
                        LinearLayout historyContainer = new LinearLayout(ApiDailyRewardActivity.this);
                        historyContainer.setOrientation(LinearLayout.VERTICAL);
                        historyContainer.setPadding(16, 16, 16, 16);

                        GradientDrawable historyBg = new GradientDrawable();
                        historyBg.setColor(Color.WHITE);
                        historyBg.setCornerRadius(16);
                        historyBg.setStroke(1, 0xFFE0E0E0);
                        historyContainer.setBackground(historyBg);

                        displayRewardHistory(historyContainer, rewardResponse.data);

                        // Thêm vào layout chính
                        LinearLayout.LayoutParams historyParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        historyParams.setMargins(0, 16, 0, 0);
                        historyContainer.setLayoutParams(historyParams);

                        // Tìm và xóa container cũ nếu có
                        int childCount = containerLayout.getChildCount();
                        for (int i = childCount - 1; i >= 0; i--) {
                            View child = containerLayout.getChildAt(i);
                            if (child instanceof LinearLayout && child != historyContainer) {
                                // Kiểm tra nếu là history container cũ
                                containerLayout.removeViewAt(i);
                            }
                        }

                        containerLayout.addView(historyContainer);
                    } else {
                        Toast.makeText(ApiDailyRewardActivity.this, "Chưa có lịch sử nhận thưởng", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Kiểm tra lỗi cụ thể
                    if (response.code() == 404) {
                        showError("Không tìm thấy lịch sử cho user này");
                    } else {
                        showError("Không thể tải lịch sử: " + response.code());
                    }
                }
            }

            @Override
            public void onFailure(Call<DailyRewardResponse> call, Throwable t) {
                showError("Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    private void displayRewardHistory(LinearLayout container, java.util.List<DailyRewardResponse.DailyRewardData> rewards) {
        // Clear existing views
        container.removeAllViews();

        for (DailyRewardResponse.DailyRewardData reward : rewards) {
            LinearLayout rewardItem = new LinearLayout(this);
            rewardItem.setOrientation(LinearLayout.HORIZONTAL);
            rewardItem.setPadding(16, 12, 16, 12);
            rewardItem.setGravity(Gravity.CENTER_VERTICAL);

            // Reward icon
            TextView icon = new TextView(this);
            String loaiThuong = reward.claimType != null ? reward.claimType : "Coins";
            icon.setText(getRewardEmoji(loaiThuong));
            icon.setTextSize(20);
            icon.setPadding(0, 0, 16, 0);
            rewardItem.addView(icon);

            // Reward info
            LinearLayout infoLayout = new LinearLayout(this);
            infoLayout.setOrientation(LinearLayout.VERTICAL);
            infoLayout.setLayoutParams(new LinearLayout.LayoutParams(0,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));

            TextView amountText = new TextView(this);
            // === SỬA DÒNG NÀY ===
            int giaTri = reward.GiaTri > 0 ? reward.GiaTri : 100;
            amountText.setText(giaTri + " " + loaiThuong);
            amountText.setTextSize(16);
            amountText.setTextColor(0xFF333333);
            infoLayout.addView(amountText);

            TextView dateText = new TextView(this);
            String dateStr = reward.claimedOn != null ? reward.claimedOn : "";
            if (dateStr.length() >= 10) {
                dateStr = dateStr.substring(0, 10);
            }
            dateText.setText(dateStr);
            dateText.setTextSize(12);
            dateText.setTextColor(0xFF757575);
            infoLayout.addView(dateText);

            rewardItem.addView(infoLayout);
            container.addView(rewardItem);

            // Separator
            if (rewards.indexOf(reward) < rewards.size() - 1) {
                View separator = new View(this);
                separator.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, 1
                ));
                separator.setBackgroundColor(0xFFE0E0E0);
                container.addView(separator);
            }
        }
    }

    private String getRewardEmoji(String rewardType) {
        if (rewardType == null) {
            return "🎁";
        }

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