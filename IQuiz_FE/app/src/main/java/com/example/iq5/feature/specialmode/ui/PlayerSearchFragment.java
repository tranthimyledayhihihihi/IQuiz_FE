package com.example.iq5.feature.specialmode.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iq5.R;
import com.example.iq5.feature.quiz.ui.QuizActivity;
import com.example.iq5.feature.specialmode.adapter.PlayerAdapter;
import com.example.iq5.feature.specialmode.data.SpecialModeRepository;
import com.example.iq5.feature.specialmode.model.PlayerItem;
import com.example.iq5.feature.specialmode.model.PlayerSearchResponse;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class PlayerSearchFragment extends Fragment {

    private SpecialModeRepository repository;
    private PlayerAdapter adapter;
    private final List<PlayerItem> fullList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_player_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        repository = new SpecialModeRepository(requireContext());

        EditText etSearch = view.findViewById(R.id.et_search_player);
        RecyclerView rvPlayers = view.findViewById(R.id.rv_players);
        TabLayout tabs = view.findViewById(R.id.tab_filter_players);

        tabs.addTab(tabs.newTab().setText("Tất cả"));
        tabs.addTab(tabs.newTab().setText("Bạn bè"));
        tabs.addTab(tabs.newTab().setText("Đang online"));

        adapter = new PlayerAdapter(new PlayerAdapter.OnPlayerClickListener() {
            @Override
            public void onFriendAction(PlayerItem item) {
                if (item == null) return;
                String status = item.friendStatus != null ? item.friendStatus : "NONE";

                switch (status) {
                    case "NONE":
                        item.friendStatus = "REQUESTED";
                        Snackbar.make(view, "Đã gửi lời mời kết bạn tới " + safe(item.displayName),
                                Snackbar.LENGTH_SHORT).show();
                        break;
                    case "REQUESTED":
                        item.friendStatus = "NONE";
                        Snackbar.make(view, "Đã hủy lời mời kết bạn với " + safe(item.displayName),
                                Snackbar.LENGTH_SHORT).show();
                        break;
                    case "FRIEND":
                        Snackbar.make(view, "Bạn đã là bạn với " + safe(item.displayName),
                                Snackbar.LENGTH_SHORT).show();
                        break;
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChallenge(PlayerItem item) {
                if (item == null) return;
                if (!"FRIEND".equals(item.friendStatus)) {
                    Snackbar.make(view,
                            "Chỉ có thể thách đấu với người trong danh sách bạn bè",
                            Snackbar.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(requireContext(), QuizActivity.class);
                intent.putExtra("ENTRY_SOURCE", "friend_challenge");
                intent.putExtra("OPPONENT_ID", item.userId);
                intent.putExtra("OPPONENT_NAME", item.displayName);
                startActivity(intent);
            }
        });
        rvPlayers.setAdapter(adapter);

        PlayerSearchResponse data = repository.searchPlayers("");
        if (data != null && data.items != null) {
            fullList.clear();
            fullList.addAll(data.items);
            filterAndShow(view, "", 0);
        }

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                int tabIndex = tabs.getSelectedTabPosition();
                filterAndShow(view, s.toString(), tabIndex);
            }
        });

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int tabIndex = tab.getPosition();
                String query = etSearch.getText().toString();
                filterAndShow(view, query, tabIndex);
            }

            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {
                int tabIndex = tab.getPosition();
                String query = etSearch.getText().toString();
                filterAndShow(view, query, tabIndex);
            }
        });
    }

    private void filterAndShow(View rootView, String query, int tabIndex) {
        String q = query == null ? "" : query.toLowerCase().trim();
        List<PlayerItem> filtered = new ArrayList<>();

        for (PlayerItem item : fullList) {
            if (item == null) continue;

            boolean ok = true;

            switch (tabIndex) {
                case 1: // Bạn bè
                    ok = "FRIEND".equals(item.friendStatus);
                    break;
                case 2: // Đang online
                    ok = item.isOnline;
                    break;
                default:
                    ok = true;
                    break;
            }

            if (ok && !q.isEmpty()) {
                String name = item.displayName != null ? item.displayName.toLowerCase() : "";
                String username = item.username != null ? item.username.toLowerCase() : "";
                boolean match = name.contains(q) || username.contains(q);
                if (!match) ok = false;
            }

            if (ok) filtered.add(item);
        }

        adapter.submitList(filtered);
    }

    private String safe(String s) {
        return s == null ? "" : s;
    }
}
