package ru.softvillage.onlineseller.ui;

import static ru.softvillage.onlineseller.AppSeller.TAG;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import org.jetbrains.annotations.NotNull;
import org.joda.time.LocalDate;

import ru.softvillage.onlineseller.AppSeller;
import ru.softvillage.onlineseller.R;
import ru.softvillage.onlineseller.dataBase.entity.LocalUser;
import ru.softvillage.onlineseller.presenter.AuthPresenter;
import ru.softvillage.onlineseller.presenter.UiPresenter;
import ru.softvillage.onlineseller.util.Md5Calc;

public class PinUserFragment extends Fragment {
    private ConstraintLayout main;
    private EditText pin_edit_text;
    private TextView title_enter_pin,
            user_name,
            title_incorrect_pin,
            button_back,
            button_auth,

    user_pin_title,
            pin_number_1,
            pin_number_2,
            pin_number_3,
            pin_number_4;
    private ImageView auth_pin_background;
    private View pin_divider_1,
            pin_divider_2,
            pin_divider_3;
    private Observer<Integer> colorStyleObserver = this::changeTheme;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public PinUserFragment() {
        // Required empty public constructor
    }

    public static PinUserFragment newInstance(String param1, String param2) {
        PinUserFragment fragment = new PinUserFragment();
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
        return inflater.inflate(R.layout.fragment_pin_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UiPresenter.getInstance().getDrawerManager().showUpButton(true);
        main = view.findViewById(R.id.main);
        pin_edit_text = view.findViewById(R.id.pin_edit_text);
        title_enter_pin = view.findViewById(R.id.title_enter_pin);
        user_name = view.findViewById(R.id.user_name);
        title_incorrect_pin = view.findViewById(R.id.title_incorrect_pin);
        button_back = view.findViewById(R.id.button_back);
        button_auth = view.findViewById(R.id.button_auth);

        user_pin_title = view.findViewById(R.id.user_pin_title);
        pin_number_1 = view.findViewById(R.id.pin_number_1);
        pin_number_2 = view.findViewById(R.id.pin_number_2);
        pin_number_3 = view.findViewById(R.id.pin_number_3);
        pin_number_4 = view.findViewById(R.id.pin_number_4);
        pin_divider_1 = view.findViewById(R.id.pin_divider_1);
        pin_divider_2 = view.findViewById(R.id.pin_divider_2);
        pin_divider_3 = view.findViewById(R.id.pin_divider_3);
        auth_pin_background = view.findViewById(R.id.auth_pin_background);

        initText();
        changeTheme(UiPresenter.getInstance().getCurrentTheme());
//        UiPresenter.getInstance().getCurrentThemeLiveData().observe(this.getViewLifecycleOwner(), colorStyleObserver);
        initButtons();
    }

    private void initButtons() {
        pin_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                title_incorrect_pin.setVisibility(View.GONE);
                Log.d(TAG, "onTextChanged: s:" + s.toString());
                changePinView(s.length());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        pin_edit_text.setOnEditorActionListener((v, actionId, event) -> {
            clickAuthButton(v);
            return false;
        });
        button_auth.setOnClickListener(v -> clickAuthButton(pin_edit_text));
        button_back.setOnClickListener(v -> requireActivity().onBackPressed());
    }

    @SuppressLint("LongLogTag")
    private void clickAuthButton(TextView v) {
        String enteredPin = Md5Calc.getHash(v.getText().toString());
        if (enteredPin.equals(AuthPresenter.getInstance().getLastSelectUserLiveData().getValue().getPin())) {
            Toast.makeText(v.getContext(), "pin верный" + "\n" + enteredPin, Toast.LENGTH_SHORT).show();
            LocalUser user = AuthPresenter.getInstance().getLastSelectUserLiveData().getValue();
            user.setLastDateAuth(LocalDate.now());
            AppSeller.getInstance().getDbHelper().updateUser(user);
        } else {
            pin_edit_text.setText("");
            title_incorrect_pin.setVisibility(View.VISIBLE);
        }
        Log.d(TAG + "_PinUserFragment", "clickAuthButton. v.getText(): " + v.getText() + "\n HashCalc: " + Md5Calc.getHash(v.getText().toString()));
    }

    private void initText() {
        LocalUser user = AuthPresenter.getInstance().getLastSelectUserLiveData().getValue();
        user_name.setText(String.format("%s %s %s", user.getSurname(), user.getName(), user.getPatronymic()));
    }

    private void changeTheme(int colorTheme) {
        if (colorTheme == UiPresenter.THEME_LIGHT) {
            main.setBackgroundColor(ContextCompat.getColor(main.getContext(), R.color.background_lt));
            title_enter_pin.setTextColor(ContextCompat.getColor(title_enter_pin.getContext(), R.color.fonts_lt));
            user_name.setTextColor(ContextCompat.getColor(user_name.getContext(), R.color.fonts_lt));

            user_pin_title.setTextColor(ContextCompat.getColor(user_pin_title.getContext(), R.color.active_fonts_lt));
            pin_number_1.setTextColor(ContextCompat.getColor(pin_number_1.getContext(), R.color.active_fonts_lt));
            pin_number_2.setTextColor(ContextCompat.getColor(pin_number_2.getContext(), R.color.active_fonts_lt));
            pin_number_3.setTextColor(ContextCompat.getColor(pin_number_3.getContext(), R.color.active_fonts_lt));
            pin_number_4.setTextColor(ContextCompat.getColor(pin_number_4.getContext(), R.color.active_fonts_lt));
            pin_divider_1.setBackgroundColor(ContextCompat.getColor(pin_divider_1.getContext(), R.color.divider_lt));
            pin_divider_2.setBackgroundColor(ContextCompat.getColor(pin_divider_2.getContext(), R.color.divider_lt));
            pin_divider_3.setBackgroundColor(ContextCompat.getColor(pin_divider_3.getContext(), R.color.divider_lt));
            auth_pin_background.setBackgroundColor(ContextCompat.getColor(auth_pin_background.getContext(), R.color.main_lt));
        } else {
            main.setBackgroundColor(ContextCompat.getColor(main.getContext(), R.color.main_dt));
            title_enter_pin.setTextColor(ContextCompat.getColor(title_enter_pin.getContext(), R.color.fonts_dt));
            user_name.setTextColor(ContextCompat.getColor(user_name.getContext(), R.color.fonts_dt));

            user_pin_title.setTextColor(ContextCompat.getColor(user_pin_title.getContext(), R.color.active_fonts_dt));
            pin_number_1.setTextColor(ContextCompat.getColor(pin_number_1.getContext(), R.color.active_fonts_dt));
            pin_number_2.setTextColor(ContextCompat.getColor(pin_number_2.getContext(), R.color.active_fonts_dt));
            pin_number_3.setTextColor(ContextCompat.getColor(pin_number_3.getContext(), R.color.active_fonts_dt));
            pin_number_4.setTextColor(ContextCompat.getColor(pin_number_4.getContext(), R.color.active_fonts_dt));
            pin_divider_1.setBackgroundColor(ContextCompat.getColor(pin_divider_1.getContext(), R.color.divider_dt));
            pin_divider_2.setBackgroundColor(ContextCompat.getColor(pin_divider_2.getContext(), R.color.divider_dt));
            pin_divider_3.setBackgroundColor(ContextCompat.getColor(pin_divider_3.getContext(), R.color.divider_dt));
            auth_pin_background.setBackgroundColor(ContextCompat.getColor(auth_pin_background.getContext(), R.color.background_dt));
        }
    }

    private void changePinView(int pinLength) {
        switch (pinLength) {
            case 0:
                pin_number_1.setText("_");
                pin_number_2.setText("_");
                pin_number_3.setText("_");
                pin_number_4.setText("_");
                break;
            case 1:
                pin_number_1.setText("*");
                pin_number_2.setText("_");
                pin_number_3.setText("_");
                pin_number_4.setText("_");
                break;
            case 2:
                pin_number_1.setText("*");
                pin_number_2.setText("*");
                pin_number_3.setText("_");
                pin_number_4.setText("_");
                break;
            case 3:
                pin_number_1.setText("*");
                pin_number_2.setText("*");
                pin_number_3.setText("*");
                pin_number_4.setText("_");
                break;
            case 4:
                pin_number_1.setText("*");
                pin_number_2.setText("*");
                pin_number_3.setText("*");
                pin_number_4.setText("*");
                break;
        }
    }
}