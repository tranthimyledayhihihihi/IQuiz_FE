package com.example.iq5.feature.result.adapter;

import android.content.Context; // Cần import Context
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat; // Dùng ContextCompat để lấy màu
import androidx.recyclerview.widget.RecyclerView;
import com.example.iq5.R;
import com.example.iq5.feature.result.model.UserStats;
import java.util.List;

public class StatsAdapter extends RecyclerView.Adapter<StatsAdapter.StatsViewHolder> {

    private final List<UserStats> statsList;
    private final Context context; // Thêm Context

    // Constructor MỚI cần nhận Context
    public StatsAdapter(List<UserStats> statsList, Context context) {
        this.statsList = statsList;
        this.context = context;
    }

    @NonNull
    @Override
    public StatsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stat_milestone, parent, false);
        return new StatsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StatsViewHolder holder, int position) {
        UserStats stat = statsList.get(position);

        holder.tvTitle.setText(stat.getStatName());
        holder.tvDesc.setText(stat.getDescription());
        holder.tvValue.setText(stat.getValue());

        // Sử dụng Context đã lưu và ContextCompat để lấy màu
        int color = ContextCompat.getColor(context, R.color.color_primary);
        holder.tvValue.setTextColor(color);
    }

    @Override
    public int getItemCount() {
        return statsList.size();
    }

    static class StatsViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDesc, tvValue;
        // ImageView imgIcon;

        public StatsViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_milestone_title);
            tvDesc = itemView.findViewById(R.id.tv_milestone_desc);
            tvValue = itemView.findViewById(R.id.tv_milestone_value);
        }
    }
}