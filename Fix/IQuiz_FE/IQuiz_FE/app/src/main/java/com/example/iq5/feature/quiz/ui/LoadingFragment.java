package com.example.iq5.feature.quiz.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.iq5.R;

public class LoadingFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inf, ViewGroup p, Bundle b) {
        return inf.inflate(R.layout.fragment_loading, p, false);
    }
}
