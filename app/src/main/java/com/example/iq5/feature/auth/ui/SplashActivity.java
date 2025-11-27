package com.example.iq5.feature.auth.ui;

import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.iq5.R;
import com.example.iq5.core.navigation.NavigationHelper;

public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_DELAY = 1500L; // 1.5s

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler(getMainLooper()).postDelayed(() -> {
            // Chuyển sang Login Activity và xóa Stack Activity (để người dùng không thể Back lại Splash)
            NavigationHelper.navigateToLogin(this, true);
        }, SPLASH_DELAY);
    }
}