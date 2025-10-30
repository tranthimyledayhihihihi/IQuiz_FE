package com.example.quiz.ui;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.quiz.R;
import com.example.quiz.utils.ViewUtils;
import com.google.android.material.button.MaterialButton;


public class DailyQuizFragment extends Fragment {
    private CountDownTimer timer;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_daily_quiz, container, false);
        ViewUtils.fadeIn(v);


        TextView tvTimer = v.findViewById(R.id.tvTimer);
        MaterialButton b2 = v.findViewById(R.id.btnOpt2);
        b2.setOnClickListener(btn -> btn.animate().scaleX(0.96f).scaleY(0.96f).setDuration(60)
                .withEndAction(() -> btn.animate().scaleX(1f).scaleY(1f).setDuration(80)).start());


        timer = new CountDownTimer(30_000, 1000) {
            public void onTick(long ms) { tvTimer.setText(String.format("%02d:%02d", ms/1000/60, (ms/1000)%60)); }
            public void onFinish() { tvTimer.setText("Hết giờ"); }
        }.start();
        return v;
    }


    @Override public void onDestroyView() {
        super.onDestroyView();
        if (timer != null) timer.cancel();
    }
}