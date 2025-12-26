package com.example.iq5.feature.specialmode.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iq5.R;
import com.example.iq5.feature.specialmode.model.WrongTopic;

import java.util.ArrayList;
import java.util.List;

public class WrongTopicAdapter extends RecyclerView.Adapter<WrongTopicAdapter.TopicViewHolder> {

    public interface OnTopicClickListener {
        void onReviewTopic(WrongTopic topic);
    }

    private final List<WrongTopic> data = new ArrayList<>();
    private final OnTopicClickListener listener;

    public WrongTopicAdapter(OnTopicClickListener listener) {
        this.listener = listener;
    }

    public void submitList(List<WrongTopic> topics) {
        data.clear();
        if (topics != null) data.addAll(topics);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TopicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_wrong_topic, parent, false);
        return new TopicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopicViewHolder holder, int position) {
        WrongTopic topic = data.get(position);

        holder.tvTopicName.setText(topic.topicName);
        holder.tvWrongCount.setText(topic.wrongCount + " câu sai");

        holder.btnReview.setOnClickListener(v -> {
            if (listener != null) {
                listener.onReviewTopic(topic);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class TopicViewHolder extends RecyclerView.ViewHolder {

        TextView tvTopicName, tvWrongCount;
        Button btnReview;

        TopicViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTopicName = itemView.findViewById(R.id.tv_topic_name);
            tvWrongCount = itemView.findViewById(R.id.tv_wrong_count);
            btnReview = itemView.findViewById(R.id.btn_review);
        }
    }
}
