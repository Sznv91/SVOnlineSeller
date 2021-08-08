package ru.softvillage.onlineseller.dataBase.entity;

import static ru.softvillage.onlineseller.network.user.entity.OrganizationTo.ORG_PREFIX;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import lombok.Data;

@Data
@Entity(tableName = "organization")
public class LocalOrg {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = ORG_PREFIX + "uuid")
    private String orgUuid;

    @ColumnInfo(name = ORG_PREFIX + "name")
    private String orgName;

    @ColumnInfo(name = ORG_PREFIX + "inn")
    private Long inn;

    @ColumnInfo(name = ORG_PREFIX + "sno")
    private String sno;

    @ColumnInfo(name = ORG_PREFIX + "address")
    private String address;

    @ColumnInfo(name = ORG_PREFIX + "paymentPlace")
    private String paymentPlace;
}
