package com.example.android.ui.findmatch;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.R;
import com.example.android.ui.lobby.LobbyActivity;
public class FindMatchActivity extends AppCompatActivity implements PhongChoiAdapter.OnPhongChoiClickListener{
    private RecyclerView recyclerViewPhongChoi;
    private PhongChoiAdapter phongChoiAdapter;
    private Button btnTaoPhong;
    private Button btnTimTrận;
    private EditText edtTenPhong;
    private EditText edtLoaiGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_match);

        initViews();
        setupRecyclerView();
        setupClickListeners();
        loadDanhSachPhong();
    }

    private void initViews() {
        recyclerViewPhongChoi = findViewById(R.id.recyclerViewPhongChoi);
        btnTaoPhong = findViewById(R.id.btnTaoPhong);
        btnTimTrận = findViewById(R.id.btnTimTrận);
        edtTenPhong = findViewById(R.id.edtTenPhong);
        edtLoaiGame = findViewById(R.id.edtLoaiGame);
    }

    private void setupRecyclerView() {
        phongChoiAdapter = new PhongChoiAdapter(this);
        recyclerViewPhongChoi.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewPhongChoi.setAdapter(phongChoiAdapter);
    }

    private void setupClickListeners() {
        btnTaoPhong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taoPhongChoi();
            }
        });

        btnTimTrận.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timTrậnNhanh();
            }
        });
    }

    private void taoPhongChoi() {
        String tenPhong = edtTenPhong.getText().toString().trim();
        String loaiGame = edtLoaiGame.getText().toString().trim();

        if (tenPhong.isEmpty()) {
            edtTenPhong.setError("Vui lòng nhập tên phòng");
            return;
        }

        if (loaiGame.isEmpty()) {
            edtLoaiGame.setError("Vui lòng nhập loại game");
            return;
        }

        // TODO: Gọi API tạo phòng
        // Giả lập tạo phòng thành công
        Toast.makeText(this, "Tạo phòng thành công!", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, LobbyActivity.class);
        intent.putExtra("ma_phong", "ABC12345");
        intent.putExtra("ten_phong", tenPhong);
        intent.putExtra("is_chu_phong", true);
        startActivity(intent);
    }

    private void timTrậnNhanh() {
        // TODO: Gọi API tìm trận nhanh
        Toast.makeText(this, "Đang tìm trận...", Toast.LENGTH_SHORT).show();
    }

    private void loadDanhSachPhong() {
        // TODO: Gọi API lấy danh sách phòng
        // Giả lập dữ liệu
        phongChoiAdapter.updateData(getSamplePhongChoiData());
    }

    private java.util.List<PhongChoi> getSamplePhongChoiData() {
        java.util.List<PhongChoi> phongChoiList = new java.util.ArrayList<>();

        phongChoiList.add(new PhongChoi("Phòng Game 1", "ABC12345", "Cờ vua", 2, 1, "CHO"));
        phongChoiList.add(new PhongChoi("Phòng Game 2", "DEF67890", "Cờ tướng", 2, 1, "CHO"));
        phongChoiList.add(new PhongChoi("Phòng Game 3", "GHI11111", "Cờ caro", 2, 2, "CHO"));

        return phongChoiList;
    }

    @Override
    public void onPhongChoiClick(PhongChoi phongChoi) {
        // TODO: Gọi API tham gia phòng
        Intent intent = new Intent(this, LobbyActivity.class);
        intent.putExtra("ma_phong", phongChoi.getMaPhong());
        intent.putExtra("ten_phong", phongChoi.getTenPhong());
        intent.putExtra("is_chu_phong", false);
        startActivity(intent);
    }
}
