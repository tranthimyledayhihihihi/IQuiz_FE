package com.example.iq5.feature.auth.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iq5.R;
import com.example.iq5.core.navigation.NavigationHelper;
import com.example.iq5.feature.auth.data.AuthRepository;
import com.example.iq5.feature.auth.model.HomeResponse;
import com.example.iq5.feature.auth.ui.QuizAdapter;

public class HomeActivity extends AppCompatActivity {

    private TextView tvWelcome;
    private RecyclerView rvQuizzes;
    private ImageView imgAvatarHome;

    private AuthRepository authRepository;
    private HomeResponse homeData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initViews();
        initRepository();
        loadHomeData();
        setupNavigation();
        setupFindFriendAction();
    }

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
            tvWelcome.setText(homeData.welcomeMessage != null
                    ? homeData.welcomeMessage
                    : getString(R.string.app_name));
        }

        if (rvQuizzes != null && homeData.featuredQuizzes != null && !homeData.featuredQuizzes.isEmpty()) {
            QuizAdapter adapter = new QuizAdapter(homeData.featuredQuizzes);
            rvQuizzes.setAdapter(adapter);
        } else {
            Toast.makeText(this, "Chưa có quiz nổi bật để hiển thị", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupNavigation() {
        // NÚT BOTTOM NAV (BTNHOME, BTNLIBRARY, BTNJOIN, BTNCREATE, BTNPROFILE)

        // Home (Đang ở Home, không làm gì)
        View btnHome = findViewById(R.id.btnHome);
        if (btnHome != null) {
            btnHome.setOnClickListener(v -> {});
        }

        // Library / Play Quiz
        View btnLibrary = findViewById(R.id.btnLibrary);
        if (btnLibrary != null) {
            btnLibrary.setOnClickListener(v -> {
                NavigationHelper.navigateToSelectCategory(this);
            });
        }

        // Nút Join (Giả định chuyển đến màn Multiplayer)
        View btnJoin = findViewById(R.id.btnJoin);
        if (btnJoin != null) {
            btnJoin.setOnClickListener(v -> {
                NavigationHelper.navigateToFindMatch(this);
            });
        }

        // Nút Create (Giả định chuyển đến màn Custom Quiz)
        View btnCreate = findViewById(R.id.btnCreate);
        if (btnCreate != null) {
            btnCreate.setOnClickListener(v -> {
                // Giả định chuyển đến màn Custom Quiz cho đơn giản
                Toast.makeText(this, "Chuyển đến màn Tạo Quiz Custom", Toast.LENGTH_SHORT).show();
            });
        }

        // Profile (Bottom Navigation)
        View btnProfileNav = findViewById(R.id.btnProfile);
        if (btnProfileNav != null) {
            btnProfileNav.setOnClickListener(v -> {
                NavigationHelper.navigateToProfile(this);
            });
        }

        // HEADER ACTIONS (Settings/Search)
        View btnSettings = findViewById(R.id.btnSettings);
        if (btnSettings != null) {
            btnSettings.setOnClickListener(v -> {
                NavigationHelper.navigateToSettings(this);
            });
        }

        View btnSearch = findViewById(R.id.btnSearch);
        if (btnSearch != null) {
            btnSearch.setOnClickListener(v -> {
                Toast.makeText(this, "Search coming soon!", Toast.LENGTH_SHORT).show();
            });
        }

        // AVATAR CLICK (Trỏ đến Profile)
        if (imgAvatarHome != null) {
            imgAvatarHome.setOnClickListener(v -> {
                NavigationHelper.navigateToProfile(this);
            });
        }

        // NAVIGATION PHỤ (Đảm bảo ID có trong XML)
        if (findViewById(R.id.btnDailyReward) != null) {
            findViewById(R.id.btnDailyReward).setOnClickListener(v -> {
                NavigationHelper.navigateToDailyReward(this);
            });
        }

        if (findViewById(R.id.btnAchievement) != null) {
            findViewById(R.id.btnAchievement).setOnClickListener(v -> {
                NavigationHelper.navigateToAchievement(this);
            });
        }

        if (findViewById(R.id.btnStats) != null) {
            findViewById(R.id.btnStats).setOnClickListener(v -> {
                NavigationHelper.navigateToStats(this);
            });
        }

        if (findViewById(R.id.btnStreak) != null) {
            findViewById(R.id.btnStreak).setOnClickListener(v -> {
                NavigationHelper.navigateToStreak(this);
            });
        }

        if (findViewById(R.id.btnMultiplayer) != null) {
            findViewById(R.id.btnMultiplayer).setOnClickListener(v -> {
                NavigationHelper.navigateToFindMatch(this);
            });
        }

        // THÊM LOGIC CHO NÚT ĐẤU TRƯỜNG MỚI
        if (findViewById(R.id.btnArena) != null) {
            findViewById(R.id.btnArena).setOnClickListener(v -> {
                // Chuyển đến SelectCategoryActivity (giống nút Library)
                NavigationHelper.navigateToSelectCategory(this);
            });
        }
    }

    // Thêm logic cho nút Tìm bạn trên banner
    private void setupFindFriendAction() {
        View btnFindFriend = findViewById(R.id.btnFindFriend);
        if (btnFindFriend != null) {
            btnFindFriend.setOnClickListener(v -> {
                NavigationHelper.navigateToFriends(this); // Giả định trỏ đến FriendsActivity
            });
        }
    }
}