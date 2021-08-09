package ru.softvillage.onlineseller;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import lombok.Getter;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.softvillage.onlineseller.dataBase.DbHelper;
import ru.softvillage.onlineseller.dataBase.LocalDataBase;
import ru.softvillage.onlineseller.network.auth.BackendAuthService;
import ru.softvillage.onlineseller.network.user.UserService;
import ru.softvillage.onlineseller.util.FragmentDispatcher;

public class AppSeller extends Application {
    public static final String TAG = BuildConfig.APPLICATION_ID;
    public static final String AUTH_BASE_URL = "https://kkt-evotor.ru/";
    public static final String USER_SERVICE_URL = "https://kkt-evotor.ru/";
    public static final int NETWORK_TIMEOUT = 10;

    @Getter
    private static AppSeller instance;
    @Getter
    private FragmentDispatcher fragmentDispatcher;
    @Getter
    private DbHelper dbHelper;
    @Getter
    private BackendAuthService networkAuthService;
    @Getter
    private UserService userService;


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
                .readTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
                .callTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS)
                .build();

        networkAuthService = new Retrofit.Builder()
                .baseUrl(AUTH_BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build()
                .create(BackendAuthService.class);

        userService = new Retrofit.Builder()
                .baseUrl(USER_SERVICE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .build()
                .create(UserService.class);
    }

    private void initDbHelper() {
        LocalDataBase dataBase = LocalDataBase.getDataBase(this);
        dbHelper = new DbHelper(dataBase);
    }

    private void initFragmentDispatcher() {
        fragmentDispatcher = new FragmentDispatcher();
    }

    public static boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) instance.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
