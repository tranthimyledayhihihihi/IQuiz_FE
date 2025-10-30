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
import com.example.quiz.utils.PrefUtils;
import com.example.quiz.utils.ViewUtils;
import java.util.*;

public class MistakeHistoryFragment extends Fragment {
    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mistake_history, container, false);
        ViewUtils.fadeIn(v);

        RecyclerView rv = v.findViewById(R.id.rvMistakes);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        Set<String> raw = PrefUtils.loadMistakes(requireContext());
        List<MistakeAdapter.Mistake> data = new ArrayList<>();
        if (raw.isEmpty()) {
            data.add(new MistakeAdapter.Mistake("2+2?", "4"));
        } else {
            for (String s : raw) {
                String[] parts = s.split("\\|\\|", -1);
                if (parts.length == 2) data.add(new MistakeAdapter.Mistake(parts[0], parts[1]));
            }
        }
        rv.setAdapter(new MistakeAdapter(data));
        return v;
    }
}
