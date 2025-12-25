package com.example.iq5.feature.multiplayer.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
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
    private final Handler handler = new Handler(Looper.getMainLooper());

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

        wsManager = WebSocketManager.getInstance();

        initViews();
        setupSignalRListeners();
        checkConnectionState();
    }

    /* ===================== UI ===================== */

    private void initViews() {
        tvStatus = findViewById(R.id.tvStatus);
        tvRoomCode = findViewById(R.id.tvRoomCode);
        tvInstruction = findViewById(R.id.tvInstruction);
        btnCopyCode = findViewById(R.id.btnCopyCode);
        btnCancel = findViewById(R.id.btnCancel);

        tvRoomCode.setText(roomCode);
        tvStatus.setText("⏳ Đang chờ người chơi khác...");
        tvInstruction.setText("Chia sẻ mã phòng này cho bạn bè để họ tham gia");

        btnCopyCode.setOnClickListener(v -> copyRoomCode());
        btnCancel.setOnClickListener(v -> cancelAndExit());
    }

    /* ===================== SIGNALR ===================== */

    private void setupSignalRListeners() {

        // ĐỐI THỦ VÀO PHÒNG
        wsManager.setOnMatchFoundListener((matchCode, opponentId, role) -> {
            runOnUiThread(() -> {
                Log.d(TAG, "🎮 Opponent joined room: " + matchCode);

                tvStatus.setText("🎮 Đối thủ đã vào phòng!");
                tvInstruction.setText("Chuẩn bị bắt đầu trận đấu...");

                handler.postDelayed(() -> {
                    Intent intent = new Intent(this, MatchResultActivity.class);
                    intent.putExtra("matchCode", matchCode);
                    intent.putExtra("opponentId", opponentId);
                    intent.putExtra("role", role);
                    startActivity(intent);
                    finish();
                }, 1500);
            });
        });

        // LỖI TỪ SERVER
        wsManager.setOnErrorListener(message -> {
            runOnUiThread(() -> {
                Log.e(TAG, "❌ SignalR error: " + message);

                if (message.toLowerCase().contains("hết hạn")
                        || message.toLowerCase().contains("expired")) {
                    Toast.makeText(this, "❌ Phòng đã hết hạn", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    tvStatus.setText("❌ " + message);
                    Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void checkConnectionState() {
        if (!wsManager.isConnected()) {
            Toast.makeText(this, "🔌 Mất kết nối server", Toast.LENGTH_SHORT).show();
            tvStatus.setText("🔌 Mất kết nối server");
        }
    }

    /* ===================== ACTIONS ===================== */

    private void copyRoomCode() {
        ClipboardManager clipboard =
                (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        ClipData clip = ClipData.newPlainText("Room Code", roomCode);
        clipboard.setPrimaryClip(clip);

        Toast.makeText(this,
                "✅ Đã copy mã phòng: " + roomCode,
                Toast.LENGTH_SHORT).show();
    }

    private void cancelAndExit() {
        // Nếu sau này có logic huỷ phòng, gọi tại đây
        Toast.makeText(this, "🚪 Đã rời phòng", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);

        // GỠ LISTENER ĐỂ TRÁNH EVENT BỊ BẮN SAI ACTIVITY
        if (wsManager != null) {
            wsManager.setOnMatchFoundListener(null);
            wsManager.setOnErrorListener(null);
        }
    }
}
