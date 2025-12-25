package com.example.iq5.feature.multiplayer.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.iq5.R;
import com.example.iq5.feature.multiplayer.data.WebSocketManager;

/**
 * ✅ PHIÊN BẢN ĐỠN GIẢN - CHỈ WEBSOCKET
 * Không cần REST API, không cần Retrofit, không cần Repository
 * Tất cả đều qua WebSocket
 */
public class MultiplayerLobbyActivity extends AppCompatActivity {

    private static final String TAG = "MultiplayerLobby";

    // ⚠️ ĐỔI URL NÀY THEO MÔI TRƯỜNG
    // Emulator: "ws://10.0.2.2:7092/ws/game"
    // Real Device: "ws://192.168.1.100:7092/ws/game"
    // Production: "wss://yourdomain.com/ws/game"
    private static final String WS_URL = "ws://10.0.2.2:5048/ws/game";

    private WebSocketManager wsManager;

    private TextView tvStatus;
    private Button btnFindMatch, btnCancelQueue, btnCreateRoom, btnJoinRoom;
    private EditText etRoomCode;

    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer_lobby);

        wsManager = WebSocketManager.getInstance();

        initViews();
        setupWebSocket();
        connectWebSocket();
        setupClickListeners();
    }

    private void initViews() {
        tvStatus = findViewById(R.id.tvStatus);
        btnFindMatch = findViewById(R.id.btnFindMatch);
        btnCancelQueue = findViewById(R.id.btnCancelQueue);
        btnCreateRoom = findViewById(R.id.btnCreateRoom);
        btnJoinRoom = findViewById(R.id.btnJoinRoom);
        etRoomCode = findViewById(R.id.etRoomCode);

        btnCancelQueue.setVisibility(Button.GONE);
        tvStatus.setText("⏳ Đang kết nối...");
        enableButtons(false);
    }

    private void setupWebSocket() {
        // Connection listener
        wsManager.setOnConnectionListener(new WebSocketManager.OnConnectionListener() {
            @Override
            public void onConnected() {
                runOnUiThread(() -> {
                    Log.d(TAG, "Mở khóa nút bấm ngay bây giờ!");
                    tvStatus.setText("✅ Đã kết nối");
                    enableButtons(true);

                    // Kiểm tra thủ công từng nút nếu enableButtons không chạy
                    btnFindMatch.setEnabled(true);
                    btnCreateRoom.setEnabled(true);
                    btnJoinRoom.setEnabled(true);
                });
            }

            @Override
            public void onDisconnected() {
                runOnUiThread(() -> {
                    Log.d(TAG, "🔌 WebSocket disconnected");
                    tvStatus.setText("🔌 Mất kết nối");
                    enableButtons(false);

                    // Auto reconnect sau 3s
                    handler.postDelayed(() -> {
                        if (!wsManager.isConnected()) {
                            tvStatus.setText("🔄 Đang kết nối lại...");
                            connectWebSocket();
                        }
                    }, 3000);
                });
            }
        });

        // Match found listener
        wsManager.setOnMatchFoundListener((matchCode, opponentId, role) -> {
            runOnUiThread(() -> {
                Log.d(TAG, "🎮 Match found: " + matchCode);
                tvStatus.setText("🎮 Đã tìm thấy đối thủ!");

                // Join match
                wsManager.joinMatch(matchCode);

                // Navigate to MatchActivity
                handler.postDelayed(() -> {
                    Intent intent = new Intent(this, MatchActivity.class);
                    intent.putExtra("matchCode", matchCode);
                    intent.putExtra("opponentId", opponentId);
                    intent.putExtra("role", role);
                    startActivity(intent);

                    // Reset UI
                    btnFindMatch.setVisibility(Button.VISIBLE);
                    btnCancelQueue.setVisibility(Button.GONE);
                }, 1500);
            });
        });

        // Room created listener
        wsManager.setOnRoomCreatedListener(roomCode -> {
            runOnUiThread(() -> {
                Log.d(TAG, "✅ Room created: " + roomCode);
                tvStatus.setText("✅ Phòng đã tạo: " + roomCode);

                Intent intent = new Intent(this, WaitingRoomActivity.class);
                intent.putExtra("roomCode", roomCode);
                startActivity(intent);
            });
        });

        // Error listener
        wsManager.setOnErrorListener(message -> {
            runOnUiThread(() -> {
                Log.e(TAG, "❌ Error: " + message);
                tvStatus.setText("❌ " + message);
                btnFindMatch.setVisibility(Button.VISIBLE);
                btnCancelQueue.setVisibility(Button.GONE);

                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

                // Reset sau 3s
                handler.postDelayed(() -> {
                    if (wsManager.isConnected()) {
                        tvStatus.setText("✅ Đã kết nối");
                    }
                }, 3000);
            });
        });
    }

    private void connectWebSocket() {
        if (wsManager.isConnected()) {
            Log.d(TAG, "Already connected");
            return;
        }

        SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        String token = prefs.getString("auth_token", "");

        if (token == null || token.isEmpty()) {
            Toast.makeText(this, "❌ Chưa đăng nhập", Toast.LENGTH_SHORT).show();
            // Navigate to login
            return;
        }

        wsManager.connect(WS_URL, token);
    }

    private void setupClickListeners() {
        // Find random match
        btnFindMatch.setOnClickListener(v -> {
            if (!wsManager.isConnected()) {
                Toast.makeText(this, "❌ Chưa kết nối server", Toast.LENGTH_SHORT).show();
                return;
            }

            wsManager.findRandomMatch();
            tvStatus.setText("⏳ Đang tìm đối thủ...");
            btnFindMatch.setVisibility(Button.GONE);
            btnCancelQueue.setVisibility(Button.VISIBLE);

            // Timeout 60s
            handler.postDelayed(() -> {
                if (btnCancelQueue.getVisibility() == Button.VISIBLE) {
                    wsManager.cancelQueue();
                    tvStatus.setText("❌ Không tìm được đối thủ sau 60 giây");
                    btnFindMatch.setVisibility(Button.VISIBLE);
                    btnCancelQueue.setVisibility(Button.GONE);
                }
            }, 60000);
        });

        // Cancel queue
        btnCancelQueue.setOnClickListener(v -> {
            wsManager.cancelQueue();
            tvStatus.setText("✅ Đã hủy tìm trận");
            btnFindMatch.setVisibility(Button.VISIBLE);
            btnCancelQueue.setVisibility(Button.GONE);
        });

        // Create room
        btnCreateRoom.setOnClickListener(v -> {
            if (!wsManager.isConnected()) {
                Toast.makeText(this, "❌ Chưa kết nối server", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(this, CreateRoomActivity.class);
            startActivity(intent);
        });

        // Join room
        btnJoinRoom.setOnClickListener(v -> {
            String roomCode = etRoomCode.getText().toString().trim().toUpperCase();

            if (roomCode.isEmpty()) {
                Toast.makeText(this, "⚠️ Vui lòng nhập mã phòng!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!wsManager.isConnected()) {
                Toast.makeText(this, "❌ Chưa kết nối server", Toast.LENGTH_SHORT).show();
                return;
            }

            wsManager.joinRoomWithCode(roomCode);
            tvStatus.setText("⏳ Đang vào phòng " + roomCode + "...");
        });
    }

    private void enableButtons(boolean enabled) {
        btnFindMatch.setEnabled(enabled);
        btnCreateRoom.setEnabled(enabled);
        btnJoinRoom.setEnabled(enabled);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Reconnect nếu mất kết nối
        if (!wsManager.isConnected()) {
            connectWebSocket();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);

        // ⚠️ KHÔNG disconnect WebSocket - các activities khác có thể đang dùng
        // Chỉ disconnect khi user logout
    }
}