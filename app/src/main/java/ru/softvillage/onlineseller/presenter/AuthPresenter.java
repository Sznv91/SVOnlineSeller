package ru.softvillage.onlineseller.presenter;


import static ru.softvillage.onlineseller.AppSeller.TAG;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.messaging.FirebaseMessaging;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.softvillage.onlineseller.AppSeller;
import ru.softvillage.onlineseller.dataBase.entity.LocalUser;
import ru.softvillage.onlineseller.network.auth.entity.ReceiveToApp;
import ru.softvillage.onlineseller.network.auth.entity.SendFromApp;
import ru.softvillage.onlineseller.util.Md5Calc;
import ru.softvillage.onlineseller.util.Prefs;

public class AuthPresenter {
    public static String TAG_LOCAL = "_" + AuthPresenter.class.getSimpleName();
    public static final Long TEN_MINUTES_IN_MILLIS = 10L * 60 * 1000;
    public static final String FIRE_BASE_TOKEN = "fire_base_token";
    public static final String DEVICE_ID = "device_id";
    public static final String FIRST_STAGE_AUTH = "first_stage_auth";
    public static final String LAST_SELECT_USER_ID = "last_select_user_id";
    public static final String KEY_NOT_RECEIVED = "key_not_received";
    public static final String KEY_FIREBASE_RECEIVE_ERROR = "key_firebase_receive_error";
    public static final String EMPTY_VALUE = "";

    @Getter
    private String fireBaseToken;
    private String deviceId;
    private final MutableLiveData<Boolean> firstStageAuth = new MutableLiveData<>();
    private final MutableLiveData<String> lastSelectUserId = new MutableLiveData<>();

    @Getter
    @Setter
    private boolean retrofitHaveInstance = false;
    private String pinCreateTimeStamp = KEY_NOT_RECEIVED;
    @Getter
    private String pin = "-----";


    private final MutableLiveData<LocalUser> lastSelectUser = new MutableLiveData<>(null);

    private static AuthPresenter instance;

    public static AuthPresenter getInstance() {
        if (instance == null) {
            instance = new AuthPresenter();
        }
        return instance;
    }

    public AuthPresenter() {
        init();
    }

    @SuppressLint("LongLogTag")
    private void init() {
        firstStageAuth.postValue(Prefs.getInstance().loadBoolean(FIRST_STAGE_AUTH));
        lastSelectUserId.postValue(Prefs.getInstance().loadString(LAST_SELECT_USER_ID));

        fireBaseToken = Prefs.getInstance().loadString(FIRE_BASE_TOKEN);
        if (TextUtils.isEmpty(fireBaseToken)) {
            FirebaseMessaging.getInstance().getToken()
                    .addOnSuccessListener(s -> {
                        Log.d(TAG + TAG_LOCAL, "OnSuccessListener FireBaseTag: " + s);
                        setFireBaseToken(s);
                    })
                    .addOnFailureListener(e -> {
                        //FireBase token не получен, необходимо перезагрузить приложение.
                        Log.d(TAG + TAG_LOCAL, "OnFailureListener FireBaseTag");
                        pin = "-----";
                        pinCreateTimeStamp = KEY_FIREBASE_RECEIVE_ERROR;
                    })
                    .addOnCanceledListener(() -> {
                        Log.d(TAG + TAG_LOCAL, "OnCanceledListener FireBaseTag");
                        pin = "-----";
                        pinCreateTimeStamp = KEY_FIREBASE_RECEIVE_ERROR;
                    });
        }

        if (!TextUtils.isEmpty(Prefs.getInstance().loadString(DEVICE_ID))) {
            deviceId = Prefs.getInstance().loadString(DEVICE_ID);
        } else {
            if (!TextUtils.isEmpty(fireBaseToken)) {
                deviceId = Md5Calc.getHash(UUID.randomUUID().toString() + fireBaseToken);
                Prefs.getInstance().saveString(DEVICE_ID, deviceId);
            } else {
                deviceId = EMPTY_VALUE;
            }
        }
    }

    public void setFireBaseToken(String fireBaseToken) {
        this.fireBaseToken = fireBaseToken;
        Prefs.getInstance().saveString(FIRE_BASE_TOKEN, fireBaseToken);
    }

    public LiveData<Boolean> getFirstStageAuthLiveData() {
        return firstStageAuth;
    }

    public void setFirstStageAuth(boolean firstStageAuth) {
        Log.d(TAG + TAG_LOCAL, "setFirstStageAuth() called with: firstStageAuth = [" + firstStageAuth + "]");
        if (this.firstStageAuth.getValue() != firstStageAuth) {
            this.firstStageAuth.postValue(firstStageAuth);
            Prefs.getInstance().saveBoolean(FIRST_STAGE_AUTH, firstStageAuth);
        }
    }

    public LiveData<String> getLastSelectUserId() {
        return lastSelectUserId;
    }

    public void setLastSelectUserId(String lastSelectUserId) {
        if (this.lastSelectUserId.getValue() != null &&
                !this.lastSelectUserId.getValue().equals(lastSelectUserId)) {
            Prefs.getInstance().saveString(LAST_SELECT_USER_ID, lastSelectUserId);
            this.lastSelectUserId.postValue(lastSelectUserId);
        }
    }

    public LiveData<LocalUser> getLastSelectUserLiveData() {
        return lastSelectUser;
    }

    public void setLastSelectUser(LocalUser user) {
        lastSelectUser.postValue(user);
    }

    public String getPinCreateTimeStamp(boolean needRequest) {
        if (pinCreateTimeStamp.equals(KEY_NOT_RECEIVED) || needRequest) {
            requestPinCode();
        }
        return pinCreateTimeStamp;
    }

    public void requestPinCode() {
        if (pinCreateTimeStamp.equals(KEY_FIREBASE_RECEIVE_ERROR)) {
            return;
        }

        if (!AuthPresenter.getInstance().isRetrofitHaveInstance()) {
            AuthPresenter.getInstance().setRetrofitHaveInstance(true);
            String mTag = "_requestPinCode() ";
            SendFromApp data = SendFromApp.builder()
                    .deviceId(AuthPresenter.getInstance().getDeviceId())
                    .fireBaseToken(AuthPresenter.getInstance().getFireBaseToken())
                    .build();

            AppSeller.getInstance().getNetworkAuthService().registrationDevice(data).enqueue(new Callback<ReceiveToApp>() {
                @Override
                public void onResponse(Call<ReceiveToApp> call, Response<ReceiveToApp> response) {
                    AuthPresenter.getInstance().setRetrofitHaveInstance(false);
                    if (response.body().isSuccess() /*&& !response.body().getError().equals("false")*/) {
                        pin = (response.body().getAuthCode());
                        pinCreateTimeStamp = (response.body().getGenerateTime());
                        Log.d(TAG + TAG_LOCAL, mTag + "onResponse() called with: call = [" + call + "], response = [" + response + "]");
                    } else {
                        Log.d(TAG + TAG_LOCAL, mTag + "onResponse() called with: call = [" + call + "], response = [" + response + "]");
                        requestPinCode();
                    }
                }

                @Override
                public void onFailure(Call<ReceiveToApp> call, Throwable t) {
                    AuthPresenter.getInstance().setRetrofitHaveInstance(false);
                    Log.d(TAG + TAG_LOCAL, mTag + "onFailure() called with: call = [" + call + "], t = [" + t + "]");
                    requestPinCode();
                }
            });
        }
    }

    public String getDeviceId() {
        if (TextUtils.isEmpty(deviceId)) {
            if (!TextUtils.isEmpty(fireBaseToken)) {
                deviceId = Md5Calc.getHash(UUID.randomUUID().toString() + fireBaseToken);
                Prefs.getInstance().saveString(DEVICE_ID, deviceId);
                return deviceId;
            } else return EMPTY_VALUE;
        } else return deviceId;
    }
}
