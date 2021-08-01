package ru.softvillage.onlineseller.dataBase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ru.softvillage.onlineseller.dataBase.entity.LocalUser;

@Dao
public interface UserDao {

    @Query("SELECT * FROM user")
    List<LocalUser> getAllUser();

    @Query("SELECT * FROM user")
    LiveData<List<LocalUser>> getAllUserLiveData();

//    LocalUser getUserById(long userUuid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void createUser(LocalUser user);

    @Update
    void updateUser(LocalUser user);

    @Query("DELETE FROM user")
    void removeAllUser();
}
