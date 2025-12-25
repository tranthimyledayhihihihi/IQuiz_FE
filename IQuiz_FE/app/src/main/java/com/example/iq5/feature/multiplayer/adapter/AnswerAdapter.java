package com.example.iq5.feature.multiplayer.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.iq5.R;
import java.util.List;

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.ViewHolder> {

    private List<String> answers;
    private List<String> letters; // A, B, C, D
    private OnAnswerClickListener listener;
    private int selectedPosition = -1;
    private boolean isEnabled = true;

    public interface OnAnswerClickListener {
        void onAnswerClick(String answer, String letter, int position);
    }

    public AnswerAdapter(List<String> answers, List<String> letters, OnAnswerClickListener listener) {
        this.answers = answers;
        this.letters = letters;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_answer, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(answers.get(position), letters.get(position), position);
    }

    @Override
    public int getItemCount() {
        return answers != null ? answers.size() : 0;
    }

    public void setSelectedPosition(int position) {
        this.selectedPosition = position;
        notifyDataSetChanged();
    }

    public void setEnabled(boolean enabled) {
        this.isEnabled = enabled;
        notifyDataSetChanged();
    }

    public void highlightCorrect(int position) {
        // TODO: Implement highlight logic
        notifyItemChanged(position);
    }

    public void highlightWrong(int position) {
        // TODO: Implement highlight logic
        notifyItemChanged(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private TextView tvAnswer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardAnswer);
            tvAnswer = itemView.findViewById(R.id.tvAnswer);
        }

        public void bind(String answer, String letter, int position) {
            tvAnswer.setText(letter + ". " + answer);

            // Selected state
            if (position == selectedPosition) {
                cardView.setCardBackgroundColor(Color.parseColor("#3498db"));
                tvAnswer.setTextColor(Color.WHITE);
            } else {
                cardView.setCardBackgroundColor(Color.parseColor("#ecf0f1"));
                tvAnswer.setTextColor(Color.BLACK);
            }

            // Click listener
            itemView.setOnClickListener(v -> {
                if (isEnabled && listener != null) {
                    selectedPosition = position;
                    listener.onAnswerClick(answer, letter, position);
                    notifyDataSetChanged();
                }
            });

            itemView.setEnabled(isEnabled);
            itemView.setAlpha(isEnabled ? 1.0f : 0.6f);
        }
    }
}