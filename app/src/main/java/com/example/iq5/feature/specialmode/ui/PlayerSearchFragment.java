package com.example.iq5.feature.specialmode.ui;

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
import com.example.iq5.feature.specialmode.adapter.PlayerAdapter;
import com.example.iq5.feature.specialmode.data.SpecialModeRepository;
import com.example.iq5.feature.specialmode.model.PlayerItem;
import com.example.iq5.feature.specialmode.model.PlayerSearchResponse;
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
        RecyclerView rv = view.findViewById(R.id.rv_players);
        TabLayout tabs = view.findViewById(R.id.tab_filter_players);

        tabs.addTab(tabs.newTab().setText("Tất cả"));
        tabs.addTab(tabs.newTab().setText("Bạn bè"));
        tabs.addTab(tabs.newTab().setText("Đang online"));

        adapter = new PlayerAdapter(item -> {
            // TODO: gửi lời mời kết bạn hoặc mở profile chi tiết
        });
        rv.setAdapter(adapter);

        PlayerSearchResponse data = repository.searchPlayers("");
        if (data != null && data.items != null) {
            fullList.clear();
            fullList.addAll(data.items);
            adapter.submitList(new ArrayList<>(fullList));
        }

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                filterAndShow(s.toString(), tabs.getSelectedTabPosition());
            }
        });

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override public void onTabSelected(TabLayout.Tab tab) {
                filterAndShow(etSearch.getText().toString(), tab.getPosition());
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {
                filterAndShow(etSearch.getText().toString(), tab.getPosition());
            }
        });
    }

    private void filterAndShow(String query, int tabIndex) {
        String q = query.toLowerCase().trim();
        List<PlayerItem> filtered = new ArrayList<>();
        for (PlayerItem item : fullList) {
            boolean ok = true;

            // filter theo tab
            if (tabIndex == 1 && !"friends".equals(item.friendStatus)) {
                ok = false;
            } else if (tabIndex == 2 && !item.isOnline) {
                ok = false;
            }

            // filter theo text
            if (ok && !q.isEmpty()) {
                boolean match = (item.displayName != null && item.displayName.toLowerCase().contains(q))
                        || (item.username != null && item.username.toLowerCase().contains(q));
                if (!match) ok = false;
            }

            if (ok) filtered.add(item);
        }
        adapter.submitList(filtered);
    }
}
