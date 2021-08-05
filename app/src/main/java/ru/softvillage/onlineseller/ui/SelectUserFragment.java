package ru.softvillage.onlineseller.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import ru.softvillage.onlineseller.AppSeller;
import ru.softvillage.onlineseller.R;
import ru.softvillage.onlineseller.dataBase.entity.LocalUser;
import ru.softvillage.onlineseller.presenter.AuthPresenter;
import ru.softvillage.onlineseller.presenter.UiPresenter;
import ru.softvillage.onlineseller.ui.recyclerView.UserItemAdapter;

public class SelectUserFragment extends Fragment {

    private ConstraintLayout main;
    private TextView title_select_user,
            button_continue;
    private RecyclerView user_select_recycler;

    private final Observer<LocalUser> lastSelectUserObserver = localUser ->
            changeTheme(UiPresenter.getInstance().getCurrentTheme());

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public SelectUserFragment() {
        // Required empty public constructor
    }

    public static SelectUserFragment newInstance(String param1, String param2) {
        SelectUserFragment fragment = new SelectUserFragment();
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
        return inflater.inflate(R.layout.fragment_select_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UiPresenter.getInstance().getDrawerManager().showUpButton(false);
        main = view.findViewById(R.id.main);
        title_select_user = view.findViewById(R.id.title_select_user);
        button_continue = view.findViewById(R.id.button_continue);
        user_select_recycler = view.findViewById(R.id.user_select_recycler);

        UiPresenter.getInstance().getCurrentThemeLiveData().observe(this.getViewLifecycleOwner(), colorObserver);
        initButtons();
        initRecycler();
    }

    private void initRecycler() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        user_select_recycler.setLayoutManager(layoutManager);
        @SuppressLint("LongLogTag")
        UserItemAdapter adapter = new UserItemAdapter(LayoutInflater.from(getContext()),
                user -> {
                    Log.d(AppSeller.TAG + "_SelectUserFragment", "adapter callback. Click on user: " + user.toString());
                });
        DividerItemDecoration divider = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(getContext().getDrawable(R.drawable.line_divider));
        user_select_recycler.addItemDecoration(divider);

        user_select_recycler.setAdapter(adapter);
        AppSeller.getInstance().getDbHelper().getDataBase().userDao().getAllUserLiveData().observe(this.getViewLifecycleOwner(), adapter::setItems);
    }

    @SuppressLint("LongLogTag")
    private void initButtons() {
        button_continue.setOnClickListener(v -> {
            Log.d(AppSeller.TAG + "_SelectUserFragment", "tap On Button Continue ");
            if (AuthPresenter.getInstance().getLastSelectUserLiveData().getValue() != null) {
                PinUserFragment pinUserFragment = PinUserFragment.newInstance(null, null);
                AppSeller.getInstance().getFragmentDispatcher().replaceFragment(pinUserFragment);
                Log.d(AppSeller.TAG + "_SelectUserFragment", "selected user " + AuthPresenter.getInstance().getLastSelectUserLiveData().getValue().toString());
            }
        });
    }

    Observer<Integer> colorObserver = this::changeTheme;

    private void changeTheme(int colorStyle) {
        AuthPresenter.getInstance().getLastSelectUserLiveData().observe(this.getViewLifecycleOwner(), lastSelectUserObserver);
        if (colorStyle == UiPresenter.THEME_LIGHT) {
            main.setBackgroundColor(ContextCompat.getColor(main.getContext(), R.color.main_lt));
            title_select_user.setTextColor(ContextCompat.getColor(title_select_user.getContext(), R.color.active_fonts_lt));
            if (AuthPresenter.getInstance().getLastSelectUserLiveData().getValue() != null) {
                button_continue.setTextColor(ContextCompat.getColor(button_continue.getContext(), R.color.icon_dt));
            } else {
                button_continue.setTextColor(ContextCompat.getColor(button_continue.getContext(), R.color.active_fonts_lt));
            }
        } else {
            main.setBackgroundColor(ContextCompat.getColor(main.getContext(), R.color.main_dt));
            title_select_user.setTextColor(ContextCompat.getColor(title_select_user.getContext(), R.color.active_fonts_dt));
            if (AuthPresenter.getInstance().getLastSelectUserLiveData().getValue() != null) {
                button_continue.setTextColor(ContextCompat.getColor(button_continue.getContext(), R.color.icon_dt));
            } else {
                button_continue.setTextColor(ContextCompat.getColor(button_continue.getContext(), R.color.active_fonts_dt));
            }
        }

    }
}