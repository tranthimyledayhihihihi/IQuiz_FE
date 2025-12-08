package com.example.iq5.feature.multiplayer.ui;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iq5.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FriendsActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private RecyclerView rvFriendsList;
    private FloatingActionButton fabAddFriend;

    // TODO: private FriendViewModel viewModel;
    // TODO: private FriendAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        // TODO: viewModel = new ViewModelProvider(this).get(FriendViewModel.class);

        initViews();
        setupToolbar();
        setupBackHandler();
        setupRecyclerView();
        setupFab();

        // Thông báo tạm thời
        Toast.makeText(this, "Danh sách bạn bè - Đang phát triển", Toast.LENGTH_SHORT).show();

        // TODO: observeViewModel();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        rvFriendsList = findViewById(R.id.rvFriendsList);
        fabAddFriend = findViewById(R.id.fabAddFriend);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Nút back trên toolbar → dùng dispatcher
        toolbar.setNavigationOnClickListener(v ->
                getOnBackPressedDispatcher().onBackPressed()
        );
    }

    private void setupBackHandler() {
        getOnBackPressedDispatcher().addCallback(this,
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        // Luồng back của màn Friends khá đơn giản: thoát luôn
                        setEnabled(false);
                        FriendsActivity.super.onBackPressed();
                    }
                });
    }

    private void setupRecyclerView() {
        rvFriendsList.setLayoutManager(new LinearLayoutManager(this));

        // Khi có FriendAdapter thì mở mấy dòng này:
        // adapter = new FriendAdapter();
        // rvFriendsList.setAdapter(adapter);
    }

    private void setupFab() {
        fabAddFriend.setOnClickListener(v -> {
            // Sau này có thể mở PlayerSearchFragment / Activity tìm kiếm bạn bè
            // hoặc show dialog nhập username / mã bạn bè.
            Toast.makeText(this,
                    "Tính năng tìm & thêm bạn bè sẽ được bổ sung.",
                    Toast.LENGTH_SHORT).show();
        });
    }

    // Khi có ViewModel:
    // private void observeViewModel() {
    //     viewModel.getFriendsList().observe(this, friends -> {
    //         adapter.submitList(friends);
    //     });
    //     viewModel.getFriendRequests().observe(this, requests -> {
    //         // TODO: cập nhật UI cho lời mời kết bạn
    //     });
    // }
}
