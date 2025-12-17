package com.example.iq5.feature.specialmode.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iq5.R;
import com.example.iq5.feature.specialmode.model.CustomQuestion;

import java.util.ArrayList;
import java.util.List;

public class CustomQuestionAdapter extends RecyclerView.Adapter<CustomQuestionAdapter.QuestionViewHolder> {

    public interface Listener {
        void onEdit(int position, CustomQuestion question);
        void onDelete(int position, CustomQuestion question);
    }

    private final List<CustomQuestion> items = new ArrayList<>();
    private final Listener listener;

    public CustomQuestionAdapter(Listener listener) {
        this.listener = listener;
    }

    public void submitList(List<CustomQuestion> list) {
        items.clear();
        if (list != null) items.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_custom_question, parent, false);
        return new QuestionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        CustomQuestion q = items.get(position);

        String contentShort = q.content;
        if (contentShort != null && contentShort.length() > 80) {
            contentShort = contentShort.substring(0, 80) + "...";
        }

        holder.tvContent.setText(contentShort != null ? contentShort : "(Chưa có nội dung)");

        String optionsText = "A) " + safe(q.optionA) +
                "   B) " + safe(q.optionB) +
                "\nC) " + safe(q.optionC) +
                "   D) " + safe(q.optionD);
        holder.tvOptions.setText(optionsText);

        holder.tvCorrect.setText("Đáp án đúng: " + safe(q.correctOption));

        holder.btnEdit.setOnClickListener(v -> {
            if (listener != null) listener.onEdit(holder.getAdapterPosition(), q);
        });

        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(holder.getAdapterPosition(), q);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class QuestionViewHolder extends RecyclerView.ViewHolder {
        TextView tvContent;
        TextView tvOptions;
        TextView tvCorrect;
        Button btnEdit;
        Button btnDelete;

        QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvContent = itemView.findViewById(R.id.tv_question_content);
            tvOptions = itemView.findViewById(R.id.tv_question_options);
            tvCorrect = itemView.findViewById(R.id.tv_correct_option);
            btnEdit = itemView.findViewById(R.id.btn_edit_question);
            btnDelete = itemView.findViewById(R.id.btn_delete_question);
        }
    }

    private String safe(String s) {
        return s == null ? "" : s;
    }
}
