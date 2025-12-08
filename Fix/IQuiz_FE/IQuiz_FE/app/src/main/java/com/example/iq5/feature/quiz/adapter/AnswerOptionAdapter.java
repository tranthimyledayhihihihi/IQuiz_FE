package com.example.iq5.feature.quiz.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.iq5.R;
import com.example.iq5.feature.quiz.model.Option; // Cần import Model Option đã tạo

import java.util.List;

public class AnswerOptionAdapter extends RecyclerView.Adapter<AnswerOptionAdapter.ViewHolder> {

    // Interface callback: PHẢI TRẢ VỀ ID (String)
    public interface OnOptionClick {
        void onSelect(Option selectedOption);
    }

    private List<Option> options; // SỬA LỖI: Sử dụng List<Option>
    private OnOptionClick listener;
    // Thêm trường để lưu trữ ID của lựa chọn đã chọn (để highlight)
    private String selectedOptionId = null;

    // Constructor chính xác để nhận List<Option>
    public AnswerOptionAdapter(List<Option> options, OnOptionClick listener) {
        this.options = options;
        this.listener = listener;
        // Xóa các constructor dư thừa khác
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

        // Hiển thị text của đáp án
        // Ví dụ: A. git status, B. git log...
        h.txt.setText(opt.getOption_id() + ". " + opt.getOption_text());

        // Logic Highlight (Tùy chọn: Highlight sau khi chọn để tạo hiệu ứng)
        if (opt.getOption_id().equals(selectedOptionId)) {
            // Áp dụng style/màu sắc đã chọn (ví dụ: nền xanh, chữ trắng)
            h.itemView.setBackgroundResource(R.drawable.bg_answer_selected);
        } else {
            // Áp dụng style mặc định
            h.itemView.setBackgroundResource(R.drawable.bg_answer_default);
        }

        h.itemView.setOnClickListener(v -> {
            if (listener != null) {
                // Tạm thời vô hiệu hóa click trên Adapter sau khi chọn lần đầu
                // Lưu lại lựa chọn để highlight
                selectedOptionId = opt.getOption_id();
                notifyDataSetChanged(); // Cập nhật UI để highlight

                // Trả về đối tượng Option đã chọn
                listener.onSelect(opt);
            }
        });
    }

    @Override
    public int getItemCount() { return options == null ? 0 : options.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt;
        ViewHolder(View v) {
            super(v);
            // Giả định ID txtOption là đúng trong item_answer_option.xml
            txt = v.findViewById(R.id.txtOption);
        }
    }
}