package com.example.iq5.core.db.dao;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.EntityInsertAdapter;
import androidx.room.RoomDatabase;
import androidx.room.util.DBUtil;
import androidx.room.util.SQLiteStatementUtil;
import androidx.sqlite.SQLiteStatement;
import com.example.iq5.core.db.entity.QuestionLocalEntity;
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
public final class QuestionDao_Impl implements QuestionDao {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<QuestionLocalEntity> __insertAdapterOfQuestionLocalEntity;

  public QuestionDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfQuestionLocalEntity = new EntityInsertAdapter<QuestionLocalEntity>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `questions` (`cauHoiId`,`chuDeId`,`noiDung`,`luaChonA`,`luaChonB`,`luaChonC`,`luaChonD`,`dapAn`,`doKho`) VALUES (?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement,
          final QuestionLocalEntity entity) {
        statement.bindLong(1, entity.getCauHoiId());
        statement.bindLong(2, entity.getChuDeId());
        if (entity.getNoiDung() == null) {
          statement.bindNull(3);
        } else {
          statement.bindText(3, entity.getNoiDung());
        }
        if (entity.getLuaChonA() == null) {
          statement.bindNull(4);
        } else {
          statement.bindText(4, entity.getLuaChonA());
        }
        if (entity.getLuaChonB() == null) {
          statement.bindNull(5);
        } else {
          statement.bindText(5, entity.getLuaChonB());
        }
        if (entity.getLuaChonC() == null) {
          statement.bindNull(6);
        } else {
          statement.bindText(6, entity.getLuaChonC());
        }
        if (entity.getLuaChonD() == null) {
          statement.bindNull(7);
        } else {
          statement.bindText(7, entity.getLuaChonD());
        }
        if (entity.getDapAn() == null) {
          statement.bindNull(8);
        } else {
          statement.bindText(8, entity.getDapAn());
        }
        if (entity.getDoKho() == null) {
          statement.bindNull(9);
        } else {
          statement.bindText(9, entity.getDoKho());
        }
      }
    };
  }

  @Override
  public void insertAllQuestions(final List<QuestionLocalEntity> questions) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __insertAdapterOfQuestionLocalEntity.insert(_connection, questions);
      return null;
    });
  }

  @Override
  public LiveData<List<QuestionLocalEntity>> getQuestionsByTopic(final int topicId) {
    final String _sql = "SELECT * FROM questions WHERE chuDeId = ?";
    return __db.getInvalidationTracker().createLiveData(new String[] {"questions"}, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, topicId);
        final int _columnIndexOfCauHoiId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "cauHoiId");
        final int _columnIndexOfChuDeId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "chuDeId");
        final int _columnIndexOfNoiDung = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "noiDung");
        final int _columnIndexOfLuaChonA = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "luaChonA");
        final int _columnIndexOfLuaChonB = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "luaChonB");
        final int _columnIndexOfLuaChonC = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "luaChonC");
        final int _columnIndexOfLuaChonD = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "luaChonD");
        final int _columnIndexOfDapAn = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "dapAn");
        final int _columnIndexOfDoKho = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "doKho");
        final List<QuestionLocalEntity> _result = new ArrayList<QuestionLocalEntity>();
        while (_stmt.step()) {
          final QuestionLocalEntity _item;
          _item = new QuestionLocalEntity();
          final int _tmpCauHoiId;
          _tmpCauHoiId = (int) (_stmt.getLong(_columnIndexOfCauHoiId));
          _item.setCauHoiId(_tmpCauHoiId);
          final int _tmpChuDeId;
          _tmpChuDeId = (int) (_stmt.getLong(_columnIndexOfChuDeId));
          _item.setChuDeId(_tmpChuDeId);
          final String _tmpNoiDung;
          if (_stmt.isNull(_columnIndexOfNoiDung)) {
            _tmpNoiDung = null;
          } else {
            _tmpNoiDung = _stmt.getText(_columnIndexOfNoiDung);
          }
          _item.setNoiDung(_tmpNoiDung);
          final String _tmpLuaChonA;
          if (_stmt.isNull(_columnIndexOfLuaChonA)) {
            _tmpLuaChonA = null;
          } else {
            _tmpLuaChonA = _stmt.getText(_columnIndexOfLuaChonA);
          }
          _item.setLuaChonA(_tmpLuaChonA);
          final String _tmpLuaChonB;
          if (_stmt.isNull(_columnIndexOfLuaChonB)) {
            _tmpLuaChonB = null;
          } else {
            _tmpLuaChonB = _stmt.getText(_columnIndexOfLuaChonB);
          }
          _item.setLuaChonB(_tmpLuaChonB);
          final String _tmpLuaChonC;
          if (_stmt.isNull(_columnIndexOfLuaChonC)) {
            _tmpLuaChonC = null;
          } else {
            _tmpLuaChonC = _stmt.getText(_columnIndexOfLuaChonC);
          }
          _item.setLuaChonC(_tmpLuaChonC);
          final String _tmpLuaChonD;
          if (_stmt.isNull(_columnIndexOfLuaChonD)) {
            _tmpLuaChonD = null;
          } else {
            _tmpLuaChonD = _stmt.getText(_columnIndexOfLuaChonD);
          }
          _item.setLuaChonD(_tmpLuaChonD);
          final String _tmpDapAn;
          if (_stmt.isNull(_columnIndexOfDapAn)) {
            _tmpDapAn = null;
          } else {
            _tmpDapAn = _stmt.getText(_columnIndexOfDapAn);
          }
          _item.setDapAn(_tmpDapAn);
          final String _tmpDoKho;
          if (_stmt.isNull(_columnIndexOfDoKho)) {
            _tmpDoKho = null;
          } else {
            _tmpDoKho = _stmt.getText(_columnIndexOfDoKho);
          }
          _item.setDoKho(_tmpDoKho);
          _result.add(_item);
        }
        return _result;
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
