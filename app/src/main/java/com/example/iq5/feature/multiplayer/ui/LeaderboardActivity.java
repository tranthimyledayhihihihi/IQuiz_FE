package com.example.iq5.feature.multiplayer.ui;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.iq5.R;
import com.example.iq5.feature.multiplayer.ui.fragments.FriendsFragment;
import com.example.iq5.feature.multiplayer.ui.fragments.LeaderboardFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class LeaderboardActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private FloatingActionButton fabAddFriend;
    private ImageButton btnBack;

    // Tiêu đề cho các tab
    private final String[] tabTitles = new String[]{"Bạn Bè", "BXH Tuần", "BXH Tháng"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_leaderboard);

        // Ẩn ActionBar mặc định (dùng layout custom)
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        initViews();
        setupBackButton();
        setupViewPagerAndTabs();
        setupFab();
    }

    private void initViews() {
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tabs);
        fabAddFriend = findViewById(R.id.fabAddFriend);
        btnBack = findViewById(R.id.btnBack);
    }

    private void setupBackButton() {
        btnBack.setOnClickListener(v -> finish());
    }

    private void setupViewPagerAndTabs() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(tabTitles[position])
        ).attach();

        // Ẩn/hiện FAB tùy theo tab
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == 0) { // Tab Bạn Bè
                    fabAddFriend.show();
                } else {
                    fabAddFriend.hide();
                }
            }
        });
    }

    private void setupFab() {
        fabAddFriend.setOnClickListener(v -> {
            // TODO: Mở màn hình / dialog tìm & thêm bạn (ví dụ PlayerSearchFragment/Activity)
            // Hiện tại để trống hoặc show Snackbar/Toast nếu muốn
            // Snackbar.make(v, "Tìm & thêm bạn bè (dev...)", Snackbar.LENGTH_SHORT).show();
        });
    }

    /**
     * Adapter cho ViewPager để quản lý các Fragment
     */
    private static class ViewPagerAdapter extends FragmentStateAdapter {
        public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 1:
                    // BXH tuần
                    return LeaderboardFragment.newInstance("week");
                case 2:
                    // BXH tháng
                    return LeaderboardFragment.newInstance("month");
                case 0:
                default:
                    // Tab bạn bè
                    return new FriendsFragment();
            }
        }

        @Override
        public int getItemCount() {
            return 3; // 3 tab
        }
    }
}
