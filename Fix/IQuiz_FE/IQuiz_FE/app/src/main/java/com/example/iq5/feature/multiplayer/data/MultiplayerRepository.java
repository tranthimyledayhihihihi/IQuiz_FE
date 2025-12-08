package com.example.iq5.feature.multiplayer.data;

import androidx.lifecycle.LiveData;
import com.example.iq5.feature.multiplayer.model.PvPMatch;
import com.example.iq5.feature.multiplayer.model.PvPRoom;

/**
 * Interface cho kho dữ liệu Chơi Online (Multiplayer)
 */
public interface MultiplayerRepository {

    // Tìm trận nhanh
    LiveData<PvPMatch> findRandomMatch(int userID);

    // Tạo phòng
    LiveData<PvPRoom> createRoom(int userID, String tenPhong);

    // Tham gia phòng
    LiveData<PvPRoom> joinRoom(String maPhong, int userID);

    // Lấy thông tin phòng (để lắng nghe)
    LiveData<PvPRoom> getRoomDetails(int phongID);

    // Lấy thông tin trận đấu (để lắng nghe)
    LiveData<PvPMatch> getMatchDetails(int tranDauID);
}