package com.example.iq5.feature.multiplayer.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.iq5.R;
import com.example.iq5.feature.multiplayer.adapter.LeaderboardAdapter;

public class LeaderboardFragment extends Fragment {

    private RecyclerView rvLeaderboard;
    private LeaderboardAdapter adapter;
    // TODO: private LeaderboardViewModel viewModel;
    private String leaderboardType = "week"; // "week" or "month"

    // Hàm tạo (để nhận loại BXH)
    public static LeaderboardFragment newInstance(String type) {
        LeaderboardFragment fragment = new LeaderboardFragment();
        Bundle args = new Bundle();
        args.putString("LEADERBOARD_TYPE", type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            leaderboardType = getArguments().getString("LEADERBOARD_TYPE", "week");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false); // Cần tạo layout này
        rvLeaderboard = view.findViewById(R.id.rvLeaderboard_fragment);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // TODO: viewModel = new ViewModelProvider(this).get(LeaderboardViewModel.class);
        setupRecyclerView();
        // TODO: loadData();
    }

    private void setupRecyclerView() {
        adapter = new LeaderboardAdapter();
        adapter.setLeaderboardType(leaderboardType);
        rvLeaderboard.setLayoutManager(new LinearLayoutManager(getContext()));
        rvLeaderboard.setAdapter(adapter);
    }

    // TODO: Cần tạo layout res/layout/fragment_leaderboard.xml với 1 RecyclerView (ID: rvLeaderboard_fragment)
}