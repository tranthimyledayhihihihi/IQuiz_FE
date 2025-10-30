package com.example.android.ui.friends;

public class Friend {
        private String name;
        private String status;
        private int points;

        public Friend(String name, String status, int points) {
            this.name = name;
            this.status = status;
            this.points = points;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public int getPoints() {
            return points;
        }

        public void setPoints(int points) {
            this.points = points;
        }
    }
