package com.example.iq5.feature.multiplayer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iq5.R;

public class RoomLobbyActivity extends AppCompatActivity {

    private TextView tvRoomCode, tvPlayer1Name, tvPlayer2Name, tvWaitingMessage;
    private Button btnStartGame, btnCancel, btnShareCode;
    private LinearLayout player2Container;
    private ProgressBar progressPlayer2;

    private String roomCode;
    private boolean isHost;
    private boolean isPlayer2Joined = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_lobby);

        // Lấy thông tin từ Intent
        roomCode = getIntent().getStringExtra("ROOM_CODE");
        isHost = getIntent().getBooleanExtra("IS_HOST", false);

        // Ánh xạ UI
        tvRoomCode = findViewById(R.id.tvRoomCode);
        tvPlayer1Name = findViewById(R.id.tvPlayer1Name);
        tvPlayer2Name = findViewById(R.id.tvPlayer2Name);
        tvWaitingMessage = findViewById(R.id.tvWaitingMessage);
        btnStartGame = findViewById(R.id.btnStartGame);
        btnCancel = findViewById(R.id.btnCancel);
        btnShareCode = findViewById(R.id.btnShareCode);
        player2Container = findViewById(R.id.player2Container);
        progressPlayer2 = findViewById(R.id.progressPlayer2);

        tvRoomCode.setText(roomCode);

        // TODO: Kết nối WebSocket tới phòng với roomCode

        if (isHost) {
            tvPlayer1Name.setText("Bạn (Chủ phòng)");
            btnStartGame.setVisibility(View.VISIBLE);
            btnCancel.setText("Hủy phòng");
            // Chủ phòng không thể bắt đầu khi chưa có người 2
            btnStartGame.setEnabled(false);
        } else {
            tvPlayer1Name.setText("Chủ phòng"); // Tên chủ phòng
            tvPlayer2Name.setText("Bạn");
            // Người vào sau sẽ thấy tên mình ở vị trí 2
            // TODO: Lấy tên chủ phòng qua WebSocket/API
            btnStartGame.setVisibility(View.GONE); // Chỉ chủ phòng thấy nút Bắt Đầu
            btnCancel.setText("Rời phòng");
            // Giả lập chủ phòng đã ở đây
            player2Container.setBackgroundResource(R.drawable.bg_player_lobby);
            progressPlayer2.setVisibility(View.GONE);
        }

        // TODO: Lắng nghe sự kiện WebSocket
        // 1. "USER_JOINED": Khi có người chơi 2 vào
        // 2. "USER_LEFT": Khi người chơi 2 rời đi
        // 3. "GAME_START": Khi chủ phòng bấm bắt đầu

        // Giả lập người chơi 2 tham gia sau 3s
        if (isHost) {
            new android.os.Handler().postDelayed(this::onPlayer2Joined, 3000);
        }

        btnStartGame.setOnClickListener(v -> {
            if (isHost && isPlayer2Joined) {
                // TODO: Gửi sự kiện "START_GAME" qua WebSocket
                startGame();
            } else {
                Toast.makeText(this, "Cần 2 người để bắt đầu", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancel.setOnClickListener(v -> {
            // TODO: Gửi sự kiện "LEAVE_ROOM" qua WebSocket
            finish(); // Đóng Activity
        });

        btnShareCode.setOnClickListener(v -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Vào chơi IQuiz với tôi! Mã phòng: " + roomCode);
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, "Chia sẻ mã phòng"));
        });
    }

    // Hàm này được gọi khi có sự kiện WebSocket "USER_JOINED"
    private void onPlayer2Joined() {
        isPlayer2Joined = true;
        tvPlayer2Name.setText("Player 2"); // Lấy tên từ WebSocket
        tvPlayer2Name.setTypeface(null, android.graphics.Typeface.BOLD);
        tvWaitingMessage.setText("Đối thủ đã sẵn sàng!");
        player2Container.setBackgroundResource(R.drawable.bg_player_lobby); // Đổi background
        progressPlayer2.setVisibility(View.GONE);

        if (isHost) {
            btnStartGame.setEnabled(true); // Cho phép chủ phòng bắt đầu
        }
    }

    // Hàm này được gọi khi có sự kiện "USER_LEFT"
    private void onPlayer2Left() {
        isPlayer2Joined = false;
        tvPlayer2Name.setText("Đang chờ...");
        tvPlayer2Name.setTypeface(null, android.graphics.Typeface.ITALIC);
        tvWaitingMessage.setText("Đang chờ người chơi khác tham gia...");
        player2Container.setBackgroundResource(R.drawable.bg_player_lobby_waiting);
        progressPlayer2.setVisibility(View.VISIBLE);

        if (isHost) {
            btnStartGame.setEnabled(false);
        }
    }

    // Hàm này được gọi khi có sự kiện "GAME_START"
    private void startGame() {
        Intent intent = new Intent(RoomLobbyActivity.this, PvPBattleActivity.class);
        intent.putExtra("ROOM_CODE", roomCode);
        // intent.putExtra("MATCH_ID", ...);
        startActivity(intent);
        finish();
    }
}