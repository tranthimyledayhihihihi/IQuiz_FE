package com.example.iq5;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.iq5.feature.multiplayer.ui.FindMatchActivity;
import com.example.iq5.feature.multiplayer.ui.LeaderboardActivity;
import com.example.iq5.feature.quiz.ui.SelectCategoryActivity;
import com.example.iq5.feature.quiz.ui.ReviewQuestionActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnGoToOnline;
    private Button btnGoToSocial;
    private Button btnStartQuiz;
    private Button btnReview;
    private Button btnSpecialMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        setupWindowInsets();
        mapViews();
        setupClickListeners();
    }

    private void setupWindowInsets() {
        View root = findViewById(R.id.main);
        if (root != null) {
            ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }
    }

    private void mapViews() {
        btnGoToOnline = findViewById(R.id.btnGoToOnline);
        btnGoToSocial = findViewById(R.id.btnGoToSocial);
        btnStartQuiz = findViewById(R.id.btnStartQuiz);
        btnReview = findViewById(R.id.btnReview);
        btnSpecialMode = findViewById(R.id.btnSpecialMode);
    }

    private void setupClickListeners() {
        if (btnGoToOnline != null) {
            btnGoToOnline.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, FindMatchActivity.class);
                startActivity(intent);
            });
        }

        if (btnGoToSocial != null) {
            btnGoToSocial.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, LeaderboardActivity.class);
                startActivity(intent);
            });
        }

        if (btnStartQuiz != null) {
            btnStartQuiz.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, SelectCategoryActivity.class);
                startActivity(intent);
            });
        }

        if (btnReview != null) {
            btnReview.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, ReviewQuestionActivity.class);
                startActivity(intent);
            });
        }

        if (btnSpecialMode != null) {
            btnSpecialMode.setOnClickListener(v -> {
                // TODO: Sau này gán sang Activity chế độ đặc biệt thật sự
            });
        }
    }
}
