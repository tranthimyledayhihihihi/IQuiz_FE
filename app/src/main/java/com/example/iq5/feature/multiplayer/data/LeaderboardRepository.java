package com.example.iq5.feature.multiplayer.data;

import androidx.lifecycle.LiveData;
import com.example.iq5.feature.multiplayer.model.LeaderboardEntry;
import java.util.List;

/**
 * Interface cho kho dữ liệu Bảng xếp hạng (Leaderboard)
 */
public interface LeaderboardRepository {

    // Lấy BXH theo tuần
    LiveData<List<LeaderboardEntry>> getWeeklyLeaderboard();

    // Lấy BXH theo tháng
    LiveData<List<LeaderboardEntry>> getMonthlyLeaderboard();

    // Lấy vị trí của người dùng hiện tại
    LiveData<LeaderboardEntry> getCurrentUserRank(int userID);
}