package com.example.iq5.feature.multiplayer.data;

import androidx.lifecycle.LiveData;
import com.example.iq5.feature.multiplayer.model.Friend;
import java.util.List;

/**
 * Interface cho kho dữ liệu Bạn bè (Friends)
 * Sẽ được implement (ví dụ: FriendRepositoryImpl)
 */
public interface FriendRepository {

    // Lấy danh sách bạn bè của user
    LiveData<List<Friend>> getFriends(int userID);

    // Lấy danh sách yêu cầu kết bạn (đang chờ)
    LiveData<List<Friend>> getFriendRequests(int userID);

    // Gửi lời mời kết bạn
    void sendFriendRequest(int userID1, int userID2);

    // Chấp nhận yêu cầu
    void acceptFriendRequest(int banBeID);

    // Từ chối hoặc hủy kết bạn
    void removeFriend(int banBeID);
}
