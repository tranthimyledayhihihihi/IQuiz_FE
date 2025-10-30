package com.example.android.ui.friends;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.R;

import java.util.ArrayList;
import java.util.List;
    public class FriendRequestAdapter extends RecyclerView.Adapter<FriendRequestAdapter.RequestViewHolder> {

        private List<FriendRequest> requestsList;
        private OnRequestActionListener listener;

        public interface OnRequestActionListener {
            void onAccept(FriendRequest request);
            void onReject(FriendRequest request);
        }

        public FriendRequestAdapter(OnRequestActionListener listener) {
            this.requestsList = new ArrayList<>();
            this.listener = listener;
        }

        @NonNull
        @Override
        public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_friend_request, parent, false);
            return new RequestViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
            FriendRequest request = requestsList.get(position);
            holder.bind(request, listener);
        }

        @Override
        public int getItemCount() {
            return requestsList.size();
        }

        public void updateData(List<FriendRequest> newRequestsList) {
            this.requestsList.clear();
            this.requestsList.addAll(newRequestsList);
            notifyDataSetChanged();
        }

        static class RequestViewHolder extends RecyclerView.ViewHolder {
            private TextView tvRequestName;
            private TextView tvRequestTime;
            private Button btnAccept;
            private Button btnReject;

            public RequestViewHolder(@NonNull View itemView) {
                super(itemView);
                tvRequestName = itemView.findViewById(R.id.tvRequestName);
                tvRequestTime = itemView.findViewById(R.id.tvRequestTime);
                btnAccept = itemView.findViewById(R.id.btnAccept);
                btnReject = itemView.findViewById(R.id.btnReject);
            }

            public void bind(FriendRequest request, OnRequestActionListener listener) {
                tvRequestName.setText(request.getName());
                tvRequestTime.setText(request.getTimeAgo());

                btnAccept.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onAccept(request);
                    }
                });

                btnReject.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onReject(request);
                    }
                });
            }
        }
    }

