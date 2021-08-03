package ru.softvillage.onlineseller.presenter;


import static ru.softvillage.onlineseller.AppSeller.TAG;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.messaging.FirebaseMessaging;

import lombok.Getter;
import ru.softvillage.onlineseller.dataBase.entity.LocalUser;
import ru.softvillage.onlineseller.util.Prefs;

public class AuthPresenter {
    public static final String FIRE_BASE_TOKEN = "fire_base_token";
    public static final String FIRST_STAGE_AUTH = "first_stage_auth";
    public static final String LAST_SELECT_USER_ID = "last_select_user_id";

    public static final String PIN_CODE = "pin_code";
    public static final String PIN_CODE_TIME_STAMP = "pin_code_time_stamp";
    public static final Long TEN_MINUTES_IN_MILLIS = 10L * 60 * 1000;

    @Getter
    private String fireBaseToken;
    private final MutableLiveData<Boolean> firstStageAuth = new MutableLiveData<>(false);
    private final MutableLiveData<Long> lastSelectUserId = new MutableLiveData<>();

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
        lastSelectUserId.postValue(Prefs.getInstance().loadLong(LAST_SELECT_USER_ID));

        fireBaseToken = Prefs.getInstance().loadString(FIRE_BASE_TOKEN);
        if (TextUtils.isEmpty(fireBaseToken)) {
            FirebaseMessaging.getInstance().getToken().addOnSuccessListener(s -> {
                Log.d(TAG + "_AuthPresenter", "FireBaseTag: " + s);
                Prefs.getInstance().saveString(FIRE_BASE_TOKEN, s);
                fireBaseToken = s;
            });
        }

    }

    public LiveData<Boolean> getFirstStageAuthLiveData() {
        return firstStageAuth;
    }

    public void setFirstStageAuth(boolean firstStageAuth) {
        Log.d(TAG, "setFirstStageAuth() called with: firstStageAuth = [" + firstStageAuth + "]");
        if (this.firstStageAuth.getValue() != firstStageAuth) {
            this.firstStageAuth.postValue(firstStageAuth);
            Prefs.getInstance().saveBoolean(FIRST_STAGE_AUTH, firstStageAuth);
        }
    }

    public LiveData<Long> getLastSelectUserId() {
        return lastSelectUserId;
    }

    public void setLastSelectUserId(long lastSelectUserId) {
        if (this.lastSelectUserId.getValue() != null &&
                !this.lastSelectUserId.getValue().equals(lastSelectUserId)) {
            Prefs.getInstance().saveLong(LAST_SELECT_USER_ID, lastSelectUserId);
            this.lastSelectUserId.postValue(lastSelectUserId);
        }
    }

    public LiveData<LocalUser> getLastSelectUserLiveData() {
        return lastSelectUser;
    }

    public void setLastSelectUser(LocalUser user) {
        lastSelectUser.postValue(user);
    }

    public int getPinCode() {
        int max = 99999;
        int min = 11111;
        max -= min;
        return (int) (Math.random() * ++max) + min;
    }
}
