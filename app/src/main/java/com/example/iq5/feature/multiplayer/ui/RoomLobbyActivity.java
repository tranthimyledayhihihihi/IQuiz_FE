package com.example.iq5.feature.multiplayer.ui;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iq5.R;
import com.example.iq5.core.navigation.NavigationHelper;

public class RoomLobbyActivity extends AppCompatActivity {

    private TextView tvRoomId, tvPlayerCount;
    private RecyclerView rvPlayers;
    private Button btnStartMatch, btnLeaveRoom;
    private String roomId;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        roomId = getIntent().getStringExtra("room_id");
        if (roomId == null) roomId = "unknown";

        Toast.makeText(this, "Phòng: " + roomId + " - 2/2 người chơi", Toast.LENGTH_SHORT).show();
        handler = new Handler(getMainLooper());
        
        autoStartMatch();
    }

    private void autoStartMatch() {
        // Tự động bắt đầu sau 2 giây
        handler.postDelayed(this::startMatch, 2000);
    }

    private void startMatch() {
        String matchId = "match_" + System.currentTimeMillis();
        NavigationHelper.navigateToPvPBattle(this, matchId);
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
