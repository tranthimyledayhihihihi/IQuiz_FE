package com.example.iq5.feature.auth.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iq5.R;
import com.example.iq5.core.navigation.NavigationHelper;
import com.example.iq5.feature.auth.data.AuthRepository;
import com.example.iq5.feature.auth.model.HomeResponse;
import com.example.iq5.feature.quiz.data.FeaturedQuiz;
import com.example.iq5.feature.quiz.data.QuizRepository;
import com.example.iq5.feature.quiz.ui.FeaturedQuizAdapter;
import com.example.iq5.feature.specialmode.ui.CustomQuizFragment;
import com.example.iq5.feature.specialmode.ui.WrongHistoryFragment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    public static final String EXTRA_RESET_HOME_UI = "RESET_HOME_UI";

    private TextView tvWelcome;
    private RecyclerView rvQuizzes;        // đề xuất (dọc) - lấy từ HomeResponse.featuredQuizzes
    private RecyclerView rvFeaturedQuiz;   // nổi bật (ngang) - lấy từ API featured mới
    private ImageView imgAvatarHome;

    private View homeContent;
    private View fragmentContainer;

    private AuthRepository authRepository;
    private QuizRepository quizRepository;

    private HomeResponse homeData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initViews();
        initRepository();

        setupBackStackUiSync();
        setupBackPressHandler();
        setupNavigation();

        loadHomeData();         // welcome + list đề xuất (dọc)
        loadFeaturedQuizzes();  // list nổi bật (ngang)

        // Nếu vừa vào Home mà không có fragment nào -> show home content
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            showHomeContent();
        } else {
            showFragmentContainer();
        }

        // Nếu intent có yêu cầu reset UI
        if (getIntent() != null && getIntent().getBooleanExtra(EXTRA_RESET_HOME_UI, false)) {
            resetToHome();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);

        if (intent != null && intent.getBooleanExtra(EXTRA_RESET_HOME_UI, false)) {
            resetToHome();
        }
    }

    private void initViews() {
        tvWelcome = findViewById(R.id.tvWelcome);
        rvQuizzes = findViewById(R.id.rvQuizzes);
        rvFeaturedQuiz = findViewById(R.id.rvFeaturedQuiz);
        imgAvatarHome = findViewById(R.id.imgAvatarHome);

        homeContent = findViewById(R.id.home_content);
        fragmentContainer = findViewById(R.id.fragment_container);

        // Danh sách đề xuất (cuộn dọc)
        if (rvQuizzes != null) {
            rvQuizzes.setLayoutManager(
                    new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            );
        }

        // Danh sách nổi bật (cuộn ngang)
        if (rvFeaturedQuiz != null) {
            rvFeaturedQuiz.setLayoutManager(
                    new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            );
        }
    }

    private void initRepository() {
        authRepository = new AuthRepository(this);
        quizRepository = new QuizRepository(this);
    }

    /**
     * Load dữ liệu tổng quan trang chủ (Welcome + list đề xuất)
     */
    private void loadHomeData() {
        authRepository.getHomeDataAsync(new AuthRepository.HomeDataCallback() {
            @Override
            public void onSuccess(HomeResponse data) {
                runOnUiThread(() -> {
                    homeData = data;

                    if (homeData == null) {
                        Toast.makeText(HomeActivity.this,
                                "Không tải được dữ liệu trang chủ", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (tvWelcome != null) {
                        tvWelcome.setText(homeData.welcomeMessage != null
                                ? homeData.welcomeMessage
                                : "Chào mừng trở lại!");
                    }

                    // List đề xuất (dọc) lấy từ HomeResponse.featuredQuizzes
                    if (rvQuizzes != null
                            && homeData.featuredQuizzes != null
                            && !homeData.featuredQuizzes.isEmpty()) {

                        QuizAdapter adapter = new QuizAdapter(homeData.featuredQuizzes);
                        rvQuizzes.setAdapter(adapter);
                    }
                });
            }
        });
    }

    /**
     * GỌI API LẤY QUIZ NỔI BẬT (FEATURED) - list ngang
     */
    private void loadFeaturedQuizzes() {
        if (quizRepository == null || rvFeaturedQuiz == null) return;

        quizRepository.getFeaturedQuiz(new Callback<List<FeaturedQuiz>>() {
            @Override
            public void onResponse(Call<List<FeaturedQuiz>> call, Response<List<FeaturedQuiz>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<FeaturedQuiz> featuredList = response.body();
                    runOnUiThread(() -> {
                        if (featuredList != null && !featuredList.isEmpty()) {
                            FeaturedQuizAdapter adapter = new FeaturedQuizAdapter(featuredList);
                            rvFeaturedQuiz.setAdapter(adapter);
                            Log.d("API_SUCCESS", "Đã hiển thị " + featuredList.size() + " quiz nổi bật");
                        } else {
                            Log.d("API_SUCCESS", "Danh sách quiz nổi bật rỗng");
                        }
                    });
                } else {
                    Log.e("API_ERROR", "Lỗi phản hồi: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<FeaturedQuiz>> call, Throwable t) {
                Log.e("API_ERROR", "Không thể kết nối BE: " + (t != null ? t.getMessage() : "unknown"));
            }
        });
    }

    private void setupNavigation() {
        // --- BOTTOM NAVIGATION ---
        View btnHome = findViewById(R.id.btnHome);
        if (btnHome != null) btnHome.setOnClickListener(v -> resetToHome());

        View btnLibrary = findViewById(R.id.btnLibrary);
        if (btnLibrary != null) btnLibrary.setOnClickListener(v ->
                showFragment(new WrongHistoryFragment(), "WrongHistory")
        );

        View btnJoin = findViewById(R.id.btnJoin);
        if (btnJoin != null) btnJoin.setOnClickListener(v -> NavigationHelper.navigateToJoinRoom(this));

        View btnCreate = findViewById(R.id.btnCreate);
        if (btnCreate != null) btnCreate.setOnClickListener(v ->
                showFragment(new CustomQuizFragment(), "CustomQuiz")
        );

        View btnProfileNav = findViewById(R.id.btnProfile);
        if (btnProfileNav != null) btnProfileNav.setOnClickListener(v -> NavigationHelper.navigateToProfile(this));

        // --- TOP BAR ACTIONS ---
        View btnSettings = findViewById(R.id.btnSettings);
        if (btnSettings != null) btnSettings.setOnClickListener(v -> NavigationHelper.navigateToSettings(this));

        View btnSearch = findViewById(R.id.btnSearch);
        if (btnSearch != null) btnSearch.setOnClickListener(v ->
                Toast.makeText(this, "Tính năng tìm kiếm sắp ra mắt!", Toast.LENGTH_SHORT).show()
        );

        // Avatar -> Profile + Debug (long click)
        if (imgAvatarHome != null) {
            imgAvatarHome.setOnClickListener(v -> NavigationHelper.navigateToProfile(this));
            imgAvatarHome.setOnLongClickListener(v -> {
                showDebugMenu();
                return true;
            });
        }

        // --- QUICK ACTIONS GRID ---
        View btnDailyReward = findViewById(R.id.btnDailyReward);
        if (btnDailyReward != null) btnDailyReward.setOnClickListener(v -> NavigationHelper.navigateToDailyReward(this));

        View btnAchievement = findViewById(R.id.btnAchievement);
        if (btnAchievement != null) btnAchievement.setOnClickListener(v -> NavigationHelper.navigateToAchievement(this));

        View btnStreak = findViewById(R.id.btnStreak);
        if (btnStreak != null) btnStreak.setOnClickListener(v -> NavigationHelper.navigateToStreak(this));

        View btnMultiplayer = findViewById(R.id.btnMultiplayer);
        if (btnMultiplayer != null) btnMultiplayer.setOnClickListener(v -> NavigationHelper.navigateToMultiplayerLobby(this));

        View btnArena = findViewById(R.id.btnArena);
        if (btnArena != null) btnArena.setOnClickListener(v -> NavigationHelper.navigateToSelectCategory(this));

        // Debug menu (long click welcome text)
        if (tvWelcome != null) {
            tvWelcome.setOnLongClickListener(v -> {
                showDebugMenu();
                return true;
            });
        }
    }

    /**
     * Đồng bộ UI theo back stack: còn fragment -> show container, hết fragment -> show home.
     */
    private void setupBackStackUiSync() {
        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                showHomeContent();
            } else {
                showFragmentContainer();
            }
        });
    }

    /**
     * Back gesture / back button: nếu còn fragment -> pop, nếu không -> thoát activity.
     */
    private void setupBackPressHandler() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                FragmentManager fm = getSupportFragmentManager();

                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack(); // listener sẽ tự showHomeContent khi về 0
                } else {
                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                }
            }
        });
    }

    private void showHomeContent() {
        if (homeContent != null) homeContent.setVisibility(View.VISIBLE);
        if (fragmentContainer != null) fragmentContainer.setVisibility(View.GONE);
    }

    private void showFragmentContainer() {
        if (homeContent != null) homeContent.setVisibility(View.GONE);
        if (fragmentContainer != null) fragmentContainer.setVisibility(View.VISIBLE);
    }

    private void resetToHome() {
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        showHomeContent();
    }

    private void showFragment(Fragment fragment, String tag) {
        showFragmentContainer();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment, tag);
        transaction.addToBackStack(tag);
        transaction.commit();
    }

    private void showDebugMenu() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("🛠️ Debug Menu");

        String[] options = {
                "🎯 API Quiz Test (Real Data)",
                "🔧 Simple Test Activity",
                "📡 API Test Tool",
                "🏠 Back to Home"
        };

        builder.setItems(options, (dialog, which) -> {
            switch (which) {
                case 0:
                case 1:
                case 2:
                    Toast.makeText(this, "Chức năng đang phát triển", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    resetToHome();
                    dialog.dismiss();
                    break;
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
}
