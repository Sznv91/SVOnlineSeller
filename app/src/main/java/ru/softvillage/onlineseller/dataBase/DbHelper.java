package ru.softvillage.onlineseller.dataBase;

import lombok.Getter;
import ru.softvillage.onlineseller.dataBase.entity.LocalUser;

public class DbHelper {

    @Getter
    private final LocalDataBase dataBase;

    public DbHelper(LocalDataBase dataBase) {
        this.dataBase = dataBase;
    }

    public void updateUser(LocalUser user){
        LocalDataBase.databaseWriteExecutor.execute(() -> {
            dataBase.userDao().updateUser(user);
        });
    }
}
