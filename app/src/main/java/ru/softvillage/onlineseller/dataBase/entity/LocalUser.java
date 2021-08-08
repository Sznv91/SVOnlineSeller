package ru.softvillage.onlineseller.dataBase.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import org.joda.time.LocalDate;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.softvillage.onlineseller.dataBase.converters.LocalDateConverter;

@Data
@RequiredArgsConstructor
@Entity(tableName = "user")
@TypeConverters({LocalDateConverter.class})
public class LocalUser {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "user_uuid")
    private final String userUuid;

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
