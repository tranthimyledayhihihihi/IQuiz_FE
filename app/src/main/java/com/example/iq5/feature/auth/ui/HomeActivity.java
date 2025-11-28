package com.example.iq5.feature.auth.ui;

import android.os.Bundle;
import android.view.View; // THÊM
import android.widget.Toast;
import android.widget.TextView;
import android.widget.ImageView;

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
import com.example.iq5.feature.specialmode.ui.WrongHistoryFragment;
import com.example.iq5.feature.specialmode.ui.CustomQuizFragment;
import com.example.iq5.feature.auth.ui.QuizAdapter; // THÊM

public class HomeActivity extends AppCompatActivity {
    private TextView tvWelcome;
    private RecyclerView rvQuizzes; //RecyclerView để hiển thị danh sách cuộn
    private AuthRepository authRepository; //đối tượng chịu trách nhiệm lấy dữ liệu
    private HomeResponse homeData; // chứa dữ liệu trả về để hiển thị lên màn hình
    private int fragmentContainerId; // Container để hiển thị Fragment
    //vòng đời khởi tạo
    private RecyclerView rvQuizzes;
    private ImageView imgAvatarHome;

    private AuthRepository authRepository;
    private HomeResponse homeData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home); // gắn giao diện XML và file java
        initViews(); //gọi các hàm con để chia nhiệm vụ
        initRepository();
        loadHomeData();
        setupBottomNavigation();
        setupHeaderActions();

        // Tìm fragment container (nếu có trong layout)
        fragmentContainerId = getFragmentContainerId();
        setupNavigation();
        setupFindFriendAction(); // THÊM HÀM XỬ LÝ NÚT TÌM BẠN
    }

    /**
     * Tìm hoặc tạo container cho Fragment
     */
    private int getFragmentContainerId() {
        // Thử tìm container có sẵn trong layout
        if (findViewById(R.id.fragment_container) != null) {
            return R.id.fragment_container;
        }
        // Nếu không có, dùng android.R.id.content (root view)
        return android.R.id.content;
    }
    private void initViews() {
        tvWelcome = findViewById(R.id.tvWelcome); //tìm kiếm các thành trong file xml bằng ID để gắn và biến java
        rvQuizzes = findViewById(R.id.rvQuizzes);
        imgAvatarHome = findViewById(R.id.imgAvatarHome);

        rvQuizzes.setLayoutManager( //cấu hình cho danh sách rvQuizzes hiển thị theo dạng danh sách đọc (VERTICAL)
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        );
        if (rvQuizzes != null) {
            rvQuizzes.setLayoutManager(
                    new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            );
        }
    }

    //xử lý dữ liệu
    private void initRepository() {
        authRepository = new AuthRepository(this);
    }
    private void loadHomeData() {
        //gọi authRepository.getHomeData() để lấy thông tin
        homeData = authRepository.getHomeData();
       //kiểm tra lỗi
        if (homeData == null) { //tránh bị crash khi ko có dữ liệu
            Toast.makeText(this, "Không tải được dữ liệu trang chủ", Toast.LENGTH_SHORT).show();
            return;
        }
        //hiển thị lơì chào
        tvWelcome.setText(homeData.welcomeMessage != null
                ? homeData.welcomeMessage
                : getString(R.string.app_name));
        //hiển thị danh sách Quiz RecyclerView nói ở trên
        if (homeData.featuredQuizzes != null && !homeData.featuredQuizzes.isEmpty()) {
            //tạo 1 class QuizAdapter trung gian xử lý việc đổ dữ liệu vào từng dòng của danh sách

        if (tvWelcome != null) {
            tvWelcome.setText(homeData.welcomeMessage != null
                    ? homeData.welcomeMessage
                    : getString(R.string.app_name));
        }

        if (rvQuizzes != null && homeData.featuredQuizzes != null && !homeData.featuredQuizzes.isEmpty()) {
            QuizAdapter adapter = new QuizAdapter(homeData.featuredQuizzes);
            //gán adapter này cho rvQuizzes
            rvQuizzes.setAdapter(adapter);
        } else {
            Toast.makeText(this, "Chưa có quiz nổi bật để hiển thị", Toast.LENGTH_SHORT).show();
        }
    }

    //thiết lập sự kiện khi ấm vào các nút ở thanh menu dưới cùng
    private void setupBottomNavigation() {
        // Home - Hiển thị lại giao diện Home (ẩn Fragment nếu có)
        findViewById(R.id.btnHome).setOnClickListener(v -> {
            showHomeContent();
        });
    private void setupNavigation() {
        // NÚT BOTTOM NAV (BTNHOME, BTNLIBRARY, BTNJOIN, BTNCREATE, BTNPROFILE)

        // Home (Đang ở Home, không làm gì)
        View btnHome = findViewById(R.id.btnHome);
        if (btnHome != null) {
            btnHome.setOnClickListener(v -> {});
        }

        // Library - Mở WrongHistoryFragment
        findViewById(R.id.btnLibrary).setOnClickListener(v -> {
            showFragment(new WrongHistoryFragment(), "WrongHistory");
        });

        // Create - Mở CustomQuizFragment (nếu có button)
        if (findViewById(R.id.btnCreate) != null) {
            findViewById(R.id.btnCreate).setOnClickListener(v -> {
                showFragment(new CustomQuizFragment(), "CustomQuiz");
            });
        }

        // Join - Mở QuizActivity (nếu có button)
        if (findViewById(R.id.btnJoin) != null) {
            findViewById(R.id.btnJoin).setOnClickListener(v -> {
                NavigationHelper.navigateToSelectCategory(this);
            });
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

        // Profile - Mở ProfileActivity
        findViewById(R.id.btnProfile).setOnClickListener(v -> {
            NavigationHelper.navigateToProfile(this);
        });
    }

    /**
     * Hiển thị Fragment trong Activity
     */
    private void showFragment(Fragment fragment, String tag) {
        // Ẩn nội dung Home
        hideHomeContent();

        // Hiển thị Fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(fragmentContainerId, fragment, tag);
        transaction.addToBackStack(tag);
        transaction.commit();
    }

    /**
     * Hiển thị lại nội dung Home
     */
    private void showHomeContent() {
        // Xóa tất cả Fragment trong back stack
        getSupportFragmentManager().popBackStack(null,
            androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);

        // Hiện lại nội dung Home
        if (tvWelcome != null) tvWelcome.setVisibility(android.view.View.VISIBLE);
        if (rvQuizzes != null) rvQuizzes.setVisibility(android.view.View.VISIBLE);
    }

    /**
     * Ẩn nội dung Home khi hiển thị Fragment
     */
    private void hideHomeContent() {
        if (tvWelcome != null) tvWelcome.setVisibility(android.view.View.GONE);
        if (rvQuizzes != null) rvQuizzes.setVisibility(android.view.View.GONE);
    }

    @Override
    public void onBackPressed() {
        // Nếu có Fragment trong back stack, pop nó ra
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            showHomeContent();
        } else {
            // Nếu không có Fragment, xử lý back bình thường
            super.onBackPressed();
        }
    }
    //sử dụng lớp NavigationHelper để chuyển sang màn khác nhằm tái sử dụng code điều hướng
    private void setupHeaderActions() {
        // Settings button in header
        findViewById(R.id.btnSettings).setOnClickListener(v -> {
            NavigationHelper.navigateToSettings(this);
        });
        // Search button
        findViewById(R.id.btnSearch).setOnClickListener(v -> {
            Toast.makeText(this, "Search coming soon!", Toast.LENGTH_SHORT).show();
        });
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

        // Avatar click -> Profile
        findViewById(R.id.imgAvatarHome).setOnClickListener(v -> {
            NavigationHelper.navigateToProfile(this);
        });
        
        // Thêm các navigation khác (nếu có button trong layout)
        setupAdditionalNavigation();
    }
    
    private void setupAdditionalNavigation() {
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
