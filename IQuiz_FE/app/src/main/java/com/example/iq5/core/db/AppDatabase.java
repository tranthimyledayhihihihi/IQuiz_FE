package com.example.iq5.core.db;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.iq5.core.db.dao.QuestionDao;
import com.example.iq5.core.db.dao.UserDao;
import com.example.iq5.core.db.dao.TopicDao; // Cần import TopicDao
import com.example.iq5.core.db.entity.QuestionLocalEntity;
import com.example.iq5.core.db.entity.TopicLocalEntity;
import com.example.iq5.core.db.entity.UserLocalEntity;

@Database(
        entities = {UserLocalEntity.class, QuestionLocalEntity.class, TopicLocalEntity.class},
        version = 1,
        exportSchema = false
)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;
    private static final String DATABASE_NAME = "QUIZ_GAME_WEB_DB";

    public abstract UserDao userDao();

    public abstract QuestionDao questionDao();

    public abstract TopicDao topicDao(); // ĐÃ KHẮC PHỤC: Thêm TopicDao
//
}
