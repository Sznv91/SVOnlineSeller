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
    //ex 0be81f2c-0cea-4244-8f02-8c8f512f2eb1
    private String orgUuid;

    @ColumnInfo(name = ORG_PREFIX + "name")
    //ex OOO “АСТОРИЯ”
    private String orgName;

    @ColumnInfo(name = ORG_PREFIX + "inn")
    //ex 6151345761
    private Long inn;

    @ColumnInfo(name = ORG_PREFIX + "sno")
    //ex УСН ДОХОД-РАСХОД
    private String sno;

    @ColumnInfo(name = ORG_PREFIX + "address")
    //ex 346500, Ростовская область, г.Шахты, ул.Шевченко 141
    private String address;

    @ColumnInfo(name = ORG_PREFIX + "paymentPlace")
    //ex 346500, Ростовская область, г.Шахты, ул.Шевченко 141
    private String paymentPlace;
}
