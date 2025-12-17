package com.example.iq5.feature.specialmode.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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
        holder.tvWrongCount.setText(topic.wrongCount + " lần sai");
        holder.tvMastery.setText("Mức độ nắm vững: " + topic.masteryLevel);

        holder.layoutTags.removeAllViews();
        if (topic.tags != null) {
            for (String tag : topic.tags) {
                TextView tagView = (TextView) LayoutInflater.from(holder.itemView.getContext())
                        .inflate(R.layout.simple_tag_view, holder.layoutTags, false);
                tagView.setText(tag);
                holder.layoutTags.addView(tagView);
            }
        }

        holder.btnReview.setOnClickListener(v -> {
            if (listener != null) listener.onReviewTopic(topic);
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class TopicViewHolder extends RecyclerView.ViewHolder {
        TextView tvTopicName, tvWrongCount, tvMastery;
        LinearLayout layoutTags;
        Button btnReview;

        TopicViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTopicName = itemView.findViewById(R.id.tv_topic_name);
            tvWrongCount = itemView.findViewById(R.id.tv_wrong_count);
            tvMastery = itemView.findViewById(R.id.tv_mastery);
            layoutTags = itemView.findViewById(R.id.layout_tags);
            btnReview = itemView.findViewById(R.id.btn_review);
        }
    }
}
