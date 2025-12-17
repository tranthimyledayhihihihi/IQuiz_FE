package com.example.iq5.core.db.dao;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.EntityInsertAdapter;
import androidx.room.RoomDatabase;
import androidx.room.util.DBUtil;
import androidx.room.util.SQLiteStatementUtil;
import androidx.sqlite.SQLiteStatement;
import com.example.iq5.core.db.Converters;
import com.example.iq5.core.db.entity.UserLocalEntity;
import java.lang.Class;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class UserDao_Impl implements UserDao {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<UserLocalEntity> __insertAdapterOfUserLocalEntity;

  public UserDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfUserLocalEntity = new EntityInsertAdapter<UserLocalEntity>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `users` (`userId`,`tenDangNhap`,`email`,`hoTen`,`vaiTro`,`ngayTao`) VALUES (?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final UserLocalEntity entity) {
        statement.bindLong(1, entity.getUserId());
        if (entity.getTenDangNhap() == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.getTenDangNhap());
        }
        if (entity.getEmail() == null) {
          statement.bindNull(3);
        } else {
          statement.bindText(3, entity.getEmail());
        }
        if (entity.getHoTen() == null) {
          statement.bindNull(4);
        } else {
          statement.bindText(4, entity.getHoTen());
        }
        if (entity.getVaiTro() == null) {
          statement.bindNull(5);
        } else {
          statement.bindText(5, entity.getVaiTro());
        }
        final Long _tmp = Converters.dateToTimestamp(entity.getNgayTao());
        if (_tmp == null) {
          statement.bindNull(6);
        } else {
          statement.bindLong(6, _tmp);
        }
      }
    };
  }

  @Override
  public void insertUser(final UserLocalEntity user) {
    DBUtil.performBlocking(__db, false, true, (_connection) -> {
      __insertAdapterOfUserLocalEntity.insert(_connection, user);
      return null;
    });
  }

  @Override
  public LiveData<UserLocalEntity> getUserById(final int id) {
    final String _sql = "SELECT * FROM users WHERE userId = ?";
    return __db.getInvalidationTracker().createLiveData(new String[] {"users"}, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
        final int _columnIndexOfUserId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "userId");
        final int _columnIndexOfTenDangNhap = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "tenDangNhap");
        final int _columnIndexOfEmail = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "email");
        final int _columnIndexOfHoTen = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "hoTen");
        final int _columnIndexOfVaiTro = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "vaiTro");
        final int _columnIndexOfNgayTao = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "ngayTao");
        final UserLocalEntity _result;
        if (_stmt.step()) {
          final int _tmpUserId;
          _tmpUserId = (int) (_stmt.getLong(_columnIndexOfUserId));
          final String _tmpTenDangNhap;
          if (_stmt.isNull(_columnIndexOfTenDangNhap)) {
            _tmpTenDangNhap = null;
          } else {
            _tmpTenDangNhap = _stmt.getText(_columnIndexOfTenDangNhap);
          }
          final String _tmpEmail;
          if (_stmt.isNull(_columnIndexOfEmail)) {
            _tmpEmail = null;
          } else {
            _tmpEmail = _stmt.getText(_columnIndexOfEmail);
          }
          final String _tmpHoTen;
          if (_stmt.isNull(_columnIndexOfHoTen)) {
            _tmpHoTen = null;
          } else {
            _tmpHoTen = _stmt.getText(_columnIndexOfHoTen);
          }
          final String _tmpVaiTro;
          if (_stmt.isNull(_columnIndexOfVaiTro)) {
            _tmpVaiTro = null;
          } else {
            _tmpVaiTro = _stmt.getText(_columnIndexOfVaiTro);
          }
          final Date _tmpNgayTao;
          final Long _tmp;
          if (_stmt.isNull(_columnIndexOfNgayTao)) {
            _tmp = null;
          } else {
            _tmp = _stmt.getLong(_columnIndexOfNgayTao);
          }
          _tmpNgayTao = Converters.fromTimestamp(_tmp);
          _result = new UserLocalEntity(_tmpUserId,_tmpTenDangNhap,_tmpEmail,_tmpHoTen,_tmpVaiTro,_tmpNgayTao);
        } else {
          _result = null;
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public LiveData<List<UserLocalEntity>> getAllUsers() {
    final String _sql = "SELECT * FROM users";
    return __db.getInvalidationTracker().createLiveData(new String[] {"users"}, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfUserId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "userId");
        final int _columnIndexOfTenDangNhap = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "tenDangNhap");
        final int _columnIndexOfEmail = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "email");
        final int _columnIndexOfHoTen = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "hoTen");
        final int _columnIndexOfVaiTro = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "vaiTro");
        final int _columnIndexOfNgayTao = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "ngayTao");
        final List<UserLocalEntity> _result = new ArrayList<UserLocalEntity>();
        while (_stmt.step()) {
          final UserLocalEntity _item;
          final int _tmpUserId;
          _tmpUserId = (int) (_stmt.getLong(_columnIndexOfUserId));
          final String _tmpTenDangNhap;
          if (_stmt.isNull(_columnIndexOfTenDangNhap)) {
            _tmpTenDangNhap = null;
          } else {
            _tmpTenDangNhap = _stmt.getText(_columnIndexOfTenDangNhap);
          }
          final String _tmpEmail;
          if (_stmt.isNull(_columnIndexOfEmail)) {
            _tmpEmail = null;
          } else {
            _tmpEmail = _stmt.getText(_columnIndexOfEmail);
          }
          final String _tmpHoTen;
          if (_stmt.isNull(_columnIndexOfHoTen)) {
            _tmpHoTen = null;
          } else {
            _tmpHoTen = _stmt.getText(_columnIndexOfHoTen);
          }
          final String _tmpVaiTro;
          if (_stmt.isNull(_columnIndexOfVaiTro)) {
            _tmpVaiTro = null;
          } else {
            _tmpVaiTro = _stmt.getText(_columnIndexOfVaiTro);
          }
          final Date _tmpNgayTao;
          final Long _tmp;
          if (_stmt.isNull(_columnIndexOfNgayTao)) {
            _tmp = null;
          } else {
            _tmp = _stmt.getLong(_columnIndexOfNgayTao);
          }
          _tmpNgayTao = Converters.fromTimestamp(_tmp);
          _item = new UserLocalEntity(_tmpUserId,_tmpTenDangNhap,_tmpEmail,_tmpHoTen,_tmpVaiTro,_tmpNgayTao);
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @Override
  public void clearUsers() {
    final String _sql = "DELETE FROM users";
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
