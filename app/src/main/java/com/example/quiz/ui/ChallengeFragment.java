package com.example.quiz.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.quiz.R;
import com.example.quiz.utils.ViewUtils;


public class ChallengeFragment extends Fragment {
    private boolean survival = true;


    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_challenge, container, false);
        ViewUtils.fadeIn(v);


        TextView chipSurvival = v.findViewById(R.id.chipSurvival);
        TextView chipSpeed = v.findViewById(R.id.chipSpeed);
        TextView tvMode = v.findViewById(R.id.tvMode);
        View card = v.findViewById(R.id.card);
        Button btn = v.findViewById(R.id.btnStart);


        chipSurvival.setOnClickListener(view -> { survival = true; tvMode.setText("Chế độ: Sinh tồn"); ViewUtils.slideUp(card);});
        chipSpeed.setOnClickListener(view -> { survival = false; tvMode.setText("Chế độ: Tốc độ"); ViewUtils.slideUp(card);});


        btn.setOnClickListener(view -> view.animate().rotation(6f).setDuration(80).withEndAction(() -> view.animate().rotation(0f).setDuration(80)).start());
        return v;
    }
}
