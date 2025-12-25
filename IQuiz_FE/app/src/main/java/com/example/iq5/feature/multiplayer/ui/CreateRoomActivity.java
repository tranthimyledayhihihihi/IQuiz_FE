package com.example.iq5.feature.multiplayer.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.iq5.R;
import com.example.iq5.feature.multiplayer.data.WebSocketManager;

public class CreateRoomActivity extends AppCompatActivity {
    private WebSocketManager wsManager;
    private TextView tvStatus, tvRoomCode;
    private Button btnCreateRoom, btnBack;

    // Lưu ý: Spinner vẫn có trong XML nhưng chúng ta sẽ không setup dữ liệu tĩnh
    // để người dùng không chọn sai so với logic 10 câu của Server.

    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_room);

        initViews();
        setupWebSocket();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Kiểm tra kết nối từ WebSocketManager (Singleton)
        if (wsManager != null && wsManager.isConnected()) {
            tvStatus.setText("✅ Sẵn sàng tạo phòng!");
            btnCreateRoom.setEnabled(true);
        } else {
            connectWebSocket();
        }
    }

    private void initViews() {
        tvStatus = findViewById(R.id.tvStatus);
        tvRoomCode = findViewById(R.id.tvRoomCode);
        btnCreateRoom = findViewById(R.id.btnCreateRoom);
        btnBack = findViewById(R.id.btnBack);

        // Ẩn Spinner đi vì Server đang fix cứng 10 câu hỏi
        View spinnerArea = findViewById(R.id.spinnerQuestionCount);
        if (spinnerArea != null) spinnerArea.setVisibility(View.GONE);

        View difficultyArea = findViewById(R.id.spinnerDifficulty);
        if (difficultyArea != null) difficultyArea.setVisibility(View.GONE);

        tvRoomCode.setVisibility(View.GONE);
        btnCreateRoom.setEnabled(false); // Đợi kết nối Socket

        btnCreateRoom.setOnClickListener(v -> createRoom());
        btnBack.setOnClickListener(v -> finish());
    }

    private void setupWebSocket() {
        wsManager = WebSocketManager.getInstance();

        wsManager.setOnConnectionListener(new WebSocketManager.OnConnectionListener() {
            @Override
            public void onConnected() {
                runOnUiThread(() -> {
                    tvStatus.setText("✅ Sẵn sàng tạo phòng!");
                    btnCreateRoom.setEnabled(true);
                });
            }

            @Override
            public void onDisconnected() {
                runOnUiThread(() -> {
                    tvStatus.setText("🔌 Mất kết nối");
                    btnCreateRoom.setEnabled(false);
                });
            }
        });

        // 1. NHẬN MÃ PHÒNG TỪ SERVER (DỮ LIỆU SOCKET)
        wsManager.setOnRoomCreatedListener(roomCode -> {
            runOnUiThread(() -> {
                // Hiển thị mã phòng ngay tại màn hình này giống bản Web
                tvRoomCode.setText("MÃ PHÒNG: " + roomCode);
                tvRoomCode.setVisibility(View.VISIBLE);

                tvStatus.setText("✅ Phòng đã được tạo (10 câu hỏi)!");
                btnCreateRoom.setEnabled(false);
                btnCreateRoom.setText("✅ ĐÃ TẠO PHÒNG");
            });
        });

        // 2. NHẬN SỰ KIỆN ĐỐI THỦ VÀO PHÒNG (DỮ LIỆU SOCKET)
        wsManager.setOnMatchFoundListener((matchCode, opponentId, role) -> {
            runOnUiThread(() -> {
                tvStatus.setText("🎮 Đối thủ đã vào phòng!");

                // Chờ 1.5s giống Web để người dùng kịp thấy thông báo
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
    }

    private void connectWebSocket() {
        SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        String token = prefs.getString("auth_token", "");

        // Luôn sử dụng cổng 5048 (HTTP) để tránh lỗi SSL 404/Refused trên Emulator
        String serverUrl = "ws://10.0.2.2:5048/ws/game";
        wsManager.connect(serverUrl, token);
    }

    private void createRoom() {
        if (wsManager.isConnected()) {
            tvStatus.setText("⏳ Đang gửi yêu cầu tạo phòng...");
            btnCreateRoom.setEnabled(false);

            // Gửi lệnh CREATE_ROOM lên Socket.
            // Vì chúng ta thống nhất dùng 10 câu, ta không cần gửi kèm config.
            wsManager.createRoom();
        } else {
            Toast.makeText(this, "Chưa kết nối đến máy chủ!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        // Gỡ listener để tránh rò rỉ bộ nhớ khi Activity bị hủy
        if (wsManager != null) {
            wsManager.setOnConnectionListener(null);
            wsManager.setOnRoomCreatedListener(null);
            wsManager.setOnMatchFoundListener(null);
        }
    }
}