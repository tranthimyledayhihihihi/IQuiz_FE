package com.example.iq5.feature.auth.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iq5.R;
import com.example.iq5.core.navigation.NavigationHelper;
import com.example.iq5.feature.auth.data.AuthRepository;
import com.example.iq5.feature.auth.model.HomeResponse;
import com.example.iq5.feature.auth.ui.QuizAdapter;
import com.example.iq5.feature.specialmode.ui.CustomQuizFragment;
import com.example.iq5.feature.specialmode.ui.WrongHistoryFragment;

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
        // ✅ GỌI API THAY VÌ ĐỌC JSON
        authRepository.getHomeDataAsync(new AuthRepository.HomeDataCallback() {
            @Override
            public void onSuccess(HomeResponse data) {
                // Cập nhật UI trên main thread
                runOnUiThread(() -> {
                    homeData = data;
                    
                    if (homeData == null) {
                        Toast.makeText(HomeActivity.this, "Không tải được dữ liệu trang chủ", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(HomeActivity.this, "✅ Đã tải từ API!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(HomeActivity.this, "Chưa có quiz nổi bật để hiển thị", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
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
            btnLibrary.setOnClickListener(v ->
                    showFragment(new WrongHistoryFragment(), "WrongHistory")
            );
        }

        // Nút Join (Giả định chuyển đến màn Multiplayer)
        View btnJoin = findViewById(R.id.btnJoin);
        if (btnJoin != null) {
            btnJoin.setOnClickListener(v -> {
                NavigationHelper.navigateToFindMatch(this);
            });
        }

        // Nút Create - HIỂN THỊ CUSTOMQUIZFRAGMENT
        View btnCreate = findViewById(R.id.btnCreate);
        if (btnCreate != null) {
            btnCreate.setOnClickListener(v -> {
                showFragment(new CustomQuizFragment(), "CustomQuiz");
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
                NavigationHelper.navigateToSelectCategory(this);
            });
        }
    }

    // Thêm logic cho nút Tìm bạn trên banner
    private void setupFindFriendAction() {
        View btnFindFriend = findViewById(R.id.btnFindFriend);
        if (btnFindFriend != null) {
            btnFindFriend.setOnClickListener(v -> {
                NavigationHelper.navigateToFriends(this);
            });
        }
    }
    // ======== THÊM METHOD showFragment() ========
    private void showFragment(androidx.fragment.app.Fragment fragment, String tag) {
        // Ẩn Home Content
        View homeContent = findViewById(R.id.home_content);
        if (homeContent != null) {
            homeContent.setVisibility(View.GONE);
        }

        // Hiển thị Fragment Container
        View fragmentContainer = findViewById(R.id.fragment_container);
        if (fragmentContainer != null) {
            fragmentContainer.setVisibility(View.VISIBLE);
        }
        // Load Fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment, tag);
        transaction.addToBackStack(tag);
        transaction.commit();
    }


}