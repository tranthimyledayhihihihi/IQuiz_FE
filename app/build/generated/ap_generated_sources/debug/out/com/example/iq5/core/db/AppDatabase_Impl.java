package com.example.iq5.core.db;

import androidx.annotation.NonNull;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenDelegate;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.SQLite;
import androidx.sqlite.SQLiteConnection;
import com.example.iq5.core.db.dao.QuestionDao;
import com.example.iq5.core.db.dao.QuestionDao_Impl;
import com.example.iq5.core.db.dao.TopicDao;
import com.example.iq5.core.db.dao.TopicDao_Impl;
import com.example.iq5.core.db.dao.UserDao;
import com.example.iq5.core.db.dao.UserDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile UserDao _userDao;

  private volatile QuestionDao _questionDao;

  private volatile TopicDao _topicDao;

  @Override
  @NonNull
  protected RoomOpenDelegate createOpenDelegate() {
    final RoomOpenDelegate _openDelegate = new RoomOpenDelegate(1, "7fcb403519cd211c6e41bb3ef1c797e9", "7163a641a8f786102b52de82ab949e6c") {
      @Override
      public void createAllTables(@NonNull final SQLiteConnection connection) {
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `users` (`userId` INTEGER NOT NULL, `tenDangNhap` TEXT, `email` TEXT, `hoTen` TEXT, `vaiTro` TEXT, `ngayTao` INTEGER, PRIMARY KEY(`userId`))");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `questions` (`cauHoiId` INTEGER NOT NULL, `chuDeId` INTEGER NOT NULL, `noiDung` TEXT, `luaChonA` TEXT, `luaChonB` TEXT, `luaChonC` TEXT, `luaChonD` TEXT, `dapAn` TEXT, `doKho` TEXT, PRIMARY KEY(`cauHoiId`))");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS `TopicLocalEntity` (`chuDeId` INTEGER NOT NULL, `tenChuDe` TEXT, PRIMARY KEY(`chuDeId`))");
        SQLite.execSQL(connection, "CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        SQLite.execSQL(connection, "INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '7fcb403519cd211c6e41bb3ef1c797e9')");
      }

      @Override
      public void dropAllTables(@NonNull final SQLiteConnection connection) {
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `users`");
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `questions`");
        SQLite.execSQL(connection, "DROP TABLE IF EXISTS `TopicLocalEntity`");
      }

      @Override
      public void onCreate(@NonNull final SQLiteConnection connection) {
      }

      @Override
      public void onOpen(@NonNull final SQLiteConnection connection) {
        internalInitInvalidationTracker(connection);
      }

      @Override
      public void onPreMigrate(@NonNull final SQLiteConnection connection) {
        DBUtil.dropFtsSyncTriggers(connection);
      }

      @Override
      public void onPostMigrate(@NonNull final SQLiteConnection connection) {
      }

      @Override
      @NonNull
      public RoomOpenDelegate.ValidationResult onValidateSchema(
          @NonNull final SQLiteConnection connection) {
        final Map<String, TableInfo.Column> _columnsUsers = new HashMap<String, TableInfo.Column>(6);
        _columnsUsers.put("userId", new TableInfo.Column("userId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("tenDangNhap", new TableInfo.Column("tenDangNhap", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("email", new TableInfo.Column("email", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("hoTen", new TableInfo.Column("hoTen", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("vaiTro", new TableInfo.Column("vaiTro", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUsers.put("ngayTao", new TableInfo.Column("ngayTao", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysUsers = new HashSet<TableInfo.ForeignKey>(0);
        final Set<TableInfo.Index> _indicesUsers = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUsers = new TableInfo("users", _columnsUsers, _foreignKeysUsers, _indicesUsers);
        final TableInfo _existingUsers = TableInfo.read(connection, "users");
        if (!_infoUsers.equals(_existingUsers)) {
          return new RoomOpenDelegate.ValidationResult(false, "users(com.example.iq5.core.db.entity.UserLocalEntity).\n"
                  + " Expected:\n" + _infoUsers + "\n"
                  + " Found:\n" + _existingUsers);
        }
        final Map<String, TableInfo.Column> _columnsQuestions = new HashMap<String, TableInfo.Column>(9);
        _columnsQuestions.put("cauHoiId", new TableInfo.Column("cauHoiId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuestions.put("chuDeId", new TableInfo.Column("chuDeId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuestions.put("noiDung", new TableInfo.Column("noiDung", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuestions.put("luaChonA", new TableInfo.Column("luaChonA", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuestions.put("luaChonB", new TableInfo.Column("luaChonB", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuestions.put("luaChonC", new TableInfo.Column("luaChonC", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuestions.put("luaChonD", new TableInfo.Column("luaChonD", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuestions.put("dapAn", new TableInfo.Column("dapAn", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsQuestions.put("doKho", new TableInfo.Column("doKho", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysQuestions = new HashSet<TableInfo.ForeignKey>(0);
        final Set<TableInfo.Index> _indicesQuestions = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoQuestions = new TableInfo("questions", _columnsQuestions, _foreignKeysQuestions, _indicesQuestions);
        final TableInfo _existingQuestions = TableInfo.read(connection, "questions");
        if (!_infoQuestions.equals(_existingQuestions)) {
          return new RoomOpenDelegate.ValidationResult(false, "questions(com.example.iq5.core.db.entity.QuestionLocalEntity).\n"
                  + " Expected:\n" + _infoQuestions + "\n"
                  + " Found:\n" + _existingQuestions);
        }
        final Map<String, TableInfo.Column> _columnsTopicLocalEntity = new HashMap<String, TableInfo.Column>(2);
        _columnsTopicLocalEntity.put("chuDeId", new TableInfo.Column("chuDeId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTopicLocalEntity.put("tenChuDe", new TableInfo.Column("tenChuDe", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final Set<TableInfo.ForeignKey> _foreignKeysTopicLocalEntity = new HashSet<TableInfo.ForeignKey>(0);
        final Set<TableInfo.Index> _indicesTopicLocalEntity = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTopicLocalEntity = new TableInfo("TopicLocalEntity", _columnsTopicLocalEntity, _foreignKeysTopicLocalEntity, _indicesTopicLocalEntity);
        final TableInfo _existingTopicLocalEntity = TableInfo.read(connection, "TopicLocalEntity");
        if (!_infoTopicLocalEntity.equals(_existingTopicLocalEntity)) {
          return new RoomOpenDelegate.ValidationResult(false, "TopicLocalEntity(com.example.iq5.core.db.entity.TopicLocalEntity).\n"
                  + " Expected:\n" + _infoTopicLocalEntity + "\n"
                  + " Found:\n" + _existingTopicLocalEntity);
        }
        return new RoomOpenDelegate.ValidationResult(true, null);
      }
    };
    return _openDelegate;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final Map<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final Map<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "users", "questions", "TopicLocalEntity");
  }

  @Override
  public void clearAllTables() {
    super.performClear(false, "users", "questions", "TopicLocalEntity");
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final Map<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(UserDao.class, UserDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(QuestionDao.class, QuestionDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(TopicDao.class, TopicDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final Set<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public UserDao userDao() {
    if (_userDao != null) {
      return _userDao;
    } else {
      synchronized(this) {
        if(_userDao == null) {
          _userDao = new UserDao_Impl(this);
        }
        return _userDao;
      }
    }
  }

  @Override
  public QuestionDao questionDao() {
    if (_questionDao != null) {
      return _questionDao;
    } else {
      synchronized(this) {
        if(_questionDao == null) {
          _questionDao = new QuestionDao_Impl(this);
        }
        return _questionDao;
      }
    }
  }

  @Override
  public TopicDao topicDao() {
    if (_topicDao != null) {
      return _topicDao;
    } else {
      synchronized(this) {
        if(_topicDao == null) {
          _topicDao = new TopicDao_Impl(this);
        }
        return _topicDao;
      }
    }
  }
}
