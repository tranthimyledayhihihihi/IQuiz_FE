
1. Cấu trúc thư mục multiplayer (CHƠI ONLINE & MẠNG XÃ HỘI)
com.example.iq5
└── feature
    └── multiplayer
        ├── adapter
        │   ├── FriendsAdapter.java
        │   ├── LeaderboardAdapter.java
        │   └── PlayerInLobbyAdapter.java (Cho sảnh chờ)
        ├── data
        │   ├── FriendRepository.java
        │   ├── LeaderboardRepository.java
        │   └── MultiplayerRepository.java
        ├── model
        │   ├── Friend.java
        │   ├── LeaderboardEntry.java
        │   ├── PvpMatch.java
        │   ├── PvpRoom.java
        │   └── User.java (Model người dùng cơ bản)
        └── ui
            ├── CompareResultActivity.java
            ├── FriendsLeaderboardActivity.java (Gộp Bạn bè & BXH)
            ├── FriendsFragment.java
            ├── LeaderboardFragment.java
            ├── FindMatchActivity.java
            ├── PvpBattleActivity.java
            └── RoomLobbyActivity.java
