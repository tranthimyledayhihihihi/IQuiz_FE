package com.example.iq5.feature.multiplayer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iq5.R;
import com.google.android.material.button.MaterialButton;

public class RoomLobbyActivity extends AppCompatActivity {

    private Toolbar toolbarLobby;
    private TextView tvCountdownTimer;
    private TextView tvCurrency;

    private MaterialButton btnReady, btnLeave, btnSpectate, btnSendChat;
    private MaterialButton btnMode1v1;

    private RecyclerView rvChat;
    private EditText etChatMessage;

    private String roomCode;
    private boolean isHost;
    private boolean isPlayerJoined = false;
    private boolean isReady = false;

    // TIMER COUNTDOWN
    private static final int COUNTDOWN_START_TIME = 5;
    private int currentCountdownTime = COUNTDOWN_START_TIME;

    private Handler countdownHandler;
    private final Runnable countdownRunnable = new Runnable() {
        @Override
        public void run() {
            if (currentCountdownTime > 0) {
                currentCountdownTime--;
                tvCountdownTimer.setText(String.format("00:%02d", currentCountdownTime));
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

        countdownHandler = new Handler(getMainLooper());

        // Lấy dữ liệu phòng
        roomCode = getIntent().getStringExtra("ROOM_CODE");
        isHost = getIntent().getBooleanExtra("IS_HOST", false);

        initViews();
        setupToolbar();
        setupOnBackPressedCallback();
        setupListeners();

        // Display initial UI state
        tvCountdownTimer.setText(String.format("00:%02d", COUNTDOWN_START_TIME));
        tvCurrency.setText("Phòng: " + roomCode);

        // Demo: nếu là host, giả lập đối thủ join sau 3s
        if (isHost) {
            new Handler(getMainLooper()).postDelayed(this::onPlayerJoined, 3000);
        }
    }

    // ---------------- INIT UI ----------------

    private void initViews() {
        toolbarLobby = findViewById(R.id.toolbarLobby);
        tvCountdownTimer = findViewById(R.id.tvCountdownTimer);
        tvCurrency = findViewById(R.id.tvCurrency);

        btnReady = findViewById(R.id.btnReady);
        btnLeave = findViewById(R.id.btnLeave);
        btnSpectate = findViewById(R.id.btnSpectate);
        btnMode1v1 = findViewById(R.id.btnMode1v1);
        btnSendChat = findViewById(R.id.btnSendChat);

        rvChat = findViewById(R.id.rvChat);
        etChatMessage = findViewById(R.id.etChatMessage);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbarLobby);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Phòng: " + roomCode);
        }

        // Icon back trên toolbar dùng dispatcher
        toolbarLobby.setNavigationOnClickListener(
                v -> getOnBackPressedDispatcher().onBackPressed()
        );
    }

    // ---------------- BACK HANDLER (gesture + nút) ----------------

    private void setupOnBackPressedCallback() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                showLeaveConfirmationDialog();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    // ---------------- LISTENERS ----------------

    private void setupListeners() {
        btnReady.setOnClickListener(v -> {
            isReady = !isReady;
            updateReadyState(isReady);
            // TODO: gửi trạng thái READY/UNREADY qua WebSocket
        });

        btnLeave.setOnClickListener(v -> showLeaveConfirmationDialog());

        btnSpectate.setOnClickListener(v -> {
            Toast.makeText(this, "Bạn đã chuyển sang chế độ khán giả!", Toast.LENGTH_SHORT).show();
            // TODO: gửi SPECTATE qua WebSocket
        });

        btnSendChat.setOnClickListener(v -> {
            String message = etChatMessage.getText().toString().trim();
            if (!message.isEmpty()) {
                // TODO: gửi message qua WebSocket
                Toast.makeText(this, "Đã gửi: " + message, Toast.LENGTH_SHORT).show();
                etChatMessage.setText("");
            }
        });

        btnMode1v1.setOnClickListener(v -> {
            Toast.makeText(this, "Chế độ 1 vs 1 đang hoạt động.", Toast.LENGTH_SHORT).show();
            // TODO: có thể dùng để chọn mode nếu sau này có nhiều chế độ
        });
    }

    // ---------------- READY & COUNTDOWN LOGIC ----------------

    private void updateReadyState(boolean ready) {
        isReady = ready;
        if (ready) {
            btnReady.setText("UNREADY");
            btnReady.setBackgroundTintList(
                    getResources().getColorStateList(R.color.colorAccent, getTheme())
            );

            if (isPlayerJoined) {
                startCountdown();
            } else {
                Toast.makeText(this, "Đang chờ đối thủ tham gia...", Toast.LENGTH_SHORT).show();
            }
        } else {
            btnReady.setText("READY");
            btnReady.setBackgroundTintList(
                    getResources().getColorStateList(R.color.colorWin, getTheme())
            );
            stopCountdown();
        }
    }

    private void startCountdown() {
        stopCountdown();
        currentCountdownTime = COUNTDOWN_START_TIME;
        tvCountdownTimer.setText(String.format("00:%02d", currentCountdownTime));

        countdownHandler.postDelayed(countdownRunnable, 1000);
        Toast.makeText(this, "Đếm ngược bắt đầu!", Toast.LENGTH_SHORT).show();
    }

    private void stopCountdown() {
        countdownHandler.removeCallbacks(countdownRunnable);
        currentCountdownTime = COUNTDOWN_START_TIME;
        tvCountdownTimer.setText(String.format("00:%02d", currentCountdownTime));
    }

    // ---------------- LEAVE ROOM ----------------

    private void showLeaveConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Rời Phòng")
                .setMessage("Bạn có chắc chắn muốn rời khỏi phòng chờ?")
                .setPositiveButton("RỜI", (dialog, which) -> {
                    // TODO: gửi LEAVE_ROOM qua WebSocket
                    stopCountdown();
                    finish();
                })
                .setNegativeButton("HỦY", null)
                .show();
    }

    // ---------------- SOCKET EVENT SIMULATION ----------------

    // Giả lập: được gọi khi WebSocket nhận "USER_JOINED"
    private void onPlayerJoined() {
        isPlayerJoined = true;
        Toast.makeText(this, "Đối thủ đã tham gia phòng!", Toast.LENGTH_SHORT).show();
        if (isReady) {
            startCountdown();
        }
    }

    // Giả lập: được gọi khi server gửi "GAME_START" hoặc countdown về 0
    private void startGame() {
        stopCountdown();
        Toast.makeText(this, "Bắt đầu trận đấu!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(RoomLobbyActivity.this, PvPBattleActivity.class);
        intent.putExtra("ROOM_CODE", roomCode);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopCountdown();
    }
}
