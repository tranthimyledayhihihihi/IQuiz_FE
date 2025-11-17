package com.example.iq5.feature.multiplayer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;

import com.example.iq5.R;
import com.google.android.material.textfield.TextInputEditText;

public class FindMatchActivity extends AppCompatActivity {

    private Button btnFindRandomMatch, btnCreateRoom, btnJoinRoom;
    private ProgressBar progressBarFinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_match);

        btnFindRandomMatch = findViewById(R.id.btnFindRandomMatch);
        btnCreateRoom = findViewById(R.id.btnCreateRoom);
        btnJoinRoom = findViewById(R.id.btnJoinRoom);
        progressBarFinding = findViewById(R.id.progressBarFinding);

        // 1. Tìm trận nhanh
        btnFindRandomMatch.setOnClickListener(v -> {
            startFindingMatch();
            // TODO: Gọi API/WebSocket để bắt đầu ghép người
            // Giả lập tìm thấy trận sau 3s
            new android.os.Handler().postDelayed(() -> {
                stopFindingMatch();
                Toast.makeText(this, "Đã tìm thấy đối thủ!", Toast.LENGTH_SHORT).show();
                // Chuyển sang màn hình thi đấu
                Intent intent = new Intent(FindMatchActivity.this, PvPBattleActivity.class);
                // intent.putExtra("MATCH_ID", ...);
                startActivity(intent);
            }, 3000);
        });

        // 2. Tạo phòng
        btnCreateRoom.setOnClickListener(v -> {
            // TODO: Gọi API để tạo phòng
            // Sau khi API trả về mã phòng, chuyển sang sảnh chờ
            String newRoomCode = "ABCD1"; // Mã giả lập
            Intent intent = new Intent(FindMatchActivity.this, RoomLobbyActivity.class);
            intent.putExtra("ROOM_CODE", newRoomCode);
            intent.putExtra("IS_HOST", true);
            startActivity(intent);
        });

        // 3. Vào phòng
        btnJoinRoom.setOnClickListener(v -> {
            showJoinRoomDialog();
        });
    }

    private void startFindingMatch() {
        btnFindRandomMatch.setText("Đang tìm trận...");
        btnFindRandomMatch.setEnabled(false);
        progressBarFinding.setVisibility(View.VISIBLE);
    }

    private void stopFindingMatch() {
        btnFindRandomMatch.setText("Tìm Trận Nhanh");
        btnFindRandomMatch.setEnabled(true);
        progressBarFinding.setVisibility(View.GONE);
    }

    private void showJoinRoomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Vào Phòng");

        View view = getLayoutInflater().inflate(R.layout.dialog_join_room, null);
        final TextInputEditText inputRoomCode = view.findViewById(R.id.inputRoomCode);
        builder.setView(view);

        builder.setPositiveButton("Vào", (dialog, which) -> {
            String roomCode = inputRoomCode.getText().toString().trim();
            if (roomCode.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập mã phòng", Toast.LENGTH_SHORT).show();
            } else {
                // TODO: Gọi API/WebSocket để kiểm tra mã phòng
                // Nếu mã hợp lệ:
                Intent intent = new Intent(FindMatchActivity.this, RoomLobbyActivity.class);
                intent.putExtra("ROOM_CODE", roomCode);
                intent.putExtra("IS_HOST", false);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    // Bạn cần tạo file dialog_join_room.xml trong res/layout
}
