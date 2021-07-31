package ru.softvillage.onlineseller.presenter;


import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;

import org.joda.time.LocalDateTime;

import lombok.Getter;
import ru.softvillage.onlineseller.AppSeller;
import ru.softvillage.onlineseller.util.Prefs;

public class AuthPresenter {
    public static final String FIRE_BASE_TOKEN = "fire_base_token";
    public static final String FIRST_STAGE_AUTH = "first_stage_auth";
    public static final String PIN_CODE = "pin_code";
    public static final String PIN_CODE_TIME_STAMP = "pin_code_time_stamp";
    public static final Long TEN_MINUTES_IN_MILLIS = 10L * 60 * 1000;

    @Getter
    private String fireBaseToken;
    @Getter
    private boolean firstStageAuth;
    private int pinCode;
    private LocalDateTime pinCodeTimeStamp;

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
        firstStageAuth = Prefs.getInstance().loadBoolean(FIRST_STAGE_AUTH);

        fireBaseToken = Prefs.getInstance().loadString(FIRE_BASE_TOKEN);
        if (TextUtils.isEmpty(fireBaseToken)) {
            FirebaseMessaging.getInstance().getToken().addOnSuccessListener(s -> {
                Log.d(AppSeller.TAG + "_AuthPresenter", "FireBaseTag: " + s);
                Prefs.getInstance().saveString(FIRE_BASE_TOKEN, s);
                fireBaseToken = s;
            });
        }

        pinCode = 12345;
    }

    public void setFirstStageAuth(boolean firstStageAuth) {
        if (this.firstStageAuth != firstStageAuth) {
            this.firstStageAuth = firstStageAuth;
            Prefs.getInstance().saveBoolean(FIRST_STAGE_AUTH, firstStageAuth);
        }
    }

    public int getPinCode() {
        return pinCode;
    }

}
