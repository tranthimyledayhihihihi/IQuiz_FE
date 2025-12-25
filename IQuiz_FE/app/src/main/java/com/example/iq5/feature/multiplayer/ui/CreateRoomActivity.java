package com.example.iq5.feature.multiplayer.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iq5.R;
import com.example.iq5.feature.multiplayer.data.WebSocketManager;

public class CreateRoomActivity extends AppCompatActivity {

    private WebSocketManager wsManager;

    private TextView tvStatus, tvRoomCode;
    private Button btnCreateRoom, btnBack;
    private Spinner spinnerQuestionCount, spinnerDifficulty;

    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_room);

        initViews();
        setupSpinners();
        setupWebSocket();
        connectWebSocket();
    }

    private void initViews() {
        tvStatus = findViewById(R.id.tvStatus);
        tvRoomCode = findViewById(R.id.tvRoomCode);
        btnCreateRoom = findViewById(R.id.btnCreateRoom);
        btnBack = findViewById(R.id.btnBack);
        spinnerQuestionCount = findViewById(R.id.spinnerQuestionCount);
        spinnerDifficulty = findViewById(R.id.spinnerDifficulty);

        tvRoomCode.setVisibility(TextView.GONE);
        tvStatus.setText("⏳ Đang kết nối...");

        btnCreateRoom.setEnabled(false);

        btnCreateRoom.setOnClickListener(v -> createRoom());
        btnBack.setOnClickListener(v -> finish());
    }

    private void setupSpinners() {
        // Question count
        String[] questionCounts = {"5 câu", "10 câu", "15 câu", "20 câu"};
        ArrayAdapter<String> questionAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                questionCounts
        );
        questionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerQuestionCount.setAdapter(questionAdapter);
        spinnerQuestionCount.setSelection(1); // 10 câu

        // Difficulty
        String[] difficulties = {"Dễ", "Trung bình", "Khó"};
        ArrayAdapter<String> difficultyAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                difficulties
        );
        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDifficulty.setAdapter(difficultyAdapter);
        spinnerDifficulty.setSelection(1); // Trung bình
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
                    tvStatus.setText("🔌 Mất kết nối WebSocket");
                    btnCreateRoom.setEnabled(false);
                });
            }
        });

        wsManager.setOnRoomCreatedListener(roomCode -> {
            runOnUiThread(() -> {
                tvRoomCode.setText("Mã phòng: " + roomCode);
                tvRoomCode.setVisibility(TextView.VISIBLE);

                tvStatus.setText("✅ Phòng đã được tạo!");
                btnCreateRoom.setEnabled(false);
                btnCreateRoom.setText("✅ Đã tạo phòng");

                Toast.makeText(
                        this,
                        "Chia sẻ mã phòng cho bạn bè: " + roomCode,
                        Toast.LENGTH_LONG
                ).show();

                handler.postDelayed(() -> {
                    Intent intent = new Intent(this, WaitingRoomActivity.class);
                    intent.putExtra("roomCode", roomCode);
                    startActivity(intent);
                    finish();
                }, 1000);
            });
        });

        wsManager.setOnMatchFoundListener((matchCode, opponentId, role) -> {
            runOnUiThread(() -> {
                tvStatus.setText("🎮 Đối thủ đã vào phòng!");

                handler.postDelayed(() -> {
                    Intent intent = new Intent(this, MatchActivity.class);
                    intent.putExtra("matchCode", matchCode);
                    intent.putExtra("opponentId", opponentId);
                    intent.putExtra("role", role);
                    startActivity(intent);
                    finish();
                }, 1200);
            });
        });

        wsManager.setOnErrorListener(message -> {
            runOnUiThread(() -> {
                tvStatus.setText("❌ " + message);
                btnCreateRoom.setEnabled(true);
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            });
        });
    }

    private void connectWebSocket() {
        if (wsManager.isConnected()) return;

        tvStatus.setText("⏳ Đang kết nối...");
        btnCreateRoom.setEnabled(false);

        SharedPreferences prefs = getSharedPreferences("app_prefs", MODE_PRIVATE);
        String token = prefs.getString("auth_token", "");

        if (token == null || token.isEmpty()) {
            Toast.makeText(this, "❌ Token không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        String serverUrl = "ws://10.0.2.2:7092/ws/game";
        wsManager.connect(serverUrl, token);
    }

    private void createRoom() {
        if (!wsManager.isConnected()) {
            Toast.makeText(this, "❌ Chưa kết nối WebSocket", Toast.LENGTH_SHORT).show();
            return;
        }

        tvStatus.setText("⏳ Đang tạo phòng...");
        btnCreateRoom.setEnabled(false);

        // Hiện tại BE chưa nhận param → chỉ gửi CREATE_ROOM
        wsManager.createRoom();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
