package com.example.android.ui.findmatch;

public class PhongChoi {
        private String tenPhong;
        private String maPhong;
        private String loaiGame;
        private int soLuongChoi;
        private int soLuongHienTai;
        private String trangThai;

        public PhongChoi(String tenPhong, String maPhong, String loaiGame, int soLuongChoi, int soLuongHienTai, String trangThai) {
            this.tenPhong = tenPhong;
            this.maPhong = maPhong;
            this.loaiGame = loaiGame;
            this.soLuongChoi = soLuongChoi;
            this.soLuongHienTai = soLuongHienTai;
            this.trangThai = trangThai;
        }

        public String getTenPhong() {
            return tenPhong;
        }

        public void setTenPhong(String tenPhong) {
            this.tenPhong = tenPhong;
        }

        public String getMaPhong() {
            return maPhong;
        }

        public void setMaPhong(String maPhong) {
            this.maPhong = maPhong;
        }

        public String getLoaiGame() {
            return loaiGame;
        }

        public void setLoaiGame(String loaiGame) {
            this.loaiGame = loaiGame;
        }

        public int getSoLuongChoi() {
            return soLuongChoi;
        }

        public void setSoLuongChoi(int soLuongChoi) {
            this.soLuongChoi = soLuongChoi;
        }

        public int getSoLuongHienTai() {
            return soLuongHienTai;
        }

        public void setSoLuongHienTai(int soLuongHienTai) {
            this.soLuongHienTai = soLuongHienTai;
        }

        public String getTrangThai() {
            return trangThai;
        }

        public void setTrangThai(String trangThai) {
            this.trangThai = trangThai;
        }

}
