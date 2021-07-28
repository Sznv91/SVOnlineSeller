package ru.softvillage.onlineseller;

import android.app.Application;

import lombok.Getter;

public class AppSeller extends Application {
    public static final String TAG = BuildConfig.APPLICATION_ID;

    @Getter
    private static AppSeller instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        /*initRetrofit();
        initDbHelper();
        initFragmentDispatcher();*/
    }
}
