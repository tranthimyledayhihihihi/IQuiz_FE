package com.example.iq5.feature.history.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iq5.R;
import com.example.iq5.core.network.HistoryApiService;
import com.example.iq5.data.repository.HistoryApiRepository;
import com.example.iq5.utils.ApiHelper;

/**
 * History Activity sử dụng API thật từ backend
 */
public class ApiHistoryActivity extends AppCompatActivity {

    private static final String TAG = "ApiHistoryActivity";
    
    // UI Components
    private TextView tvTotalQuizzes, tvStreakInfo, tvNoHistory;
    private RecyclerView rvHistory;
    private Button btnRefresh, btnLoadMore;
    private ProgressBar progressBar;
    
    // Repository
    private HistoryApiRepository historyRepository;
    
    // Data
    private HistoryApiService.HistoryResponse currentHistory;
    private int currentPage = 1;
    private final int pageSize = 20;
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Sử dụng layout đơn giản nếu layout chính không tồn tại
        try {
            setContentView(R.layout.activity_main); // Using existing layout as placeholder
        } catch (Exception e) {
            setContentView(android.R.layout.activity_list_item);
        }

        initViews();
        initRepository();
        loadData();
        setupButtons();
    }
    
    private void initViews() {
        // Comment out missing views - these need to be added to the layout
        // tvTotalQuizzes = findViewById(R.id.tvTotalQuizzes);
        // tvStreakInfo = findViewById(R.id.tvStreakInfo);
        // tvNoHistory = findViewById(R.id.tvNoHistory);
        // rvHistory = findViewById(R.id.rvHistory);
        // btnRefresh = findViewById(R.id.btnRefresh);
        // btnLoadMore = findViewById(R.id.btnLoadMore);
        progressBar = findViewById(R.id.progressBar);
        
        // Setup RecyclerView
        if (rvHistory != null) {
            rvHistory.setLayoutManager(new LinearLayoutManager(this));
        }
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
        if (isLoading) return;
        
        isLoading = true;
        showLoading(true);
        
        historyRepository.getMyHistory(pageNumber, pageSize, 
            new HistoryApiRepository.HistoryCallback() {
            @Override
            public void onSuccess(HistoryApiService.HistoryResponse history) {
                runOnUiThread(() -> {
                    isLoading = false;
                    showLoading(false);
                    currentHistory = history;
                    currentPage = pageNumber;
                    
                    displayHistory(history);
                    
                    Toast.makeText(ApiHistoryActivity.this, 
                        "✅ Đã tải " + history.getDanhSach().size() + " kết quả!", 
                        Toast.LENGTH_SHORT).show();
                });
            }
            
            @Override
            public void onUnauthorized() {
                runOnUiThread(() -> {
                    isLoading = false;
                    showLoading(false);
                    Toast.makeText(ApiHistoryActivity.this, 
                        "❌ Phiên đăng nhập hết hạn", Toast.LENGTH_SHORT).show();
                    
                    // Clear token và quay về login
                    ApiHelper.clearToken(ApiHistoryActivity.this);
                    finish();
                });
            }
            
            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    isLoading = false;
                    showLoading(false);
                    Toast.makeText(ApiHistoryActivity.this, 
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
                    displayStreakInfo(streak);
                });
            }
            
            @Override
            public void onUnauthorized() {
                runOnUiThread(() -> {
                    Toast.makeText(ApiHistoryActivity.this, 
                        "❌ Không thể tải streak - Token hết hạn", Toast.LENGTH_SHORT).show();
                });
            }
            
            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    Toast.makeText(ApiHistoryActivity.this, 
                        "❌ Lỗi tải streak: " + error, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
    
    /**
     * Hiển thị lịch sử
     */
    private void displayHistory(HistoryApiService.HistoryResponse history) {
        // Update total count
        if (tvTotalQuizzes != null) {
            tvTotalQuizzes.setText("Tổng: " + history.getTongSoKetQua() + " lần làm bài");
        }
        
        // Show/hide no history message
        if (tvNoHistory != null) {
            if (history.getDanhSach() == null || history.getDanhSach().isEmpty()) {
                tvNoHistory.setVisibility(View.VISIBLE);
                rvHistory.setVisibility(View.GONE);
            } else {
                tvNoHistory.setVisibility(View.GONE);
                rvHistory.setVisibility(View.VISIBLE);
            }
        }
        
        // Update load more button
        if (btnLoadMore != null) {
            boolean hasMorePages = history.getTrangHienTai() < history.getTongSoTrang();
            btnLoadMore.setVisibility(hasMorePages ? View.VISIBLE : View.GONE);
        }
        
        // TODO: Setup adapter cho RecyclerView
        // HistoryAdapter adapter = new HistoryAdapter(history.getDanhSach(), this::onHistoryItemClick);
        // rvHistory.setAdapter(adapter);
        
        // Tạm thời hiển thị thông tin quiz gần nhất
        if (history.getDanhSach() != null && !history.getDanhSach().isEmpty()) {
            HistoryApiService.HistoryItem latest = history.getDanhSach().get(0);
            Toast.makeText(this, 
                "Quiz gần nhất: " + latest.getDiem() + " điểm (" + 
                latest.getSoCauDung() + "/" + latest.getTongCauHoi() + ")", 
                Toast.LENGTH_LONG).show();
        }
    }
    
    /**
     * Hiển thị thông tin streak
     */
    private void displayStreakInfo(HistoryApiService.StreakInfo streak) {
        if (tvStreakInfo != null) {
            String streakText = "🔥 Chuỗi: " + streak.getSoNgayLienTiep() + " ngày";
            if (streak.getMessage() != null) {
                streakText += " - " + streak.getMessage();
            }
            tvStreakInfo.setText(streakText);
        }
    }
    
    /**
     * Xử lý khi click vào item lịch sử
     */
    private void onHistoryItemClick(HistoryApiService.HistoryItem item) {
        // Navigate to history detail
        Intent intent = new Intent(this, ApiHistoryDetailActivity.class);
        intent.putExtra("attemptId", item.getQuizAttemptID());
        startActivity(intent);
    }
    
    /**
     * Load more history
     */
    private void loadMoreHistory() {
        if (currentHistory != null && currentPage < currentHistory.getTongSoTrang()) {
            loadHistory(currentPage + 1);
        }
    }
    
    /**
     * Setup button listeners
     */
    private void setupButtons() {
        // Back button
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        
        // Refresh button
        if (btnRefresh != null) {
            btnRefresh.setOnClickListener(v -> {
                Toast.makeText(this, "🔄 Đang làm mới...", Toast.LENGTH_SHORT).show();
                currentPage = 1;
                loadData();
            });
        }
        
        // Load more button
        if (btnLoadMore != null) {
            btnLoadMore.setOnClickListener(v -> loadMoreHistory());
        }
    }
    
    /**
     * Show/hide loading indicator
     */
    private void showLoading(boolean show) {
        if (progressBar != null) {
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }
}