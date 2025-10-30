package com.example.quiz;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.quiz.ui.*;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {
    private FrameLayout container;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_SpecialModes);
        setContentView(R.layout.activity_main);
        container = findViewById(R.id.fragment_container);


        BottomNavigationView bottom = findViewById(R.id.bottom_nav);
        bottom.setOnItemSelectedListener(this::onNavSelected);


// Mặc định mở Quiz mỗi ngày
        if (savedInstanceState == null) {
            switchFragment(new DailyQuizFragment());
            bottom.setSelectedItemId(R.id.nav_quiz);
        }
    }


    private boolean onNavSelected(@NonNull MenuItem item) {
        Fragment f;
        int id = item.getItemId();
        if (id == R.id.nav_quiz) f = new DailyQuizFragment();
        else if (id == R.id.nav_mistake) f = new MistakeHistoryFragment();
        else if (id == R.id.nav_challenge) f = new ChallengeFragment();
        else if (id == R.id.nav_custom) f = new CustomSetFragment();
        else f = new PlayerSearchFragment();


        switchFragment(f);
        return true;
    }


    private void switchFragment(Fragment f) {
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.fragment_container, f)
                .commit();
    }
}