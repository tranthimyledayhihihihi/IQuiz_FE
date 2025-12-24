package com.example.iq5.debug;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iq5.core.network.ApiClient;
import com.example.iq5.core.network.UserApiService;
import com.example.iq5.core.prefs.PrefsManager;
import com.example.iq5.data.model.UserStreakResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Simple test activity for Streak API
 */
public class TestStreakActivity extends AppCompatActivity {

    private static final String TAG = "TestStreakActivity";
    
    private TextView tvResult;
    private UserApiService userApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        createLayout();
        initApiService();
    }

    private void createLayout() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(32, 32, 32, 32);
        layout.setBackgroundColor(0xFFF5F5F5);
        
        // Title
        TextView title = new TextView(this);
        title.setText("🔥 STREAK API TEST");
        title.setTextSize(20);
        title.setTextColor(0xFF333333);
        title.setPadding(0, 0, 0, 24);
        layout.addView(title);
        
        // Test button
        Button btnTest = new Button(this);
        btnTest.setText("🧪 Test Streak API");
        btnTest.setTextSize(16);
        btnTest.setPadding(16, 16, 16, 16);
        btnTest.setOnClickListener(v -> testStreakApi());
        layout.addView(btnTest);
        
        // Open Streak Activity button
        Button btnOpenStreak = new Button(this);
        btnOpenStreak.setText("🔥 Open Streak Activity");
        btnOpenStreak.setTextSize(16);
        btnOpenStreak.setPadding(16, 16, 16, 16);
        btnOpenStreak.setOnClickListener(v -> openStreakActivity());
        layout.addView(btnOpenStreak);
        
        // Result
        tvResult = new TextView(this);
        tvResult.setText("Nhấn button để test...");
        tvResult.setTextSize(12);
        tvResult.setPadding(0, 24, 0, 0);
        tvResult.setTextColor(0xFF666666);
        layout.addView(tvResult);
        
        setContentView(layout);
    }

    private void initApiService() {
        try {
            PrefsManager prefsManager = new PrefsManager(this);
            Retrofit retrofit = ApiClient.getClient(prefsManager);
            userApiService = retrofit.create(UserApiService.class);
            
            Log.d(TAG, "✅ API Service initialized");
            updateResult("✅ API Service đã khởi tạo thành công");
        } catch (Exception e) {
            Log.e(TAG, "❌ Failed to init API service", e);
            updateResult("❌ Lỗi khởi tạo API: " + e.getMessage());
        }
    }

    private void testStreakApi() {
        updateResult("🔄 Đang test Streak API...");
        
        // Kiểm tra token trước
        PrefsManager prefsManager = new PrefsManager(this);
        String token = prefsManager.getAuthToken();
        
        if (token == null || token.isEmpty()) {
            updateResult("❌ KHÔNG CÓ TOKEN!\n\n" +
                    "User chưa đăng nhập.\n" +
                    "Hãy đăng nhập trước khi test Streak API.\n\n" +
                    "Hoặc dùng token test:\n" +
                    "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...");
            return;
        }
        
        updateResult("🔄 Token found: " + token.substring(0, Math.min(50, token.length())) + "...\n\nCalling API...");
        
        if (userApiService == null) {
            updateResult("❌ API Service chưa được khởi tạo");
            return;
        }

        try {
            Call<UserStreakResponse> call = userApiService.getMyStreak();
            
            call.enqueue(new Callback<UserStreakResponse>() {
                @Override
                public void onResponse(Call<UserStreakResponse> call, Response<UserStreakResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        UserStreakResponse streak = response.body();
                        String result = "✅ STREAK API THÀNH CÔNG!\n\n" +
                                "Số ngày liên tiếp: " + streak.getSoNgayLienTiep() + "\n" +
                                "Ngày cập nhật cuối: " + streak.getNgayCapNhatCuoi() + "\n\n" +
                                "Response code: " + response.code();
                        
                        updateResult(result);
                        Toast.makeText(TestStreakActivity.this, 
                            "🔥 Streak: " + streak.getSoNgayLienTiep() + " ngày", 
                            Toast.LENGTH_SHORT).show();
                        
                        Log.d(TAG, "✅ Streak API success: " + streak.getSoNgayLienTiep() + " days");
                    } else {
                        String result = "❌ STREAK API THẤT BẠI!\n\n" +
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
                        Toast.makeText(TestStreakActivity.this, 
                            "❌ API Error: " + response.code(), 
                            Toast.LENGTH_LONG).show();
                        
                        Log.e(TAG, "❌ Streak API failed: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<UserStreakResponse> call, Throwable t) {
                    String result = "❌ NETWORK ERROR!\n\n" +
                            "Error: " + t.getMessage() + "\n" +
                            "Type: " + t.getClass().getSimpleName() + "\n\n" +
                            "Có thể:\n" +
                            "- Backend chưa chạy (http://localhost:5048)\n" +
                            "- Không có kết nối mạng\n" +
                            "- URL sai (emulator cần 10.0.2.2)";
                    
                    updateResult(result);
                    Toast.makeText(TestStreakActivity.this, 
                        "❌ Network Error: " + t.getMessage(), 
                        Toast.LENGTH_LONG).show();
                    
                    Log.e(TAG, "❌ Streak API network error", t);
                }
            });
            
        } catch (Exception e) {
            updateResult("❌ Exception khi gọi API: " + e.getMessage());
            Log.e(TAG, "❌ Exception calling streak API", e);
        }
    }

    private void openStreakActivity() {
        try {
            android.content.Intent intent = new android.content.Intent(this, 
                com.example.iq5.feature.result.ui.ApiStreakActivity.class);
            startActivity(intent);
            
            Toast.makeText(this, "🔥 Mở Streak Activity...", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            updateResult("❌ Lỗi mở Streak Activity: " + e.getMessage());
            Log.e(TAG, "❌ Error opening streak activity", e);
        }
    }

    private void updateResult(String text) {
        runOnUiThread(() -> {
            if (tvResult != null) {
                tvResult.setText(text);
            }
        });
    }
}