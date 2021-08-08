package ru.softvillage.onlineseller.dataBase.entity;

import static ru.softvillage.onlineseller.network.user.entity.LocalUserTo.USER_PREFIX;
import static ru.softvillage.onlineseller.network.user.entity.OrganizationTo.ORG_PREFIX;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import lombok.Data;

@Data
@Entity(tableName = "user_org_join",
        primaryKeys = {USER_PREFIX + "uuid", ORG_PREFIX + "uuid"},
        foreignKeys = {
                @ForeignKey(entity = LocalUser.class,
                        parentColumns = "user_uuid",
                        childColumns = "user_uuid"),
                @ForeignKey(entity = LocalOrg.class,
                        parentColumns = "org_uuid",
                        childColumns = "org_uuid")
        },
        indices = {
                @Index(name = USER_PREFIX + "uuid", value = USER_PREFIX + "uuid"),
                @Index(name = ORG_PREFIX + "uuid", value = ORG_PREFIX + "uuid")
        })
public class UserOrgJoin {
    @NonNull
    @ColumnInfo(name = USER_PREFIX + "uuid")
    private final String userUuid;

    @NonNull
    @ColumnInfo(name = ORG_PREFIX + "uuid")
    private final String orgUuid;
}
