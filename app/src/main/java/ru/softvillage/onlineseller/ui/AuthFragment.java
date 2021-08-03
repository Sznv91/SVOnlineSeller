package ru.softvillage.onlineseller.ui;

import static ru.softvillage.onlineseller.AppSeller.TAG;

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

import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.softvillage.onlineseller.AppSeller;
import ru.softvillage.onlineseller.R;
import ru.softvillage.onlineseller.network.auth.entity.ReceiveToApp;
import ru.softvillage.onlineseller.network.auth.entity.SendFromApp;
import ru.softvillage.onlineseller.presenter.AppPresenter;
import ru.softvillage.onlineseller.presenter.AuthPresenter;
import ru.softvillage.onlineseller.presenter.UiPresenter;
import ru.softvillage.onlineseller.ui.dialog.AboutDialog;
import ru.softvillage.onlineseller.util.Md5Calc;

public class AuthFragment extends Fragment implements View.OnClickListener {
    public static String TAG_LOCAL = "_" + AuthFragment.class.getSimpleName();
    private Handler timerHandler;
    private Runnable timerRun;
    private ConstraintLayout main_fragment_auth,
            auth_pin_layout;
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
            auth_title_demo_mode;

    Observer<Integer> observer = this::changeColor;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public AuthFragment() {
        // Required empty public constructor
    }

    public static AuthFragment newInstance(String param1, String param2) {
        AuthFragment fragment = new AuthFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_auth, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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

        expired_timer.setOnClickListener(this);
        auth_title_demo_mode.setOnClickListener(this);

        UiPresenter.getInstance().getCurrentThemeLiveData().observe(getViewLifecycleOwner(), observer);

        fillToDisplayHeight();
        getRegCode();


        timerHandler = new Handler();
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
        }
        auth_title_demo_mode.setTextColor(ContextCompat.getColor(auth_title_demo_mode.getContext(), R.color.header_lt));
    }

    @Override
    public void onDestroy() {
        UiPresenter.getInstance().getCurrentThemeLiveData().removeObserver(observer);
        super.onDestroy();
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

    private void getRegCode() {
        SendFromApp data = SendFromApp.builder()
                .deviceId(Md5Calc.getHash(AuthPresenter.getInstance().getFireBaseToken()))
                .fireBaseToken(AuthPresenter.getInstance().getFireBaseToken())
                .build();
        AppSeller.getInstance().getNetworkAuthService().registrationDevice(data).enqueue(new Callback<ReceiveToApp>() {
            @Override
            public void onResponse(Call<ReceiveToApp> call, Response<ReceiveToApp> response) {
                setPinOnUi(response.body().getAuthCode());
                timerRun = new Runnable() {
                    @Override
                    public void run() {
                        if (timerRun != null && timerHandler != null) {
                            timeTicker(response.body().getGenerateTime());
                            timerHandler.postDelayed(timerRun, 1000);
                        }

                    }
                };
                timerHandler.postDelayed(timerRun, 1000);

                Log.d(TAG + TAG_LOCAL, "onResponse() called with: call = [" + call + "], response = [" + response + "]");
            }

            @Override
            public void onFailure(Call<ReceiveToApp> call, Throwable t) {
                Log.d(TAG + TAG_LOCAL, "onFailure() called with: call = [" + call + "], t = [" + t + "]");
            }
        });
    }

    private void timeTicker(String generateTime) {
        Long now = DateTime.now().getMillis();
        String sNow = String.valueOf(now);
        String[] sGenerateTimeArray = generateTime.split("\\.");
        String sGenerateTime = sGenerateTimeArray[0] + sGenerateTimeArray[1];
        sGenerateTime = sGenerateTime.substring(0, sNow.length());

        Long millisGenerateTime = Long.parseLong(sGenerateTime);
        int minutes = 0;
        int seconds = 0;
        int baseData = (int) ((now - millisGenerateTime) / 1000);
        minutes = baseData / 60;
        seconds = baseData - (minutes * 60);
        int finalMinutes = minutes;
        int finalSeconds = seconds;
        try {
            getActivity().runOnUiThread(() -> expired_timer.setText(String.format(getString(R.string.minute_second_format), finalMinutes, finalSeconds)));
        } catch (NullPointerException e) {
            //ignore exception
        }
    }
}