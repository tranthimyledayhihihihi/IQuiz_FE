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
import com.example.iq5.feature.multiplayer.data.api.ApiService;

/**
 * ✅ MULTIPLAYER LOBBY – VERSION HOÀN CHỈNH
 * - WebSocket matchmaking
 * - Hiển thị số người online (REST API)
 * - Auto reconnect
 * - Không phá kiến trúc cũ
 */
public class MultiplayerLobbyActivity extends AppCompatActivity {
    private boolean isNavigatingToMatch = false;

    private static final String TAG = "MultiplayerLobby";

    // ================== UI ==================
    private TextView tvStatus;
    private TextView tvOnlineCount;
    private Button btnFindMatch, btnCancelQueue, btnCreateRoom, btnJoinRoom;
    private EditText etRoomCode;

    // ================== HANDLER ==================
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Handler onlineCountHandler = new Handler(Looper.getMainLooper());
    private Runnable onlineCountRunnable;

    // ================== WS ==================
    private static final String WS_URL = "ws://172.26.93.231:5048/ws/game";
    private WebSocketManager wsManager;

    // =========================================================
    // LIFECYCLE
    // =========================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer_lobby);

        WebSocketManager.resetInstance(); // ⭐ RESET SẠCH
        wsManager = WebSocketManager.getInstance();


        initViews();
        setupWebSocket();
        connectWebSocket();
        setupClickListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();

        isNavigatingToMatch = false; // ⭐⭐⭐ BẮT BUỘC

        setupWebSocket();
        connectWebSocket();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        onlineCountHandler.removeCallbacksAndMessages(null);
        // ⚠️ KHÔNG disconnect WS – dùng chung cho nhiều màn
    }
    @Override
    protected void onPause() {
        super.onPause();

        onlineCountHandler.removeCallbacksAndMessages(null); // ⭐ THÊM

        if (!isNavigatingToMatch) {
            wsManager.cancelQueue();
            wsManager.disconnect();
        }
    }



    // =========================================================
    // INIT VIEW
    // =========================================================
    private void initViews() {
        tvStatus = findViewById(R.id.tvStatus);
        tvOnlineCount = findViewById(R.id.tvOnlineCount);

        btnFindMatch = findViewById(R.id.btnFindMatch);
        btnCancelQueue = findViewById(R.id.btnCancelQueue);
        btnCreateRoom = findViewById(R.id.btnCreateRoom);
        btnJoinRoom = findViewById(R.id.btnJoinRoom);
        etRoomCode = findViewById(R.id.etRoomCode);

        btnCancelQueue.setVisibility(Button.GONE);
        tvStatus.setText("⏳ Đang kết nối...");
        tvOnlineCount.setText("👥 ... người đang online");

        enableButtons(false);
    }

    // =========================================================
    // WEBSOCKET SETUP
    // =========================================================
    private void setupWebSocket() {

        // ===== CONNECT / DISCONNECT =====
        wsManager.setOnConnectionListener(new WebSocketManager.OnConnectionListener() {
            @Override
            public void onConnected() {
                runOnUiThread(() -> {
                    Log.d(TAG, "✅ WebSocket connected");
                    tvStatus.setText("✅ Đã kết nối");
                    enableButtons(true);

                    // Bắt đầu update online count
                    startOnlineCountUpdater();
                });
            }

            @Override
            public void onDisconnected() {
                runOnUiThread(() -> {
                    Log.d(TAG, "🔌 WebSocket disconnected");
                    tvStatus.setText("🔌 Mất kết nối");
                    enableButtons(false);

                    // Reconnect sau 3s
                    handler.postDelayed(() -> {
                        if (!wsManager.isConnected()) {
                            tvStatus.setText("🔄 Đang kết nối lại...");
                            connectWebSocket();
                        }
                    }, 3000);
                });
            }
        });

        // ===== MATCH FOUND =====
        wsManager.setOnMatchFoundListener((matchCode, opponentId, role) -> {
            runOnUiThread(() -> {
                tvStatus.setText("🎮 Đã tìm thấy đối thủ!");
                isNavigatingToMatch = true;
                wsManager.joinMatch(matchCode);

                handler.postDelayed(() -> {
                    Intent intent = new Intent(this, MatchResultActivity.class);
                    intent.putExtra("matchCode", matchCode);
                    intent.putExtra("opponentId", opponentId);
                    intent.putExtra("role", role);
                    startActivity(intent);

                    btnFindMatch.setVisibility(Button.VISIBLE);
                    btnCancelQueue.setVisibility(Button.GONE);
                }, 1500);
            });
        });

        // ===== ROOM CREATED =====
        wsManager.setOnRoomCreatedListener(roomCode -> {
            runOnUiThread(() -> {
                tvStatus.setText("✅ Phòng đã tạo: " + roomCode);
                Intent intent = new Intent(this, WaitingRoomActivity.class);
                intent.putExtra("roomCode", roomCode);
                startActivity(intent);
            });
        });

        // ===== ERROR =====
        wsManager.setOnErrorListener(message -> {
            runOnUiThread(() -> {
                tvStatus.setText("❌ " + message);
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

                btnFindMatch.setVisibility(Button.VISIBLE);
                btnCancelQueue.setVisibility(Button.GONE);

                handler.postDelayed(() -> {
                    if (wsManager.isConnected()) {
                        tvStatus.setText("✅ Đã kết nối");
                    }
                }, 3000);
            });
        });
    }

    // =========================================================
    // CONNECT WS
    // =========================================================
    private void connectWebSocket() {
        SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        String token = prefs.getString("auth_token", "");

        if (token == null || token.isEmpty()) {
            Toast.makeText(this, "❌ Chưa đăng nhập", Toast.LENGTH_SHORT).show();
            return;
        }

        wsManager.connect(WS_URL, token);
    }


    // =========================================================
    // ONLINE COUNT
    // =========================================================
    private void startOnlineCountUpdater() {
        onlineCountRunnable = new Runnable() {
            @Override
            public void run() {
                if (wsManager.isConnected()) {
                    updateOnlineCount();
                }
                onlineCountHandler.postDelayed(this, 2000); // 5s
            }
        };
        onlineCountHandler.post(onlineCountRunnable);
    }

    private void updateOnlineCount() {
        ApiService.getInstance(this).getOnlineCount(new ApiService.ApiCallback<Integer>() {
            @Override
            public void onSuccess(Integer count) {
                runOnUiThread(() ->
                        tvOnlineCount.setText("👥 " + count + " người đang online")
                );
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() ->
                        tvOnlineCount.setText("👥 ? người đang online")
                );
            }
        });
    }

    // =========================================================
    // CLICK LISTENERS
    // =========================================================
    private void setupClickListeners() {

        btnFindMatch.setOnClickListener(v -> {
            if (!wsManager.isConnected()) {
                Toast.makeText(this, "❌ Chưa kết nối server", Toast.LENGTH_SHORT).show();
                return;
            }

            wsManager.findRandomMatch();
            tvStatus.setText("⏳ Đang tìm đối thủ...");
            btnFindMatch.setVisibility(Button.GONE);
            btnCancelQueue.setVisibility(Button.VISIBLE);

            handler.postDelayed(() -> {
                if (btnCancelQueue.getVisibility() == Button.VISIBLE) {
                    wsManager.cancelQueue();
                    tvStatus.setText("❌ Không tìm được đối thủ sau 60 giây");
                    btnFindMatch.setVisibility(Button.VISIBLE);
                    btnCancelQueue.setVisibility(Button.GONE);
                }
            }, 60000);
        });

        btnCancelQueue.setOnClickListener(v -> {
            wsManager.cancelQueue();
            tvStatus.setText("✅ Đã hủy tìm trận");
            btnFindMatch.setVisibility(Button.VISIBLE);
            btnCancelQueue.setVisibility(Button.GONE);
        });

        btnCreateRoom.setOnClickListener(v -> {
            if (!wsManager.isConnected()) {
                Toast.makeText(this, "❌ Chưa kết nối server", Toast.LENGTH_SHORT).show();
                return;
            }
            startActivity(new Intent(this, CreateRoomActivity.class));
        });

        btnJoinRoom.setOnClickListener(v -> {
            String roomCode = etRoomCode.getText().toString().trim().toUpperCase();
            if (roomCode.isEmpty()) {
                Toast.makeText(this, "⚠️ Vui lòng nhập mã phòng!", Toast.LENGTH_SHORT).show();
                return;
            }
            wsManager.joinRoomWithCode(roomCode);
            tvStatus.setText("⏳ Đang vào phòng " + roomCode + "...");
        });
    }

    // =========================================================
    // UTIL
    // =========================================================
    private void enableButtons(boolean enabled) {
        btnFindMatch.setEnabled(enabled);
        btnCreateRoom.setEnabled(enabled);
        btnJoinRoom.setEnabled(enabled);
    }
}
