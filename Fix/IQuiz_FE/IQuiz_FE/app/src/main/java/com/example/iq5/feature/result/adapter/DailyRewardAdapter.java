package com.example.iq5.feature.result.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iq5.R;
import com.example.iq5.feature.result.model.DailyReward;

import java.util.List;

public class DailyRewardAdapter extends RecyclerView.Adapter<DailyRewardAdapter.RewardViewHolder> {

    private final List<DailyReward> rewards;
    private final Context context;

    public DailyRewardAdapter(List<DailyReward> rewards, Context context) {
        this.rewards = rewards;
        this.context = context;
    }

    @NonNull
    @Override
    public RewardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_daily_reward, parent, false);
        return new RewardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RewardViewHolder holder, int position) {
        DailyReward reward = rewards.get(position);

        // Hiển thị số ngày
        holder.tvDayNumber.setText("Ngày " + reward.getDayNumber());

        // Xử lý màu nền CardView và màu chữ
        int bgColor;
        int textColor = ContextCompat.getColor(context, R.color.color_text_secondary);

        if (reward.isToday()) {
            // Ngày hôm nay
            bgColor = ContextCompat.getColor(context, R.color.color_reward_day_today);
            textColor = ContextCompat.getColor(context, R.color.color_white);
        } else if (reward.isClaimed()) {
            // Ngày đã nhận
            bgColor = ContextCompat.getColor(context, R.color.color_reward_day_claimed);
        } else {
            // Ngày chưa nhận
            bgColor = ContextCompat.getColor(context, R.color.color_reward_day_unclaimed);
        }

        holder.cardRewardBox.setCardBackgroundColor(bgColor);
        holder.tvDayNumber.setTextColor(textColor);

        // Icon reward (có thể đổi theo kiểu phần thưởng)
        holder.imgRewardIcon.setImageResource(R.drawable.ic_gift_box); // hoặc đổi dựa trên JSON nếu có
    }

    @Override
    public int getItemCount() {
        return rewards.size();
    }

    static class RewardViewHolder extends RecyclerView.ViewHolder {
        CardView cardRewardBox;
        ImageView imgRewardIcon;
        TextView tvDayNumber;

        public RewardViewHolder(@NonNull View itemView) {
            super(itemView);
            cardRewardBox = itemView.findViewById(R.id.card_reward_box);
            imgRewardIcon = itemView.findViewById(R.id.img_reward_icon);
            tvDayNumber = itemView.findViewById(R.id.tv_day_number);
        }
    }
}
