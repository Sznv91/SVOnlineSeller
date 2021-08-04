package ru.softvillage.onlineseller.ui;

import static ru.softvillage.onlineseller.AppSeller.TAG;
import static ru.softvillage.onlineseller.presenter.AuthPresenter.TEN_MINUTES_IN_MILLIS;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;

import lombok.Setter;
import ru.softvillage.onlineseller.R;
import ru.softvillage.onlineseller.presenter.AppPresenter;
import ru.softvillage.onlineseller.presenter.AuthPresenter;
import ru.softvillage.onlineseller.presenter.UiPresenter;
import ru.softvillage.onlineseller.ui.dialog.AboutDialog;

public class AuthFragmentV2 extends Fragment implements View.OnClickListener {
    public static String TAG_LOCAL = "_" + AuthFragmentV2.class.getSimpleName();
    @Setter
    private Handler timerHandler;
    @Setter
    private Runnable timerRun;
    private ConstraintLayout main_fragment_auth,
            auth_pin_layout,
            auth_load_kay_holder;
    private ImageView auth_big_logo,
            auth_pin_background;
    private View pin_divider_1,
            pin_divider_2,
            pin_divider_3,
            pin_divider_4;
    private TextView pin_number_1,
            pin_number_2,
            pin_number_3,
            pin_number_4,
            pin_number_5,
            expired_timer,
            auth_title_demo_mode,
            auth_title_progress;

    Observer<Integer> observer = this::changeColor;

    private AuthFragmentViewModel mViewModel;

    public static AuthFragmentV2 newInstance() {
        return new AuthFragmentV2();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_auth, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(AuthFragmentV2.this).get(AuthFragmentViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG + TAG_LOCAL, "onViewCreated() called with: view = [" + view + "], savedInstanceState = [" + savedInstanceState + "]");
        UiPresenter.getInstance().getDrawerManager().showUpButton(false);
        main_fragment_auth = view.findViewById(R.id.main_fragment_auth);
        auth_pin_layout = view.findViewById(R.id.auth_pin_layout);
        auth_big_logo = view.findViewById(R.id.auth_big_logo);
        auth_pin_background = view.findViewById(R.id.auth_pin_background);
        pin_divider_1 = view.findViewById(R.id.pin_divider_1);
        pin_divider_2 = view.findViewById(R.id.pin_divider_2);
        pin_divider_3 = view.findViewById(R.id.pin_divider_3);
        pin_divider_4 = view.findViewById(R.id.pin_divider_4);
        pin_number_1 = view.findViewById(R.id.pin_number_1);
        pin_number_2 = view.findViewById(R.id.pin_number_2);
        pin_number_3 = view.findViewById(R.id.pin_number_3);
        pin_number_4 = view.findViewById(R.id.pin_number_4);
        pin_number_5 = view.findViewById(R.id.pin_number_5);
        expired_timer = view.findViewById(R.id.expired_timer);
        auth_title_demo_mode = view.findViewById(R.id.auth_title_demo_mode);
        auth_load_kay_holder = view.findViewById(R.id.auth_load_kay_holder);
        auth_title_progress = view.findViewById(R.id.auth_title_progress);

        expired_timer.setOnClickListener(this);
        auth_title_demo_mode.setOnClickListener(this);

        UiPresenter.getInstance().getCurrentThemeLiveData().observe(getViewLifecycleOwner(), observer);
        fillToDisplayHeight();


        timerHandler = new Handler();
        timerRun = new Runnable() {
            @Override
            public void run() {
                if (timerRun != null && timerHandler != null) {
                    timeTicker(AuthPresenter.getInstance().getPinCreateTimeStamp(false));
                    timerHandler.postDelayed(
                            timerRun, 1000);
                }
            }
        };
        timerHandler.post(timerRun);
    }

    private void fillToDisplayHeight() {
        int displayHeight = getResources().getDisplayMetrics().heightPixels;
        int imageViewMargin = displayHeight / 7;
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) auth_big_logo.getLayoutParams();
        params.topMargin = imageViewMargin;
        auth_big_logo.setLayoutParams(params);
        auth_big_logo.requestLayout();
    }

    private void setPinOnUi(String pin) {
        pin_number_1.setText(pin.substring(0, 1));
        pin_number_2.setText(pin.substring(1, 2));
        pin_number_3.setText(pin.substring(2, 3));
        pin_number_4.setText(pin.substring(3, 4));
        pin_number_5.setText(pin.substring(4, 5));
    }

    private void changeColor(int themeStyle) {
        if (themeStyle == UiPresenter.THEME_LIGHT) {
            main_fragment_auth.setBackgroundColor(ContextCompat.getColor(main_fragment_auth.getContext(), R.color.background_lt));
            auth_pin_background.setImageDrawable(ContextCompat.getDrawable(auth_pin_background.getContext(), R.drawable.ic_pin_background_light));
            pin_divider_1.setBackgroundColor(ContextCompat.getColor(pin_divider_1.getContext(), R.color.divider_lt));
            pin_divider_2.setBackgroundColor(ContextCompat.getColor(pin_divider_2.getContext(), R.color.divider_lt));
            pin_divider_3.setBackgroundColor(ContextCompat.getColor(pin_divider_3.getContext(), R.color.divider_lt));
            pin_divider_4.setBackgroundColor(ContextCompat.getColor(pin_divider_4.getContext(), R.color.divider_lt));
            pin_number_1.setTextColor(ContextCompat.getColor(pin_number_1.getContext(), R.color.active_fonts_lt));
            pin_number_2.setTextColor(ContextCompat.getColor(pin_number_2.getContext(), R.color.active_fonts_lt));
            pin_number_3.setTextColor(ContextCompat.getColor(pin_number_3.getContext(), R.color.active_fonts_lt));
            pin_number_4.setTextColor(ContextCompat.getColor(pin_number_4.getContext(), R.color.active_fonts_lt));
            pin_number_5.setTextColor(ContextCompat.getColor(pin_number_5.getContext(), R.color.active_fonts_lt));
            auth_title_demo_mode.setBackground(ContextCompat.getDrawable(auth_title_demo_mode.getContext(), R.drawable.bg_dialog_black));
            auth_load_kay_holder.setBackgroundColor(ContextCompat.getColor(auth_load_kay_holder.getContext(), R.color.background_lt));
            auth_title_progress.setTextColor(ContextCompat.getColor(pin_number_1.getContext(), R.color.active_fonts_lt));
        } else {
            main_fragment_auth.setBackgroundColor(ContextCompat.getColor(main_fragment_auth.getContext(), R.color.main_dt));
            auth_pin_background.setImageDrawable(ContextCompat.getDrawable(auth_pin_background.getContext(), R.drawable.ic_pin_background_dark));
            pin_divider_1.setBackgroundColor(ContextCompat.getColor(pin_divider_1.getContext(), R.color.divider_dt));
            pin_divider_2.setBackgroundColor(ContextCompat.getColor(pin_divider_2.getContext(), R.color.divider_dt));
            pin_divider_3.setBackgroundColor(ContextCompat.getColor(pin_divider_3.getContext(), R.color.divider_dt));
            pin_divider_4.setBackgroundColor(ContextCompat.getColor(pin_divider_4.getContext(), R.color.divider_dt));
            pin_number_1.setTextColor(ContextCompat.getColor(pin_number_1.getContext(), R.color.active_fonts_dt));
            pin_number_2.setTextColor(ContextCompat.getColor(pin_number_2.getContext(), R.color.active_fonts_dt));
            pin_number_3.setTextColor(ContextCompat.getColor(pin_number_3.getContext(), R.color.active_fonts_dt));
            pin_number_4.setTextColor(ContextCompat.getColor(pin_number_4.getContext(), R.color.active_fonts_dt));
            pin_number_5.setTextColor(ContextCompat.getColor(pin_number_5.getContext(), R.color.active_fonts_dt));
            auth_title_demo_mode.setBackground(ContextCompat.getDrawable(auth_title_demo_mode.getContext(), R.drawable.bg_dialog));
            auth_load_kay_holder.setBackgroundColor(ContextCompat.getColor(auth_load_kay_holder.getContext(), R.color.main_dt));
            auth_title_progress.setTextColor(ContextCompat.getColor(pin_number_1.getContext(), R.color.active_fonts_dt));
        }
        auth_title_demo_mode.setTextColor(ContextCompat.getColor(auth_title_demo_mode.getContext(), R.color.header_lt));
    }

    @SuppressLint({"NonConstantResourceId", "LongLogTag"})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.expired_timer:
                AboutDialog dialog = AboutDialog.newInstance(AboutDialog.TYPE_AUTH_WHAT_I_DO);
                dialog.show(getChildFragmentManager(), AboutDialog.TYPE_AUTH_WHAT_I_DO);
                break;
            case R.id.auth_title_demo_mode:
                Log.d(TAG + TAG_LOCAL, "click on demo-mode title");
                AuthPresenter.getInstance().setFirstStageAuth(true);
                AppPresenter.getInstance().populateDemoUser(true);
                break;
        }
    }

    private void timeTicker(String generateTime) {
        if (generateTime.equals(AuthPresenter.KEY_NOT_RECEIVED)) {
            AuthPresenter.getInstance().getPinCreateTimeStamp(true);
            return;
        }
        Long now = DateTime.now().getMillis();
        String sNow = String.valueOf(now);
        String[] sGenerateTimeArray = generateTime.split("\\.");
        String sGenerateTime = sGenerateTimeArray[0] + sGenerateTimeArray[1];
        sGenerateTime = sGenerateTime.substring(0, sNow.length());

        Long millisGenerateTime = Long.parseLong(sGenerateTime);
        int minutes = 0;
        int seconds = 0;
        long delta = now - millisGenerateTime + 1000;
        delta = TEN_MINUTES_IN_MILLIS - delta;
        int baseData = (int) delta / 1000;

        minutes = baseData / 60;
        seconds = baseData - (minutes * 60);
        if (baseData < 0) {
            AuthPresenter.getInstance().getPinCreateTimeStamp(true);
            requireActivity().runOnUiThread(() -> auth_load_kay_holder.setVisibility(View.VISIBLE));
            return;
        }
        setPinOnUi(AuthPresenter.getInstance().getPin());
        int finalMinutes = minutes;
        int finalSeconds = seconds;
        try {
            getActivity().runOnUiThread(() -> {
                expired_timer.setText(String.format(getString(R.string.minute_second_format), finalMinutes, finalSeconds));
                auth_load_kay_holder.setVisibility(View.GONE);
            });
        } catch (NullPointerException e) {
            //ignore exception
        }
    }

}