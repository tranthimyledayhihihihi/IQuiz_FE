package com.example.iq5.feature.quiz.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.iq5.feature.quiz.data.FeaturedQuiz;
import com.example.iq5.R;

import java.util.ArrayList;
import java.util.List;

public class FeaturedQuizAdapter extends RecyclerView.Adapter<FeaturedQuizAdapter.ViewHolder> {

    private List<FeaturedQuiz> quizList;

    public FeaturedQuizAdapter(List<FeaturedQuiz> quizList) {
        this.quizList = quizList != null ? quizList : new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_featured_quiz, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FeaturedQuiz quiz = quizList.get(position);

        if (quiz != null) {
            holder.tvQuizTitle.setText(quiz.getTitle() != null ? quiz.getTitle() : "No Title");
            holder.tvQuizDescription.setText(quiz.getDescription() != null ? quiz.getDescription() : "No Description");
            holder.tvQuestionCount.setText(quiz.getQuestionCount() + " câu hỏi");
        }
    }

    @Override
    public int getItemCount() {
        return quizList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvQuizTitle;
        TextView tvQuizDescription;
        TextView tvQuestionCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuizTitle = itemView.findViewById(R.id.tvQuizTitle);
            tvQuizDescription = itemView.findViewById(R.id.tvQuizDescription);
            tvQuestionCount = itemView.findViewById(R.id.tvQuestionCount);
        }
    }
}