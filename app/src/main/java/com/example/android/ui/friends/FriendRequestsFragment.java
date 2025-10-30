package com.example.android.ui.friends;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.R;

import java.util.ArrayList;
import java.util.List;
    public class FriendRequestsFragment extends Fragment {

        private RecyclerView recyclerViewRequests;
        private FriendRequestAdapter requestAdapter;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_friend_requests, container, false);

            initViews(view);
            setupRecyclerView();
            loadRequests();

            return view;
        }

        private void initViews(View view) {
            recyclerViewRequests = view.findViewById(R.id.recyclerViewRequests);
        }

        private void setupRecyclerView() {
            requestAdapter = new FriendRequestAdapter(new FriendRequestAdapter.OnRequestActionListener() {
                @Override
                public void onAccept(FriendRequest request) {
                    // TODO: Gọi API chấp nhận lời mời
                    Toast.makeText(getContext(), "Đã chấp nhận lời mời từ " + request.getName(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onReject(FriendRequest request) {
                    // TODO: Gọi API từ chối lời mời
                    Toast.makeText(getContext(), "Đã từ chối lời mời từ " + request.getName(), Toast.LENGTH_SHORT).show();
                }
            });
            recyclerViewRequests.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerViewRequests.setAdapter(requestAdapter);
        }

        private void loadRequests() {
            // TODO: Gọi API lấy danh sách lời mời kết bạn
            List<FriendRequest> requests = getSampleRequestsData();
            requestAdapter.updateData(requests);
        }

        private List<FriendRequest> getSampleRequestsData() {
            List<FriendRequest> requests = new ArrayList<>();
            requests.add(new FriendRequest("Người chơi A", "2 giờ trước"));
            requests.add(new FriendRequest("Người chơi B", "1 ngày trước"));
            return requests;
        }
    }

