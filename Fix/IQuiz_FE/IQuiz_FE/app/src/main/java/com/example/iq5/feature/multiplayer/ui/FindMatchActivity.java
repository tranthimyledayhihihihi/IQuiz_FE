package com.example.iq5.feature.multiplayer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.iq5.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;

public class FindMatchActivity extends AppCompatActivity {

    private MaterialToolbar toolbarFindMatch;
    private Button btnFindRandomMatch, btnCreateRoom, btnJoinRoom;
    private ProgressBar progressBarFinding;

    private Handler handler;
    private boolean isFinding = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_match);

        handler = new Handler(getMainLooper());

        initViews();
        setupToolbar();
        setupListeners();
        setupBackHandler();
    }

    // -------------------- INIT UI --------------------

    private void initViews() {
        toolbarFindMatch = findViewById(R.id.toolbarFindMatch);

        btnFindRandomMatch = findViewById(R.id.btnFindRandomMatch);
        btnCreateRoom = findViewById(R.id.btnCreateRoom);
        btnJoinRoom = findViewById(R.id.btnJoinRoom);
        progressBarFinding = findViewById(R.id.progressBarFinding);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbarFindMatch);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Nút back trên toolbar → dùng dispatcher thay vì onBackPressed
        toolbarFindMatch.setNavigationOnClickListener(v ->
                getOnBackPressedDispatcher().onBackPressed()
        );
    }

    private void setupListeners() {
        // 1. Tìm trận nhanh
        btnFindRandomMatch.setOnClickListener(v -> {
            startFindingMatch();

            // Giả lập tìm thấy trận sau 3s
            handler.postDelayed(() -> {
                if (!isFinding) return; // Đã hủy giữa chừng

                stopFindingMatch();
                Toast.makeText(this, "Đã tìm thấy đối thủ!", Toast.LENGTH_SHORT).show();

                // Chuyển sang màn PvPBattleActivity
                Intent intent = new Intent(FindMatchActivity.this, PvPBattleActivity.class);
                // TODO: putExtra MATCH_ID/ROOM_CODE nếu có
                startActivity(intent);
            }, 3000);
        });

        // 2. Tạo phòng
        btnCreateRoom.setOnClickListener(v -> {
            // TODO: Gọi API tạo phòng thật, hiện tại fake mã phòng
            String newRoomCode = "ABCD1";

            Intent intent = new Intent(FindMatchActivity.this, RoomLobbyActivity.class);
            intent.putExtra("ROOM_CODE", newRoomCode);
            intent.putExtra("IS_HOST", true);
            startActivity(intent);
        });

        // 3. Join phòng
        btnJoinRoom.setOnClickListener(v -> showJoinRoomDialog());
    }

    // -------------------- BACK HANDLER MỚI --------------------

    private void setupBackHandler() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (isFinding && progressBarFinding.getVisibility() == View.VISIBLE) {
                    // Đang tìm trận → hủy tìm, không thoát Activity
                    stopFindingMatch();
                    Toast.makeText(FindMatchActivity.this,
                            "Đã hủy tìm trận.", Toast.LENGTH_SHORT).show();
                } else {
                    // Không tìm trận → thoát như bình thường
                    setEnabled(false); // tránh loop
                    FindMatchActivity.super.onBackPressed();
                }
            }
        });
    }

    // -------------------- LOGIC TÌM TRẬN --------------------

    private void startFindingMatch() {
        isFinding = true;

        btnFindRandomMatch.setText("Đang tìm trận...");
        btnFindRandomMatch.setEnabled(false);
        progressBarFinding.setVisibility(View.VISIBLE);

        btnCreateRoom.setEnabled(false);
        btnJoinRoom.setEnabled(false);
    }

    private void stopFindingMatch() {
        isFinding = false;

        btnFindRandomMatch.setText("Tìm Trận Nhanh");
        btnFindRandomMatch.setEnabled(true);
        progressBarFinding.setVisibility(View.GONE);

        btnCreateRoom.setEnabled(true);
        btnJoinRoom.setEnabled(true);

        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }

    // -------------------- JOIN ROOM DIALOG --------------------

    private void showJoinRoomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Vào Phòng");

        View view = getLayoutInflater().inflate(R.layout.dialog_join_room, null);
        final TextInputEditText inputRoomCode = view.findViewById(R.id.inputRoomCode);
        builder.setView(view);

        builder.setPositiveButton("Vào", (dialog, which) -> {
            String roomCode = inputRoomCode.getText() != null
                    ? inputRoomCode.getText().toString().trim()
                    : "";

            if (roomCode.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập mã phòng", Toast.LENGTH_SHORT).show();
            } else {
                // TODO: verify mã phòng qua API / WebSocket
                Intent intent = new Intent(FindMatchActivity.this, RoomLobbyActivity.class);
                intent.putExtra("ROOM_CODE", roomCode);
                intent.putExtra("IS_HOST", false);
                startActivity(intent);
            }
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }
}
