package com.example.iq5.feature.multiplayer.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
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

        toolbar = findViewById(R.id.toolbar);
        rvFriendsList = findViewById(R.id.rvFriendsList);
        fabAddFriend = findViewById(R.id.fabAddFriend);

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // TODO: setupRecyclerView();
        // TODO: observeViewModel();

        fabAddFriend.setOnClickListener(v -> {
            // TODO: Hiển thị dialog/activity tìm kiếm bạn bè
        });
    }

    // private void setupRecyclerView() {
    //     adapter = new FriendAdapter();
    //     rvFriendsList.setAdapter(adapter);
    // }

    // private void observeViewModel() {
    //     viewModel.getFriendsList().observe(this, friends -> {
    //         adapter.submitList(friends);
    //     });
    //     viewModel.getFriendRequests().observe(this, requests -> {
    //         // TODO: Cập nhật UI cho requests
    //     });
    // }
}