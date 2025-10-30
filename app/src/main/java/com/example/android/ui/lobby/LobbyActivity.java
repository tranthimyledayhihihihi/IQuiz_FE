package com.example.android.ui.lobby;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.R;
import com.example.android.ui.battle.BattleActivity;

public class LobbyActivity extends AppCompatActivity implements NguoiChoiAdapter.OnNguoiChoiClickListener {

    private TextView tvTenPhong;
    private TextView tvMaPhong;
    private TextView tvLoaiGame;
    private RecyclerView recyclerViewNguoiChoi;
    private NguoiChoiAdapter nguoiChoiAdapter;
    private Button btnBatDau;
    private Button btnRoiPhong;
    private Button btnInviteFriend;

    private String maPhong;
    private String tenPhong;
    private boolean isChuPhong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        getIntentData();
        initViews();
        setupRecyclerView();
        setupClickListeners();
        loadNguoiChoiTrongPhong();
    }

    private void getIntentData() {
        Intent intent = getIntent();
        maPhong = intent.getStringExtra("ma_phong");
        tenPhong = intent.getStringExtra("ten_phong");
        isChuPhong = intent.getBooleanExtra("is_chu_phong", false);
    }

    private void initViews() {
        tvTenPhong = findViewById(R.id.tvTenPhong);
        tvMaPhong = findViewById(R.id.tvMaPhong);
        tvLoaiGame = findViewById(R.id.tvLoaiGame);
        recyclerViewNguoiChoi = findViewById(R.id.recyclerViewNguoiChoi);
        btnBatDau = findViewById(R.id.btnBatDau);
        btnRoiPhong = findViewById(R.id.btnRoiPhong);
        btnInviteFriend = findViewById(R.id.btnInviteFriend);

        tvTenPhong.setText(tenPhong);
        tvMaPhong.setText("Mã phòng: " + maPhong);
        tvLoaiGame.setText("Loại game: Cờ vua"); // TODO: lấy từ intent hoặc API

        // Chỉ chủ phòng mới có thể bắt đầu trận
        btnBatDau.setVisibility(isChuPhong ? View.VISIBLE : View.GONE);
    }

    private void setupRecyclerView() {
        nguoiChoiAdapter = new NguoiChoiAdapter(this);
        recyclerViewNguoiChoi.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewNguoiChoi.setAdapter(nguoiChoiAdapter);
    }

    private void setupClickListeners() {
        btnBatDau.setOnClickListener(v -> batDauTran());
        btnRoiPhong.setOnClickListener(v -> roiPhong());
        btnInviteFriend.setOnClickListener(v -> inviteFriend());
    }

    private void batDauTran() {
        Toast.makeText(this, "Bắt đầu trận đấu!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, BattleActivity.class);
        intent.putExtra("ma_phong", maPhong);
        intent.putExtra("ten_phong", tenPhong);
        startActivity(intent);
    }

    private void roiPhong() {
        Toast.makeText(this, "Đã rời phòng", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void inviteFriend() {
        Toast.makeText(this, "Chức năng mời bạn bè", Toast.LENGTH_SHORT).show();
    }

    private void loadNguoiChoiTrongPhong() {
        nguoiChoiAdapter.updateData(getSampleNguoiChoiData());
    }

    private java.util.List<NguoiChoi> getSampleNguoiChoiData() {
        java.util.List<NguoiChoi> nguoiChoiList = new java.util.ArrayList<>();
        nguoiChoiList.add(new NguoiChoi("Người chơi 1", "ONLINE", true));
        nguoiChoiList.add(new NguoiChoi("Người chơi 2", "ONLINE", false));
        return nguoiChoiList;
    }

    @Override
    public void onNguoiChoiClick(NguoiChoi nguoiChoi) {
        Toast.makeText(this, "Click vào " + nguoiChoi.getTen(), Toast.LENGTH_SHORT).show();
    }

    // 🟢 Thêm 2 hàm xử lý onClick trong XML
    public void onBackClick(View view) {
        // Khi bấm nút quay lại trong layout
        finish();
    }

    public void onInviteFriendClick(View view) {
        // Khi bấm nút "Mời bạn bè" trong layout (nếu XML có android:onClick="onInviteFriendClick")
        inviteFriend();
    }
}
