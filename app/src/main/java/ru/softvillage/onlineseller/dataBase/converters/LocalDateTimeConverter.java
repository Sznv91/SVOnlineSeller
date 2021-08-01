package ru.softvillage.onlineseller.dataBase.converters;

import androidx.room.TypeConverter;

import org.joda.time.LocalDateTime;

public class LocalDateTimeConverter {

    @TypeConverter
    public static LocalDateTime stringToLocalDateTime(String data) {
        if (data.equals("null")) {
            return null;
        }
        return LocalDateTime.parse(data);
    }

    @TypeConverter
    public static String LocalDateTimeToString(LocalDateTime data) {
        if (data == null) {
            return "null";
        }
        return data.toString();
    }
}
