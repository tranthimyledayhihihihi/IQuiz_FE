package com.example.iq5.core.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.example.iq5.core.db.entity.QuestionLocalEntity;
import com.example.iq5.core.db.entity.TopicLocalEntity;
import java.util.List;

@Dao
public interface QuestionDao {
    // QUESTION DAO
    @Query("SELECT * FROM questions WHERE chuDeId = :topicId")
    LiveData<List<QuestionLocalEntity>> getQuestionsByTopic(int topicId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllQuestions(List<QuestionLocalEntity> questions);

    // TOPIC DAO
    @Query("SELECT * FROM topics")
    LiveData<List<TopicLocalEntity>> getAllTopics();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTopics(List<TopicLocalEntity> topics);
}