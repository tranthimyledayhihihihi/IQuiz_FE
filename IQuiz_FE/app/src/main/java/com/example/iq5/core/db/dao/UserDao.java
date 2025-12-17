package com.example.iq5.core.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.example.iq5.core.db.entity.UserLocalEntity;
import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM users WHERE userId = :id")
    LiveData<UserLocalEntity> getUserById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(UserLocalEntity user);

    @Query("SELECT * FROM users")
    LiveData<List<UserLocalEntity>> getAllUsers();

    @Query("DELETE FROM users")
    void clearUsers();
}