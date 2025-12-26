package com.example.iq5.feature.specialmode.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iq5.R;
import com.example.iq5.data.model.QuizSubmissionModel;

import java.util.List;

public class CustomQuestionAdapter
        extends RecyclerView.Adapter<CustomQuestionAdapter.QuestionVH> {

    private final List<QuizSubmissionModel.CauHoiSubmission> items;

    public CustomQuestionAdapter(List<QuizSubmissionModel.CauHoiSubmission> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public QuestionVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_custom_question, parent, false);
        return new QuestionVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionVH h, int pos) {
        QuizSubmissionModel.CauHoiSubmission q = items.get(pos);

        h.tvContent.setText(q.getNoiDung());
        h.tvOptions.setText(
                "A) " + q.getDapAnA() +
                        "  B) " + q.getDapAnB() +
                        "\nC) " + q.getDapAnC() +
                        "  D) " + q.getDapAnD()
        );
        h.tvCorrect.setText("Đáp án đúng: " + q.getDapAnDung());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class QuestionVH extends RecyclerView.ViewHolder {
        TextView tvContent, tvOptions, tvCorrect;

        QuestionVH(@NonNull View v) {
            super(v);
            tvContent = v.findViewById(R.id.tv_question_content);
            tvOptions = v.findViewById(R.id.tv_question_options);
            tvCorrect = v.findViewById(R.id.tv_correct_option);
        }
    }
}
