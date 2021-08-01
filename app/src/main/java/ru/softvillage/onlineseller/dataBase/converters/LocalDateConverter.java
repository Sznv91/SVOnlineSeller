package ru.softvillage.onlineseller.dataBase.converters;

import androidx.room.TypeConverter;

import org.joda.time.LocalDate;

public class LocalDateConverter {

    @TypeConverter
    public static LocalDate stringToLocalDateTime(String data) {
        if (data.equals("null")) {
            return null;
        }
        return LocalDate.parse(data);
    }

    @TypeConverter
    public static String LocalDateTimeToString(LocalDate data) {
        if (data == null) {
            return "null";
        }
        return data.toString();
    }
}
