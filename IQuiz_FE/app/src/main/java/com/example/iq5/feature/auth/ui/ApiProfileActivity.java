package com.example.iq5.feature.auth.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.iq5.R;
import com.example.iq5.core.navigation.NavigationHelper;
import com.example.iq5.data.model.UserProfileModel;
import com.example.iq5.data.repository.UserProfileApiRepository;
import com.example.iq5.utils.ApiHelper;

/**
 * ProfileActivity sử dụng API thật từ backend
 */
public class ApiProfileActivity extends AppCompatActivity {

    private static final String TAG = "ApiProfileActivity";
    
    // UI Components
    private TextView tvName, tvEmail, tvQuizTaken, tvAvgScore, tvRole, tvRank;
    private TextView tvJoinDate, tvLastLogin;
    private ImageView imgAvatar;
    private LinearLayout btnSettings, btnShare, btnLogout, btnEditProfile;
    private ProgressBar progressBar;
    
    // Repository
    private UserProfileApiRepository profileRepository;
    
    // Data
    private UserProfileModel currentProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initViews();
        initRepository();
        loadProfileData();
        setupButtons();
    }
    
    private void initViews() {
        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        tvQuizTaken = findViewById(R.id.tvQuizTaken);
        tvAvgScore = findViewById(R.id.tvAvgScore);
        tvRole = findViewById(R.id.tvRole);
        tvRank = findViewById(R.id.tvRank);
        tvJoinDate = findViewById(R.id.tvJoinDate);
        // tvLastLogin = findViewById(R.id.tvLastLogin); // Comment out if not in layout
        imgAvatar = findViewById(R.id.imgAvatar);
        
        btnSettings = findViewById(R.id.btnSettings);
        btnShare = findViewById(R.id.btnShare);
        btnLogout = findViewById(R.id.btnLogout);
        // btnEditProfile = findViewById(R.id.btnEditProfile); // Comment out if not in layout
        progressBar = findViewById(R.id.progressBar);
    }
    
    private void initRepository() {
        profileRepository = new UserProfileApiRepository(this);
    }
    
    /**
     * Load profile data từ API
     */
    private void loadProfileData() {
        showLoading(true);
        
        profileRepository.getMyProfile(new UserProfileApiRepository.ProfileCallback() {
            @Override
            public void onSuccess(UserProfileModel profile) {
                runOnUiThread(() -> {
                    showLoading(false);
                    currentProfile = profile;
                    displayProfileData(profile);
                    
                    Toast.makeText(ApiProfileActivity.this, 
                        "✅ Đã tải profile từ API!", Toast.LENGTH_SHORT).show();
                });
            }
            
            @Override
            public void onUnauthorized() {
                runOnUiThread(() -> {
                    showLoading(false);
                    Toast.makeText(ApiProfileActivity.this, 
                        "❌ Phiên đăng nhập hết hạn", Toast.LENGTH_SHORT).show();
                    
                    // Clear token và chuyển về login
                    ApiHelper.clearToken(ApiProfileActivity.this);
                    navigateToLogin();
                });
            }
            
            @Override
            public void onError(String error) {
                runOnUiThread(() -> {
                    showLoading(false);
                    Toast.makeText(ApiProfileActivity.this, 
                        "❌ Lỗi tải profile: " + error, Toast.LENGTH_LONG).show();
                    
                    // Fallback: Load từ SharedPreferences
                    loadProfileFromPrefs();
                });
            }
        });
    }
    
    /**
     * Hiển thị dữ liệu profile lên UI
     */
    private void displayProfileData(UserProfileModel profile) {
        // Basic info
        if (profile.getHoTen() != null) {
            tvName.setText(profile.getHoTen());
        }
        
        if (profile.getEmail() != null) {
            tvEmail.setText(profile.getEmail());
        }
        
        if (profile.getVaiTro() != null) {
            tvRole.setText(profile.getVaiTro());
        }
        
        // Dates
        if (profile.getNgayDangKy() != null) {
            tvJoinDate.setText("Tham gia: " + formatDate(profile.getNgayDangKy()));
        }
        
        if (profile.getLanDangNhapCuoi() != null && tvLastLogin != null) {
            tvLastLogin.setText("Lần cuối: " + formatDate(profile.getLanDangNhapCuoi()));
        }
        
        // Avatar
        if (profile.getAnhDaiDien() != null && !profile.getAnhDaiDien().isEmpty()) {
            Glide.with(this)
                .load(profile.getAnhDaiDien())
                .placeholder(R.drawable.ic_avatar_placeholder)
                .error(R.drawable.ic_avatar_default)
                .into(imgAvatar);
        } else {
            imgAvatar.setImageResource(R.drawable.ic_avatar_default);
        }
        
        // TODO: Stats sẽ được thêm khi backend có API
        tvQuizTaken.setText("Đang cập nhật...");
        tvAvgScore.setText("Đang cập nhật...");
        tvRank.setText("Đang cập nhật...");
    }
    
    /**
     * Fallback: Load profile từ SharedPreferences
     */
    private void loadProfileFromPrefs() {
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String userName = prefs.getString("user_name", "Người dùng");
        String userEmail = prefs.getString("user_email", "");
        String userRole = prefs.getString("user_role", "Player");

        tvName.setText(userName);
        if (!userEmail.isEmpty()) {
            tvEmail.setText(userEmail);
        }
        tvRole.setText(userRole);
        
        // Set default values
        tvJoinDate.setText("Không có thông tin");
        tvLastLogin.setText("Không có thông tin");
        tvQuizTaken.setText("0");
        tvAvgScore.setText("0");
        tvRank.setText("Chưa xếp hạng");
    }
    
    /**
     * Setup button listeners
     */
    private void setupButtons() {
        // Back button
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        
        // Settings button
        if (btnSettings != null) {
            btnSettings.setOnClickListener(v -> {
                NavigationHelper.navigateToSettings(this);
            });
        }
        
        // Edit Profile button (only if exists in layout)
        // if (btnEditProfile != null) {
        //     btnEditProfile.setOnClickListener(v -> {
        //         // TODO: Navigate to edit profile screen
        //         Toast.makeText(this, "Chỉnh sửa profile sẽ được thêm sau", 
        //             Toast.LENGTH_SHORT).show();
        //     });
        // }
        
        // Share button
        if (btnShare != null) {
            btnShare.setOnClickListener(v -> shareApp());
        }
        
        // Logout button
        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> showLogoutDialog());
        }
    }
    
    /**
     * Share app
     */
    private void shareApp() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "IQuiz - Quiz Game App");
        shareIntent.putExtra(Intent.EXTRA_TEXT, 
            "Hãy thử IQuiz - Ứng dụng quiz thú vị! Tải ngay để cùng chơi!");
        startActivity(Intent.createChooser(shareIntent, "Chia sẻ qua"));
    }
    
    /**
     * Show logout confirmation dialog
     */
    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
            .setTitle("Đăng Xuất")
            .setMessage("Bạn có chắc chắn muốn đăng xuất?")
            .setPositiveButton("Đăng Xuất", (dialog, which) -> performLogout())
            .setNegativeButton("Hủy", null)
            .show();
    }
    
    /**
     * Perform logout với API
     */
    private void performLogout() {
        showLoading(true);
        
        // Call logout API (nếu có)
        // Hiện tại chỉ clear local data
        clearUserData();
        
        runOnUiThread(() -> {
            showLoading(false);
            Toast.makeText(this, "✅ Đăng xuất thành công!", Toast.LENGTH_SHORT).show();
            navigateToLogin();
        });
    }
    
    /**
     * Clear user data
     */
    private void clearUserData() {
        // Clear SharedPreferences
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        prefs.edit().clear().apply();
        
        // Clear token
        ApiHelper.clearToken(this);
    }
    
    /**
     * Navigate to login screen
     */
    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    
    /**
     * Format date string
     */
    private String formatDate(String dateString) {
        // TODO: Implement proper date formatting
        return dateString != null ? dateString : "Không có thông tin";
    }
    
    /**
     * Show/hide loading indicator
     */
    private void showLoading(boolean show) {
        if (progressBar != null) {
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // Refresh profile data khi quay lại từ settings
        if (currentProfile != null) {
            loadProfileData();
        }
    }
}