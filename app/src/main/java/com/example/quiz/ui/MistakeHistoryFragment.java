package com.example.quiz.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quiz.R;
import com.example.quiz.adapters.MistakeAdapter;
import com.example.quiz.utils.ViewUtils;
import java.util.*;


public class MistakeHistoryFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mistake_history, container, false);
        ViewUtils.fadeIn(v);


        RecyclerView rv = v.findViewById(R.id.rvMistakes);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setLayoutAnimation(android.view.animation.AnimationUtils.loadLayoutAnimation(getContext(), R.anim.list_fall_down));


        List<MistakeAdapter.Mistake> mock = Arrays.asList(
                new MistakeAdapter.Mistake("2+2?", "4"),
                new MistakeAdapter.Mistake("Thủ đô VN?", "Hà Nội"),
                new MistakeAdapter.Mistake("Năm nhuận?", "366 ngày")
        );
        rv.setAdapter(new MistakeAdapter(mock));


        TextView chipRecent = v.findViewById(R.id.chipRecent);
        TextView chipReview = v.findViewById(R.id.chipReview);
        chipRecent.setOnClickListener(view -> ViewUtils.slideUp(rv));
        chipReview.setOnClickListener(view -> ViewUtils.fadeIn(rv));
        return v;
    }
}
