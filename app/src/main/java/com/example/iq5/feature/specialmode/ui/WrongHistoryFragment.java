package com.example.iq5.feature.specialmode.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iq5.R;
import com.example.iq5.feature.specialmode.adapter.WrongTopicAdapter;
import com.example.iq5.feature.specialmode.data.SpecialModeRepository;
import com.example.iq5.feature.specialmode.model.WrongAnswersResponse;

public class WrongHistoryFragment extends Fragment {

    private SpecialModeRepository repository;
    private WrongTopicAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wrong_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        repository = new SpecialModeRepository(requireContext());

        TextView tvTotalWrong = view.findViewById(R.id.tv_total_wrong);
        RecyclerView rv = view.findViewById(R.id.rv_wrong_topics);

        adapter = new WrongTopicAdapter(topic -> {
            // TODO: mở màn quiz review theo topic.topicId
        });
        rv.setAdapter(adapter);

        WrongAnswersResponse data = repository.getWrongAnswers();
        if (data != null) {
            tvTotalWrong.setText("Tổng " + data.totalWrongQuestions + " câu sai cần ôn lại");
            adapter.submitList(data.topics);
        }
    }
}
