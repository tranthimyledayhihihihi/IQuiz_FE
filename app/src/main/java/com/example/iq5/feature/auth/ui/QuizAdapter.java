package com.example.iq5.feature.auth.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iq5.R;
import com.example.iq5.feature.auth.model.HomeResponse;

import java.util.List;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.QuizViewHolder> {

    private List<HomeResponse.QuizItem> quizzes;

    public QuizAdapter(List<HomeResponse.QuizItem> quizzes) {
        this.quizzes = quizzes;
    }

    @NonNull
    @Override
    public QuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_quiz, parent, false);
        return new QuizViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizViewHolder holder, int position) {
        HomeResponse.QuizItem quiz = quizzes.get(position);
        holder.tvTitle.setText(quiz.title);
        holder.tvDifficulty.setText(quiz.difficulty);
    }

    @Override
    public int getItemCount() {
        return quizzes != null ? quizzes.size() : 0;
    }

    static class QuizViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDifficulty;

        public QuizViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvQuizTitle);
            tvDifficulty = itemView.findViewById(R.id.tvQuizDifficulty);
        }
    }
}
