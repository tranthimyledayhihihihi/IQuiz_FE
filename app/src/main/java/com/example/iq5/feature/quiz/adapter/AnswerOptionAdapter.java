package com.example.iq5.feature.quiz.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iq5.R;   // <-- phải đúng namespace app
import java.util.List;

public class AnswerOptionAdapter extends RecyclerView.Adapter<AnswerOptionAdapter.ViewHolder> {

    public interface OnOptionClick {
        void onSelect(String option);
    }

    private List<String> options;
    private OnOptionClick listener;

    public AnswerOptionAdapter(List<String> options, OnOptionClick listener) {
        this.options = options;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_answer_option, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder h, int pos) {
        h.txt.setText(options.get(pos));
        h.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onSelect(options.get(pos));
        });
    }

    @Override
    public int getItemCount() { return options == null ? 0 : options.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt;
        ViewHolder(View v) {
            super(v);
            txt = v.findViewById(R.id.txtOption);
        }
    }
}
