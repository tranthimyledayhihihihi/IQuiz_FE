package com.example.iq5.feature.multiplayer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem; // Cần import MenuItem để xử lý nút Back
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;

import com.example.iq5.R;
import com.google.android.material.appbar.MaterialToolbar; // Import MaterialToolbar
import com.google.android.material.textfield.TextInputEditText;

public class FindMatchActivity extends AppCompatActivity {

    private MaterialToolbar toolbarFindMatch; // Khai báo Toolbar
    private Button btnFindRandomMatch, btnCreateRoom, btnJoinRoom;
    private ProgressBar progressBarFinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_match);

        toolbarFindMatch = findViewById(R.id.toolbarFindMatch); // Lấy tham chiếu

        // 1. THIẾT LẬP TOOLBAR LÀM ACTIONBAR
        setSupportActionBar(toolbarFindMatch);
        if (getSupportActionBar() != null) {
            // Kích hoạt nút điều hướng (sử dụng icon đã set trong XML)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

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

    /**
     * BƯỚC BẮT BUỘC: Xử lý sự kiện khi nhấn vào nút Quay lại (Icon điều hướng)
     */
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        // Tùy chọn: Nếu đang tìm trận, hủy quá trình tìm trận trước khi thoát
        if (progressBarFinding.getVisibility() == View.VISIBLE) {
            stopFindingMatch();
            Toast.makeText(this, "Đã hủy tìm trận.", Toast.LENGTH_SHORT).show();
            // Nếu bạn muốn chặn hoàn toàn khi đang tìm, bạn không gọi super.onBackPressed()
        } else {
            // Trở về Activity trước đó (MainActivity)
            super.onBackPressed();
        }
    }


    private void startFindingMatch() {
        btnFindRandomMatch.setText("Đang tìm trận...");
        btnFindRandomMatch.setEnabled(false);
        progressBarFinding.setVisibility(View.VISIBLE);
        // Tùy chọn: Vô hiệu hóa các nút khác khi đang tìm
        btnCreateRoom.setEnabled(false);
        btnJoinRoom.setEnabled(false);
    }

    private void stopFindingMatch() {
        btnFindRandomMatch.setText("Tìm Trận Nhanh");
        btnFindRandomMatch.setEnabled(true);
        progressBarFinding.setVisibility(View.GONE);
        // Tùy chọn: Kích hoạt lại các nút khác
        btnCreateRoom.setEnabled(true);
        btnJoinRoom.setEnabled(true);
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
}