package com.example.android.ui.battle;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.android.R;
import com.example.android.ui.results.ResultsActivity;
public class BattleActivity extends AppCompatActivity{
    private TextView tvTenPhong;
    private TextView tvNguoiChoi1;
    private TextView tvNguoiChoi2;
    private TextView tvDiem1;
    private TextView tvDiem2;
    private TextView tvThoiGian;
    private Button btnDauHang;
    private Button btnMove1;
    private Button btnMove2;

    private String maPhong;
    private String tenPhong;
    private int diemNguoiChoi1 = 0;
    private int diemNguoiChoi2 = 0;
    private CountDownTimer countDownTimer;
    private long thoiGianConLai = 300000; // 5 phút

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle);

        getIntentData();
        initViews();
        setupClickListeners();
        startTimer();
    }

    private void getIntentData() {
        Intent intent = getIntent();
        maPhong = intent.getStringExtra("ma_phong");
        tenPhong = intent.getStringExtra("ten_phong");
    }

    private void initViews() {
        tvTenPhong = findViewById(R.id.tvTenPhong);
        tvNguoiChoi1 = findViewById(R.id.tvNguoiChoi1);
        tvNguoiChoi2 = findViewById(R.id.tvNguoiChoi2);
        tvDiem1 = findViewById(R.id.tvDiem1);
        tvDiem2 = findViewById(R.id.tvDiem2);
        tvThoiGian = findViewById(R.id.tvThoiGian);
        btnDauHang = findViewById(R.id.btnDauHang);
        btnMove1 = findViewById(R.id.btnMove1);
        btnMove2 = findViewById(R.id.btnMove2);

        tvTenPhong.setText(tenPhong);
        tvNguoiChoi1.setText("Người chơi 1");
        tvNguoiChoi2.setText("Người chơi 2");
        updateScore();
    }

    private void setupClickListeners() {
        btnDauHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSurrenderDialog();
            }
        });

        btnMove1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeMove(1);
            }
        });

        btnMove2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeMove(2);
            }
        });
    }

    private void makeMove(int nguoiChoi) {
        // TODO: Gọi API thực hiện nước đi
        if (nguoiChoi == 1) {
            diemNguoiChoi1++;
        } else {
            diemNguoiChoi2++;
        }
        updateScore();

        // Kiểm tra điều kiện thắng
        if (diemNguoiChoi1 >= 10 || diemNguoiChoi2 >= 10) {
            endGame(nguoiChoi);
        }
    }

    private void updateScore() {
        tvDiem1.setText("Điểm: " + diemNguoiChoi1);
        tvDiem2.setText("Điểm: " + diemNguoiChoi2);
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(thoiGianConLai, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                thoiGianConLai = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {
                // Hết thời gian, người có điểm cao hơn thắng
                if (diemNguoiChoi1 > diemNguoiChoi2) {
                    endGame(1);
                } else if (diemNguoiChoi2 > diemNguoiChoi1) {
                    endGame(2);
                } else {
                    endGame(0); // Hòa
                }
            }
        };
        countDownTimer.start();
    }

    private void updateTimer() {
        long minutes = thoiGianConLai / 60000;
        long seconds = (thoiGianConLai % 60000) / 1000;
        tvThoiGian.setText(String.format("Thời gian: %02d:%02d", minutes, seconds));
    }

    private void showSurrenderDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận")
                .setMessage("Bạn có chắc chắn muốn đầu hàng?")
                .setPositiveButton("Có", (dialog, which) -> {
                    // TODO: Gọi API đầu hàng
                    endGame(2); // Người chơi 2 thắng
                })
                .setNegativeButton("Không", null)
                .show();
    }

    private void endGame(int nguoiThang) {
        countDownTimer.cancel();

        String nguoiThangText;
        if (nguoiThang == 1) {
            nguoiThangText = "Người chơi 1";
        } else if (nguoiThang == 2) {
            nguoiThangText = "Người chơi 2";
        } else {
            nguoiThangText = "Hòa";
        }

        Intent intent = new Intent(this, ResultsActivity.class);
        intent.putExtra("ma_phong", maPhong);
        intent.putExtra("ten_phong", tenPhong);
        intent.putExtra("nguoi_thang", nguoiThangText);
        intent.putExtra("diem_1", diemNguoiChoi1);
        intent.putExtra("diem_2", diemNguoiChoi2);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
    public void onBackClick(View view) {
        // Ví dụ: Quay lại màn hình trước
        finish();
    }
}
