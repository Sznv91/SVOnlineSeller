package ru.softvillage.onlineseller.presenter;

import android.app.UiModeManager;
import android.content.Context;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import lombok.Setter;
import ru.softvillage.onlineseller.AppSeller;
import ru.softvillage.onlineseller.ui.left_menu.DrawerMenuManager;
import ru.softvillage.onlineseller.ui.left_menu.IThemeChangeMainActivity;
import ru.softvillage.onlineseller.util.Prefs;

public class UiPresenter {
    public static final int THEME_LIGHT = 0;
    public static final int THEME_DARK = 1;

    private static final String CURRENT_THEME = "current_theme";

    private static UiPresenter instance;
    private IThemeChangeMainActivity menuThemeChanger; // исп для динамического обновления темы в левом меню.
    private DrawerMenuManager drawerMenuManager;

    private int currentTheme;
    private MutableLiveData<Integer> currentThemeLiveData;
    private final UiModeManager uiManager = (UiModeManager) AppSeller.getInstance().getApplicationContext().getSystemService(Context.UI_MODE_SERVICE);
    @Setter
    private ISelectUserFragment iSelectUserFragment;

    public static UiPresenter getInstance() {
        if (instance == null) {
            instance = new UiPresenter();
        }
        return instance;
    }

    private UiPresenter() {
        initPresenter();
    }

    private void initPresenter() {
        int tCurrentTheme = Prefs.getInstance().loadInt(CURRENT_THEME);
        if (tCurrentTheme < 0) {
            if (getCurrentModeType() == UiModeManager.MODE_NIGHT_NO) {
                tCurrentTheme = THEME_LIGHT;
            } else {
                tCurrentTheme = THEME_DARK;
            }
        }
        currentThemeLiveData = new MutableLiveData<>(tCurrentTheme);
        setCurrentTheme(tCurrentTheme);
    }

    public void setIThemeChangeMainActivity(IThemeChangeMainActivity menuThemeChanger) {
        this.menuThemeChanger = menuThemeChanger;
    }

    public void setDrawerMenuManager(DrawerMenuManager drawerMenuManager) {
        this.drawerMenuManager = drawerMenuManager;
    }

    public DrawerMenuManager getDrawerManager() {
        return drawerMenuManager;
    }

    public int getCurrentTheme() {
        return currentTheme;
    }

    public void setCurrentTheme(int currentTheme) {
        if (currentTheme < 0 || currentTheme > THEME_DARK) {
            currentTheme = THEME_LIGHT;
        }
        if (this.currentTheme != currentTheme) {
            Prefs.getInstance().saveInt(CURRENT_THEME, currentTheme);
            this.currentTheme = currentTheme;
            currentThemeLiveData.postValue(currentTheme);
            if (menuThemeChanger != null) menuThemeChanger.themeChange(currentTheme);
        }
        uiModeChanger(currentTheme != THEME_LIGHT);
    }

    public LiveData<Integer> getCurrentThemeLiveData() {
        return currentThemeLiveData;
    }

    private int getCurrentModeType() {
        return uiManager.getCurrentModeType();
    }

    private void uiModeChanger(boolean darkModeOn) {
        if (darkModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//            uiManager.setNightMode(UiModeManager.MODE_NIGHT_YES);
        } else {
//            uiManager.setNightMode(UiModeManager.MODE_NIGHT_NO);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    public interface ISelectUserFragment {
        void networkLoadHolder(boolean needShow);

        void roomLoadShowHolder();
    }
}
