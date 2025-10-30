package com.example.android.ui.friends;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.R;

import java.util.ArrayList;
import java.util.List;

    public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.FriendViewHolder> {

        private List<Friend> friendsList;
        private OnFriendClickListener listener;

        public interface OnFriendClickListener {
            void onFriendClick(Friend friend);
        }

        public FriendsAdapter(OnFriendClickListener listener) {
            this.friendsList = new ArrayList<>();
            this.listener = listener;
        }

        @NonNull
        @Override
        public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_friend, parent, false);
            return new FriendViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
            Friend friend = friendsList.get(position);
            holder.bind(friend, listener);
        }

        @Override
        public int getItemCount() {
            return friendsList.size();
        }

        public void updateData(List<Friend> newFriendsList) {
            this.friendsList.clear();
            this.friendsList.addAll(newFriendsList);
            notifyDataSetChanged();
        }

        static class FriendViewHolder extends RecyclerView.ViewHolder {
            private TextView tvFriendName;
            private TextView tvFriendStatus;
            private TextView tvFriendPoints;

            public FriendViewHolder(@NonNull View itemView) {
                super(itemView);
                tvFriendName = itemView.findViewById(R.id.tvFriendName);
                tvFriendStatus = itemView.findViewById(R.id.tvFriendStatus);
                tvFriendPoints = itemView.findViewById(R.id.tvFriendPoints);
            }

            public void bind(Friend friend, OnFriendClickListener listener) {
                tvFriendName.setText(friend.getName());
                tvFriendPoints.setText(friend.getPoints() + " điểm");

                String statusText = "Trực tuyến";
                if ("ONLINE".equals(friend.getStatus())) {
                    statusText = "Trực tuyến";
                } else if ("OFFLINE".equals(friend.getStatus())) {
                    statusText = "Ngoại tuyến";
                }
                tvFriendStatus.setText(statusText);

                itemView.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onFriendClick(friend);
                    }
                });
            }
        }
    }

