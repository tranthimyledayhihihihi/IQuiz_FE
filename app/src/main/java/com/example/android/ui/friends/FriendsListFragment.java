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

    public class FriendsListFragment extends Fragment {

        private RecyclerView recyclerViewFriends;
        private FriendsAdapter friendsAdapter;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_friends_list, container, false);

            initViews(view);
            setupRecyclerView();
            loadFriends();

            return view;
        }

        private void initViews(View view) {
            recyclerViewFriends = view.findViewById(R.id.recyclerViewFriends);
        }

        private void setupRecyclerView() {
            friendsAdapter = new FriendsAdapter(new FriendsAdapter.OnFriendClickListener() {
                @Override
                public void onFriendClick(Friend friend) {
                    Toast.makeText(getContext(), "Click vào " + friend.getName(), Toast.LENGTH_SHORT).show();
                }
            });
            recyclerViewFriends.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerViewFriends.setAdapter(friendsAdapter);
        }

        private void loadFriends() {
            // TODO: Gọi API lấy danh sách bạn bè
            List<Friend> friends = getSampleFriendsData();
            friendsAdapter.updateData(friends);
        }

        private List<Friend> getSampleFriendsData() {
            List<Friend> friends = new ArrayList<>();
            friends.add(new Friend("Người bạn 1", "ONLINE", 1250));
            friends.add(new Friend("Người bạn 2", "OFFLINE", 980));
            friends.add(new Friend("Người bạn 3", "ONLINE", 2100));
            return friends;
        }
    }

