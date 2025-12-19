package com.example.iq5.feature.history.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iq5.core.network.HistoryApiService;
import com.example.iq5.data.repository.HistoryApiRepository;
import com.example.iq5.utils.ApiHelper;

/**
 * Simple History Activity sử dụng API thật từ backend
 */
public class SimpleApiHistoryActivity extends AppCompatActivity {

    private static final String TAG = "SimpleApiHistoryActivity";
    
    // Repository
    private HistoryApiRepository historyRepository;
    
    // Data
    private HistoryApiService.HistoryResponse currentHistory;
    private final int pageSize = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(android.R.layout.activity_list_item);

        initRepository();
        loadData();
        
        Toast.makeText(this, "📚 History Activity - Đang tải từ API...", 
            Toast.LENGTH_LONG).show();
    }
    
    private void initRepository() {
        historyRepository = new HistoryApiRepository(this);
    }
    
    /**
     * Load tất cả dữ liệu
     */
    private void loadData() {
        loadHistory(1); // Load trang đầu tiên
        loadStreakInfo();
    }
    
    /**
     * Load lịch sử làm bài
     */
    private void loadHistory(int pageNumber) {
        historyRepository.getMyHistory(pageNumber, pageSize, 
            new HistoryApiRepository.HistoryCallback() {
            @Override
            public void onSuccess(HistoryApiService.HistoryResponse history) {
                runOnUiThread(() -> {
                    currentHistory = history;
                    
                    String message = "✅ Đã tải " + history.getDanhSach().size() + 
                                   " kết quả từ tổng " + history.getTongSoKetQua() + " lần làm bài";
                    Toast.makeText(SimpleApiHistoryActivity.this, message, Toast.LENGTH_LONG).show();
                    
                    // Hiển thị quiz gần nhất
                    if (history.getDanhSach() != null && !history.getDanhSach().isEmpty()) {
                        HistoryApiService.HistoryItem latest = history.getDanhSach().get(0);
                        Toast.makeText(SimpleApiHistoryActivity.this, 
                            "📝 Quiz gần nhất: " + latest.getDiem() + " điểm (" + 
                            latest.getSoCauDung() + "/" + latest.getTongCauHoi() + ")", 
                            Toast.LENGTH_LONG).show();
                    }
                });
            }
            
            @Override
            public void onUnauthorized() {
                runOnUiThread(() -> {
                    Toast.makeText(SimpleApiHistoryActivity.this, 
                        "❌ Phiên đăng nhập hết hạn", Toast.LENGTH_SHORT).show();
                    
                    // Clear token và quay về login
                    ApiHelper.clearToken(SimpleApiHistoryActivity.this);
                    finish();
                });
            }
            
            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(SimpleApiHistoryActivity.this, 
                        "❌ Lỗi tải lịch sử: " + error, Toast.LENGTH_LONG).show();
                });
            }
        });
    }
    
    /**
     * Load thông tin streak
     */
    private void loadStreakInfo() {
        historyRepository.getStreakFromHistory(new HistoryApiRepository.StreakCallback() {
            @Override
            public void onSuccess(HistoryApiService.StreakInfo streak) {
                runOnUiThread(() -> {
                    String streakText = "🔥 Chuỗi: " + streak.getSoNgayLienTiep() + " ngày";
                    if (streak.getMessage() != null) {
                        streakText += " - " + streak.getMessage();
                    }
                    Toast.makeText(SimpleApiHistoryActivity.this, streakText, Toast.LENGTH_LONG).show();
                });
            }
            
            @Override
            public void onUnauthorized() {
                runOnUiThread(() -> {
                    Toast.makeText(SimpleApiHistoryActivity.this, 
                        "❌ Không thể tải streak - Token hết hạn", Toast.LENGTH_SHORT).show();
                });
            }
            
            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(SimpleApiHistoryActivity.this, 
                        "❌ Lỗi tải streak: " + error, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
    
    /**
     * Xem chi tiết một lần làm bài
     */
    public void viewHistoryDetail(int attemptId) {
        Intent intent = new Intent(this, SimpleApiHistoryDetailActivity.class);
        intent.putExtra("attemptId", attemptId);
        startActivity(intent);
    }
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(this, "👋 Thoát History Activity", Toast.LENGTH_SHORT).show();
    }
}