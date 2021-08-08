package ru.softvillage.onlineseller.dataBase;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import ru.softvillage.onlineseller.dataBase.entity.LocalOrg;
import ru.softvillage.onlineseller.dataBase.entity.LocalUser;
import ru.softvillage.onlineseller.dataBase.entity.UserOrgJoin;

@Dao
public interface UserOrgJoinDao {
    @Insert
    void insert(UserOrgJoin userOrgJoin);

    @Query("SELECT * FROM user " +
            "INNER JOIN user_org_join " +
            "ON user.user_uuid=user_org_join.user_uuid " +
            "WHERE user_org_join.org_uuid =:orgUuid")
    List<LocalUser> getUsersForOrganization(final int orgUuid);

    @Query("SELECT * FROM organization " +
            "INNER JOIN user_org_join " +
            "ON organization.org_uuid = user_org_join.org_uuid " +
            "WHERE user_org_join.user_uuid =:userUuid")
    List<LocalOrg> getOrganizationForUsers(final int userUuid);
}
