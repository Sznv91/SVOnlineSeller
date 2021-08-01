package ru.softvillage.onlineseller.dataBase.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import org.joda.time.LocalDate;

import lombok.Data;
import ru.softvillage.onlineseller.dataBase.converters.LocalDateConverter;

@Data
@Entity(tableName = "user")
@TypeConverters({LocalDateConverter.class})
public class LocalUser {

    @PrimaryKey
    private Long userUuid;

    @ColumnInfo
    private String surname;
    @ColumnInfo
    private String name;
    @ColumnInfo
    private String patronymic;

    @ColumnInfo
    private String pin;

    @ColumnInfo(name = "last_date_auth")
    private LocalDate lastDateAuth;

}
