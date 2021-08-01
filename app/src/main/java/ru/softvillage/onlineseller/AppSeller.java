package ru.softvillage.onlineseller;

import android.app.Application;

import lombok.Getter;
import ru.softvillage.onlineseller.dataBase.DbHelper;
import ru.softvillage.onlineseller.dataBase.LocalDataBase;
import ru.softvillage.onlineseller.util.FragmentDispatcher;

public class AppSeller extends Application {
    public static final String TAG = BuildConfig.APPLICATION_ID;

    @Getter
    private static AppSeller instance;
    @Getter
    private FragmentDispatcher fragmentDispatcher;
    @Getter
    private DbHelper dbHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
//        initRetrofit();
        initDbHelper();
        initFragmentDispatcher();
    }

    private void initDbHelper() {
        LocalDataBase dataBase = LocalDataBase.getDataBase(this);
        dbHelper = new DbHelper(dataBase);
    }

    private void initFragmentDispatcher() {
        fragmentDispatcher = new FragmentDispatcher();
    }
}
