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
import com.example.iq5.core.network.UserApiService;
import com.example.iq5.core.prefs.PrefsManager;
import com.example.iq5.data.model.UserProfileModel;
import com.example.iq5.feature.result.ui.ApiStatsActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Test activity for Stats API
 */
public class TestStatsActivity extends AppCompatActivity {

    private static final String TAG = "TestStatsActivity";
    
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
        title.setText("📊 TEST STATS API");
        title.setTextSize(20);
        title.setTextColor(0xFF333333);
        title.setPadding(0, 0, 0, 24);
        layout.addView(title);
        
        // Test buttons
        Button btnTestStats = new Button(this);
        btnTestStats.setText("📊 Test Stats API");
        btnTestStats.setOnClickListener(v -> testStatsApi());
        layout.addView(btnTestStats);
        
        Button btnOpenStats = new Button(this);
        btnOpenStats.setText("🔗 Open Stats Activity");
        btnOpenStats.setOnClickListener(v -> openStatsActivity());
        layout.addView(btnOpenStats);
        
        Button btnTestProfile = new Button(this);
        btnTestProfile.setText("👤 Test Profile with Stats");
        btnTestProfile.setOnClickListener(v -> testProfileWithStats());
        layout.addView(btnTestProfile);
        
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
            
            updateResult("✅ API Service initialized");
            Log.d(TAG, "✅ API Service initialized");
        } catch (Exception e) {
            updateResult("❌ Failed to init API service: " + e.getMessage());
            Log.e(TAG, "❌ Failed to init API service", e);
        }
    }

    private void testStatsApi() {
        updateResult("🔄 Testing stats API...");
        
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
                        
                        String result = "✅ STATS API SUCCESS!\n\n";
                        result += "User: " + profile.getHoTen() + "\n";
                        result += "Email: " + profile.getEmail() + "\n\n";
                        
                        if (profile.getThongKe() != null) {
                            UserProfileModel.ThongKeModel stats = profile.getThongKe();
                            result += "📊 THỐNG KÊ:\n";
                            result += "Quiz hoàn thành: " + stats.getSoBaiQuizHoanThanh() + "\n";
                            result += "Điểm trung bình: " + String.format("%.1f", stats.getDiemTrungBinh()) + "\n";
                            result += "Tổng câu đúng: " + stats.getTongSoCauDung() + "\n";
                            result += "Tổng câu hỏi: " + stats.getTongSoCauHoi() + "\n";
                            result += "Tỷ lệ đúng: " + String.format("%.1f%%", stats.getTyLeDung()) + "\n\n";
                            
                            // Calculate derived stats
                            int totalScore = (int)(stats.getSoBaiQuizHoanThanh() * stats.getDiemTrungBinh());
                            result += "📈 TÍNH TOÁN:\n";
                            result += "Tổng điểm ước tính: " + totalScore + "\n";
                            result += "Cấp độ: " + getUserLevel(stats.getDiemTrungBinh()) + "\n";
                            result += "Thành tựu: " + getAchievementLevel(stats.getSoBaiQuizHoanThanh());
                        } else {
                            result += "⚠️ Không có dữ liệu thống kê";
                        }
                        
                        updateResult(result);
                        Toast.makeText(TestStatsActivity.this, 
                            "✅ Stats loaded: " + profile.getHoTen(), Toast.LENGTH_SHORT).show();
                        
                        Log.d(TAG, "✅ Stats API success");
                    } else {
                        handleApiError("Stats API", response);
                    }
                }

                @Override
                public void onFailure(Call<UserProfileModel> call, Throwable t) {
                    handleNetworkError("Stats API", t);
                }
            });
            
        } catch (Exception e) {
            updateResult("❌ Exception in stats API: " + e.getMessage());
            Log.e(TAG, "❌ Exception in stats API", e);
        }
    }

    private void testProfileWithStats() {
        updateResult("🔄 Testing profile with detailed stats...");
        
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
                        
                        String result = "✅ PROFILE WITH STATS SUCCESS!\n\n";
                        
                        // Basic info
                        result += "👤 THÔNG TIN CƠ BẢN:\n";
                        result += "Tên: " + profile.getHoTen() + "\n";
                        result += "Email: " + profile.getEmail() + "\n";
                        result += "Vai trò: " + profile.getVaiTro() + "\n";
                        result += "Ngày tham gia: " + profile.getNgayDangKy() + "\n\n";
                        
                        // Settings
                        if (profile.getCaiDat() != null) {
                            result += "⚙️ CÀI ĐẶT:\n";
                            result += "Âm thanh: " + (profile.getCaiDat().isAmThanh() ? "Bật" : "Tắt") + "\n";
                            result += "Nhạc nền: " + (profile.getCaiDat().isNhacNen() ? "Bật" : "Tắt") + "\n";
                            result += "Thông báo: " + (profile.getCaiDat().isThongBao() ? "Bật" : "Tắt") + "\n";
                            result += "Ngôn ngữ: " + profile.getCaiDat().getNgonNgu() + "\n\n";
                        }
                        
                        // Detailed stats
                        if (profile.getThongKe() != null) {
                            UserProfileModel.ThongKeModel stats = profile.getThongKe();
                            result += "📊 THỐNG KÊ CHI TIẾT:\n";
                            result += "• Quiz hoàn thành: " + stats.getSoBaiQuizHoanThanh() + "\n";
                            result += "• Điểm trung bình: " + String.format("%.1f/100", stats.getDiemTrungBinh()) + "\n";
                            result += "• Tổng câu đúng: " + stats.getTongSoCauDung() + "\n";
                            result += "• Tổng câu hỏi: " + stats.getTongSoCauHoi() + "\n";
                            result += "• Tỷ lệ chính xác: " + String.format("%.1f%%", stats.getTyLeDung()) + "\n\n";
                            
                            result += "🏆 ĐÁNH GIÁ:\n";
                            result += "• Cấp độ: " + getUserLevel(stats.getDiemTrungBinh()) + "\n";
                            result += "• Thành tựu: " + getAchievementLevel(stats.getSoBaiQuizHoanThanh()) + "\n";
                            result += "• Tổng điểm ước tính: " + (int)(stats.getSoBaiQuizHoanThanh() * stats.getDiemTrungBinh());
                        } else {
                            result += "⚠️ Chưa có dữ liệu thống kê\nHãy chơi quiz để có thống kê!";
                        }
                        
                        updateResult(result);
                        Toast.makeText(TestStatsActivity.this, 
                            "✅ Profile loaded with stats", Toast.LENGTH_SHORT).show();
                        
                        Log.d(TAG, "✅ Profile with stats success");
                    } else {
                        handleApiError("Profile with Stats", response);
                    }
                }

                @Override
                public void onFailure(Call<UserProfileModel> call, Throwable t) {
                    handleNetworkError("Profile with Stats", t);
                }
            });
            
        } catch (Exception e) {
            updateResult("❌ Exception in profile with stats: " + e.getMessage());
            Log.e(TAG, "❌ Exception in profile with stats", e);
        }
    }

    private void openStatsActivity() {
        try {
            Intent intent = new Intent(this, ApiStatsActivity.class);
            startActivity(intent);
            Toast.makeText(this, "📊 Opening Stats Activity...", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            updateResult("❌ Error opening Stats Activity: " + e.getMessage());
            Log.e(TAG, "❌ Error opening stats activity", e);
        }
    }

    private String getUserLevel(double avgScore) {
        if (avgScore >= 90) return "Cấp 5 - Xuất sắc";
        if (avgScore >= 80) return "Cấp 4 - Giỏi";
        if (avgScore >= 70) return "Cấp 3 - Khá";
        if (avgScore >= 60) return "Cấp 2 - Trung bình";
        return "Cấp 1 - Mới bắt đầu";
    }

    private String getAchievementLevel(int quizCount) {
        if (quizCount >= 50) return "🏆 Huyền thoại";
        if (quizCount >= 20) return "🥇 Chuyên gia";
        if (quizCount >= 10) return "🥈 Thành thạo";
        if (quizCount >= 5) return "🥉 Tập sự";
        return "🆕 Người mới";
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