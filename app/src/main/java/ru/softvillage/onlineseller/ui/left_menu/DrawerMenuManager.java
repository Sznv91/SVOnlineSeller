package ru.softvillage.onlineseller.ui.left_menu;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import java.util.Locale;

import ru.softvillage.onlineseller.BuildConfig;
import ru.softvillage.onlineseller.R;
import ru.softvillage.onlineseller.presenter.AuthPresenter;
import ru.softvillage.onlineseller.presenter.UiPresenter;
import ru.softvillage.onlineseller.ui.dialog.AboutDialog;
import ru.softvillage.onlineseller.ui.dialog.ExitDialog;

import static ru.softvillage.onlineseller.AppSeller.TAG;

public class DrawerMenuManager<T extends AppCompatActivity> implements
        IThemeChangeMainActivity,
        View.OnClickListener {

    private final AppCompatActivity activity;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private ConstraintLayout drawerMenu,
            main_menu,
            about_menu,
            about_constraint_layout,
            about_menu_feedback,
            about_menu_rate_the_app,
            about_menu_privacy_policy,
            about_menu_user_agreement,
            about_menu_licenses,
            about_menu_data_protection;
    private ImageView changeTheme,
            icon_about,
            about_menu_image_back,
            about_menu_icon_feedback,
            about_menu_icon_rate_the_app,
            about_menu_icon_privacy_policy,
            about_menu_icon_user_agreement,
            about_menu_icon_licenses,
            about_menu_icon_data_protection;

    private Drawable dIconAbout,
            dIcon_about_menu_button_back,
            dIcon_about_menu_icon_feedback,
            dIcon_about_menu_icon_rate_the_app,
            dIcon_about_menu_icon_privacy_policy,
            dIcon_about_menu_icon_user_agreement,
            dIcon_about_menu_icon_licenses,
            dIcon_about_menu_icon_data_protection;

    private TextView title_about,
            about_menu_title_about,
            about_menu_version,
            about_menu_title_feedback,
            about_menu_title_rate_the_app,
            about_menu_title_privacy_policy,
            about_menu_title_user_agreement,
            about_menu_title_licenses,
            about_menu_title_data_protection,
            titleExit,
            version;

    private View divider_left_menu_title,
            about_menu_divider_title,
            dividerExit;

    private FrameLayout changeThemeBottom,
            about_menu_button_back;

    private ActionBarDrawerToggle toggle;
    private ActionBar mActionBar;
    private boolean mToolBarNavigationListenerIsRegistered = false;

    public DrawerMenuManager(T activity) {
        this.activity = activity;
        initMenu();
        UiPresenter.getInstance().setIThemeChangeMainActivity(this);
    }

    private void initMenu() {
        toolbar = activity.findViewById(R.id.toolbar);
        drawer = activity.findViewById(R.id.drawer);
        drawerMenu = activity.findViewById(R.id.drawer_menu);
        changeTheme = activity.findViewById(R.id.changeTheme);
        divider_left_menu_title = activity.findViewById(R.id.divider_left_menu_title);
        dividerExit = activity.findViewById(R.id.dividerExit);
        changeThemeBottom = activity.findViewById(R.id.changeThemeBottom);

        dIconAbout = ContextCompat.getDrawable(activity.getApplicationContext(), R.drawable.ic_left_menu_about);
        dIcon_about_menu_button_back = ContextCompat.getDrawable(activity.getApplicationContext(), R.drawable.ic_left_menu_arrow_back);
        dIcon_about_menu_icon_feedback = ContextCompat.getDrawable(activity.getApplicationContext(), R.drawable.ic_icon_feedback);
        dIcon_about_menu_icon_rate_the_app = ContextCompat.getDrawable(activity.getApplicationContext(), R.drawable.ic_icon_menu_icon_rate_the_app);
        dIcon_about_menu_icon_privacy_policy = ContextCompat.getDrawable(activity.getApplicationContext(), R.drawable.ic_icon_privacy_policy);
        dIcon_about_menu_icon_user_agreement = ContextCompat.getDrawable(activity.getApplicationContext(), R.drawable.ic_icon_user_agreement);
        dIcon_about_menu_icon_licenses = ContextCompat.getDrawable(activity.getApplicationContext(), R.drawable.ic_icon_licenses);
        dIcon_about_menu_icon_data_protection = ContextCompat.getDrawable(activity.getApplicationContext(), R.drawable.ic_icon_data_protection);

        main_menu = activity.findViewById(R.id.main_menu);
        about_menu = activity.findViewById(R.id.about_menu);
        about_constraint_layout = activity.findViewById(R.id.about_constraint_layout);
        icon_about = activity.findViewById(R.id.icon_about);
        about_menu_image_back = activity.findViewById(R.id.about_menu_image_back);
        about_menu_icon_feedback = activity.findViewById(R.id.about_menu_icon_feedback);
        about_menu_icon_rate_the_app = activity.findViewById(R.id.about_menu_icon_rate_the_app);
        about_menu_icon_privacy_policy = activity.findViewById(R.id.about_menu_icon_privacy_policy);
        about_menu_icon_user_agreement = activity.findViewById(R.id.about_menu_icon_user_agreement);
        about_menu_icon_licenses = activity.findViewById(R.id.about_menu_icon_licenses);
        about_menu_icon_data_protection = activity.findViewById(R.id.about_menu_icon_data_protection);
        about_menu_divider_title = activity.findViewById(R.id.about_menu_divider_title);
        about_menu_button_back = activity.findViewById(R.id.about_menu_button_back);
        title_about = activity.findViewById(R.id.title_about);
        titleExit = activity.findViewById(R.id.titleExit);
        version = activity.findViewById(R.id.version);
        about_menu_title_about = activity.findViewById(R.id.about_menu_title_about);
        about_menu_version = activity.findViewById(R.id.about_menu_version);
        about_menu_title_feedback = activity.findViewById(R.id.about_menu_title_feedback);
        about_menu_title_rate_the_app = activity.findViewById(R.id.about_menu_title_rate_the_app);
        about_menu_title_privacy_policy = activity.findViewById(R.id.about_menu_title_privacy_policy);
        about_menu_title_user_agreement = activity.findViewById(R.id.about_menu_title_user_agreement);
        about_menu_title_licenses = activity.findViewById(R.id.about_menu_title_licenses);
        about_menu_title_data_protection = activity.findViewById(R.id.about_menu_title_data_protection);

        about_menu_feedback = activity.findViewById(R.id.about_menu_feedback);
        about_menu_rate_the_app = activity.findViewById(R.id.about_menu_rate_the_app);
        about_menu_privacy_policy = activity.findViewById(R.id.about_menu_privacy_policy);
        about_menu_user_agreement = activity.findViewById(R.id.about_menu_user_agreement);
        about_menu_licenses = activity.findViewById(R.id.about_menu_licenses);
        about_menu_data_protection = activity.findViewById(R.id.about_menu_data_protection);

        changeThemeBottom.setOnClickListener(this);
        about_menu_button_back.setOnClickListener(this);
        about_constraint_layout.setOnClickListener(this);

        about_menu_button_back.setOnClickListener(this);
        about_menu_feedback.setOnClickListener(this);
        about_menu_rate_the_app.setOnClickListener(this);
        about_menu_privacy_policy.setOnClickListener(this);
        about_menu_user_agreement.setOnClickListener(this);
        about_menu_licenses.setOnClickListener(this);
        about_menu_data_protection.setOnClickListener(this);
        titleExit.setOnClickListener(this);

        updateUITheme();
        updateVersion();

        toolbar.setTitle(activity.getString(R.string.app_name));
        activity.setSupportActionBar(toolbar);
        toggle = new ActionBarDrawerToggle(activity, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mActionBar = activity.getSupportActionBar();
        drawer.addDrawerListener(toggle);
        //Обработка дейсвий с меню (Выезжание - заезжание)
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull @org.jetbrains.annotations.NotNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull @org.jetbrains.annotations.NotNull View drawerView) {
                if (!AuthPresenter.getInstance().isFirstStageAuth()){
                    makeVisiblyAboutMenu();
                }
            }

            @Override
            public void onDrawerClosed(@NonNull @org.jetbrains.annotations.NotNull View drawerView) {
                makeVisiblyMainMenu();
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        toggle.syncState();
        drawerMenu.post(new Runnable() {
            @Override
            public void run() {
                DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) drawerMenu.getLayoutParams();

                int currentOrientation = activity.getResources().getConfiguration().orientation;
                if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                    params.width = Double.valueOf(activity.getResources().getDisplayMetrics().widthPixels * 0.3).intValue();
                } else {
                    params.width = Double.valueOf(activity.getResources().getDisplayMetrics().widthPixels * 0.87).intValue();

                }
                drawerMenu.setLayoutParams(params);
            }
        });
        UiPresenter.getInstance().setDrawerMenuManager(this);
    }

    private void updateUITheme() {
        themeChange(UiPresenter.getInstance().getCurrentTheme());
    }

    private void updateVersion() {
        version.setText(String.format(Locale.getDefault(), "v %s (%d)", BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE));
        about_menu_version.setText(String.format("Версия %s (%d)", BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE));
    }

    @Override
    public void themeChange(int themeStyle) {
        changeIconColor(themeStyle);
        if (themeStyle == UiPresenter.THEME_LIGHT) {
            main_menu.setBackgroundColor(ContextCompat.getColor(drawerMenu.getContext(), R.color.background_lt));
            toolbar.setBackgroundColor(ContextCompat.getColor(toolbar.getContext(), R.color.header_lt));
            drawer.setBackgroundColor(ContextCompat.getColor(drawer.getContext(), R.color.background_lt));
            drawerMenu.setBackgroundColor(ContextCompat.getColor(drawerMenu.getContext(), R.color.background_lt));
            divider_left_menu_title.setBackgroundColor(ContextCompat.getColor(divider_left_menu_title.getContext(), R.color.divider_lt));
            dividerExit.setBackgroundColor(ContextCompat.getColor(dividerExit.getContext(), R.color.divider_lt));
            changeTheme.setImageResource(R.drawable.ic_moon);

            title_about.setTextColor(ContextCompat.getColor(title_about.getContext(), R.color.fonts_lt));
            titleExit.setTextColor(ContextCompat.getColor(title_about.getContext(), R.color.fonts_lt));
            version.setTextColor(ContextCompat.getColor(title_about.getContext(), R.color.active_fonts_lt));
            about_menu_title_about.setTextColor(ContextCompat.getColor(about_menu_title_about.getContext(), R.color.fonts_lt));
            about_menu_version.setTextColor(ContextCompat.getColor(about_menu_version.getContext(), R.color.active_fonts_lt));
            about_menu_title_feedback.setTextColor(ContextCompat.getColor(about_menu_title_feedback.getContext(), R.color.fonts_lt));
            about_menu_title_rate_the_app.setTextColor(ContextCompat.getColor(about_menu_title_rate_the_app.getContext(), R.color.fonts_lt));
            about_menu_title_privacy_policy.setTextColor(ContextCompat.getColor(about_menu_title_privacy_policy.getContext(), R.color.fonts_lt));
            about_menu_title_user_agreement.setTextColor(ContextCompat.getColor(about_menu_title_user_agreement.getContext(), R.color.fonts_lt));
            about_menu_title_licenses.setTextColor(ContextCompat.getColor(about_menu_title_licenses.getContext(), R.color.fonts_lt));
            about_menu_title_data_protection.setTextColor(ContextCompat.getColor(about_menu_title_data_protection.getContext(), R.color.fonts_lt));
            about_menu_divider_title.setBackgroundColor(ContextCompat.getColor(about_menu_divider_title.getContext(), R.color.divider_lt));
        } else {
            toolbar.setBackgroundColor(ContextCompat.getColor(toolbar.getContext(), R.color.background_dt));
            drawer.setBackgroundColor(ContextCompat.getColor(drawer.getContext(), R.color.background_dt));
            drawerMenu.setBackgroundColor(ContextCompat.getColor(drawerMenu.getContext(), R.color.background_dt));
            divider_left_menu_title.setBackgroundColor(ContextCompat.getColor(divider_left_menu_title.getContext(), R.color.divider_dt));
            dividerExit.setBackgroundColor(ContextCompat.getColor(dividerExit.getContext(), R.color.divider_dt));
            changeTheme.setImageResource(R.drawable.ic_sun);

            title_about.setTextColor(ContextCompat.getColor(title_about.getContext(), R.color.fonts_dt));
            titleExit.setTextColor(ContextCompat.getColor(title_about.getContext(), R.color.fonts_dt));
            version.setTextColor(ContextCompat.getColor(title_about.getContext(), R.color.active_fonts_dt));
            about_menu_title_about.setTextColor(ContextCompat.getColor(about_menu_title_about.getContext(), R.color.fonts_dt));
            about_menu_version.setTextColor(ContextCompat.getColor(about_menu_version.getContext(), R.color.active_fonts_dt));
            about_menu_title_feedback.setTextColor(ContextCompat.getColor(about_menu_title_feedback.getContext(), R.color.fonts_dt));
            about_menu_title_rate_the_app.setTextColor(ContextCompat.getColor(about_menu_title_rate_the_app.getContext(), R.color.fonts_dt));
            about_menu_title_privacy_policy.setTextColor(ContextCompat.getColor(about_menu_title_privacy_policy.getContext(), R.color.fonts_dt));
            about_menu_title_user_agreement.setTextColor(ContextCompat.getColor(about_menu_title_user_agreement.getContext(), R.color.fonts_dt));
            about_menu_title_licenses.setTextColor(ContextCompat.getColor(about_menu_title_licenses.getContext(), R.color.fonts_dt));
            about_menu_title_data_protection.setTextColor(ContextCompat.getColor(about_menu_title_data_protection.getContext(), R.color.fonts_dt));
            about_menu_divider_title.setBackgroundColor(ContextCompat.getColor(about_menu_divider_title.getContext(), R.color.divider_dt));
        }
        icon_about.setImageDrawable(dIconAbout);

        about_menu_image_back.setImageDrawable(dIcon_about_menu_button_back);
        about_menu_icon_feedback.setImageDrawable(dIcon_about_menu_icon_feedback);
        about_menu_icon_rate_the_app.setImageDrawable(dIcon_about_menu_icon_rate_the_app);
        about_menu_icon_privacy_policy.setImageDrawable(dIcon_about_menu_icon_privacy_policy);
        about_menu_icon_user_agreement.setImageDrawable(dIcon_about_menu_icon_user_agreement);
        about_menu_icon_licenses.setImageDrawable(dIcon_about_menu_icon_licenses);
        about_menu_icon_data_protection.setImageDrawable(dIcon_about_menu_icon_data_protection);
    }

    private void changeIconColor(int themeStyle) {
        int iconColor = ContextCompat.getColor(activity.getApplicationContext(), R.color.icon_dt);
        int arrowBackColor = ContextCompat.getColor(activity.getApplicationContext(), R.color.active_fonts_dt);
        if (themeStyle == UiPresenter.THEME_LIGHT) {
            iconColor = ContextCompat.getColor(activity.getApplicationContext(), R.color.fonts_lt);
            arrowBackColor = ContextCompat.getColor(activity.getApplicationContext(), R.color.active_fonts_lt);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            dIcon_about_menu_button_back.setColorFilter(new BlendModeColorFilter(arrowBackColor, BlendMode.SRC_IN));
            dIconAbout.setColorFilter(new BlendModeColorFilter(iconColor, BlendMode.SRC_IN));
            dIcon_about_menu_icon_feedback.setColorFilter(new BlendModeColorFilter(iconColor, BlendMode.SRC_IN));
            dIcon_about_menu_icon_rate_the_app.setColorFilter(new BlendModeColorFilter(iconColor, BlendMode.SRC_IN));
            dIcon_about_menu_icon_privacy_policy.setColorFilter(new BlendModeColorFilter(iconColor, BlendMode.SRC_IN));
            dIcon_about_menu_icon_user_agreement.setColorFilter(new BlendModeColorFilter(iconColor, BlendMode.SRC_IN));
            dIcon_about_menu_icon_licenses.setColorFilter(new BlendModeColorFilter(iconColor, BlendMode.SRC_IN));
            dIcon_about_menu_icon_data_protection.setColorFilter(new BlendModeColorFilter(iconColor, BlendMode.SRC_IN));
        } else {
            dIcon_about_menu_button_back.setColorFilter(arrowBackColor, PorterDuff.Mode.SRC_IN);
            dIconAbout.setColorFilter(iconColor, PorterDuff.Mode.SRC_IN);
            dIcon_about_menu_icon_feedback.setColorFilter(iconColor, PorterDuff.Mode.SRC_IN);
            dIcon_about_menu_icon_rate_the_app.setColorFilter(iconColor, PorterDuff.Mode.SRC_IN);
            dIcon_about_menu_icon_privacy_policy.setColorFilter(iconColor, PorterDuff.Mode.SRC_IN);
            dIcon_about_menu_icon_user_agreement.setColorFilter(iconColor, PorterDuff.Mode.SRC_IN);
            dIcon_about_menu_icon_licenses.setColorFilter(iconColor, PorterDuff.Mode.SRC_IN);
            dIcon_about_menu_icon_data_protection.setColorFilter(iconColor, PorterDuff.Mode.SRC_IN);
        }
    }

    @SuppressLint({"LongLogTag", "NonConstantResourceId"})
    @Override
    public void onClick(View v) {
        AboutDialog dialog;
        switch (v.getId()) {
            case R.id.titleExit:
                ExitDialog exitDialog = ExitDialog.newInstance();
                exitDialog.setCancelable(false);
                exitDialog.show(activity.getSupportFragmentManager(), ExitDialog.class.getSimpleName());
                break;
            case R.id.changeThemeBottom:
                int currentTheme = UiPresenter.getInstance().getCurrentTheme();
                UiPresenter.getInstance().setCurrentTheme(currentTheme + 1);
                break;
            case R.id.about_constraint_layout:
                makeVisiblyAboutMenu();
                break;
            case R.id.about_menu_button_back:
                makeVisiblyMainMenu();
                break;
            case R.id.about_menu_feedback:
                dialog = AboutDialog.newInstance(AboutDialog.TYPE_FEEDBACK);
                dialog.setCancelable(false);
                dialog.show(activity.getSupportFragmentManager(), AboutDialog.TYPE_FEEDBACK);
                Log.d(TAG + "_DrawerMenuManager", "tap on about_menu_feedback");
                break;
            case R.id.about_menu_rate_the_app:
                dialog = AboutDialog.newInstance(AboutDialog.TYPE_RATE_THE_APP);
                dialog.setCancelable(false);
                dialog.show(activity.getSupportFragmentManager(), AboutDialog.TYPE_RATE_THE_APP);
                Log.d(TAG + "_DrawerMenuManager", "tap on about_menu_rate_the_app");
                break;
            case R.id.about_menu_privacy_policy:
                dialog = AboutDialog.newInstance(AboutDialog.TYPE_PRIVACY_POLICY);
                dialog.setCancelable(false);
                dialog.show(activity.getSupportFragmentManager(), AboutDialog.TYPE_PRIVACY_POLICY);
                Log.d(TAG + "_DrawerMenuManager", "tap on about_menu_privacy_policy");
                break;
            case R.id.about_menu_user_agreement:
                dialog = AboutDialog.newInstance(AboutDialog.TYPE_USER_AGREEMENT);
                dialog.setCancelable(false);
                dialog.show(activity.getSupportFragmentManager(), AboutDialog.TYPE_USER_AGREEMENT);
                Log.d(TAG + "_DrawerMenuManager", "tap on about_menu_user_agreement");
                break;
            case R.id.about_menu_licenses:
                dialog = AboutDialog.newInstance(AboutDialog.TYPE_LICENSES);
                dialog.setCancelable(false);
                dialog.show(activity.getSupportFragmentManager(), AboutDialog.TYPE_LICENSES);
                Log.d(TAG + "_DrawerMenuManager", "tap on about_menu_licenses");
                break;
            case R.id.about_menu_data_protection:
                dialog = AboutDialog.newInstance(AboutDialog.TYPE_DATA_PROTECTION);
                dialog.setCancelable(false);
                dialog.show(activity.getSupportFragmentManager(), AboutDialog.TYPE_DATA_PROTECTION);
                Log.d(TAG + "_DrawerMenuManager", "tap on about_menu_data_protection");
                break;
        }
    }

    private void makeVisiblyMainMenu() {
        main_menu.setVisibility(View.VISIBLE);
        version.setVisibility(View.VISIBLE);
        about_menu.setVisibility(View.GONE);
    }

    private void makeVisiblyAboutMenu() {
        main_menu.setVisibility(View.GONE);
        version.setVisibility(View.GONE);
        about_menu.setVisibility(View.VISIBLE);
    }

    public void showUpButton(boolean show) {
        // To keep states of ActionBar and ActionBarDrawerToggle synchronized,
        // when you enable on one, you disable on the other.
        // And as you may notice, the order for this operation is disable first, then enable - VERY VERY IMPORTANT.
        if (show) {
            //Запрещаяем выезжание меню справа
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            // Remove hamburger
            toggle.setDrawerIndicatorEnabled(false);
            // Show back button
            mActionBar.setDisplayHomeAsUpEnabled(true);
            // when DrawerToggle is disabled i.e. setDrawerIndicatorEnabled(false), navigation icon
            // clicks are disabled i.e. the UP button will not work.
            // We need to add a listener, as in below, so DrawerToggle will forward
            // click events to this listener.
            if (!mToolBarNavigationListenerIsRegistered) {
                toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.onBackPressed();
                    }
                });

                mToolBarNavigationListenerIsRegistered = true;
            }

        } else {
            //Разрешаем выезжание меню справа
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            // Remove back button
            mActionBar.setDisplayHomeAsUpEnabled(false);
            // Show hamburger
            toggle.setDrawerIndicatorEnabled(true);
            // Remove the/any drawer toggle listener
            toggle.setToolbarNavigationClickListener(null);
            mToolBarNavigationListenerIsRegistered = false;
        }

        // So, one may think "Hmm why not simplify to:
        // .....
        // getSupportActionBar().setDisplayHomeAsUpEnabled(enable);
        // mDrawer.setDrawerIndicatorEnabled(!enable);
        // ......
        // To re-iterate, the order in which you enable and disable views IS important #dontSimplify.
    }
}
