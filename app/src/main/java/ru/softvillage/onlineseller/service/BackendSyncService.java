package ru.softvillage.onlineseller.service;

import static ru.softvillage.onlineseller.AppSeller.TAG;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.softvillage.onlineseller.AppSeller;
import ru.softvillage.onlineseller.dataBase.entity.LocalOrg;
import ru.softvillage.onlineseller.dataBase.entity.LocalUser;
import ru.softvillage.onlineseller.dataBase.entity.UserOrgJoin;
import ru.softvillage.onlineseller.network.user.entity.LocalUserTo;
import ru.softvillage.onlineseller.network.user.entity.NetworkAnswer;
import ru.softvillage.onlineseller.network.user.entity.OrganizationTo;
import ru.softvillage.onlineseller.presenter.AppPresenter;
import ru.softvillage.onlineseller.presenter.AuthPresenter;
import ru.softvillage.onlineseller.presenter.UiPresenter;
import ru.softvillage.onlineseller.util.Md5Calc;

public class BackendSyncService extends Service {
    private static final String LOCAL_TAG = "_" + BackendSyncService.class.getSimpleName();

    public BackendSyncService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(this::recursionNetworker).start();
        return Service.START_STICKY;
    }

    private void recursionNetworker() {
        if (UiPresenter.getInstance().getISelectUserFragment() != null){
            UiPresenter.getInstance().getISelectUserFragment().networkLoadHolder(true);
        }
        try {
            Response<NetworkAnswer> response = AppSeller.getInstance().getUserService().getUsers(AuthPresenter.getInstance().getDeviceId(),
                    AuthPresenter.getInstance().getFireBaseToken()).execute();
            if (response.code() != 200) {
                Log.d(TAG + LOCAL_TAG, "recursionNetworker() called response.code()" + response.code());
                recursionNetworker();
            } else {
                Log.d(TAG + LOCAL_TAG, "recursionNetworker() called response.code()" + response.code());
                List<LocalUserTo> receivedList = response.body().getUserTos();
                for (LocalUserTo userTo : receivedList) {
                    LocalUser user = new LocalUser(userTo.getUserUuid());
                    user.setSurname(userTo.getSurname());
                    user.setName(userTo.getName());
                    user.setPatronymic(userTo.getPatronymic());
                    user.setPin(Md5Calc.getHash(userTo.getPin()));
                    AppSeller.getInstance().getDbHelper().getDataBase().userDao().createUser(user);

                    for (OrganizationTo orgTo : userTo.getAvailableOrg()) {
                        LocalOrg localOrg = new LocalOrg(orgTo.getOrgUuid());
                        localOrg.setOrgName(orgTo.getOrgName());
                        localOrg.setInn(orgTo.getInn());
                        localOrg.setSno(orgTo.getSno());
                        localOrg.setAddress(orgTo.getAddress());
                        localOrg.setPaymentPlace(orgTo.getPaymentPlace());
                        AppSeller.getInstance().getDbHelper().getDataBase().orgDao().createOrg(localOrg);

                        UserOrgJoin userOrgJoin = new UserOrgJoin(user.getUserUuid(), localOrg.getOrgUuid());
                        AppSeller.getInstance().getDbHelper().getDataBase().userOrgJoinDao().insert(userOrgJoin);
                    }
                }
                AppPresenter.getInstance().setNeedLoadUserFromNetwork(false);
                stopSelf();
            }
        } catch (IOException e) {
            Log.d(TAG + LOCAL_TAG, "recursionNetworker() called catch (IOException e) " + e.getMessage());
            recursionNetworker();
        }
    }
}