package com.example.iq5.feature.quiz.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iq5.R;
import com.example.iq5.feature.quiz.model.Question;

import java.util.List;

public class ReviewQuestionAdapter extends RecyclerView.Adapter<ReviewQuestionAdapter.ViewHolder> {

    // Sử dụng 'final' vì list không thay đổi sau khi khởi tạo
    private final List<Question> list;

    public ReviewQuestionAdapter(List<Question> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup p, int v) {
        // Khắc phục lỗi: Cannot resolve symbol 'item_review_question'
        return new ViewHolder(LayoutInflater.from(p.getContext())
                .inflate(R.layout.item_review_question, p, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder h, int pos) {
        Question q = list.get(pos);

        // Sử dụng tên biến rõ ràng hơn: h.txtQuestion và h.txtAnswer
        h.txtQuestion.setText(q.getContent());
        h.txtAnswer.setText("Đáp án đúng: " + q.getCorrect());
    }

    @Override
    public int getItemCount() {
        // Sử dụng toán tử điều kiện để xử lý trường hợp list là null, mặc dù 'final' làm giảm khả năng này
        return list == null ? 0 : list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        // Tên biến rõ ràng hơn thay vì q, a
        TextView txtQuestion, txtAnswer;

        ViewHolder(View v) {
            super(v);
            // Khắc phục lỗi ID: R.id.txtReviewQuestion
            txtQuestion = v.findViewById(R.id.txtReviewQuestion);
            // Khắc phục lỗi ID: R.id.txtReviewAnswer
            txtAnswer = v.findViewById(R.id.txtReviewAnswer);
        }
    }
}