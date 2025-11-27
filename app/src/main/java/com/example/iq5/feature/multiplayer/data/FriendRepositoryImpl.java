package com.example.iq5.feature.multiplayer.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.iq5.feature.multiplayer.model.Friend;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Triển khai Interface FriendRepository sử dụng dữ liệu MOCK (Tạm thời)
 */
public class FriendRepositoryImpl implements FriendRepository {

    // Dữ liệu mẫu (Giả lập việc lưu trữ trong bộ nhớ/DB)
    private static final List<Friend> MOCK_DATA_LIST = createMockFriendList();

    // LiveData để giả lập việc theo dõi thay đổi dữ liệu (Observer Pattern)
    private final MutableLiveData<List<Friend>> liveFriends = new MutableLiveData<>();
    private final MutableLiveData<List<Friend>> liveRequests = new MutableLiveData<>();

    // Constructor: Thiết lập dữ liệu ban đầu
    public FriendRepositoryImpl() {
        // Giả sử user hiện tại có ID là 1
        updateLiveDatas(1);
    }

    /**
     * Hàm tạo danh sách bạn bè mẫu (mock data)
     */
    private static List<Friend> createMockFriendList() {
        List<Friend> friends = new ArrayList<>();

        // --- Dữ liệu MOCK - Giả sử UserID hiện tại là 1 ---
        // Friend(banBeID, tenNguoiBan, anhDaiDienUrl, diemSo, level, isOnline, trangThai)

        // 1. Bạn bè đang Online
        friends.add(new Friend(101, "Nguyễn Thị Thúy Huy", "url_1", 5000, 15, true, "Bạn bè"));
        friends.add(new Friend(102, "Trần Lan", "url_2", 8000, 18, true, "Bạn bè"));

        // 2. Bạn bè đang Offline
        friends.add(new Friend(103, "Hoàng Phát", "url_3", 6500, 12, false, "Bạn bè"));
        friends.add(new Friend(104, "Lê Văn T", "url_4", 12000, 22, false, "Bạn bè"));

        // 3. Yêu cầu kết bạn (Chờ xác nhận, UserID2 = 1)
        Friend request = new Friend(201, "Yêu cầu mới", "url_5", 0, 10, true, "Chờ xác nhận");
        request.setUserID1(2);
        request.setUserID2(1);
        friends.add(request);

        return friends;
    }

    /**
     * Hàm nội bộ để lọc và cập nhật LiveData (Friends và Requests)
     */
    private void updateLiveDatas(int currentUserID) {
        // Lọc danh sách bạn bè (trạng thái "Bạn bè")
        List<Friend> friendsList = MOCK_DATA_LIST.stream()
                .filter(f -> "Bạn bè".equals(f.getTrangThai()))
                .collect(Collectors.toList());
        liveFriends.setValue(friendsList);

        // Lọc danh sách yêu cầu (trạng thái "Chờ xác nhận" và UserID2 là user hiện tại)
        List<Friend> requestsList = MOCK_DATA_LIST.stream()
                .filter(f -> "Chờ xác nhận".equals(f.getTrangThai()) && f.getUserID2() == currentUserID)
                .collect(Collectors.toList());
        liveRequests.setValue(requestsList);
    }

    // --- TRIỂN KHAI CÁC PHƯƠNG THỨC TỪ INTERFACE ---

    @Override
    public LiveData<List<Friend>> getFriends(int userID) {
        updateLiveDatas(userID);
        return liveFriends;
    }

    @Override
    public LiveData<List<Friend>> getFriendRequests(int userID) {
        updateLiveDatas(userID);
        return liveRequests;
    }

    @Override
    public void sendFriendRequest(int userID1, int userID2) {
        // MOCK: Giả lập gửi yêu cầu
        Friend newRequest = new Friend();
        newRequest.setBanBeID(MOCK_DATA_LIST.size() + 1000);
        newRequest.setUserID1(userID1);
        newRequest.setUserID2(userID2);
        newRequest.setTrangThai("Chờ xác nhận");
        newRequest.setNgayKetBan(new Date());

        newRequest.setTenNguoiBan("Người được mời");
        MOCK_DATA_LIST.add(newRequest);
        updateLiveDatas(userID1);
    }

    @Override
    public void acceptFriendRequest(int banBeID) {
        // MOCK: Tìm và cập nhật trạng thái
        MOCK_DATA_LIST.stream()
                .filter(f -> f.getBanBeID() == banBeID && "Chờ xác nhận".equals(f.getTrangThai()))
                .findFirst()
                .ifPresent(f -> {
                    f.setTrangThai("Bạn bè");
                    f.setNgayKetBan(new Date());
                });

        updateLiveDatas(1); // Giả sử ID người dùng hiện tại là 1
    }

    @Override
    public void removeFriend(int banBeID) {
        // MOCK: Xóa khỏi danh sách
        MOCK_DATA_LIST.removeIf(f -> f.getBanBeID() == banBeID);

        updateLiveDatas(1); // Giả sử ID người dùng hiện tại là 1
    }
}