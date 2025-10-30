package com.example.quiz.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quiz.R;
import java.util.List;


public class MistakeAdapter extends RecyclerView.Adapter<MistakeAdapter.VH> {
    public static class Mistake { public String q, a; public Mistake(String q, String a){this.q=q;this.a=a;} }
    private final List<Mistake> data;
    public MistakeAdapter(List<Mistake> data){ this.data=data; }


    static class VH extends RecyclerView.ViewHolder {
        TextView tvQ, tvA;
        VH(@NonNull View itemView) { super(itemView); tvQ=itemView.findViewById(R.id.tvQ); tvA=itemView.findViewById(R.id.tvA);} }


    @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup p, int vt) {
        View v = LayoutInflater.from(p.getContext()).inflate(R.layout.item_mistake, p, false);
        return new VH(v);
    }


    @Override public void onBindViewHolder(@NonNull VH h, int i) {
        Mistake m = data.get(i);
        h.tvQ.setText("Hỏi: " + m.q);
        h.tvA.setText("Đáp án đúng: " + m.a);
        h.itemView.setAlpha(0f); h.itemView.animate().alpha(1f).setDuration(200).start();
    }


    @Override public int getItemCount(){ return data.size(); }
}
