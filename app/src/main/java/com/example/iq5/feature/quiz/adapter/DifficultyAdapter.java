package com.example.iq5.feature.quiz.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.iq5.R;
import com.example.iq5.feature.quiz.model.Difficulty;

import java.util.List;

public class DifficultyAdapter extends RecyclerView.Adapter<DifficultyAdapter.ViewHolder> {

    public interface OnClick {
        void onSelect(Difficulty d);
    }

    private List<Difficulty> list;
    private OnClick listener;

    public DifficultyAdapter(List<Difficulty> list, OnClick listener) {
        this.list = list;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_difficulty, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder h, int pos) {
        Difficulty d = list.get(pos);
        h.txt.setText(d.getLevel());
        h.itemView.setOnClickListener(v -> listener.onSelect(d));
    }

    @Override
    public int getItemCount() { return list.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt;
        ViewHolder(View v) {
            super(v);
            // Sửa lỗi ở đây: Sử dụng ID chính xác từ item_difficulty.xml
            txt = v.findViewById(R.id.txtDifficultyLevel);
        }
    }
}
