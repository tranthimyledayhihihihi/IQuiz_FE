package com.example.iq5.feature.specialmode.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iq5.R;
import com.example.iq5.feature.specialmode.adapter.CustomQuizAdapter;
import com.example.iq5.feature.specialmode.data.SpecialModeRepository;
import com.example.iq5.feature.specialmode.model.CustomQuizItem;
import com.example.iq5.feature.specialmode.model.CustomQuizResponse;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class CustomQuizFragment extends Fragment {

    private SpecialModeRepository repository;
    private CustomQuizAdapter adapter;
    private final List<CustomQuizItem> localList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_custom_quiz, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        repository = new SpecialModeRepository(requireContext());

        EditText etTitle = view.findViewById(R.id.et_quiz_title);
        RecyclerView rv = view.findViewById(R.id.rv_custom_quiz);
        View btnCreate = view.findViewById(R.id.btn_add_custom_quiz);

        adapter = new CustomQuizAdapter(new CustomQuizAdapter.OnCustomQuizListener() {
            @Override
            public void onShare(CustomQuizItem item) {
                // TODO: mở dialog share code hoặc deep link
            }

            @Override
            public void onStart(CustomQuizItem item) {
                // TODO: mở màn quiz với bộ câu hỏi tương ứng
            }
        });
        rv.setAdapter(adapter);

        CustomQuizResponse data = repository.getCustomQuizzes();
        if (data != null && data.quizzes != null) {
            localList.clear();
            localList.addAll(data.quizzes);
            adapter.submitList(new ArrayList<>(localList));
        }

        btnCreate.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            if (TextUtils.isEmpty(title)) {
                Snackbar.make(view, "Nhập tên bộ câu hỏi trước đã", Snackbar.LENGTH_SHORT).show();
                return;
            }
            CustomQuizItem item = new CustomQuizItem();
            item.id = "LOCAL_" + System.currentTimeMillis();
            item.title = title;
            item.description = "Bộ câu hỏi do bạn tự tạo.";
            item.questionCount = 0;
            item.isPublic = false;
            item.visibility = "private";

            localList.add(0, item);
            adapter.submitList(new ArrayList<>(localList));
            etTitle.setText("");
        });
    }
}
