package com.example.iq5.feature.social.ui;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iq5.core.network.SocialApiService;
import com.example.iq5.data.repository.SocialApiRepository;

/**
 * Leaderboard Activity sử dụng API thật từ backend
 */
public class ApiLeaderboardActivity extends AppCompatActivity {

    private static final String TAG = "ApiLeaderboardActivity";
    
    // Repository
    private SocialApiRepository socialRepository;
    
    // Data
    private SocialApiService.LeaderboardResponse currentLeaderboard;
    private String currentType = "weekly"; // "weekly" or "monthly"
    private final int pageSize = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(android.R.layout.activity_list_item);

        initRepository();
        loadData();
        
        Toast.makeText(this, "🏅 Leaderboard Activity - Đang tải từ API...", 
            Toast.LENGTH_LONG).show();
    }
    
    private void initRepository() {
        socialRepository = new SocialApiRepository(this);
    }
    
    /**
     * Load tất cả dữ liệu
     */
    private void loadData() {
        loadLeaderboard(currentType);
        loadOnlineCount();
    }
    
    /**
     * Load bảng xếp hạng
     */
    private void loadLeaderboard(String type) {
        currentType = type;
        
        socialRepository.getLeaderboard(type, 1, pageSize, 
            new SocialApiRepository.LeaderboardCallback() {
            @Override
            public void onSuccess(SocialApiService.LeaderboardResponse leaderboard) {
                runOnUiThread(() -> {
                    currentLeaderboard = leaderboard;
                    
                    String title = type.equals("weekly") ? "Tuần" : "Tháng";
                    String message = "✅ Bảng xếp hạng " + title + ": " + 
                                   leaderboard.getTongSoNguoi() + " người chơi";
                    Toast.makeText(ApiLeaderboardActivity.this, message, Toast.LENGTH_LONG).show();
                    
                    // Hiển thị top 1 nếu có
                    if (leaderboard.getDanhSach() != null && !leaderboard.getDanhSach().isEmpty()) {
                        SocialApiService.RankingUser topPlayer = leaderboard.getDanhSach().get(0);
                        Toast.makeText(ApiLeaderboardActivity.this, 
                            "🥇 Top 1: " + topPlayer.getHoTen() + " - " + topPlayer.getTotalScore() + " điểm", 
                            Toast.LENGTH_LONG).show();
                    }
                });
            }
            
            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(ApiLeaderboardActivity.this, 
                        "❌ Lỗi tải bảng xếp hạng: " + error, Toast.LENGTH_LONG).show();
                });
            }
        });
    }
    
    /**
     * Load số người online
     */
    private void loadOnlineCount() {
        socialRepository.getOnlineCount(new SocialApiRepository.OnlineCountCallback() {
            @Override
            public void onSuccess(int onlineCount) {
                runOnUiThread(() -> {
                    Toast.makeText(ApiLeaderboardActivity.this, 
                        "🟢 " + onlineCount + " người đang online", Toast.LENGTH_SHORT).show();
                });
            }
            
            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(ApiLeaderboardActivity.this, 
                        "❌ Lỗi tải số người online: " + error, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
    
    /**
     * Switch to weekly leaderboard
     */
    public void loadWeeklyLeaderboard() {
        Toast.makeText(this, "📅 Chuyển sang bảng xếp hạng tuần...", Toast.LENGTH_SHORT).show();
        loadLeaderboard("weekly");
    }
    
    /**
     * Switch to monthly leaderboard
     */
    public void loadMonthlyLeaderboard() {
        Toast.makeText(this, "📅 Chuyển sang bảng xếp hạng tháng...", Toast.LENGTH_SHORT).show();
        loadLeaderboard("monthly");
    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(this, "👋 Thoát Leaderboard Activity", Toast.LENGTH_SHORT).show();
    }
}