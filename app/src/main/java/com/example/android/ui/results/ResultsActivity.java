package com.example.android.ui.results;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.android.R;
import com.example.android.ui.lobby.LobbyActivity;
public class ResultsActivity extends AppCompatActivity {

        private TextView tvTenPhong;
        private TextView tvNguoiThang;
        private TextView tvDiem1;
        private TextView tvDiem2;
        private TextView tvKetQua;
        private Button btnChoiLai;
        private Button btnVeSảnh;
        private Button btnChiaSe;

        private String maPhong;
        private String tenPhong;
        private String nguoiThang;
        private int diem1;
        private int diem2;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_results);

            getIntentData();
            initViews();
            setupClickListeners();
            displayResults();
        }

        private void getIntentData() {
            Intent intent = getIntent();
            maPhong = intent.getStringExtra("ma_phong");
            tenPhong = intent.getStringExtra("ten_phong");
            nguoiThang = intent.getStringExtra("nguoi_thang");
            diem1 = intent.getIntExtra("diem_1", 0);
            diem2 = intent.getIntExtra("diem_2", 0);
        }

        private void initViews() {
            tvTenPhong = findViewById(R.id.tvTenPhong);
            tvNguoiThang = findViewById(R.id.tvNguoiThang);
            tvDiem1 = findViewById(R.id.tvDiem1);
            tvDiem2 = findViewById(R.id.tvDiem2);
            tvKetQua = findViewById(R.id.tvKetQua);
            btnChoiLai = findViewById(R.id.btnChoiLai);
            btnVeSảnh = findViewById(R.id.btnVeSanh);
            btnChiaSe = findViewById(R.id.btnChiaSe);

            tvTenPhong.setText(tenPhong);
        }

        private void setupClickListeners() {
            btnChoiLai.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    choiLai();
                }
            });

            btnVeSảnh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    veSảnh();
                }
            });

            btnChiaSe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chiaSeKetQua();
                }
            });
        }

        private void displayResults() {
            tvDiem1.setText("Điểm: " + diem1);
            tvDiem2.setText("Điểm: " + diem2);

            if ("Hòa".equals(nguoiThang)) {
                tvNguoiThang.setText("Kết quả: Hòa");
                tvKetQua.setText("Cả hai người chơi đều có điểm số bằng nhau!");
            } else {
                tvNguoiThang.setText("Người thắng: " + nguoiThang);
                tvKetQua.setText("Chúc mừng " + nguoiThang + " đã chiến thắng!");
            }
        }

        private void choiLai() {
            // TODO: Gọi API tạo phòng mới hoặc reset trận đấu
            Toast.makeText(this, "Tạo phòng mới để chơi lại", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, LobbyActivity.class);
            intent.putExtra("ma_phong", maPhong);
            intent.putExtra("ten_phong", tenPhong);
            intent.putExtra("is_chu_phong", true);
            startActivity(intent);
            finish();
        }

        private void veSảnh() {
            // TODO: Quay về màn hình chính hoặc sảnh chờ
            finish();
        }

        private void chiaSeKetQua() {
            // TODO: Chia sẻ kết quả lên mạng xã hội
            String shareText = "Tôi vừa chơi " + tenPhong + " và kết quả là " +
                    (diem1 > diem2 ? "thắng" : diem2 > diem1 ? "thua" : "hòa") +
                    " với tỷ số " + diem1 + " - " + diem2;

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            startActivity(Intent.createChooser(shareIntent, "Chia sẻ kết quả"));
        }
    }

