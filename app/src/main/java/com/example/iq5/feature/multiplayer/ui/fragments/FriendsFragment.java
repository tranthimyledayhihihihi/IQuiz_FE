package com.example.iq5.feature.multiplayer.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iq5.R;
import com.example.iq5.feature.multiplayer.adapter.FriendsAdapter;
import com.example.iq5.feature.multiplayer.model.Friend;

public class FriendsFragment extends Fragment implements FriendsAdapter.OnFriendInteractionListener {

    private RecyclerView rvFriendsList;
    private FriendsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        rvFriendsList = view.findViewById(R.id.rvFriendsList_fragment);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        adapter = new FriendsAdapter(this);
        rvFriendsList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvFriendsList.setAdapter(adapter);
    }

    @Override
    public void onChallengeClick(Friend friend) {
        Toast.makeText(getContext(), "Thách đấu " + friend.getTenNguoiBan(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAcceptClick(Friend friend) {
        Toast.makeText(getContext(), "Chấp nhận " + friend.getTenNguoiBan(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeclineClick(Friend friend) {
        Toast.makeText(getContext(), "Từ chối " + friend.getTenNguoiBan(), Toast.LENGTH_SHORT).show();
    }
}
