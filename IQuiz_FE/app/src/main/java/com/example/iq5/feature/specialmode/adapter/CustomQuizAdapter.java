package com.example.iq5.feature.specialmode.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iq5.R;
import com.example.iq5.feature.specialmode.model.CustomQuizItem;

import java.util.ArrayList;
import java.util.List;

public class CustomQuizAdapter extends RecyclerView.Adapter<CustomQuizAdapter.CustomViewHolder> {

    public interface OnCustomQuizListener {
        void onShare(CustomQuizItem item);
        void onEdit(CustomQuizItem item);
        void onStart(CustomQuizItem item);
        void onDelete(CustomQuizItem item);
    }

    private final List<CustomQuizItem> data = new ArrayList<>();
    private final OnCustomQuizListener listener;

    public CustomQuizAdapter(OnCustomQuizListener listener) {
        this.listener = listener;
    }

    public void submitList(List<CustomQuizItem> items) {
        data.clear();
        if (items != null) data.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_custom_quiz, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        CustomQuizItem item = data.get(position);

        holder.tvTitle.setText(item.tenQuiz);
        holder.tvDesc.setText(item.moTa);
        holder.tvMeta.setText(item.soLuongCauHoi + " câu • " + item.trangThai);

        holder.btnStart.setText("Sửa câu hỏi");
        holder.btnStart.setOnClickListener(v -> listener.onEdit(item));

        holder.btnShare.setText("Xóa");
        holder.btnShare.setOnClickListener(v -> listener.onDelete(item));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDesc, tvMeta;
        Button btnShare, btnStart;

        CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_custom_title);
            tvDesc = itemView.findViewById(R.id.tv_custom_desc);
            tvMeta = itemView.findViewById(R.id.tv_custom_meta);
            btnShare = itemView.findViewById(R.id.btn_share);
            btnStart = itemView.findViewById(R.id.btn_start_custom);
        }
    }
}
