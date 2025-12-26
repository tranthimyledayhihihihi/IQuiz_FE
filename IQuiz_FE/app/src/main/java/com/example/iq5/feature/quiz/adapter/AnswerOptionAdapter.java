package com.example.iq5.feature.quiz.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.iq5.R;
import com.example.iq5.feature.quiz.model.Option;

import java.util.List;

public class AnswerOptionAdapter extends RecyclerView.Adapter<AnswerOptionAdapter.ViewHolder> {

    public interface OnOptionClick {
        void onSelect(Option selectedOption);
    }

    private final List<Option> options;
    private final OnOptionClick listener;

    // Highlight option đã chọn
    private String selectedOptionId = null;

    // KHÓA chọn 1 lần/câu (chống bấm lại)
    private boolean selectionLocked = false;

    public AnswerOptionAdapter(List<Option> options, OnOptionClick listener) {
        this.options = options;
        this.listener = listener;
    }

    /** Gọi khi sang câu mới */
    public void resetSelection() {
        selectedOptionId = null;
        selectionLocked = false;
        notifyDataSetChanged();
    }

    /** Activity có thể khóa ngay sau khi nhận click */
    public void lockSelection() {
        selectionLocked = true;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_answer_option, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder h, int pos) {
        Option opt = options.get(pos);

        h.txt.setText(opt.getOption_id() + ". " + opt.getOption_text());

        if (opt.getOption_id() != null && opt.getOption_id().equals(selectedOptionId)) {
            h.itemView.setBackgroundResource(R.drawable.bg_answer_selected);
        } else {
            h.itemView.setBackgroundResource(R.drawable.bg_answer_default);
        }

        h.itemView.setOnClickListener(v -> {
            if (selectionLocked) return;
            if (listener == null) return;

            selectedOptionId = opt.getOption_id();
            selectionLocked = true; // khóa ngay
            notifyDataSetChanged();

            listener.onSelect(opt);
        });
    }

    @Override
    public int getItemCount() {
        return options == null ? 0 : options.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt;
        ViewHolder(View v) {
            super(v);
            txt = v.findViewById(R.id.txtOption);
        }
    }
}
