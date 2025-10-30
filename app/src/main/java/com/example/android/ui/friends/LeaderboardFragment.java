package com.example.android.ui.friends;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.R;

import java.util.ArrayList;
import java.util.List;


    public class LeaderboardFragment extends Fragment {

        private RecyclerView recyclerViewLeaderboard;
        private LeaderboardAdapter leaderboardAdapter;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);

            initViews(view);
            setupRecyclerView();
            loadLeaderboard();

            return view;
        }

        private void initViews(View view) {
            recyclerViewLeaderboard = view.findViewById(R.id.recyclerViewLeaderboard);
        }

        private void setupRecyclerView() {
            leaderboardAdapter = new LeaderboardAdapter(new LeaderboardAdapter.OnPlayerClickListener() {
                @Override
                public void onPlayerClick(LeaderboardPlayer player) {
                    Toast.makeText(getContext(), "Click vào " + player.getName(), Toast.LENGTH_SHORT).show();
                }
            });
            recyclerViewLeaderboard.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerViewLeaderboard.setAdapter(leaderboardAdapter);
        }

        private void loadLeaderboard() {
            // TODO: Gọi API lấy bảng xếp hạng
            List<LeaderboardPlayer> players = getSampleLeaderboardData();
            leaderboardAdapter.updateData(players);
        }

        private List<LeaderboardPlayer> getSampleLeaderboardData() {
            List<LeaderboardPlayer> players = new ArrayList<>();
            players.add(new LeaderboardPlayer("Người chơi 1", 1, 2500));
            players.add(new LeaderboardPlayer("Người chơi 2", 2, 2300));
            players.add(new LeaderboardPlayer("Người chơi 3", 3, 2100));
            players.add(new LeaderboardPlayer("Người chơi 4", 4, 1900));
            players.add(new LeaderboardPlayer("Người chơi 5", 5, 1700));
            return players;
        }
    }

