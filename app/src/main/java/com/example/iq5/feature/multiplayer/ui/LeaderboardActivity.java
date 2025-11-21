package com.example.iq5.feature.multiplayer.ui;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iq5.R;
import com.example.iq5.core.navigation.NavigationHelper;

public class LeaderboardActivity extends AppCompatActivity {

    private RecyclerView rvLeaderboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.makeText(this, "Bảng xếp hạng - Đang phát triển", Toast.LENGTH_SHORT).show();
        
        // Auto back sau 2 giây
        new android.os.Handler(getMainLooper()).postDelayed(() -> {
            NavigationHelper.goBack(this);
        }, 2000);
    }
}
