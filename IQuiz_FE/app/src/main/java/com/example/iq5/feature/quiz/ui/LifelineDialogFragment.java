package com.example.iq5.feature.quiz.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.iq5.R;
import com.example.iq5.feature.quiz.model.Lifeline;

import java.util.List;

public class LifelineDialogFragment extends DialogFragment {

    public interface OnSelect {
        void onUse(Lifeline lifeline);
    }

    private List<Lifeline> lifelineList;
    private OnSelect listener;

    public LifelineDialogFragment(List<Lifeline> lifelineList, OnSelect listener) {
        this.lifelineList = lifelineList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_lifeline);

        LinearLayout container = dialog.findViewById(R.id.containerLifelines);

        // Thêm button cho mỗi lifeline
        for (Lifeline lf : lifelineList) {
            Button btn = new Button(getContext());
            btn.setText(lf.getName() + " (" + lf.getCountRemaining() + ")");
            btn.setEnabled(lf.getCountRemaining() > 0);
            btn.setOnClickListener(v -> {
                listener.onUse(lf);
                dismiss();
            });
            container.addView(btn);
        }

        return dialog;
    }
}
