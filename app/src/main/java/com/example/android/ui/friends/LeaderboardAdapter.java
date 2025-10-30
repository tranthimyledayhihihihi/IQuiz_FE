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
public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.PlayerViewHolder> {

        private List<LeaderboardPlayer> playersList;
        private OnPlayerClickListener listener;

        public interface OnPlayerClickListener {
            void onPlayerClick(LeaderboardPlayer player);
        }

        public LeaderboardAdapter(OnPlayerClickListener listener) {
            this.playersList = new ArrayList<>();
            this.listener = listener;
        }

        @NonNull
        @Override
        public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_leaderboard_player, parent, false);
            return new PlayerViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PlayerViewHolder holder, int position) {
            LeaderboardPlayer player = playersList.get(position);
            holder.bind(player, listener);
        }

        @Override
        public int getItemCount() {
            return playersList.size();
        }

        public void updateData(List<LeaderboardPlayer> newPlayersList) {
            this.playersList.clear();
            this.playersList.addAll(newPlayersList);
            notifyDataSetChanged();
        }

        static class PlayerViewHolder extends RecyclerView.ViewHolder {
            private TextView tvRank;
            private TextView tvPlayerName;
            private TextView tvPoints;

            public PlayerViewHolder(@NonNull View itemView) {
                super(itemView);
                tvRank = itemView.findViewById(R.id.tvRank);
                tvPlayerName = itemView.findViewById(R.id.tvPlayerName);
                tvPoints = itemView.findViewById(R.id.tvPoints);
            }

            public void bind(LeaderboardPlayer player, OnPlayerClickListener listener) {
                tvRank.setText("#" + player.getRank());
                tvPlayerName.setText(player.getName());
                tvPoints.setText(player.getPoints() + " điểm");

                // Highlight top 3 players
                if (player.getRank() <= 3) {
                    tvRank.setTextColor(itemView.getContext().getResources().getColor(R.color.secondary_color));
                } else {
                    tvRank.setTextColor(itemView.getContext().getResources().getColor(R.color.text_secondary));
                }

                itemView.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onPlayerClick(player);
                    }
                });
            }
        }
    }

