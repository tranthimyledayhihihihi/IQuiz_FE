package com.example.iq5.feature.multiplayer.adapter;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.example.iq5.R;
import com.example.iq5.feature.multiplayer.model.LeaderboardEntry;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Adapter cho RecyclerView trong LeaderboardActivity
 */
public class LeaderboardAdapter extends ListAdapter<LeaderboardEntry, LeaderboardAdapter.LeaderboardViewHolder> {

    private String leaderboardType = "week"; // "week" or "month"

    public LeaderboardAdapter() {
        super(DIFF_CALLBACK);
    }

    public void setLeaderboardType(String type) {
        this.leaderboardType = type;
    }

    // DiffUtil
    private static final DiffUtil.ItemCallback<LeaderboardEntry> DIFF_CALLBACK = new DiffUtil.ItemCallback<LeaderboardEntry>() {
        @Override
        public boolean areItemsTheSame(@NonNull LeaderboardEntry oldItem, @NonNull LeaderboardEntry newItem) {
            return oldItem.getUserID() == newItem.getUserID();
        }
        @Override
        public boolean areContentsTheSame(@NonNull LeaderboardEntry oldItem, @NonNull LeaderboardEntry newItem) {
            return oldItem.getDiemTuan() == newItem.getDiemTuan() &&
                    oldItem.getDiemThang() == newItem.getDiemThang();
        }
    };

    @NonNull
    @Override
    public LeaderboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_leaderboard, parent, false);
        return new LeaderboardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaderboardViewHolder holder, int position) {
        LeaderboardEntry entry = getItem(position);
        holder.bind(entry, leaderboardType);
    }

    // ViewHolder
    static class LeaderboardViewHolder extends RecyclerView.ViewHolder {
        TextView tvRank, tvPlayerName, tvPlayerScore, tvPlayerInfo;
        CircleImageView imgAvatar;

        public LeaderboardViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRank = itemView.findViewById(R.id.tvRank);
            tvPlayerName = itemView.findViewById(R.id.tvPlayerName);
            tvPlayerScore = itemView.findViewById(R.id.tvPlayerScore);
            tvPlayerInfo = itemView.findViewById(R.id.tvPlayerInfo); // Bạn có thể thêm level...
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
        }

        public void bind(LeaderboardEntry entry, String type) {
            int rank;
            int score;

            if ("week".equals(type)) {
                rank = entry.getHangTuan();
                score = entry.getDiemTuan();
            } else {
                rank = entry.getHangThang();
                score = entry.getDiemThang();
            }

            tvRank.setText(String.valueOf(rank));
            tvPlayerName.setText(entry.getTenNguoiDung());
            tvPlayerScore.setText(String.valueOf(score));
            // TODO: Load ảnh bằng Glide/Picasso

            // Highlight Top 3
            if (rank == 1) {
                tvRank.setTextColor(Color.parseColor("#FFD700")); // Gold
                tvRank.setTypeface(null, Typeface.BOLD);
            } else if (rank == 2) {
                tvRank.setTextColor(Color.parseColor("#C0C0C0")); // Silver
                tvRank.setTypeface(null, Typeface.BOLD);
            } else if (rank == 3) {
                tvRank.setTextColor(Color.parseColor("#CD7F32")); // Bronze
                tvRank.setTypeface(null, Typeface.BOLD);
            } else {
                tvRank.setTextColor(itemView.getResources().getColor(R.color.colorPrimary));
                tvRank.setTypeface(null, Typeface.NORMAL);
            }
        }
    }
}