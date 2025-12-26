package com.example.iq5.feature.multiplayer.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iq5.R;
import com.example.iq5.feature.multiplayer.data.WebSocketManager;

public class CreateRoomActivity extends AppCompatActivity {

    private static final String TAG = "CreateRoomActivity";

    private WebSocketManager wsManager;
    private TextView tvStatus, tvRoomCode;
    private Button btnCreateRoom, btnBack;

    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_room);

        initViews();
        setupWebSocket(); // Đổi tên từ setupSignalR
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (wsManager != null && wsManager.isConnected()) {
            tvStatus.setText("✅ Sẵn sàng tạo phòng!");
            btnCreateRoom.setEnabled(true);
        } else {
            connectWebSocket(); // Đổi tên từ connectSignalR
        }
    }

    /* ===================== INIT UI ===================== */

    private void initViews() {
        tvStatus = findViewById(R.id.tvStatus);
        tvRoomCode = findViewById(R.id.tvRoomCode);
        btnCreateRoom = findViewById(R.id.btnCreateRoom);
        btnBack = findViewById(R.id.btnBack);

        // Ẩn các spinner vì server fix mặc định 10 câu
        View spinnerArea = findViewById(R.id.spinnerQuestionCount);
        if (spinnerArea != null) spinnerArea.setVisibility(View.GONE);

        View difficultyArea = findViewById(R.id.spinnerDifficulty);
        if (difficultyArea != null) difficultyArea.setVisibility(View.GONE);

        tvRoomCode.setVisibility(View.GONE);
        btnCreateRoom.setEnabled(false);

        btnCreateRoom.setOnClickListener(v -> createRoom());
        btnBack.setOnClickListener(v -> finish());
    }

    /* ===================== WEBSOCKET LOGIC ===================== */

    private void setupWebSocket() {
        wsManager = WebSocketManager.getInstance();

        wsManager.setOnConnectionListener(new WebSocketManager.OnConnectionListener() {
            @Override
            public void onConnected() {
                runOnUiThread(() -> {
                    Log.d(TAG, "✅ WebSocket connected");
                    tvStatus.setText("✅ Sẵn sàng tạo phòng!");
                    btnCreateRoom.setEnabled(true);
                });
            }

            @Override
            public void onDisconnected() {
                runOnUiThread(() -> {
                    Log.e(TAG, "🔌 WebSocket disconnected");
                    tvStatus.setText("🔌 Mất kết nối");
                    btnCreateRoom.setEnabled(false);
                });
            }
        });

        // Nhận mã phòng từ Server
        wsManager.setOnRoomCreatedListener(roomCode -> {
            runOnUiThread(() -> {
                Log.d(TAG, "🏠 Room created: " + roomCode);
                tvRoomCode.setText("MÃ PHÒNG: " + roomCode);
                tvRoomCode.setVisibility(View.VISIBLE);

                tvStatus.setText("✅ Phòng đã được tạo (10 câu hỏi)");
                btnCreateRoom.setEnabled(false);
                btnCreateRoom.setText("✅ ĐÃ TẠO PHÒNG");
            });
        });

        // Khi có đối thủ tham gia, chuyển sang màn hình thi đấu (MatchActivity)
        wsManager.setOnMatchFoundListener((matchCode, opponentId, role) -> {
            runOnUiThread(() -> {
                tvStatus.setText("🎮 Đối thủ đã vào phòng!");

                handler.postDelayed(() -> {
                    // Chuyển sang MatchResultActivity (là màn hình thi đấu chính trong code của bạn)
                    Intent intent = new Intent(this, MatchResultActivity.class);
                    intent.putExtra("matchCode", matchCode);
                    intent.putExtra("opponentId", opponentId);
                    intent.putExtra("role", role);
                    startActivity(intent);
                    finish();
                }, 1500); // Đợi 1.5s giống logic trên Web
            });
        });
    }

    private void connectWebSocket() {
        if (wsManager.isConnected()) {
            return;
        }

        SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        String token = prefs.getString("auth_token", "");

        if (token == null || token.isEmpty()) {
            Toast.makeText(this, "❌ Chưa đăng nhập", Toast.LENGTH_SHORT).show();
            return;
        }

        // URL chuẩn cho WebSocket kết nối tới Backend của bạn qua cổng 5048
        String wsUrl = "ws://172.26.97.66:5048/ws/game";
        Log.d(TAG, "🔌 Connecting WebSocket: " + wsUrl);

        wsManager.connect(wsUrl, token);
    }

    /* ===================== ACTIONS ===================== */

    private void createRoom() {
        if (!wsManager.isConnected()) {
            Toast.makeText(this, "❌ Chưa kết nối server", Toast.LENGTH_SHORT).show();
            return;
        }

        tvStatus.setText("⏳ Đang gửi yêu cầu tạo phòng...");
        btnCreateRoom.setEnabled(false);

        // Gửi lệnh CREATE_ROOM qua Socket
        wsManager.createRoom();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);

        // Gỡ listener để tránh rò rỉ bộ nhớ
        if (wsManager != null) {
            wsManager.setOnConnectionListener(null);
            wsManager.setOnRoomCreatedListener(null);
            wsManager.setOnMatchFoundListener(null);
        }
    }
}