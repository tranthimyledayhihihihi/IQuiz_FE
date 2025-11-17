package com.example.iq5.feature.specialmode.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iq5.R;
import com.example.iq5.feature.specialmode.model.DailyQuizItem;

import java.util.ArrayList;
import java.util.List;

public class DailyQuizAdapter extends RecyclerView.Adapter<DailyQuizAdapter.DailyViewHolder> {

    public interface OnDailyQuizClickListener {
        void onPlayClicked(DailyQuizItem item);
    }

    private final List<DailyQuizItem> data = new ArrayList<>();
    private final OnDailyQuizClickListener listener;

    public DailyQuizAdapter(OnDailyQuizClickListener listener) {
        this.listener = listener;
    }

    public void submitList(List<DailyQuizItem> items) {
        data.clear();
        if (items != null) data.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DailyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_daily_quiz, parent, false);
        return new DailyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DailyViewHolder holder, int position) {
        DailyQuizItem item = data.get(position);
        holder.tvTitle.setText(item.title);
        holder.tvDesc.setText(item.description);
        holder.tvMeta.setText(item.questionCount + " câu • " + item.estimatedTime + " phút");
        holder.tvReward.setText("+" + item.rewardCoins + " 💰 • +" + item.rewardExp + " XP");

        if (item.isNew) {
            holder.tvBadge.setVisibility(View.VISIBLE);
            holder.tvBadge.setText("MỚI");
        } else if (item.isCompleted) {
            holder.tvBadge.setVisibility(View.VISIBLE);
            holder.tvBadge.setText("ĐÃ XONG");
        } else {
            holder.tvBadge.setVisibility(View.GONE);
        }

        holder.btnPlay.setOnClickListener(v -> {
            if (listener != null) listener.onPlayClicked(item);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class DailyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDesc, tvMeta, tvReward, tvBadge;
        Button btnPlay;

        DailyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_quiz_title);
            tvDesc = itemView.findViewById(R.id.tv_quiz_desc);
            tvMeta = itemView.findViewById(R.id.tv_quiz_meta);
            tvReward = itemView.findViewById(R.id.tv_quiz_reward);
            tvBadge = itemView.findViewById(R.id.tv_badge);
            btnPlay = itemView.findViewById(R.id.btn_play_now);
        }
    }
}
