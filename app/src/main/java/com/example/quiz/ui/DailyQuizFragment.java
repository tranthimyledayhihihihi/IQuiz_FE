package com.example.quiz.ui;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.quiz.R;
import com.example.quiz.model.Question;
import com.example.quiz.utils.PrefUtils;
import com.example.quiz.utils.ViewUtils;
import com.google.android.material.button.MaterialButton;

import java.util.Arrays;
import java.util.List;

public class DailyQuizFragment extends Fragment {

    private DailyQuizViewModel vm;
    private CountDownTimer timer;
    private ProgressBar progress;
    private TextView tvTimer, tvScore, tvQuestion;
    private MaterialButton b1,b2,b3,b4;
    private ToneGenerator tone;
    private View motionRoot;

    private static final int ROUND_MS = 30_000;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_daily_quiz, container, false);
        ViewUtils.fadeIn(v);

        motionRoot = v.findViewById(R.id.motionRoot);
        progress = v.findViewById(R.id.progressTimer);
        tvTimer = v.findViewById(R.id.tvTimer);
        tvScore = v.findViewById(R.id.tvScore);
        tvQuestion = v.findViewById(R.id.tvQuestion);

        b1 = v.findViewById(R.id.btnOpt1);
        b2 = v.findViewById(R.id.btnOpt2);
        b3 = v.findViewById(R.id.btnOpt3);
        b4 = v.findViewById(R.id.btnOpt4);

        vm = new ViewModelProvider(this).get(DailyQuizViewModel.class);
        tvScore.setText("Điểm: " + vm.getScore());

        vm.getQuestion().observe(getViewLifecycleOwner(), this::bindQuestion);

        List<MaterialButton> buttons = Arrays.asList(b1,b2,b3,b4);
        for (int i=0;i<buttons.size();i++) {
            final int idx = i;
            buttons.get(i).setOnClickListener(view -> onAnswerSelected((MaterialButton) view, idx));
        }

        tone = new ToneGenerator(AudioManager.STREAM_MUSIC, 60);

        startRoundTimer();
        return v;
    }

    private void bindQuestion(Question q) {
        if (q == null) return;
        tvQuestion.setText(q.text);
        b1.setText(q.options.get(0));
        b2.setText(q.options.get(1));
        b3.setText(q.options.get(2));
        b4.setText(q.options.get(3));

        enableButtons(true);
        resetButtonStyles();
        restartProgress();
        startRoundTimer();
    }

    private void onAnswerSelected(MaterialButton btn, int optionIndex) {
        enableButtons(false);

        boolean correct = vm.answer(optionIndex);
        animateButtonResult(btn, correct);

        btn.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP);
        playTone(correct);

        if (!correct) {
            Question q = vm.getQuestion().getValue();
            if (q != null) PrefUtils.addMistake(requireContext(), q.text, q.options.get(q.answerIndex));
        }

        tvScore.setText("Điểm: " + vm.getScore());

        // highlight đáp án đúng
        highlightRightAnswer();

        // delay 1s rồi chuyển câu + motion
        btn.postDelayed(() -> {
            transitionOutIn();
            vm.nextQuestion();
        }, 1000);

        if (vm.survivalMode && !correct) {
            // kết thúc (đơn giản): reset score
            PrefUtils.saveScore(requireContext(), vm.getScore());
            vm.resetScore();
        }
    }

    private void startRoundTimer() {
        cancelTimer();
        progress.setMax(ROUND_MS);
        progress.setProgress(ROUND_MS);

        timer = new CountDownTimer(ROUND_MS, 16) {
            @Override public void onTick(long ms) {
                tvTimer.setText(String.format("%02d:%02d", (ms/1000)/60, (ms/1000)%60));
                progress.setProgress((int) ms);
            }
            @Override public void onFinish() {
                tvTimer.setText("Hết giờ");
                enableButtons(false);
                // tự sang câu tiếp theo
                transitionOutIn();
                vm.nextQuestion();
            }
        }.start();
    }

    private void restartProgress() {
        ValueAnimator va = ValueAnimator.ofInt(progress.getMax(), 0);
        va.setDuration(ROUND_MS);
        va.addUpdateListener(a -> progress.setProgress((int) a.getAnimatedValue()));
        va.start();
    }

    private void animateButtonResult(MaterialButton btn, boolean correct) {
        int from = getResources().getColor(android.R.color.white);
        int to = getResources().getColor(correct ? R.color.green_ok : R.color.red_bad);
        ObjectAnimator.ofArgb(btn, "strokeColor", from, to)
                .setDuration(160)
                .start();

        if (!correct) {
            btn.animate().rotation(3f).setDuration(60)
                    .withEndAction(() -> btn.animate().rotation(-3f).setDuration(60)
                            .withEndAction(() -> btn.animate().rotation(0f).setDuration(60)).start()).start();
        } else {
            btn.animate().scaleX(1.04f).scaleY(1.04f).setDuration(80)
                    .withEndAction(() -> btn.animate().scaleX(1f).scaleY(1f).setDuration(80)).start();
        }
    }

    private void highlightRightAnswer() {
        Question q = vm.getQuestion().getValue();
        if (q == null) return;
        MaterialButton right =
                q.answerIndex==0 ? b1 : q.answerIndex==1 ? b2 :
                        q.answerIndex==2 ? b3 : b4;

        ObjectAnimator.ofArgb(right, "strokeColor",
                        getResources().getColor(android.R.color.white),
                        getResources().getColor(R.color.green_ok))
                .setDuration(160).start();
    }

    private void transitionOutIn() {
        // dùng MotionLayout: setTransition và transitionToEnd rồi ngay lập tức về Start
        try {
            androidx.constraintlayout.motion.widget.MotionLayout ml =
                    (androidx.constraintlayout.motion.widget.MotionLayout) motionRoot;
            ml.setTransition(R.id.start, R.id.end);
            ml.transitionToEnd();
            ml.postDelayed(() -> {
                ml.setProgress(0f);
                ml.setTransition(R.id.start, R.id.end);
            }, 180);
        } catch (Exception ignore) { }
    }

    private void enableButtons(boolean enable) {
        b1.setEnabled(enable); b2.setEnabled(enable); b3.setEnabled(enable); b4.setEnabled(enable);
    }

    private void resetButtonStyles() {
        int white = getResources().getColor(android.R.color.white);
        b1.setStrokeColor(android.content.res.ColorStateList.valueOf(white));
        b2.setStrokeColor(android.content.res.ColorStateList.valueOf(white));
        b3.setStrokeColor(android.content.res.ColorStateList.valueOf(white));
        b4.setStrokeColor(android.content.res.ColorStateList.valueOf(white));
    }

    private void playTone(boolean correct) {
        if (tone == null) return;
        tone.startTone(correct ? ToneGenerator.TONE_PROP_BEEP : ToneGenerator.TONE_CDMA_SOFT_ERROR_LITE, 120);
    }

    private void cancelTimer() {
        if (timer != null) { timer.cancel(); timer = null; }
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        cancelTimer();
        if (tone != null) { tone.release(); tone = null; }
    }
}
