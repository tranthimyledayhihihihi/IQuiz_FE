package com.example.android.ui.findmatch;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android.R;

import java.util.ArrayList;
import java.util.List;
public class PhongChoiAdapter extends RecyclerView.Adapter<PhongChoiAdapter.PhongChoiViewHolder> {

        private List<PhongChoi> phongChoiList;
        private OnPhongChoiClickListener listener;

        public interface OnPhongChoiClickListener {
            void onPhongChoiClick(PhongChoi phongChoi);
        }

        public PhongChoiAdapter(OnPhongChoiClickListener listener) {
            this.phongChoiList = new ArrayList<>();
            this.listener = listener;
        }

        @NonNull
        @Override
        public PhongChoiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_phong_choi, parent, false);
            return new PhongChoiViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PhongChoiViewHolder holder, int position) {
            PhongChoi phongChoi = phongChoiList.get(position);
            holder.bind(phongChoi, listener);
        }

        @Override
        public int getItemCount() {
            return phongChoiList.size();
        }

        public void updateData(List<PhongChoi> newPhongChoiList) {
            this.phongChoiList.clear();
            this.phongChoiList.addAll(newPhongChoiList);
            notifyDataSetChanged();
        }

        static class PhongChoiViewHolder extends RecyclerView.ViewHolder {
            private TextView tvTenPhong;
            private TextView tvMaPhong;
            private TextView tvLoaiGame;
            private TextView tvSoLuong;
            private TextView tvTrangThai;

            public PhongChoiViewHolder(@NonNull View itemView) {
                super(itemView);
                tvTenPhong = itemView.findViewById(R.id.tvTenPhong);
                tvMaPhong = itemView.findViewById(R.id.tvMaPhong);
                tvLoaiGame = itemView.findViewById(R.id.tvLoaiGame);
                tvSoLuong = itemView.findViewById(R.id.tvSoLuong);
                tvTrangThai = itemView.findViewById(R.id.tvTrangThai);
            }

            public void bind(PhongChoi phongChoi, OnPhongChoiClickListener listener) {
                tvTenPhong.setText(phongChoi.getTenPhong());
                tvMaPhong.setText("Mã: " + phongChoi.getMaPhong());
                tvLoaiGame.setText(phongChoi.getLoaiGame());
                tvSoLuong.setText(phongChoi.getSoLuongHienTai() + "/" + phongChoi.getSoLuongChoi());

                String trangThaiText = "Chờ người chơi";
                if ("CHO".equals(phongChoi.getTrangThai())) {
                    trangThaiText = "Chờ người chơi";
                } else if ("DANG_CHOI".equals(phongChoi.getTrangThai())) {
                    trangThaiText = "Đang chơi";
                }
                tvTrangThai.setText(trangThaiText);

                itemView.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onPhongChoiClick(phongChoi);
                    }
                });
            }
        }
    }


