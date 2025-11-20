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
    // Thêm trường để theo dõi lựa chọn hiện tại (Tùy chọn, nhưng rất cần thiết cho UI)
    private String selectedDifficultyId;

    public DifficultyAdapter(List<Difficulty> list, OnClick listener) {
        this.list = list;
        this.listener = listener;
        // Khởi tạo giá trị mặc định cho lựa chọn đầu tiên (ví dụ: "easy")
        if (!list.isEmpty()) {
            this.selectedDifficultyId = list.get(0).getId();
        }
    }

    // Phương thức công khai để cập nhật lựa chọn
    public void setSelectedDifficulty(String id) {
        this.selectedDifficultyId = id;
        notifyDataSetChanged(); // Yêu cầu RecyclerView vẽ lại để áp dụng highlight
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

        // FIX: Sử dụng getName() hoặc getContent() thay vì getLevel()
        h.txt.setText(d.getName());

        // Logic Highlight (Rất quan trọng cho trải nghiệm người dùng)
        if (d.getId().equals(selectedDifficultyId)) {
            // Áp dụng style/màu sắc khi được chọn
            h.itemView.setBackgroundResource(R.drawable.bg_difficulty_selected); // Giả định có drawable này
            h.txt.setTextColor(h.itemView.getContext().getResources().getColor(R.color.white)); // Giả định có màu white
        } else {
            // Áp dụng style/màu sắc mặc định
            h.itemView.setBackgroundResource(R.drawable.bg_difficulty_default); // Giả định có drawable này
            h.txt.setTextColor(h.itemView.getContext().getResources().getColor(R.color.black)); // Giả định có màu black
        }

        h.itemView.setOnClickListener(v -> {
            // Cập nhật và thông báo lắng nghe
            setSelectedDifficulty(d.getId());
            listener.onSelect(d);
        });
    }

    @Override
    public int getItemCount() { return list.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt;
        ViewHolder(View v) {
            super(v);
            // Giả định ID txtDifficultyLevel là đúng trong item_difficulty.xml
            txt = v.findViewById(R.id.txtDifficultyLevel);
        }
    }
}