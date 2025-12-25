package com.example.iq5.feature.multiplayer.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.iq5.R;
import com.example.iq5.feature.multiplayer.data.WebSocketManager;

public class WaitingRoomActivity extends AppCompatActivity {

    private static final String TAG = "WaitingRoomActivity";

    private WebSocketManager wsManager;
    private TextView tvStatus, tvRoomCode, tvInstruction;
    private Button btnCopyCode, btnCancel;
    private String roomCode;
    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_room);

        roomCode = getIntent().getStringExtra("roomCode");
        if (roomCode == null || roomCode.isEmpty()) {
            Toast.makeText(this, "❌ Mã phòng không hợp lệ!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        setupWebSocket();
    }

    private void initViews() {
        tvStatus = findViewById(R.id.tvStatus);
        tvRoomCode = findViewById(R.id.tvRoomCode);
        tvInstruction = findViewById(R.id.tvInstruction);
        btnCopyCode = findViewById(R.id.btnCopyCode);
        btnCancel = findViewById(R.id.btnCancel);

        tvRoomCode.setText(roomCode);
        tvStatus.setText("⏳ Đang chờ người chơi khác...");
        tvInstruction.setText("Chia sẻ mã phòng này cho bạn bè để họ có thể tham gia!");

        btnCopyCode.setOnClickListener(v -> copyRoomCode());
        btnCancel.setOnClickListener(v -> cancelAndExit());
    }

    private void setupWebSocket() {
        wsManager = WebSocketManager.getInstance();

        wsManager.setOnMatchFoundListener((matchCode, opponentId, role) -> {
            runOnUiThread(() -> {
                tvStatus.setText("🎮 Đối thủ đã vào phòng!");
                tvInstruction.setText("Bắt đầu trận đấu...");

                handler.postDelayed(() -> {
                    Intent intent = new Intent(this, MatchActivity.class);
                    intent.putExtra("matchCode", matchCode);
                    intent.putExtra("opponentId", opponentId);
                    intent.putExtra("role", role);
                    startActivity(intent);
                    finish();
                }, 1500);
            });
        });

        wsManager.setOnErrorListener(message -> {
            runOnUiThread(() -> {
                if (message.contains("hết hạn") || message.contains("expired")) {
                    Toast.makeText(this, "❌ Phòng đã hết hạn", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    tvStatus.setText("❌ " + message);
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void copyRoomCode() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Room Code", roomCode);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "✅ Đã copy mã phòng: " + roomCode, Toast.LENGTH_SHORT).show();
    }

    private void cancelAndExit() {
        // TODO: Send cancel room message to server
        Toast.makeText(this, "Đã hủy phòng", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(null);
    }
}