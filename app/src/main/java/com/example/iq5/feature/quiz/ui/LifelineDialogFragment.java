package com.example.iq5.feature.quiz.ui;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.iq5.R;
import com.example.iq5.feature.quiz.model.Lifeline;

import java.util.List;

public class LifelineDialogFragment extends DialogFragment {

    public LifelineDialogFragment() {

    }

    public interface OnSelect {
        void onUse(Lifeline lf);
    }

    private List<Lifeline> list;
    private OnSelect listener;

    public LifelineDialogFragment(List<Lifeline> list, OnSelect listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle b) {
        Dialog d = new Dialog(getActivity());
        d.setContentView(R.layout.dialog_lifeline);
        return d;
    }
}
