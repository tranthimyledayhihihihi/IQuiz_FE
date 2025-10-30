package com.example.android.ui.friends;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.tabs.TabLayout;
import com.example.android.R;

public class FriendsActivity extends AppCompatActivity {

    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        initViews();
        setupTabs();
        showFragment(new FriendsListFragment());
    }

    private void initViews() {
        tabLayout = findViewById(R.id.tabLayout);
    }

    private void setupTabs() {
        tabLayout.addTab(tabLayout.newTab().setText("Bạn Bè"));
        tabLayout.addTab(tabLayout.newTab().setText("Lời Mời"));
        tabLayout.addTab(tabLayout.newTab().setText("Bảng XH"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragment = null;
                switch (tab.getPosition()) {
                    case 0:
                        fragment = new FriendsListFragment();
                        break;
                    case 1:
                        fragment = new FriendRequestsFragment();
                        break;
                    case 2:
                        fragment = new LeaderboardFragment();
                        break;
                }
                if (fragment != null) {
                    showFragment(fragment);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void showFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.commit();
    }

    // 🟢 Thêm 2 hàm này để khớp với XML
    public void onBackClick(View view) {
        finish(); // Quay lại màn hình trước
    }

    public void onAddFriendClick(View view) {
        Toast.makeText(this, "Thêm bạn mới!", Toast.LENGTH_SHORT).show();
        // Có thể mở Fragment hoặc Activity khác tại đây
    }
}
