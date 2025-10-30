package com.example.android.ui.friends;

public class LeaderboardPlayer {
        private String name;
        private int rank;
        private int points;

        public LeaderboardPlayer(String name, int rank, int points) {
            this.name = name;
            this.rank = rank;
            this.points = points;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getRank() {
            return rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }

        public int getPoints() {
            return points;
        }

        public void setPoints(int points) {
            this.points = points;
        }
    }
