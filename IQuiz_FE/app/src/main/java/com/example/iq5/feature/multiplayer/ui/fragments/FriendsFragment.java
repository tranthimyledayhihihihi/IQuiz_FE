package com.example.iq5.feature.multiplayer.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iq5.R;
import com.example.iq5.feature.multiplayer.adapter.FriendsAdapter;
import com.example.iq5.feature.multiplayer.data.FriendRepository;
import com.example.iq5.feature.multiplayer.data.FriendRepositoryImpl;
import com.example.iq5.feature.multiplayer.model.Friend;
import java.util.List;

public class FriendsFragment extends Fragment implements FriendsAdapter.OnFriendInteractionListener {

    private RecyclerView rvFriendsList;
    private FriendsAdapter adapter;

    private FriendRepository friendRepository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        // Đảm bảo ID này khớp với fragment_friends.xml
        rvFriendsList = view.findViewById(R.id.rvFriendsList_fragment);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 1. Khởi tạo Repository (sử dụng dữ liệu mẫu)
        // Đã sửa lỗi Cannot resolve symbol 'FriendRepository'
        friendRepository = new FriendRepositoryImpl();

        // 2. Thiết lập RecyclerView và Adapter
        setupRecyclerView();

        // 3. Load và quan sát dữ liệu
        loadFriendsData();
    }

    private void setupRecyclerView() {
        adapter = new FriendsAdapter(this);
        rvFriendsList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvFriendsList.setAdapter(adapter);
    }

    private void loadFriendsData() {
        // ID người dùng hiện tại (Giả lập là 1)
        int currentUserId = 1;

        // Lắng nghe danh sách bạn bè
        friendRepository.getFriends(currentUserId).observe(getViewLifecycleOwner(), friendsList -> {
            if (friendsList != null) {
                // Gán dữ liệu bạn bè vào Adapter
                // Đã sửa lỗi submitList
                adapter.submitList(friendsList);
            }
        });

        // Lắng nghe danh sách yêu cầu kết bạn
        friendRepository.getFriendRequests(currentUserId).observe(getViewLifecycleOwner(), requestsList -> {
            // Đã sửa lỗi .isEmpty() và .size() bằng cách kiểm tra null
            if (requestsList != null && !requestsList.isEmpty()) {
                Toast.makeText(getContext(), "Có " + requestsList.size() + " yêu cầu kết bạn mới!", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onChallengeClick(Friend friend) {
        Toast.makeText(getContext(), "Đang gửi yêu cầu thách đấu đến " + friend.getTenNguoiBan(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAcceptClick(Friend friend) {
        // Thực hiện logic chấp nhận kết bạn qua Repository
        friendRepository.acceptFriendRequest(friend.getBanBeID());
        Toast.makeText(getContext(), "Đã chấp nhận kết bạn với " + friend.getTenNguoiBan(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeclineClick(Friend friend) {
        // Thực hiện logic từ chối kết bạn qua Repository
        friendRepository.removeFriend(friend.getBanBeID());
        Toast.makeText(getContext(), "Đã từ chối lời mời của " + friend.getTenNguoiBan(), Toast.LENGTH_SHORT).show();
    }
}