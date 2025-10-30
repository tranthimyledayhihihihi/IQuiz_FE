package com.example.quiz.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.quiz.R;
import com.example.quiz.utils.ViewUtils;


public class PlayerSearchFragment extends Fragment {
    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_player_search, container, false);
        ViewUtils.fadeIn(v);
        EditText q = v.findViewById(R.id.edtQuery);
        TextView res = v.findViewById(R.id.tvResult);
        Button search = v.findViewById(R.id.btnSearch);
        search.setOnClickListener(btn -> {
            String s = q.getText().toString().trim();
            res.setText(s.isEmpty()? "Nhập tên để tìm" : ("Tìm thấy: " + s + " (mock)"));
            ViewUtils.slideUp(res);
        });
        return v;
    }
}
