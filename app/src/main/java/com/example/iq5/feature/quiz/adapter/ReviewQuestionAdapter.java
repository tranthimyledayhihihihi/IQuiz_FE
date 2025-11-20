package com.example.iq5.feature.quiz.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iq5.R;
import com.example.iq5.feature.quiz.model.Option;
import com.example.iq5.feature.quiz.model.Question;

import java.util.List;

public class ReviewQuestionAdapter extends RecyclerView.Adapter<ReviewQuestionAdapter.ViewHolder> {

    private final List<Question> list;

    public ReviewQuestionAdapter(List<Question> list) {
        this.list = list;
    }

    private String findOptionTextById(List<Option> options, String id) {
        if (id == null) return "Chưa trả lời";
        for (Option opt : options) {
            if (id.equals(opt.getOption_id())) {
                return opt.getOption_text();
            }
        }
        return "Không tìm thấy";
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup p, int v) {
        return new ViewHolder(LayoutInflater.from(p.getContext())
                .inflate(R.layout.item_review_question, p, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder h, int pos) {
        Question q = list.get(pos);
        String userSelectedId = q.getUser_selected_answer_id();
        String correctId = q.getCorrect_answer_id();
        boolean isCorrect = correctId != null && correctId.equals(userSelectedId);

        String correctText = findOptionTextById(q.getOptions(), correctId);
        String userText = findOptionTextById(q.getOptions(), userSelectedId);

        h.txtQuestion.setText((pos + 1) + ". " + q.getQuestion_text());
        h.txtStatusBadge.setText(isCorrect ? "ĐÚNG" : "SAI");
        h.txtStatusBadge.setBackgroundColor(ContextCompat.getColor(h.itemView.getContext(),
                isCorrect ? R.color.status_success : R.color.status_error));
        h.txtStatusBadge.setTextColor(ContextCompat.getColor(h.itemView.getContext(), R.color.white));

        h.txtCorrectAnswer.setText("Đáp án đúng: " + correctId + ". " + correctText);

        if (userSelectedId != null && !isCorrect) {
            h.txtUserAnswer.setText("Bạn đã chọn: " + userSelectedId + ". " + userText);
            h.txtUserAnswer.setVisibility(View.VISIBLE);
        } else if (userSelectedId == null) {
            h.txtUserAnswer.setText("Bạn chưa trả lời câu này.");
            h.txtUserAnswer.setVisibility(View.VISIBLE);
        } else {
            h.txtUserAnswer.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtQuestion, txtStatusBadge, txtCorrectAnswer, txtUserAnswer;

        ViewHolder(View v) {
            super(v);
            txtQuestion = v.findViewById(R.id.txtReviewQuestion);
            txtStatusBadge = v.findViewById(R.id.txtStatusBadge);
            txtCorrectAnswer = v.findViewById(R.id.txtCorrectAnswer);
            txtUserAnswer = v.findViewById(R.id.txtUserAnswer);
        }
    }
}
