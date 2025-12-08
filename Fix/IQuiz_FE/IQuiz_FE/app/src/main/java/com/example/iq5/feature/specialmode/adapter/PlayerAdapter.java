package com.example.iq5.feature.specialmode.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iq5.R;
import com.example.iq5.feature.specialmode.model.PlayerItem;

import java.util.ArrayList;
import java.util.List;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.PlayerViewHolder> {

    public interface OnPlayerClickListener {
        void onFriendAction(PlayerItem item);

        void onChallenge(PlayerItem item);
    }

    private final List<PlayerItem> data = new ArrayList<>();
    private final OnPlayerClickListener listener;

    public PlayerAdapter(OnPlayerClickListener listener) {
        this.listener = listener;
    }

    public void submitList(List<PlayerItem> items) {
        data.clear();
        if (items != null) data.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_player, parent, false);
        return new PlayerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerViewHolder holder, int position) {
        PlayerItem item = data.get(position);
        holder.tvDisplayName.setText(item.displayName);
        holder.tvUsername.setText("@" + item.username);
        holder.tvRankPoints.setText("Top " + item.rank + " • " + item.totalPoints + " điểm");

        holder.viewOnline.setVisibility(item.isOnline ? View.VISIBLE : View.GONE);

        String text;
        switch (item.friendStatus) {
            case "requested":
                text = "Đã gửi";
                holder.btnFriendAction.setEnabled(false);
                break;
            case "friends":
                text = "Bạn bè";
                holder.btnFriendAction.setEnabled(false);
                break;
            default:
                text = "Kết bạn";
                holder.btnFriendAction.setEnabled(true);
                break;
        }
        holder.btnFriendAction.setText(text);

        holder.btnFriendAction.setOnClickListener(v -> {
            if (listener != null) listener.onFriendAction(item);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class PlayerViewHolder extends RecyclerView.ViewHolder {
        TextView tvDisplayName, tvUsername, tvRankPoints;
        View viewOnline;
        Button btnFriendAction;

        PlayerViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDisplayName = itemView.findViewById(R.id.tv_display_name);
            tvUsername = itemView.findViewById(R.id.tv_username);
            tvRankPoints = itemView.findViewById(R.id.tv_rank_points);
            viewOnline = itemView.findViewById(R.id.view_online);
            btnFriendAction = itemView.findViewById(R.id.btn_friend_action);
        }
    }
}
