package com.example.iq5.core.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;

// DÒNG IMPORT QUAN TRỌNG NHẤT
import com.example.iq5.core.db.entity.TopicLocalEntity;

@Dao
public interface TopicDao {

    // Lấy tất cả các chủ đề (LiveData)
    @Query("SELECT * FROM TopicLocalEntity")
    LiveData<List<TopicLocalEntity>> getAllTopics();

    // Chèn hoặc thay thế danh sách chủ đề
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTopics(List<TopicLocalEntity> topics);

    // Phương thức xóa tất cả chủ đề (tùy chọn)
    @Query("DELETE FROM TopicLocalEntity")
    void deleteAllTopics();
}