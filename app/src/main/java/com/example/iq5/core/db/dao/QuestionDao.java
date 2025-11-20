package com.example.iq5.core.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;

import com.example.iq5.core.db.entity.QuestionLocalEntity;

@Dao
public interface QuestionDao {

    // Lấy danh sách câu hỏi theo ID chủ đề
    @Query("SELECT * FROM questions WHERE chuDeId = :topicId")
    LiveData<List<QuestionLocalEntity>> getQuestionsByTopic(int topicId);

    // Chèn hoặc thay thế danh sách câu hỏi
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllQuestions(List<QuestionLocalEntity> questions);
}
