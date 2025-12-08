package com.example.iq5.feature.quiz.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.iq5.R;   // <-- phải đúng namespace app
import com.example.iq5.feature.quiz.model.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    public interface OnSelect {
        void onClick(Category c);
    }

    private final List<Category> list;
    private final OnSelect listener;

    public CategoryAdapter(List<Category> list, OnSelect listener) {
        this.list = list;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup p, int v) {
        return new ViewHolder(LayoutInflater.from(p.getContext())
                .inflate(R.layout.item_category, p, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder h, int pos) {
        Category c = list.get(pos);
        h.txt.setText(c.getName());
        h.itemView.setOnClickListener(v -> listener.onClick(c));
    }

    @Override
    public int getItemCount() { return list.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt;
        ViewHolder(View v) {
            super(v);
            txt = v.findViewById(R.id.txtCategoryName);
        }
    }
}
