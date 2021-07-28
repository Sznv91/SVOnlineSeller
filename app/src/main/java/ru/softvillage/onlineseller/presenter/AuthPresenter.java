package ru.softvillage.onlineseller.presenter;


import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;

import org.joda.time.Duration;
import org.joda.time.LocalDateTime;

import java.util.concurrent.ThreadLocalRandom;

import lombok.Getter;
import ru.softvillage.onlineseller.util.Prefs;

import static ru.softvillage.onlineseller.AppSeller.TAG;

public class AuthPresenter {
    public static final String FIRST_STAGE_AUTH = "first_stage_auth";
    public static final String PIN_CODE = "pin_code";
    public static final String PIN_CODE_TIME_STAMP = "pin_code_time_stamp";
    public static final Long TEN_MINUTES_IN_MILLIS = 10L * 60 * 1000;

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
        if (TextUtils.isEmpty(Prefs.getInstance().loadString(PIN_CODE_TIME_STAMP))) {
            generateAndSavePin();
        } else {
            pinCodeTimeStamp = LocalDateTime.parse(Prefs.getInstance().loadString(PIN_CODE_TIME_STAMP));
            pinCode = Prefs.getInstance().loadInt(PIN_CODE);
            Duration duration = new Duration(pinCodeTimeStamp.toDateTime(), LocalDateTime.now().toDateTime());
            Log.d(TAG + "AuthPresenter", "duration: " + duration.getMillis());
            if (duration.getMillis() >= TEN_MINUTES_IN_MILLIS) {
                generateAndSavePin();
            }
        }
        pinCode = Prefs.getInstance().loadInt(PIN_CODE);
        pinCode = pinCode == -1 ? generateAndSavePin() : pinCode;
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

    private int generateAndSavePin() {
        pinCodeTimeStamp = LocalDateTime.now();
        int tPinCode = ThreadLocalRandom.current().nextInt(11111, 99999);
        Prefs.getInstance().saveInt(PIN_CODE, tPinCode);
        Prefs.getInstance().saveString(PIN_CODE_TIME_STAMP, pinCodeTimeStamp.toString());
        return tPinCode;
    }
}
