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

    public AchievementAdapter(List<Achievement> achievements) {
        this.achievements = achievements;
    }

    @NonNull
    @Override
    public AchievementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_achievement, parent, false);
        return new AchievementViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AchievementViewHolder holder, int position) {
        Achievement achievement = achievements.get(position);
        Context context = holder.itemView.getContext();

        // 1. Cập nhật Tiêu đề và Mô tả
        holder.tvTitle.setText(achievement.getTitle());
        holder.tvDesc.setText(achievement.getDescription());

        // 2. Đặt Icon (Sử dụng Getter đã sửa trong model)
        holder.imgIcon.setImageResource(achievement.getIconResId());

        // 3. Logic Màu sắc và Tiến trình
        if (achievement.isUnlocked()) {
            // Đã mở khóa: Icon Vàng (Gold)
            holder.imgIcon.setColorFilter(ContextCompat.getColor(context, R.color.color_gold));
            holder.progressBar.setVisibility(View.GONE);

            // Màu Text cho tiêu đề
            holder.tvTitle.setTextColor(ContextCompat.getColor(context, R.color.color_gold));
        } else {
            // Chưa mở khóa: Icon Xám và hiển thị Progress
            holder.imgIcon.setColorFilter(ContextCompat.getColor(context, R.color.color_text_tertiary));
            holder.progressBar.setVisibility(View.VISIBLE);

            // Màu Text cho tiêu đề
            holder.tvTitle.setTextColor(ContextCompat.getColor(context, R.color.color_text_tertiary));

            // TODO: Thiết lập Max và Progress cho thanh ProgressBar nếu cần
            // holder.progressBar.setMax(achievement.getTargetProgress());
            // holder.progressBar.setProgress(achievement.getCurrentProgress());
        }
    }

    @Override
    public int getItemCount() {
        return achievements.size();
    }

    static class AchievementViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDesc;
        ImageView imgIcon;
        ProgressBar progressBar;

        public AchievementViewHolder(@NonNull View itemView) {
            super(itemView);
            // KHẮC PHỤC LỖI ID: Ánh xạ Tiêu đề phải dùng ID tv_achievement_name
            tvTitle = itemView.findViewById(R.id.tv_achievement_name);

            // Ánh xạ Mô tả (Đã khớp với XML)
            tvDesc = itemView.findViewById(R.id.tv_achievement_desc);

            // Các thành phần khác
            imgIcon = itemView.findViewById(R.id.img_achievement_icon);
            progressBar = itemView.findViewById(R.id.pb_achievement_progress);
        }
    }
}