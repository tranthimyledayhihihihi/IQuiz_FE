package com.example.iq5.feature.result.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.iq5.R;
import com.example.iq5.feature.result.model.Achievement;
import java.util.List;

public class AchievementAdapter extends RecyclerView.Adapter<AchievementAdapter.AchievementViewHolder> {

    private final List<Achievement> achievements;
    private final Context context;

    public AchievementAdapter(List<Achievement> achievements, Context context) {
        this.achievements = achievements;
        this.context = context;
    }

    @NonNull
    @Override
    public AchievementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_achievement, parent, false);
        return new AchievementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AchievementViewHolder holder, int position) {
        Achievement achievement = achievements.get(position);

        // 1. Cập nhật Tiêu đề và Mô tả
        holder.tvTitle.setText(achievement.getTitle());
        holder.tvDesc.setText(achievement.getDescription());

        // 2. Đặt Icon - Chuyển đổi từ String sang Resource ID
        int iconResId = getIconResourceId(achievement.getIconResId());
        holder.imgIcon.setImageResource(iconResId);

        // 3. Logic Màu sắc và Tiến trình
        if (achievement.isUnlocked()) {
            // ✅ Đã mở khóa: Icon Vàng
            holder.imgIcon.setColorFilter(
                    ContextCompat.getColor(context, R.color.color_gold)
            );
            holder.progressBar.setVisibility(View.GONE);
            holder.tvTitle.setTextColor(
                    ContextCompat.getColor(context, R.color.color_gold)
            );
            holder.tvDesc.setTextColor(
                    ContextCompat.getColor(context, R.color.color_text_tertiary)
            );
        } else {
            // 🔒 Chưa mở khóa: Icon Xám + Progress Bar
            holder.imgIcon.setColorFilter(
                    ContextCompat.getColor(context, R.color.color_text_tertiary)
            );
            holder.progressBar.setVisibility(View.VISIBLE);
            holder.tvTitle.setTextColor(
                    ContextCompat.getColor(context, R.color.color_text_tertiary)
            );
            holder.tvDesc.setTextColor(
                    ContextCompat.getColor(context, R.color.color_text_secondary)
            );

            // Thiết lập Progress Bar
            holder.progressBar.setMax(achievement.getTargetProgress());
            holder.progressBar.setProgress(achievement.getCurrentProgress());
        }
    }

    @Override
    public int getItemCount() {
        return achievements != null ? achievements.size() : 0;
    }

    /**
     * Chuyển đổi tên icon string sang Resource ID
     */
    private int getIconResourceId(String iconName) {
        int resId = context.getResources().getIdentifier(
                iconName,
                "drawable",
                context.getPackageName()
        );
        // Nếu không tìm thấy, trả về icon mặc định
        return resId != 0 ? resId : R.drawable.ic_trophy;
    }

    static class AchievementViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDesc;
        ImageView imgIcon;
        ProgressBar progressBar;

        public AchievementViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_achievement_name);
            tvDesc = itemView.findViewById(R.id.tv_achievement_desc);
            imgIcon = itemView.findViewById(R.id.img_achievement_icon);
            progressBar = itemView.findViewById(R.id.pb_achievement_progress);
        }
    }
}