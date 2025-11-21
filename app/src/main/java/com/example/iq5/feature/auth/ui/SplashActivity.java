package com.example.iq5.feature.auth.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iq5.R;
import com.example.iq5.feature.auth.data.AuthRepository;
import com.example.iq5.feature.auth.model.SplashResponse;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        AuthRepository repo = new AuthRepository(this);
        SplashResponse data = repo.getSplashData();

        int delay = data.loadingTime > 0 ? data.loadingTime : 1500;

        new Handler(getMainLooper()).postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        }, delay);
    }
}
