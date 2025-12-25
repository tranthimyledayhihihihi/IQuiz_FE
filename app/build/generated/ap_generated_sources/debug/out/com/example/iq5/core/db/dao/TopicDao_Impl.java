package com.example.iq5.core.db.dao;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.EntityInsertAdapter;
import androidx.room.RoomDatabase;
import androidx.room.util.DBUtil;
import androidx.room.util.SQLiteStatementUtil;
import androidx.sqlite.SQLiteStatement;
import com.example.iq5.core.db.entity.TopicLocalEntity;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class TopicDao_Impl implements TopicDao {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<TopicLocalEntity> __insertAdapterOfTopicLocalEntity;

  public TopicDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfTopicLocalEntity = new EntityInsertAdapter<TopicLocalEntity>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `TopicLocalEntity` (`chuDeId`,`tenChuDe`) VALUES (?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final TopicLocalEntity entity) {
        statement.bindLong(1, entity.getChuDeId());
        if (entity.getTenChuDe() == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.getTenChuDe());
        }
      }
    };
  }

  @Override
  public void insertTopics(final List<TopicLocalEntity> topics) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __insertAdapterOfTopicLocalEntity.insert(_connection, topics);
      return null;
    });
  }

  @Override
  public LiveData<List<TopicLocalEntity>> getAllTopics() {
    final String _sql = "SELECT * FROM TopicLocalEntity";
    return __db.getInvalidationTracker().createLiveData(new String[] {"TopicLocalEntity"}, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfChuDeId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "chuDeId");
        final int _columnIndexOfTenChuDe = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "tenChuDe");
        final List<TopicLocalEntity> _result = new ArrayList<TopicLocalEntity>();
        while (_stmt.step()) {
          final TopicLocalEntity _item;
          _item = new TopicLocalEntity();
          final int _tmpChuDeId;
          _tmpChuDeId = (int) (_stmt.getLong(_columnIndexOfChuDeId));
          _item.setChuDeId(_tmpChuDeId);
          final String _tmpTenChuDe;
          if (_stmt.isNull(_columnIndexOfTenChuDe)) {
            _tmpTenChuDe = null;
          } else {
            _tmpTenChuDe = _stmt.getText(_columnIndexOfTenChuDe);
          }
          _item.setTenChuDe(_tmpTenChuDe);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public void deleteAllTopics() {
    final String _sql = "DELETE FROM TopicLocalEntity";
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        _stmt.step();
        return null;
      } finally {
        _stmt.close();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
