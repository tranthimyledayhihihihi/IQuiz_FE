package com.example.iq5.feature.auth.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.iq5.R;
import com.example.iq5.core.navigation.NavigationHelper;
import com.example.iq5.feature.auth.data.AuthRepository;
import com.example.iq5.feature.auth.model.ProfileResponse;
import com.example.iq5.utils.ApiHelper;

public class ProfileActivity extends AppCompatActivity {

    TextView tvName, tvEmail, tvQuizTaken, tvAvgScore, tvRole, tvRank;
    ImageView imgAvatar;
    LinearLayout btnSettings, btnShare, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize views
        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        tvQuizTaken = findViewById(R.id.tvQuizTaken);
        tvAvgScore = findViewById(R.id.tvAvgScore);
        tvRole = findViewById(R.id.tvRole);
        tvRank = findViewById(R.id.tvRank);
        imgAvatar = findViewById(R.id.imgAvatar);
        
        btnSettings = findViewById(R.id.btnSettings);
        btnShare = findViewById(R.id.btnShare);
        btnLogout = findViewById(R.id.btnLogout);

        // Load profile data
        loadProfileData();

        // Back button
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // Settings button
        btnSettings.setOnClickListener(v -> {
            NavigationHelper.navigateToSettings(this);
        });

        // Share button
        btnShare.setOnClickListener(v -> {
            shareApp();
        });

        // Logout button
        btnLogout.setOnClickListener(v -> {
            showLogoutDialog();
        });
    }

    private void loadProfileData() {
        AuthRepository repo = new AuthRepository(this);
        ProfileResponse p = repo.getProfileData();

        if (p != null) {
            tvName.setText(p.name != null ? p.name : "Người dùng");
            tvEmail.setText(p.email != null ? p.email : "");
            
            if (p.stats != null) {
                tvQuizTaken.setText(String.valueOf(p.stats.quizzesTaken));
                tvAvgScore.setText(String.valueOf(p.stats.avgScore));
            }

            // Load avatar
            if (p.avatarUrl != null && !p.avatarUrl.isEmpty()) {
                Glide.with(this).load(p.avatarUrl).into(imgAvatar);
            }
        }

        // Load user info from SharedPreferences (from login)
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String userName = prefs.getString("user_name", "Người dùng");
        String userEmail = prefs.getString("user_email", "");
        String userRole = prefs.getString("user_role", "Player");

        tvName.setText(userName);
        if (!userEmail.isEmpty()) {
            tvEmail.setText(userEmail);
        }
        tvRole.setText(userRole);
    }

    private void shareApp() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "IQuiz - Quiz Game App");
        shareIntent.putExtra(Intent.EXTRA_TEXT, 
            "Hãy thử IQuiz - Ứng dụng quiz thú vị! Tải ngay để cùng chơi!");
        startActivity(Intent.createChooser(shareIntent, "Chia sẻ qua"));
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
            .setTitle("Đăng Xuất")
            .setMessage("Bạn có chắc chắn muốn đăng xuất?")
            .setPositiveButton("Đăng Xuất", (dialog, which) -> {
                performLogout();
            })
            .setNegativeButton("Hủy", null)
            .show();
    }

    private void performLogout() {
        // Clear token
        ApiHelper.clearToken(this);
        
        // Clear user data
        SharedPreferences prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);
        prefs.edit().clear().apply();
        
        // Show message
        Toast.makeText(this, "Đã đăng xuất thành công", Toast.LENGTH_SHORT).show();
        
        // Navigate to login and clear back stack
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
