package ru.softvillage.onlineseller.presenter;

import android.annotation.SuppressLint;
import android.util.Log;

import java.util.UUID;

import lombok.Getter;
import ru.softvillage.onlineseller.AppSeller;
import ru.softvillage.onlineseller.dataBase.entity.LocalOrg;
import ru.softvillage.onlineseller.dataBase.entity.LocalUser;
import ru.softvillage.onlineseller.dataBase.entity.UserOrgJoin;
import ru.softvillage.onlineseller.util.Md5Calc;
import ru.softvillage.onlineseller.util.Prefs;

public class AppPresenter {

    public final static String IS_CHECKED_USER_AGREEMENT = "is_checked_user_agreement";
    public final static String IS_NEED_LOAD_FROM_NETWORK = "is_need_load_from_network";

    @Getter
    private boolean isCheckedUserAgreement;

    @Getter
    private boolean needLoadUserFromNetwork;

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
        needLoadUserFromNetwork = Prefs.getInstance().loadBoolean(IS_NEED_LOAD_FROM_NETWORK, true);
    }

    public void setCheckedUserAgreement(boolean checkedUserAgreement) {
        if (this.isCheckedUserAgreement != checkedUserAgreement) {
            isCheckedUserAgreement = checkedUserAgreement;
            Prefs.getInstance().saveBoolean(IS_CHECKED_USER_AGREEMENT, checkedUserAgreement);
        }
    }

    public void setNeedLoadUserFromNetwork(boolean needLoadUserFromNetwork) {
        if (this.needLoadUserFromNetwork != needLoadUserFromNetwork) {
            this.needLoadUserFromNetwork = needLoadUserFromNetwork;
            Prefs.getInstance().saveBoolean(IS_NEED_LOAD_FROM_NETWORK, needLoadUserFromNetwork);
            if (UiPresenter.getInstance().getISelectUserFragment() != null) {
                UiPresenter.getInstance().getISelectUserFragment().networkLoadHolder(needLoadUserFromNetwork);
            }
        }
    }

    @SuppressLint("LongLogTag")
    public void populateDemoUser(boolean turnOnDemoMode) {
        new Thread(() -> {
            String[] names = new String[]{"Павлов Тимур Егорович",
                    "Тимофеев Артур Максимович",
                    "Щукина Мария Владимировна",
                    "Егоров Мирон Артёмович",
                    "Баженова Екатерина Фёдоровна",
                    "Новиков Артемий Дмитриевич",
                    "Кондрашов Константин Михайлович",
                    "Федоров Владимир Анатольевич"};

            String[] orgRegAddress = new String[]{"871679, Омская область, город Мытищи, въезд Ломоносова, 05",
                    "385013, Саратовская область, город Подольск, спуск 1905 года, 22",
                    "941909, Вологодская область, город Орехово-Зуево, бульвар Ломоносова, 07",
                    "931104, Калининградская область, город Наро-Фоминск, шоссе Гоголя, 52",
                    "527732, Калужская область, город Серпухов, шоссе Славы, 22",
                    "945972, Самарская область, город Одинцово, пл. Косиора, 70",
                    "495469, Амурская область, город Подольск, проезд Сталина, 66",
                    "820971, Омская область, город Клин, пер. Косиора, 34",
                    "045670, Мурманская область, город Мытищи, спуск Сталина, 39",
                    "005263, Свердловская область, город Серебряные Пруды, шоссе Ленина, 12"};

            String[] orgPaymentPlace = new String[]{"630675, Магаданская область, город Москва, въезд 1905 года, 91",
                    "505891, Псковская область, город Одинцово, шоссе Балканская, 46",
                    "953740, Тамбовская область, город Орехово-Зуево, пр. Домодедовская, 29",
                    "259992, Брянская область, город Клин, въезд 1905 года, 20",
                    "850264, Тамбовская область, город Ногинск, бульвар Славы, 10",
                    "038332, Астраханская область, город Пушкино, ул. Космонавтов, 66",
                    "055660, Брянская область, город Пушкино, проезд Ленина, 34",
                    "413607, Кировская область, город Талдом, ул. Бухарестская, 96",
                    "761469, Ульяновская область, город Щёлково, проезд Косиора, 65",
                    "319276, Мурманская область, город Сергиев Посад, шоссе Ленина, 85"
            };

            String[] orgNames = new String[]{"АквакомпьютерыЛэвэл",
                    "Орионкомпьютеры",
                    "компьютерыТепло",
                    "Москомпьютеры",
                    "компьютерыКраско",
                    "ДонкомпьютерыЭко",
                    "РосткомпьютерыГарант",
                    "компьютерыЛекс",
                    "компьютерыКорона",
                    "компьютерыАтом"
            };


            if (turnOnDemoMode) {
                setNeedLoadUserFromNetwork(false);

                LocalOrg mainOrg = new LocalOrg(UUID.randomUUID().toString());
                mainOrg.setOrgName("Основная организация");
                mainOrg.setInn(1111111111L + (long) (Math.random() * ((9999999999L - 1111111111L) + 1L)));
                mainOrg.setSno("УСН ДОХОД-РАСХОД");
                mainOrg.setAddress(orgRegAddress[(int) (Math.random() * ((orgRegAddress.length - 1)))]);
                mainOrg.setPaymentPlace(orgPaymentPlace[(int) (Math.random() * ((orgPaymentPlace.length - 1)))]);
                AppSeller.getInstance().getDbHelper().getDataBase().orgDao().createOrg(mainOrg);

                for (int i = names.length - 1; i >= 0; i--) {
                    String[] namePartial = names[i].split(" ");
                    LocalUser user = new LocalUser(UUID.randomUUID().toString());
                    user.setSurname(namePartial[0]);
                    user.setName(namePartial[1]);
                    user.setPatronymic(namePartial[2]);
                    user.setPin(Md5Calc.getHash("0000"));
                    AppSeller.getInstance().getDbHelper().getDataBase().userDao().createUser(user);

                    UserOrgJoin mainUserOrgJoin = new UserOrgJoin(user.getUserUuid(), mainOrg.getOrgUuid());
                    AppSeller.getInstance().getDbHelper().getDataBase().userOrgJoinDao().insert(mainUserOrgJoin);

                    int max = 5;
                    int min = 1;
                    int random = min + (int) (Math.random() * ((max - min) + 1));
                    for (int b = 0; b <= random; b++) {
                        LocalOrg org = new LocalOrg(UUID.randomUUID().toString());
                        org.setOrgName(orgNames[(int) (Math.random() * ((orgNames.length - 1)))]);
                        org.setInn(1111111111L + (long) (Math.random() * ((9999999999L - 1111111111L) + 1L)));
                        org.setSno("УСН ДОХОД-РАСХОД");
                        org.setAddress(orgRegAddress[(int) (Math.random() * ((orgRegAddress.length - 1)))]);
                        org.setPaymentPlace(orgPaymentPlace[(int) (Math.random() * ((orgPaymentPlace.length - 1)))]);

                        AppSeller.getInstance().getDbHelper().getDataBase().orgDao().createOrg(org);
                        UserOrgJoin userOrgJoin = new UserOrgJoin(user.getUserUuid(), org.getOrgUuid());
                        AppSeller.getInstance().getDbHelper().getDataBase().userOrgJoinDao().insert(userOrgJoin);
                    }
                }
            } else {
                setNeedLoadUserFromNetwork(true);
                AppSeller.getInstance().getDbHelper().getDataBase().userOrgJoinDao().removeAllUserOrgJoin();
                AppSeller.getInstance().getDbHelper().getDataBase().userDao().removeAllUser();
                AppSeller.getInstance().getDbHelper().getDataBase().orgDao().removeAllOrgs();

                AuthPresenter.getInstance().setLastSelectUserId("-1");
                AuthPresenter.getInstance().setLastSelectUser(null);
            }
            Log.d(AppSeller.TAG + "_AppPresenter", "User Table size:" + AppSeller.getInstance().getDbHelper().getDataBase().userDao().getAllUser().size());
        }).start();


    }
}
