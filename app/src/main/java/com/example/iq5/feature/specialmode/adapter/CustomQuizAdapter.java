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
        void onStart(CustomQuizItem item);
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
        holder.tvTitle.setText(item.title);
        holder.tvDesc.setText(item.description);

        String visibility = item.visibility != null ? item.visibility : (item.isPublic ? "public" : "private");
        String visText = "public".equals(visibility) ? "Công khai" : "Private";
        holder.tvMeta.setText(item.questionCount + " câu • " + visText);

        holder.btnShare.setOnClickListener(v -> {
            if (listener != null) listener.onShare(item);
        });

        holder.btnStart.setOnClickListener(v -> {
            if (listener != null) listener.onStart(item);
        });
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
