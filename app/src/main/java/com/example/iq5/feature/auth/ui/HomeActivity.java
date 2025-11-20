package com.example.iq5.feature.auth.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iq5.R;
import com.example.iq5.feature.auth.data.AuthRepository;
import com.example.iq5.feature.auth.model.HomeResponse;

public class HomeActivity extends AppCompatActivity {

    TextView tvWelcome;
    RecyclerView rvQuizzes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tvWelcome = findViewById(R.id.tvWelcome);
        rvQuizzes = findViewById(R.id.rvQuizzes);

        AuthRepository repo = new AuthRepository(this);
        HomeResponse data = repo.getHomeData();

        // Set welcome message
        tvWelcome.setText(data.welcomeMessage);

        // Setup RecyclerView
        rvQuizzes.setLayoutManager(new LinearLayoutManager(this));
        QuizAdapter adapter = new QuizAdapter(data.featuredQuizzes);
        rvQuizzes.setAdapter(adapter);

        // Bottom Navigation
        findViewById(R.id.btnHome).setOnClickListener(v -> {
            // Already on Home
        });

        findViewById(R.id.btnLibrary).setOnClickListener(v -> {
            Toast.makeText(this, "Library coming soon!", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.btnProfile).setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
        });

        // Settings button in header
        findViewById(R.id.btnSettings).setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, SettingsActivity.class));
        });

        // Search button
        findViewById(R.id.btnSearch).setOnClickListener(v -> {
            Toast.makeText(this, "Search coming soon!", Toast.LENGTH_SHORT).show();
        });

        // Avatar click -> Profile
        findViewById(R.id.imgAvatarHome).setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
        });
    }
}
