package com.example.iq5.feature.auth.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iq5.R;
import com.example.iq5.core.navigation.NavigationHelper;
import com.example.iq5.feature.auth.data.AuthRepository;
import com.example.iq5.feature.auth.model.HomeResponse;
import com.example.iq5.feature.specialmode.ui.CustomQuizFragment;
import com.example.iq5.feature.specialmode.ui.WrongHistoryFragment;

public class HomeActivity extends AppCompatActivity {

    private TextView tvWelcome;
    private RecyclerView rvQuizzes;
    private ImageView imgAvatarHome;

    private AuthRepository authRepository;
    private HomeResponse homeData;

    // Container để show Fragment (nếu layout có fragment_container thì dùng, không thì dùng root)
    private int fragmentContainerId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initViews();
        initRepository();
        loadHomeData();

        fragmentContainerId = getFragmentContainerId();

        setupBottomNavigation();
        setupHeaderActions();
        setupAdditionalNavigation();
        setupFindFriendAction();
        setupBackHandler();
    }

    // ---------------- INIT ----------------

    private void initViews() {
        tvWelcome = findViewById(R.id.tvWelcome);
        rvQuizzes = findViewById(R.id.rvQuizzes);
        imgAvatarHome = findViewById(R.id.imgAvatarHome);

        if (rvQuizzes != null) {
            rvQuizzes.setLayoutManager(
                    new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            );
        }
    }

    private void initRepository() {
        authRepository = new AuthRepository(this);
    }

    private void loadHomeData() {
        homeData = authRepository.getHomeData();

        if (homeData == null) {
            Toast.makeText(this, "Không tải được dữ liệu trang chủ", Toast.LENGTH_SHORT).show();
            return;
        }

        if (tvWelcome != null) {
            tvWelcome.setText(
                    homeData.welcomeMessage != null
                            ? homeData.welcomeMessage
                            : getString(R.string.app_name)
            );
        }

        if (rvQuizzes != null
                && homeData.featuredQuizzes != null
                && !homeData.featuredQuizzes.isEmpty()) {

            QuizAdapter adapter = new QuizAdapter(homeData.featuredQuizzes);
            rvQuizzes.setAdapter(adapter);
        } else {
            Toast.makeText(this, "Chưa có quiz nổi bật để hiển thị", Toast.LENGTH_SHORT).show();
        }
    }

    private int getFragmentContainerId() {
        if (findViewById(R.id.fragment_container) != null) {
            return R.id.fragment_container;
        }
        return android.R.id.content;
    }

    // ---------------- BACK HANDLER (gesture + nút) ----------------

    private void setupBackHandler() {
        getOnBackPressedDispatcher().addCallback(this,
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        // Nếu đang có fragment trên back stack → pop & quay lại Home
                        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                            getSupportFragmentManager().popBackStack();
                            showHomeContent();
                        } else {
                            // Không có fragment → thoát Activity
                            setEnabled(false);
                            HomeActivity.super.onBackPressed();
                        }
                    }
                });
    }

    // ---------------- BOTTOM NAV ----------------

    private void setupBottomNavigation() {
        // Home → quay lại màn Home, ẩn Fragment nếu có
        View btnHome = findViewById(R.id.btnHome);
        if (btnHome != null) {
            btnHome.setOnClickListener(v -> showHomeContent());
        }

        // Library → mở WrongHistoryFragment (ôn tập câu sai)
        View btnLibrary = findViewById(R.id.btnLibrary);
        if (btnLibrary != null) {
            btnLibrary.setOnClickListener(v ->
                    showFragment(new WrongHistoryFragment(), "WrongHistory")
            );
        }

        // Create → mở CustomQuizFragment (tạo bộ quiz riêng)
        View btnCreate = findViewById(R.id.btnCreate);
        if (btnCreate != null) {
            btnCreate.setOnClickListener(v ->
                    showFragment(new CustomQuizFragment(), "CustomQuiz")
            );
        }

        // Join → tới Multiplayer (tìm trận PvP)
        View btnJoin = findViewById(R.id.btnJoin);
        if (btnJoin != null) {
            btnJoin.setOnClickListener(v ->
                    NavigationHelper.navigateToFindMatch(this)
            );
        }

        // Profile → ProfileActivity
        View btnProfile = findViewById(R.id.btnProfile);
        if (btnProfile != null) {
            btnProfile.setOnClickListener(v ->
                    NavigationHelper.navigateToProfile(this)
            );
        }
    }

    // ---------------- HIỂN THỊ FRAGMENT / HOME ----------------

    private void showFragment(Fragment fragment, String tag) {
        hideHomeContent();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(fragmentContainerId, fragment, tag);
        transaction.addToBackStack(tag);
        transaction.commit();
    }

    private void showHomeContent() {
        // Xoá toàn bộ fragment trên back stack
        getSupportFragmentManager().popBackStack(
                null,
                androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
        );

        if (tvWelcome != null) tvWelcome.setVisibility(View.VISIBLE);
        if (rvQuizzes != null) rvQuizzes.setVisibility(View.VISIBLE);
    }

    private void hideHomeContent() {
        if (tvWelcome != null) tvWelcome.setVisibility(View.GONE);
        if (rvQuizzes != null) rvQuizzes.setVisibility(View.GONE);
    }

    // ---------------- HEADER & NAV PHỤ ----------------

    private void setupHeaderActions() {
        // Settings
        View btnSettings = findViewById(R.id.btnSettings);
        if (btnSettings != null) {
            btnSettings.setOnClickListener(v ->
                    NavigationHelper.navigateToSettings(this)
            );
        }

        // Search
        View btnSearch = findViewById(R.id.btnSearch);
        if (btnSearch != null) {
            btnSearch.setOnClickListener(v ->
                    Toast.makeText(this, "Search coming soon!", Toast.LENGTH_SHORT).show()
            );
        }

        // Avatar → Profile
        if (imgAvatarHome != null) {
            imgAvatarHome.setOnClickListener(v ->
                    NavigationHelper.navigateToProfile(this)
            );
        }
    }

    private void setupAdditionalNavigation() {
        // Daily reward
        View btnDailyReward = findViewById(R.id.btnDailyReward);
        if (btnDailyReward != null) {
            btnDailyReward.setOnClickListener(v ->
                    NavigationHelper.navigateToDailyReward(this)
            );
        }

        // Achievement
        View btnAchievement = findViewById(R.id.btnAchievement);
        if (btnAchievement != null) {
            btnAchievement.setOnClickListener(v ->
                    NavigationHelper.navigateToAchievement(this)
            );
        }

        // Stats
        View btnStats = findViewById(R.id.btnStats);
        if (btnStats != null) {
            btnStats.setOnClickListener(v ->
                    NavigationHelper.navigateToStats(this)
            );
        }

        // Streak
        View btnStreak = findViewById(R.id.btnStreak);
        if (btnStreak != null) {
            btnStreak.setOnClickListener(v ->
                    NavigationHelper.navigateToStreak(this)
            );
        }

        // Multiplayer shortcut
        View btnMultiplayer = findViewById(R.id.btnMultiplayer);
        if (btnMultiplayer != null) {
            btnMultiplayer.setOnClickListener(v ->
                    NavigationHelper.navigateToFindMatch(this)
            );
        }

        // ĐẤU TRƯỜNG (Arena) → giống chọn category quiz
        View btnArena = findViewById(R.id.btnArena);
        if (btnArena != null) {
            btnArena.setOnClickListener(v ->
                    NavigationHelper.navigateToSelectCategory(this)
            );
        }
    }

    // Banner “Tìm bạn”
    private void setupFindFriendAction() {
        View btnFindFriend = findViewById(R.id.btnFindFriend);
        if (btnFindFriend != null) {
            btnFindFriend.setOnClickListener(v ->
                    NavigationHelper.navigateToFriends(this)
            );
        }
    }
}
