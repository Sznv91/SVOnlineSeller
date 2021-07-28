package ru.softvillage.onlineseller.presenter;

import lombok.Getter;
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
}
