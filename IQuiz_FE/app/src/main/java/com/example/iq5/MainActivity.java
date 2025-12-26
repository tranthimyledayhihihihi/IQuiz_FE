package com.example.iq5;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.iq5.feature.multiplayer.ui.MultiplayerLobbyActivity;
import com.example.iq5.feature.quiz.ui.ReviewQuestionActivity;

import com.example.iq5.core.navigation.NavigationHelper;

public class MainActivity extends AppCompatActivity {

    private Button btnGoToOnline;
    private Button btnGoToSocial;
    private Button btnStartQuiz;
    private Button btnReview;
    private Button btnSpecialMode;
    
    // API Debug buttons
    private Button btnApiTest;
    private Button btnApiDebug;

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
        
        // API Debug buttons (có thể null nếu không có trong layout)
        // btnApiTest = findViewById(R.id.btnApiTest);
        // btnApiDebug = findViewById(R.id.btnApiDebug);
    }

    private void setupClickListeners() {
        if (btnGoToOnline != null) {
            btnGoToOnline.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, MultiplayerLobbyActivity.class);
                startActivity(intent);
            });
        }



        if (btnStartQuiz != null) {
            btnStartQuiz.setOnClickListener(v -> {
                // Sử dụng API Select Category thay vì mock data
                NavigationHelper.navigateToApiSelectCategory(this);
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
                // Chuyển đến Special Mode (chức năng thật)
                Toast.makeText(this, "Special Mode đang phát triển", Toast.LENGTH_SHORT).show();
            });
        }
        
        // Xóa các debug buttons - không cần thiết nữa
        if (btnApiTest != null) {
            btnApiTest.setVisibility(View.GONE);
        }
        
        if (btnApiDebug != null) {
            btnApiDebug.setVisibility(View.GONE);
        }
    }
}
