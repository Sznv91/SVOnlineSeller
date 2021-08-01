package ru.softvillage.onlineseller.presenter;

import android.annotation.SuppressLint;
import android.util.Log;

import lombok.Getter;
import ru.softvillage.onlineseller.AppSeller;
import ru.softvillage.onlineseller.dataBase.entity.LocalUser;
import ru.softvillage.onlineseller.util.Md5Calc;
import ru.softvillage.onlineseller.util.Prefs;

public class AppPresenter {

    public final static String IS_CHECKED_USER_AGREEMENT = "is_checked_user_agreement";
    @Getter
    private boolean isCheckedUserAgreement;

    private static AppPresenter instance;

    public static AppPresenter getInstance() {
        if (instance == null) {
            instance = new AppPresenter();
        }
        return instance;
    }

    public AppPresenter() {
        init();
    }

    private void init() {
        isCheckedUserAgreement = Prefs.getInstance().loadBoolean(IS_CHECKED_USER_AGREEMENT);
    }

    public void setCheckedUserAgreement(boolean checkedUserAgreement) {
        if (this.isCheckedUserAgreement != checkedUserAgreement) {
            isCheckedUserAgreement = checkedUserAgreement;
            Prefs.getInstance().saveBoolean(IS_CHECKED_USER_AGREEMENT, checkedUserAgreement);
        }
    }

    @SuppressLint("LongLogTag")
    public void populateDemoUser(boolean turnOnDemoMode) {
        new Thread(() -> {
            if (turnOnDemoMode) {
                for (int i = 0; i < 6; i++) {
                    LocalUser user = new LocalUser();
                    user.setSurname("Петров" + i);
                    user.setName("Иван" + (i - 1));
                    user.setPatronymic("Геннадевич" + (i + 1));
                    user.setPin(Md5Calc.getHash(String.valueOf((i + 1) * 11111)));
                    AppSeller.getInstance().getDbHelper().getDataBase().userDao().createUser(user);
                }
            } else {
                AppSeller.getInstance().getDbHelper().getDataBase().userDao().removeAllUser();
                AuthPresenter.getInstance().setLastSelectUserId(-1);
                AuthPresenter.getInstance().setLastSelectUser(null);
            }
            Log.d(AppSeller.TAG + "_AppPresenter", "User Table size:" + AppSeller.getInstance().getDbHelper().getDataBase().userDao().getAllUser().size());
        }).start();


    }
}
