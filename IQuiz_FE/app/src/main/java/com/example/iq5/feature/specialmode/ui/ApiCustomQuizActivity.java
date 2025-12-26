package com.example.iq5.feature.specialmode.ui;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iq5.core.network.ApiClient;
import com.example.iq5.core.prefs.PrefsManager;
import com.example.iq5.data.api.ApiService;
import com.example.iq5.data.model.CustomQuizResponse;
import com.example.iq5.feature.specialmode.model.CustomQuizItem;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiCustomQuizActivity extends AppCompatActivity {

    private LinearLayout containerLayout;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // UI
        ScrollView scrollView = new ScrollView(this);
        containerLayout = new LinearLayout(this);
        containerLayout.setOrientation(LinearLayout.VERTICAL);
        containerLayout.setPadding(32, 32, 32, 32);
        scrollView.addView(containerLayout);
        setContentView(scrollView);

        // API
        apiService = ApiClient
                .getClient(new PrefsManager(this))
                .create(ApiService.class);

        loadMyCustomQuizzes();
    }

    private void loadMyCustomQuizzes() {

        TextView loading = new TextView(this);
        loading.setText("🔄 Đang tải quiz tùy chỉnh...");
        loading.setTextSize(16);
        containerLayout.addView(loading);

        apiService.getMyCustomQuizzes(
                "Bearer " + new PrefsManager(this).getToken(),
                1,
                20
        ).enqueue(new Callback<CustomQuizResponse>() {
            @Override
            public void onResponse(Call<CustomQuizResponse> call,
                                   Response<CustomQuizResponse> response) {

                containerLayout.removeView(loading);

                if (!response.isSuccessful() || response.body() == null) {
                    showError("Không tải được quiz");
                    return;
                }

                displayQuizzes(response.body());
            }

            @Override
            public void onFailure(Call<CustomQuizResponse> call, Throwable t) {
                containerLayout.removeView(loading);
                showError(t.getMessage());
            }
        });
    }

    private void displayQuizzes(CustomQuizResponse response) {

        if (response.danhSach == null || response.danhSach.isEmpty()) {
            TextView empty = new TextView(this);
            empty.setText("📭 Chưa có quiz tùy chỉnh nào");
            empty.setTextSize(16);
            containerLayout.addView(empty);
            return;
        }

        for (CustomQuizItem quiz : response.danhSach) {

            LinearLayout card = new LinearLayout(this);
            card.setOrientation(LinearLayout.VERTICAL);
            card.setPadding(24, 24, 24, 24);
            card.setBackgroundColor(0xFFF5F5F5);

            // Title
            TextView title = new TextView(this);
            title.setText("🎯 " + quiz.tenQuiz);
            title.setTextSize(16);
            card.addView(title);

            // Description
            if (quiz.moTa != null && !quiz.moTa.isEmpty()) {
                TextView desc = new TextView(this);
                desc.setText("📝 " + quiz.moTa);
                card.addView(desc);
            }

            // Meta
            TextView meta = new TextView(this);
            meta.setText(
                    "📊 " + quiz.soLuongCauHoi +
                            " câu • ⏳ " + quiz.trangThai
            );
            card.addView(meta);

            // Date
            if (quiz.ngayTao != null && quiz.ngayTao.length() >= 10) {
                TextView date = new TextView(this);
                date.setText("📅 " + quiz.ngayTao.substring(0, 10));
                card.addView(date);
            }

            containerLayout.addView(card);
        }

        TextView summary = new TextView(this);
        summary.setText("📊 Tổng: " + response.danhSach.size() + " quiz");
        summary.setPadding(0, 20, 0, 0);
        containerLayout.addView(summary);
    }

    private void showError(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
