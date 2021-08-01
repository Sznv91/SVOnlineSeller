package ru.softvillage.onlineseller.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import ru.softvillage.onlineseller.AppSeller;
import ru.softvillage.onlineseller.R;
import ru.softvillage.onlineseller.presenter.AppPresenter;
import ru.softvillage.onlineseller.presenter.AuthPresenter;
import ru.softvillage.onlineseller.presenter.UiPresenter;
import ru.softvillage.onlineseller.ui.dialog.AboutDialog;
import ru.softvillage.onlineseller.ui.left_menu.DrawerMenuManager;

public class MainActivity extends AppCompatActivity {

    private Observer<Boolean> authObserver = new Observer<Boolean>() {
        @Override
        public void onChanged(Boolean firstStageAuth) {
            if (firstStageAuth) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_holder, SelectUserFragment.newInstance(null, null)).commit();
            } else {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_holder, AuthFragment.newInstance(null, null)).commit();
            }
        }
    };

    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppSeller.getInstance().getFragmentDispatcher().setActivity(this);
        DrawerMenuManager<MainActivity> manager = new DrawerMenuManager<>(this); // Инициализация бокового меню

        if (!AppPresenter.getInstance().isCheckedUserAgreement()) {
            AboutDialog dialog = AboutDialog.newInstance(AboutDialog.TYPE_USER_AGREEMENT);
            dialog.setCancelable(false);
            dialog.show(getSupportFragmentManager(), AboutDialog.TYPE_USER_AGREEMENT);
        }

        AuthPresenter.getInstance().getFirstStageAuthLiveData().observe(this, authObserver);
        Log.d(AppSeller.TAG + "_MainActivity", "_getFireBaseTag: " + AuthPresenter.getInstance().getFireBaseToken());
    }

    @Override
    protected void onDestroy() {
        AppSeller.getInstance().getFragmentDispatcher().setActivity(null);
        UiPresenter.getInstance().setIThemeChangeMainActivity(null);
        UiPresenter.getInstance().setDrawerMenuManager(null);
        super.onDestroy();
    }
}