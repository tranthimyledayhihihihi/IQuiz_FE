package com.example.iq5.feature.specialmode.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iq5.R;
import com.example.iq5.feature.quiz.ui.QuizActivity;
import com.example.iq5.feature.specialmode.adapter.WrongTopicAdapter;
import com.example.iq5.feature.specialmode.data.SpecialModeRepository;
import com.example.iq5.feature.specialmode.model.WrongAnswersResponse;
import com.example.iq5.feature.specialmode.model.WrongAnswerItem;
import com.example.iq5.feature.specialmode.model.WrongTopic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WrongHistoryFragment extends Fragment {

    private SpecialModeRepository repository;
    private WrongTopicAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wrong_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        repository = new SpecialModeRepository(requireContext());

        TextView tvTotalWrong = view.findViewById(R.id.tv_total_wrong);
        RecyclerView rv = view.findViewById(R.id.rv_wrong_topics);

        adapter = new WrongTopicAdapter((WrongTopic topic) -> {
            Intent intent = new Intent(requireContext(), QuizActivity.class);
            intent.putExtra("ENTRY_SOURCE", "wrong_history");
            intent.putExtra("TOPIC_ID", topic.topicId);
            intent.putExtra("TOPIC_NAME", topic.topicName);
            intent.putExtra("SUGGESTED_QUIZ_ID", topic.suggestedQuizId);
            startActivity(intent);
        });
        rv.setAdapter(adapter);

        // ===============================
        // GỌI API WRONG QUESTION (JWT)
        // ===============================
        repository.getWrongAnswers()
                .enqueue(new Callback<WrongAnswersResponse>() {
                    @Override
                    public void onResponse(Call<WrongAnswersResponse> call,
                                           Response<WrongAnswersResponse> response) {

                        if (!response.isSuccessful()
                                || response.body() == null
                                || !response.body().success) {

                            tvTotalWrong.setText("Không có dữ liệu câu sai");
                            return;
                        }

                        List<WrongAnswerItem> data = response.body().data;
                        tvTotalWrong.setText(
                                "Tổng " + data.size() + " câu sai cần ôn lại"
                        );

                        // ===============================
                        // GOM THEO CHỦ ĐỀ
                        // ===============================
                        Map<String, WrongTopic> topicMap = new HashMap<>();

                        for (WrongAnswerItem item : data) {
                            String topicName = item.tenChuDe;

                            if (!topicMap.containsKey(topicName)) {
                                WrongTopic topic = new WrongTopic();
                                topic.topicId = topicName; // dùng tên làm key
                                topic.topicName = topicName;
                                topic.wrongCount = 0;
                                topic.suggestedQuizId =
                                        String.valueOf(item.cauHoiID);
                                topicMap.put(topicName, topic);
                            }

                            topicMap.get(topicName).wrongCount++;
                        }

                        adapter.submitList(
                                new ArrayList<>(topicMap.values())
                        );
                    }

                    @Override
                    public void onFailure(Call<WrongAnswersResponse> call,
                                          Throwable t) {
                        tvTotalWrong.setText(
                                "Không tải được lịch sử câu sai"
                        );
                    }
                });
    }
}
