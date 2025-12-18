package com.example.iq5.feature.specialmode.ui;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.iq5.R;
import com.google.android.material.button.MaterialButton;

public class SpecialModeTestActivity extends AppCompatActivity {

    MaterialButton btnDaily, btnWrong, btnChallenge, btnCustom, btnPlayers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specialmode_test);

        btnDaily = findViewById(R.id.btn_test_daily);
        btnWrong = findViewById(R.id.btn_test_wrong);
        btnChallenge = findViewById(R.id.btn_test_challenge);
        btnCustom = findViewById(R.id.btn_test_custom);
        btnPlayers = findViewById(R.id.btn_test_players);  // 👈 nhớ lấy view

        btnDaily.setOnClickListener(v -> showFragment(new DailyQuizFragment()));
        btnWrong.setOnClickListener(v -> showFragment(new WrongHistoryFragment()));
        btnChallenge.setOnClickListener(v -> showFragment(new ChallengeModesFragment()));
        btnCustom.setOnClickListener(v -> showFragment(new CustomQuizFragment()));
        btnPlayers.setOnClickListener(v -> showFragment(new PlayerSearchFragment())); // 👈 PLAYER

        // Mặc định mở DAILY
        showFragment(new DailyQuizFragment());
    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}

