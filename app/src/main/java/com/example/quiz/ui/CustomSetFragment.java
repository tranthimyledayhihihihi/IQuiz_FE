package com.example.quiz.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.quiz.R;
import com.example.quiz.utils.ViewUtils;


public class CustomSetFragment extends Fragment {
    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_custom_set, container, false);
        ViewUtils.fadeIn(v);
        EditText name = v.findViewById(R.id.edtSetName);
        Button save = v.findViewById(R.id.btnSave);
        save.setOnClickListener(btn -> {
            btn.animate().translationY(-6f).setDuration(80).withEndAction(() -> btn.animate().translationY(0f).setDuration(80)).start();
            Toast.makeText(getContext(), "Đã lưu '" + name.getText().toString() + "' (mock)", Toast.LENGTH_SHORT).show();
        });
        return v;
    }
}
