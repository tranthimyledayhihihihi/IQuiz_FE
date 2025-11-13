package com.example.iq5.core.db;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.iq5.core.db.dao.UserDao;
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
    private static final String DATABASE_NAME = "quanlydoan_db";

    public abstract UserDao userDao();
    public abstract QuestionDao questionDao();

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, DATABASE_NAME)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}