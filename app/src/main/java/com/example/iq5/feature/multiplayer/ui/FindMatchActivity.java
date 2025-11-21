package com.example.iq5.feature.multiplayer.ui;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iq5.R;
import com.example.iq5.core.navigation.NavigationHelper;

public class FindMatchActivity extends AppCompatActivity {

    private TextView tvSearching;
    private ProgressBar progressBar;
    private Button btnCancel;
    private Handler handler;
    private boolean isSearching = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Sử dụng layout tạm thời - có thể thay bằng layout thật sau
        setContentView(R.layout.activity_main);

        Toast.makeText(this, "Đang tìm đối thủ...", Toast.LENGTH_SHORT).show();
        handler = new Handler(getMainLooper());
        
        startSearching();
    }

    private void startSearching() {
        isSearching = true;

        // Giả lập tìm match sau 3 giây
        handler.postDelayed(() -> {
            if (isSearching) {
                onMatchFound();
            }
        }, 3000);
    }

    private void onMatchFound() {
        Toast.makeText(this, "Đã tìm thấy đối thủ!", Toast.LENGTH_SHORT).show();
        
        // Chuyển sang Room Lobby với roomId giả lập
        String roomId = "room_" + System.currentTimeMillis();
        NavigationHelper.navigateToRoomLobby(this, roomId);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }
}
