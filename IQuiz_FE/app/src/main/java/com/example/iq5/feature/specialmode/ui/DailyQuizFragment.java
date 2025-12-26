package com.example.iq5.feature.specialmode.ui;

import android.content.Intent;
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
import com.example.iq5.feature.quiz.ui.QuizActivity;
import com.example.iq5.feature.specialmode.adapter.DailyQuizAdapter;
import com.example.iq5.feature.specialmode.data.DailyQuizRepository;
import com.example.iq5.feature.specialmode.model.DailyQuizItem;
import com.example.iq5.feature.specialmode.model.DailyQuizResponse;

public class DailyQuizFragment extends Fragment {

    private DailyQuizRepository repository;
    private DailyQuizAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_daily_quiz, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        repository = new DailyQuizRepository(requireContext());

        TextView tvDate = view.findViewById(R.id.tv_date);
        TextView tvStreak = view.findViewById(R.id.tv_streak);
        TextView tvRewardToday = view.findViewById(R.id.tv_reward_today);
        RecyclerView rv = view.findViewById(R.id.rv_daily_quiz);

        adapter = new DailyQuizAdapter(item -> {
            Intent intent = new Intent(requireContext(), QuizActivity.class);
            intent.putExtra("ENTRY_SOURCE", "daily_quiz");
            intent.putExtra("DAILY_ID", item.id);
            startActivity(intent);
        });
        rv.setAdapter(adapter);

        DailyQuizResponse data = repository.getDailyQuiz();
        if (data != null) {
            tvDate.setText(data.date);
            tvStreak.setText("Chuỗi " + data.streakDays + " ngày 🔥");
            tvRewardToday.setText("+" + data.totalRewardsToday + " 💰 hôm nay");
            adapter.submitList(data.items);
        }
    }
}
