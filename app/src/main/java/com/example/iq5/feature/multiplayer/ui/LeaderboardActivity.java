package com.example.iq5.feature.multiplayer.ui;

import android.os.Bundle;
import android.widget.ImageButton; // Import ImageButton
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
    private ImageButton btnBack; // Khai báo ImageButton

    // Tiêu đề cho các tab
    private final String[] tabTitles = new String[]{"Bạn Bè", "BXH Tuần", "BXH Tháng"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_leaderboard);

        // Ẩn ActionBar mặc định (vì chúng ta dùng Toolbar tùy chỉnh)
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tabs);
        fabAddFriend = findViewById(R.id.fabAddFriend);
        btnBack = findViewById(R.id.btnBack); // Lấy tham chiếu đến nút Quay lại

        // Xử lý sự kiện cho nút Quay lại
        btnBack.setOnClickListener(v -> {
            // Hàm này sẽ đóng Activity hiện tại và quay về Activity trước đó (ví dụ: MainActivity)
            finish();
        });

        // Setup ViewPager và Adapter
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

        // Liên kết TabLayout với ViewPager
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

        fabAddFriend.setOnClickListener(v -> {
            // TODO: Hiển thị Dialog/Activity để tìm và thêm bạn
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
                    // Trả về LeaderboardFragment với loại "Tuần"
                    return LeaderboardFragment.newInstance("week");
                case 2:
                    // Trả về LeaderboardFragment với loại "Tháng"
                    return LeaderboardFragment.newInstance("month");
                case 0:
                default:
                    // Trả về FriendsFragment
                    return new FriendsFragment();
            }
        }

        @Override
        public int getItemCount() {
            return 3; // 3 tabs
        }
    }
}