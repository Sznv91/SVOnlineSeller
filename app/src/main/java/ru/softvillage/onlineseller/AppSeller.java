package ru.softvillage.onlineseller;

import android.app.Application;

import lombok.Getter;
import ru.softvillage.onlineseller.util.FragmentDispatcher;

public class AppSeller extends Application {
    public static final String TAG = BuildConfig.APPLICATION_ID;

    @Getter
    private static AppSeller instance;
    @Getter
    private FragmentDispatcher fragmentDispatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
//        initRetrofit();
//        initDbHelper();
        initFragmentDispatcher();
    }

    private void initFragmentDispatcher() {
        fragmentDispatcher = new FragmentDispatcher();
    }
}
