package ru.softvillage.onlineseller.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import ru.softvillage.onlineseller.AppSeller;
import ru.softvillage.onlineseller.R;
import ru.softvillage.onlineseller.presenter.AppPresenter;
import ru.softvillage.onlineseller.presenter.AuthPresenter;
import ru.softvillage.onlineseller.presenter.UiPresenter;
import ru.softvillage.onlineseller.ui.dialog.AboutDialog;
import ru.softvillage.onlineseller.ui.left_menu.DrawerMenuManager;

public class MainActivity extends AppCompatActivity {

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

        if (savedInstanceState == null) {
            if (!AuthPresenter.getInstance().isFirstStageAuth()){
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_holder, AuthFragment.newInstance(null, null)).commit();
            }
        }
    }

    @Override
    protected void onDestroy() {
        AppSeller.getInstance().getFragmentDispatcher().setActivity(null);
        UiPresenter.getInstance().setIThemeChangeMainActivity(null);
        UiPresenter.getInstance().setDrawerMenuManager(null);
        super.onDestroy();
    }
}