package com.example.iq5.feature.auth.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iq5.R;
import com.example.iq5.feature.auth.data.AuthRepository;
import com.example.iq5.feature.auth.model.HomeResponse;

public class HomeActivity extends AppCompatActivity {

    private TextView tvWelcome;
    private RecyclerView rvQuizzes;

    private AuthRepository authRepository;
    private HomeResponse homeData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initViews();
        initRepository();
        loadHomeData();
        setupBottomNavigation();
        setupHeaderActions();
    }

    private void initViews() {
        tvWelcome = findViewById(R.id.tvWelcome);
        rvQuizzes = findViewById(R.id.rvQuizzes);

        rvQuizzes.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        );
    }

    private void initRepository() {
        authRepository = new AuthRepository(this);
    }

    private void loadHomeData() {
        homeData = authRepository.getHomeData();

        if (homeData == null) {
            Toast.makeText(this, "Không tải được dữ liệu trang chủ", Toast.LENGTH_SHORT).show();
            return;
        }

        // Set welcome message
        tvWelcome.setText(homeData.welcomeMessage != null
                ? homeData.welcomeMessage
                : getString(R.string.app_name));

        // Setup RecyclerView
        if (homeData.featuredQuizzes != null && !homeData.featuredQuizzes.isEmpty()) {
            QuizAdapter adapter = new QuizAdapter(homeData.featuredQuizzes);
            rvQuizzes.setAdapter(adapter);
        } else {
            Toast.makeText(this, "Chưa có quiz nổi bật để hiển thị", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupBottomNavigation() {
        // Home
        findViewById(R.id.btnHome).setOnClickListener(v -> {
            // Đang ở Home rồi, không làm gì
        });

        // Library
        findViewById(R.id.btnLibrary).setOnClickListener(v -> {
            Toast.makeText(this, "Library coming soon!", Toast.LENGTH_SHORT).show();
        });

        // Profile
        findViewById(R.id.btnProfile).setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
            startActivity(intent);
        });
    }

    private void setupHeaderActions() {
        // Settings button in header
        findViewById(R.id.btnSettings).setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        // Search button
        findViewById(R.id.btnSearch).setOnClickListener(v -> {
            Toast.makeText(this, "Search coming soon!", Toast.LENGTH_SHORT).show();
        });

        // Avatar click -> Profile
        findViewById(R.id.imgAvatarHome).setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
            startActivity(intent);
        });
    }
}
