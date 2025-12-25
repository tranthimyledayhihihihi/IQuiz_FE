package com.example.iq5.feature.multiplayer.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.iq5.R;
import com.example.iq5.feature.multiplayer.data.WebSocketManager;

public class JoinRoomActivity extends AppCompatActivity {

    private static final String TAG = "JoinRoomActivity";

    private WebSocketManager wsManager;
    private TextView tvStatus;
    private EditText etRoomCode;
    private Button btnJoinRoom, btnBack;
    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_room);

        initViews();
        setupWebSocket();
        connectWebSocket();

        // Check if room code passed from intent (QR code, deep link)
        String roomCode = getIntent().getStringExtra("roomCode");
        if (roomCode != null && !roomCode.isEmpty()) {
            etRoomCode.setText(roomCode);
        }
    }

    private void initViews() {
        tvStatus = findViewById(R.id.tvStatus);
        etRoomCode = findViewById(R.id.etRoomCode);
        btnJoinRoom = findViewById(R.id.btnJoinRoom);
        btnBack = findViewById(R.id.btnBack);

        // Auto uppercase
        etRoomCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    String upper = s.toString().toUpperCase();
                    if (!s.toString().equals(upper)) {
                        etRoomCode.setText(upper);
                        etRoomCode.setSelection(upper.length());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        btnJoinRoom.setOnClickListener(v -> joinRoom());
        btnBack.setOnClickListener(v -> finish());

        tvStatus.setText("✅ Sẵn sàng vào phòng!");
    }

    private void setupWebSocket() {
        wsManager = WebSocketManager.getInstance();

        wsManager.setOnConnectionListener(new WebSocketManager.OnConnectionListener() {
            @Override
            public void onConnected() {
                runOnUiThread(() -> {
                    tvStatus.setText("✅ Sẵn sàng vào phòng!");
                    btnJoinRoom.setEnabled(true);
                });
            }

            @Override
            public void onDisconnected() {
                runOnUiThread(() -> {
                    tvStatus.setText("🔌 Mất kết nối");
                    btnJoinRoom.setEnabled(false);
                });
            }
        });

        wsManager.setOnMatchFoundListener((matchCode, opponentId, role) -> {
            runOnUiThread(() -> {
                tvStatus.setText("🎮 Đã vào phòng thành công!");
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
                tvStatus.setText("❌ " + message);
                btnJoinRoom.setEnabled(true);
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();

                handler.postDelayed(() -> {
                    tvStatus.setText("✅ Sẵn sàng vào phòng!");
                }, 3000);
            });
        });
    }

    private void connectWebSocket() {
        if (!wsManager.isConnected()) {
            tvStatus.setText("⏳ Đang kết nối...");
            btnJoinRoom.setEnabled(false);

            SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
            String token = prefs.getString("auth_token", "");
            String serverUrl = "ws://10.0.2.2:5048/ws/game";
            wsManager.connect(serverUrl, token);
        }
    }

    private void joinRoom() {
        String code = etRoomCode.getText().toString().trim();

        if (code.isEmpty()) {
            Toast.makeText(this, "⚠️ Vui lòng nhập mã phòng!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!wsManager.isConnected()) {
            Toast.makeText(this, "❌ Chưa kết nối WebSocket", Toast.LENGTH_SHORT).show();
            return;
        }

        wsManager.joinRoomWithCode(code);
        tvStatus.setText("⏳ Đang vào phòng...");
        btnJoinRoom.setEnabled(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(null);
    }
}