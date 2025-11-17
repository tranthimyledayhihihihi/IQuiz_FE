package com.example.iq5.feature.multiplayer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.example.iq5.R;
import com.example.iq5.feature.multiplayer.model.Friend;
import com.google.android.material.button.MaterialButton;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Adapter cho RecyclerView trong FriendsActivity
 */
// SỬA LỖI: Đổi tên class thành FriendsAdapter để khớp tên file
public class FriendsAdapter extends ListAdapter<Friend, FriendsAdapter.FriendViewHolder> {

    // (Tùy chọn) Interface để xử lý click
    public interface OnFriendInteractionListener {
        void onChallengeClick(Friend friend);
        void onAcceptClick(Friend friend);
        void onDeclineClick(Friend friend);
    }
    private OnFriendInteractionListener listener;

    public FriendsAdapter(OnFriendInteractionListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    // DiffUtil để tối ưu RecyclerView
    private static final DiffUtil.ItemCallback<Friend> DIFF_CALLBACK = new DiffUtil.ItemCallback<Friend>() {
        @Override
        public boolean areItemsTheSame(@NonNull Friend oldItem, @NonNull Friend newItem) {
            return oldItem.getBanBeID() == newItem.getBanBeID();
        }
        @Override
        public boolean areContentsTheSame(@NonNull Friend oldItem, @NonNull Friend newItem) {
            return oldItem.getTrangThai().equals(newItem.getTrangThai()) &&
                    oldItem.isOnline() == newItem.isOnline();
        }
    };

    @NonNull
    @Override
    public FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_friend, parent, false);
        return new FriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendViewHolder holder, int position) {
        Friend friend = getItem(position);
        holder.bind(friend, listener);
    }

    static class FriendViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imgFriendAvatar;
        TextView tvFriendName, tvFriendStatus;
        MaterialButton btnChallenge;
        LinearLayout layoutPending;
        MaterialButton btnAccept, btnDecline;

        public FriendViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFriendAvatar = itemView.findViewById(R.id.imgFriendAvatar);
            tvFriendName = itemView.findViewById(R.id.tvFriendName);
            tvFriendStatus = itemView.findViewById(R.id.tvFriendStatus);
            btnChallenge = itemView.findViewById(R.id.btnChallenge);
            layoutPending = itemView.findViewById(R.id.layoutPending);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnDecline = itemView.findViewById(R.id.btnDecline);
        }

        public void bind(Friend friend, OnFriendInteractionListener listener) {
            tvFriendName.setText(friend.getTenNguoiBan());
            // TODO: Load ảnh bằng Glide/Picasso
            // Glide.with(itemView.getContext()).load(friend.getAnhDaiDienNguoiBan()).into(imgFriendAvatar);

            if ("Bạn bè".equals(friend.getTrangThai())) {
                layoutPending.setVisibility(View.GONE);
                btnChallenge.setVisibility(View.VISIBLE);

                if (friend.isOnline()) {
                    tvFriendStatus.setText("Online");
                    tvFriendStatus.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.colorWin));
                    btnChallenge.setEnabled(true);
                } else {
                    tvFriendStatus.setText("Offline");
                    tvFriendStatus.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.colorTextSecondary));
                    btnChallenge.setEnabled(false);
                }

                btnChallenge.setOnClickListener(v -> listener.onChallengeClick(friend));

            } else if ("Chờ xác nhận".equals(friend.getTrangThai())) {
                layoutPending.setVisibility(View.VISIBLE);
                btnChallenge.setVisibility(View.GONE);
                tvFriendStatus.setText("Đang chờ bạn xác nhận...");
                tvFriendStatus.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.colorAccent));

                btnAccept.setOnClickListener(v -> listener.onAcceptClick(friend));
                btnDecline.setOnClickListener(v -> listener.onDeclineClick(friend));
            }
        }
    }
}