package com.example.iq5.feature.specialmode.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.iq5.R;
import com.example.iq5.feature.quiz.ui.QuizActivity;
import com.example.iq5.feature.specialmode.data.ChallengeModeRepository;
import com.example.iq5.feature.specialmode.model.ChallengeMode;
import com.example.iq5.feature.specialmode.model.ChallengeModesResponse;

public class ChallengeModesFragment extends Fragment {

    private ChallengeModeRepository repository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_challenge_modes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        repository = new ChallengeModeRepository(requireContext());

        TextView tvSurvivalDesc = view.findViewById(R.id.tv_survival_desc);
        TextView tvSurvivalMeta = view.findViewById(R.id.tv_survival_meta);
        TextView tvLives = view.findViewById(R.id.tv_lives);
        Button btnStartSurvival = view.findViewById(R.id.btn_start_survival);

        TextView tvSpeedDesc = view.findViewById(R.id.tv_speed_desc);
        TextView tvSpeedMeta = view.findViewById(R.id.tv_speed_meta);
        Button btnStartSpeed = view.findViewById(R.id.btn_start_speed);

        // LOAD DỮ LIỆU MOCK
        ChallengeModesResponse data = repository.getChallengeModes();
        if (data != null && data.modes != null) {

            for (ChallengeMode mode : data.modes) {

                if ("SURVIVAL".equals(mode.modeId)) {

                    tvSurvivalDesc.setText(mode.description);

                    String meta = "Thời gian/câu: " + mode.timePerQuestion +
                            "s • Độ khó: " + mode.difficultyRange;
                    tvSurvivalMeta.setText(meta);

                    int lives = mode.maxLives != null ? mode.maxLives : 3;
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < lives; i++) sb.append("♥");
                    tvLives.setText(sb.toString());

                } else if ("SPEED".equals(mode.modeId)) {

                    tvSpeedDesc.setText(mode.description);

                    String meta = "Tổng thời gian: " + mode.totalTime +
                            "s • Thời gian/câu: " + mode.timePerQuestion + "s";
                    tvSpeedMeta.setText(meta);
                }
            }
        }

        // START SURVIVAL
        btnStartSurvival.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), QuizActivity.class);
            intent.putExtra("ENTRY_SOURCE", "challenge");
            intent.putExtra("MODE_TYPE", "SURVIVAL");
            startActivity(intent);
        });

        // START SPEED
        btnStartSpeed.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), QuizActivity.class);
            intent.putExtra("ENTRY_SOURCE", "challenge");
            intent.putExtra("MODE_TYPE", "SPEED");
            startActivity(intent);
        });
    }
}
