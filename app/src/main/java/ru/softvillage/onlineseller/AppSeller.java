package ru.softvillage.onlineseller;

import android.app.Application;
import android.util.Log;

import com.google.gson.Gson;

import lombok.Getter;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.softvillage.onlineseller.dataBase.DbHelper;
import ru.softvillage.onlineseller.dataBase.LocalDataBase;
import ru.softvillage.onlineseller.network.auth.BackendAuthService;
import ru.softvillage.onlineseller.util.FragmentDispatcher;

public class AppSeller extends Application {
    public static final String TAG = BuildConfig.APPLICATION_ID;
    public static final String AUTH_BASE_URL = "https://kkt-evotor.ru/";

    @Getter
    private static AppSeller instance;
    @Getter
    private FragmentDispatcher fragmentDispatcher;
    @Getter
    private DbHelper dbHelper;
    @Getter
    private BackendAuthService networkAuthService;


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initRetrofit();
        initDbHelper();
        initFragmentDispatcher();
    }

    private void initRetrofit() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(s -> Log.d(TAG + "_Network", s));
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        networkAuthService = new Retrofit.Builder()
                .baseUrl(AUTH_BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build()
                .create(BackendAuthService.class);
    }

    private void initDbHelper() {
        LocalDataBase dataBase = LocalDataBase.getDataBase(this);
        dbHelper = new DbHelper(dataBase);
    }

    private void initFragmentDispatcher() {
        fragmentDispatcher = new FragmentDispatcher();
    }
}
