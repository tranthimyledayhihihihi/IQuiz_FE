package com.example.android.ui.lobby;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.R;

import java.util.ArrayList;
import java.util.List;
public class NguoiChoiAdapter extends RecyclerView.Adapter<NguoiChoiAdapter.NguoiChoiViewHolder> {

        private List<NguoiChoi> nguoiChoiList;
        private OnNguoiChoiClickListener listener;

        public interface OnNguoiChoiClickListener {
            void onNguoiChoiClick(NguoiChoi nguoiChoi);
        }

        public NguoiChoiAdapter(OnNguoiChoiClickListener listener) {
            this.nguoiChoiList = new ArrayList<>();
            this.listener = listener;
        }

        @NonNull
        @Override
        public NguoiChoiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_nguoi_choi, parent, false);
            return new NguoiChoiViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull NguoiChoiViewHolder holder, int position) {
            NguoiChoi nguoiChoi = nguoiChoiList.get(position);
            holder.bind(nguoiChoi, listener);
        }

        @Override
        public int getItemCount() {
            return nguoiChoiList.size();
        }

        public void updateData(List<NguoiChoi> newNguoiChoiList) {
            this.nguoiChoiList.clear();
            this.nguoiChoiList.addAll(newNguoiChoiList);
            notifyDataSetChanged();
        }

        static class NguoiChoiViewHolder extends RecyclerView.ViewHolder {
            private TextView tvTenNguoiChoi;
            private TextView tvTrangThai;
            private TextView tvChuPhong;

            public NguoiChoiViewHolder(@NonNull View itemView) {
                super(itemView);
                tvTenNguoiChoi = itemView.findViewById(R.id.tvTenNguoiChoi);
                tvTrangThai = itemView.findViewById(R.id.tvTrangThai);
                tvChuPhong = itemView.findViewById(R.id.tvChuPhong);
            }

            public void bind(NguoiChoi nguoiChoi, OnNguoiChoiClickListener listener) {
                tvTenNguoiChoi.setText(nguoiChoi.getTen());

                String trangThaiText = "Trực tuyến";
                if ("ONLINE".equals(nguoiChoi.getTrangThai())) {
                    trangThaiText = "Trực tuyến";
                } else if ("OFFLINE".equals(nguoiChoi.getTrangThai())) {
                    trangThaiText = "Ngoại tuyến";
                }
                tvTrangThai.setText(trangThaiText);

                tvChuPhong.setVisibility(nguoiChoi.isChuPhong() ? View.VISIBLE : View.GONE);

                itemView.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onNguoiChoiClick(nguoiChoi);
                    }
                });
            }
        }
    }

