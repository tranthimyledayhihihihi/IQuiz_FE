package com.example.android.ui.lobby;

public class NguoiChoi {
        private String ten;
        private String trangThai;
        private boolean isChuPhong;

        public NguoiChoi(String ten, String trangThai, boolean isChuPhong) {
            this.ten = ten;
            this.trangThai = trangThai;
            this.isChuPhong = isChuPhong;
        }

        public String getTen() {
            return ten;
        }

        public void setTen(String ten) {
            this.ten = ten;
        }

        public String getTrangThai() {
            return trangThai;
        }

        public void setTrangThai(String trangThai) {
            this.trangThai = trangThai;
        }

        public boolean isChuPhong() {
            return isChuPhong;
        }

        public void setChuPhong(boolean chuPhong) {
            isChuPhong = chuPhong;
        }
    }

