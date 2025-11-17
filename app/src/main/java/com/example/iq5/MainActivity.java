package com.example.iq5;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;

// Import màn hình FindMatchActivity (Màn 1)
import com.example.iq5.feature.multiplayer.ui.FindMatchActivity;
import com.example.iq5.feature.multiplayer.ui.LeaderboardActivity;
// SỬA LỖI: Import đúng Activity chứa ViewPager (Màn 5)


public class MainActivity extends AppCompatActivity {

    private Button btnGoToOnline;
    private Button btnGoToSocial; // Nút mới

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Tải layout activity_main.xml

        // Ánh xạ nút 1 (Luồng Chơi Game)
        btnGoToOnline = findViewById(R.id.btnGoToOnline);
        // Ánh xạ nút 2 (Luồng Xã Hội)
        btnGoToSocial = findViewById(R.id.btnGoToSocial);

        // Đặt sự kiện click cho Luồng 1
        btnGoToOnline.setOnClickListener(v -> {
            // Mở Màn hình 1: FindMatchActivity
            Intent intent = new Intent(MainActivity.this, FindMatchActivity.class);
            startActivity(intent);
        });

        // Đặt sự kiện click cho Luồng 2
        btnGoToSocial.setOnClickListener(v -> {
            // SỬA LỖI: Mở Màn hình 5 (FriendsLeaderboardActivity)
            Intent intent = new Intent(MainActivity.this, LeaderboardActivity.class);
            startActivity(intent);
        });
    }
}