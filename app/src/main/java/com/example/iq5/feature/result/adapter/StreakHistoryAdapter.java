package com.example.iq5.feature.result.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.iq5.R;
import com.example.iq5.feature.result.model.StreakDay;
import java.util.List;

public class StreakHistoryAdapter extends RecyclerView.Adapter<StreakHistoryAdapter.StreakViewHolder> {

    private final List<StreakDay> historyList;

    public StreakHistoryAdapter(List<StreakDay> historyList) {
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public StreakViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Ánh xạ đến item_streak_day.xml
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_streak_day, parent, false);
        return new StreakViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StreakViewHolder holder, int position) {
        StreakDay day = historyList.get(position);
        Context context = holder.itemView.getContext();

        // Giả định bạn đã thêm string resource streak_day_format="Ngày %d"
        holder.tvDayLabel.setText(context.getString(R.string.streak_day_format, day.getDayNumber()));
        holder.tvDate.setText(day.getDate());

        if (day.isCompleted()) {
            // Đã hoàn thành: Dấu tích Xanh lá và điểm thưởng
            holder.imgStatusIcon.setImageResource(R.drawable.ic_check_circle);
            holder.imgStatusIcon.setColorFilter(ContextCompat.getColor(context, R.color.color_success));
            holder.tvReward.setText("+" + day.getRewardPoints() + " ĐIỂM");
            holder.tvReward.setTextColor(ContextCompat.getColor(context, R.color.color_cyan_accent));
        } else {
            // Ngày bị lỡ: Dấu X Đỏ hoặc Xám
            holder.imgStatusIcon.setImageResource(R.drawable.ic_close_circle);
            holder.imgStatusIcon.setColorFilter(ContextCompat.getColor(context, R.color.color_error)); // Dùng màu Đỏ
            holder.tvReward.setText("Bỏ lỡ");
            holder.tvReward.setTextColor(ContextCompat.getColor(context, R.color.color_error));
        }
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    static class StreakViewHolder extends RecyclerView.ViewHolder {
        TextView tvDayLabel, tvDate, tvReward;
        ImageView imgStatusIcon;

        public StreakViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ánh xạ ID từ item_streak_day.xml
            tvDayLabel = itemView.findViewById(R.id.tv_streak_day_label);
            tvDate = itemView.findViewById(R.id.tv_streak_date);
            tvReward = itemView.findViewById(R.id.tv_streak_reward);
            imgStatusIcon = itemView.findViewById(R.id.img_streak_status_icon);
        }
    }
}