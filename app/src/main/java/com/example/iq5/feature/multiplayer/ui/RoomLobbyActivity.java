package com.example.iq5.feature.multiplayer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iq5.R;
import com.google.android.material.button.MaterialButton;

public class RoomLobbyActivity extends AppCompatActivity {

    private Toolbar toolbarLobby;
    private TextView tvCountdownTimer;
    private TextView tvCurrency;

    private MaterialButton btnReady, btnLeave, btnSpectate, btnSendChat;
    private MaterialButton btnMode1v1; // Giữ lại nút 1v1

    private RecyclerView rvChat;
    private EditText etChatMessage;

    private String roomCode;
    private boolean isHost;
    private boolean isPlayerJoined = false;
    private boolean isReady = false;

    // LOGIC TIMER
    private final int COUNTDOWN_START_TIME = 5;
    private int currentCountdownTime = COUNTDOWN_START_TIME;

    private Handler countdownHandler = new Handler();
    private Runnable countdownRunnable = new Runnable() {
        @Override
        public void run() {
            if (currentCountdownTime > 0) {
                currentCountdownTime--;
                String timeStr = String.format("00:%02d", currentCountdownTime);
                tvCountdownTimer.setText(timeStr);
                countdownHandler.postDelayed(this, 1000);
            } else {
                startGame();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_lobby);

        setupOnBackPressedCallback();

        roomCode = getIntent().getStringExtra("ROOM_CODE");
        isHost = getIntent().getBooleanExtra("IS_HOST", false);

        // --- Ánh xạ UI ---
        toolbarLobby = findViewById(R.id.toolbarLobby);
        setSupportActionBar(toolbarLobby);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Phòng: " + roomCode);
        }

        tvCountdownTimer = findViewById(R.id.tvCountdownTimer);
        tvCurrency = findViewById(R.id.tvCurrency);

        btnReady = findViewById(R.id.btnReady);
        btnLeave = findViewById(R.id.btnLeave);
        btnSpectate = findViewById(R.id.btnSpectate);
        btnMode1v1 = findViewById(R.id.btnMode1v1);
        // btnModeTeam đã bị xóa và không được ánh xạ.

        rvChat = findViewById(R.id.rvChat);
        etChatMessage = findViewById(R.id.etChatMessage);
        btnSendChat = findViewById(R.id.btnSendChat);

        // Giả lập hiển thị ban đầu
        tvCountdownTimer.setText("00:" + String.format("%02d", COUNTDOWN_START_TIME));
        tvCurrency.setText("Phòng: " + roomCode);

        // --- Logic Ban đầu và Listeners ---
        setupListeners();

        if (isHost) {
            new android.os.Handler().postDelayed(this::onPlayerJoined, 3000);
        }
    }

    private void setupListeners() {

        btnReady.setOnClickListener(v -> {
            isReady = !isReady; // Đảo trạng thái Ready
            updateReadyState(isReady);
            // TODO: Gửi trạng thái READY/UNREADY qua WebSocket
        });

        btnLeave.setOnClickListener(v -> {
            showLeaveConfirmationDialog();
        });

        btnSpectate.setOnClickListener(v -> {
            Toast.makeText(this, "Bạn đã chuyển sang chế độ khán giả!", Toast.LENGTH_SHORT).show();
            // TODO: Gửi sự kiện SPECTATE qua WebSocket
        });

        btnSendChat.setOnClickListener(v -> {
            String message = etChatMessage.getText().toString().trim();
            if (!message.isEmpty()) {
                // TODO: Gửi tin nhắn qua WebSocket
                Toast.makeText(this, "Đã gửi: " + message, Toast.LENGTH_SHORT).show();
                etChatMessage.setText("");
            }
        });

        // Listener cho nút btnMode1v1 (Nếu có)
        btnMode1v1.setOnClickListener(v -> {
            Toast.makeText(this, "Chế độ 1 vs 1 đang hoạt động.", Toast.LENGTH_SHORT).show();
        });
    }

    /**
     * Cập nhật trạng thái nút Ready và logic đếm ngược (Chỉ 1 vs 1).
     */
    private void updateReadyState(boolean ready) {
        isReady = ready;
        if (ready) {
            btnReady.setText("UNREADY");
            btnReady.setBackgroundTintList(getResources().getColorStateList(R.color.colorAccent, getTheme()));

            // Bắt đầu đếm ngược nếu đối thủ đã tham gia
            if (isPlayerJoined) {
                startCountdown();
            } else {
                Toast.makeText(this, "Đang chờ đối thủ tham gia...", Toast.LENGTH_SHORT).show();
            }
        } else {
            btnReady.setText("READY");
            btnReady.setBackgroundTintList(getResources().getColorStateList(R.color.colorWin, getTheme()));
            stopCountdown();
        }
    }

    /**
     * Bắt đầu đếm ngược để khởi động trò chơi.
     */
    private void startCountdown() {
        stopCountdown(); // Đảm bảo timer cũ đã dừng
        currentCountdownTime = COUNTDOWN_START_TIME;

        tvCountdownTimer.setText("00:" + String.format("%02d", currentCountdownTime));
        countdownHandler.postDelayed(countdownRunnable, 1000);
        Toast.makeText(this, "Đếm ngược bắt đầu!", Toast.LENGTH_SHORT).show();
    }

    /**
     * Dừng đếm ngược.
     */
    private void stopCountdown() {
        countdownHandler.removeCallbacks(countdownRunnable);
        currentCountdownTime = COUNTDOWN_START_TIME;
        tvCountdownTimer.setText("00:" + String.format("%02d", currentCountdownTime));
    }

    /**
     * Thiết lập OnBackPressedCallback để xử lý nút Back vật lý/cử chỉ.
     */
    private void setupOnBackPressedCallback() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                showLeaveConfirmationDialog();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    // Phương thức xử lý khi nhấn vào icon điều hướng trên Toolbar.
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    // Gán chức năng hỏi xác nhận cho hành động Back
    @Override
    public void onBackPressed() {
        showLeaveConfirmationDialog();
    }

    /**
     * Hiển thị hộp thoại xác nhận rời phòng.
     */
    private void showLeaveConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Rời Phòng")
                .setMessage("Bạn có chắc chắn muốn rời khỏi phòng chờ?")
                .setPositiveButton("RỜI", (dialog, which) -> {
                    // TODO: Gửi sự kiện LEAVE_ROOM qua WebSocket
                    stopCountdown();
                    finish();
                })
                .setNegativeButton("HỦY", null)
                .show();
    }
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


    // Hàm này được gọi khi có sự kiện WebSocket "USER_JOINED"
    private void onPlayerJoined() {
        isPlayerJoined = true;
        Toast.makeText(this, "Đối thủ đã tham gia phòng!", Toast.LENGTH_SHORT).show();
        // Nếu đã ready, bắt đầu đếm ngược ngay khi đối thủ tham gia
        if (isReady) {
            startCountdown();
        }
    }

    // Hàm này được gọi khi có sự kiện "GAME_START"
    private void startGame() {
        stopCountdown();
        Toast.makeText(this, "Bắt đầu trận đấu!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(RoomLobbyActivity.this, PvPBattleActivity.class);
        intent.putExtra("ROOM_CODE", roomCode);
        startActivity(intent);
        finish();
    }
}