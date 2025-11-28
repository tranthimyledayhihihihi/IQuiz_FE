package com.example.iq5.feature.auth.ui;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.iq5.R;
import com.example.iq5.core.navigation.NavigationHelper;
import com.example.iq5.feature.auth.data.AuthRepository;
import com.example.iq5.feature.auth.model.ProfileResponse;

public class ProfileActivity extends AppCompatActivity {

    TextView tvName, tvEmail, tvQuizTaken, tvAvgScore;
    ImageView imgAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        tvQuizTaken = findViewById(R.id.tvQuizTaken);
        tvAvgScore = findViewById(R.id.tvAvgScore);
        imgAvatar = findViewById(R.id.imgAvatar);

        AuthRepository repo = new AuthRepository(this);
        ProfileResponse p = repo.getProfileData();

        tvName.setText(p.name);
        tvEmail.setText(p.email);
        tvQuizTaken.setText(String.valueOf(p.stats.quizzesTaken));
        tvAvgScore.setText(String.valueOf(p.stats.avgScore));

        Glide.with(this).load(p.avatarUrl).into(imgAvatar);

        // Navigate to Settings
        findViewById(R.id.btnSettings).setOnClickListener(v -> {
            NavigationHelper.navigateToSettings(this);
        });
    }

}
