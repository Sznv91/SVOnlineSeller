package ru.softvillage.onlineseller.dataBase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ru.softvillage.onlineseller.dataBase.entity.LocalOrg;

@Dao
public interface OrgDao {
    @Query("SELECT * FROM organization")
    List<LocalOrg> getAllOrgs();

    @Query("SELECT * FROM organization")
    LiveData<List<LocalOrg>> getAllOrgsLiveData();

//    LocalUser getUserById(long userUuid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void createOrg(LocalOrg org);

    @Update
    void updateOrg(LocalOrg org);

    @Query("DELETE FROM organization")
    void removeAllOrgs();
}
