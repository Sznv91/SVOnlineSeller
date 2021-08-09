package ru.softvillage.onlineseller.ui;

import static ru.softvillage.onlineseller.AppSeller.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import ru.softvillage.onlineseller.presenter.AppPresenter;
import ru.softvillage.onlineseller.presenter.AuthPresenter;
import ru.softvillage.onlineseller.presenter.UiPresenter;
import ru.softvillage.onlineseller.service.BackendSyncService;
import ru.softvillage.onlineseller.ui.recyclerView.UserItemAdapter;

public class SelectUserFragment extends Fragment implements UiPresenter.ISelectUserFragment {
    private static final String LOCAL_TAG = "_" + SelectUserFragment.class.getSimpleName();

    private ConstraintLayout main,
            load_from_network_holder,
            data_holder,
            no_users_holder;
    private TextView title_select_user,
            button_continue,
            load_from_network_title,
            no_users_title;
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
    public void onDestroyView() {
        UiPresenter.getInstance().setISelectUserFragment(null);
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UiPresenter.getInstance().getDrawerManager().showUpButton(false);
        UiPresenter.getInstance().setISelectUserFragment(this);

        main = view.findViewById(R.id.main);
        load_from_network_holder = view.findViewById(R.id.load_from_network_holder);
        data_holder = view.findViewById(R.id.data_holder);
        no_users_holder = view.findViewById(R.id.no_users_holder);
        title_select_user = view.findViewById(R.id.title_select_user);
        button_continue = view.findViewById(R.id.button_continue);
        load_from_network_title = view.findViewById(R.id.load_from_network_title);
        no_users_title = view.findViewById(R.id.no_users_title);
        user_select_recycler = view.findViewById(R.id.user_select_recycler);

        UiPresenter.getInstance().getCurrentThemeLiveData().observe(this.getViewLifecycleOwner(), colorObserver);
        initButtons();
        initRecycler();

        if (AppPresenter.getInstance().isNeedLoadUserFromNetwork()) networkLoadHolder(true);
    }

    private void startSyncService() {
        if (!AppSeller.isMyServiceRunning(BackendSyncService.class)) {
            Intent startIntent = new Intent(AppSeller.getInstance().getApplicationContext(), BackendSyncService.class);
            startIntent.setAction("start");
            AppSeller.getInstance().startService(startIntent);
        }
    }

    private void initRecycler() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        user_select_recycler.setLayoutManager(layoutManager);
        @SuppressLint("LongLogTag")
        UserItemAdapter adapter = new UserItemAdapter(LayoutInflater.from(getContext()),
                user -> {
                    Log.d(TAG + "_SelectUserFragment", "adapter callback. Click on user: " + user.toString());
                });
        DividerItemDecoration divider = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(getContext().getDrawable(R.drawable.line_divider));
        user_select_recycler.addItemDecoration(divider);

        user_select_recycler.setAdapter(adapter);
        AppSeller.getInstance().getDbHelper().getDataBase().userDao().getAllUserLiveData().observe(this.getViewLifecycleOwner(), entityList -> {
            if (entityList.size() == 0) {
                no_users_holder.setVisibility(View.VISIBLE);
            } else {
                no_users_holder.setVisibility(View.GONE);
                adapter.setItems(entityList);
                networkLoadHolder(false);
            }
        });
    }

    @SuppressLint("LongLogTag")
    private void initButtons() {
        button_continue.setOnClickListener(v -> {
            Log.d(TAG + "_SelectUserFragment", "tap On Button Continue ");
            if (AuthPresenter.getInstance().getLastSelectUserLiveData().getValue() != null) {
                PinUserFragment pinUserFragment = PinUserFragment.newInstance(null, null);
                AppSeller.getInstance().getFragmentDispatcher().replaceFragment(pinUserFragment);
                Log.d(TAG + "_SelectUserFragment", "selected user " + AuthPresenter.getInstance().getLastSelectUserLiveData().getValue().toString());
            }
        });
    }

    Observer<Integer> colorObserver = this::changeTheme;

    private void changeTheme(int colorStyle) {
        AuthPresenter.getInstance().getLastSelectUserLiveData().observe(this.getViewLifecycleOwner(), lastSelectUserObserver);
        if (colorStyle == UiPresenter.THEME_LIGHT) {
            main.setBackgroundColor(ContextCompat.getColor(main.getContext(), R.color.main_lt));
            load_from_network_holder.setBackgroundColor(ContextCompat.getColor(load_from_network_holder.getContext(), R.color.main_lt));
            no_users_holder.setBackgroundColor(ContextCompat.getColor(no_users_holder.getContext(), R.color.main_lt));
            title_select_user.setTextColor(ContextCompat.getColor(title_select_user.getContext(), R.color.active_fonts_lt));
            load_from_network_title.setTextColor(ContextCompat.getColor(title_select_user.getContext(), R.color.fonts_lt));
            no_users_title.setTextColor(ContextCompat.getColor(no_users_title.getContext(), R.color.active_fonts_lt));
            if (AuthPresenter.getInstance().getLastSelectUserLiveData().getValue() != null) {
                button_continue.setTextColor(ContextCompat.getColor(button_continue.getContext(), R.color.icon_dt));
            } else {
                button_continue.setTextColor(ContextCompat.getColor(button_continue.getContext(), R.color.active_fonts_lt));
            }
        } else {
            main.setBackgroundColor(ContextCompat.getColor(main.getContext(), R.color.main_dt));
            load_from_network_holder.setBackgroundColor(ContextCompat.getColor(load_from_network_holder.getContext(), R.color.main_dt));
            no_users_holder.setBackgroundColor(ContextCompat.getColor(no_users_holder.getContext(), R.color.main_dt));
            title_select_user.setTextColor(ContextCompat.getColor(title_select_user.getContext(), R.color.active_fonts_dt));
            load_from_network_title.setTextColor(ContextCompat.getColor(title_select_user.getContext(), R.color.fonts_dt));
            no_users_title.setTextColor(ContextCompat.getColor(no_users_title.getContext(), R.color.active_fonts_dt));
            if (AuthPresenter.getInstance().getLastSelectUserLiveData().getValue() != null) {
                button_continue.setTextColor(ContextCompat.getColor(button_continue.getContext(), R.color.icon_dt));
            } else {
                button_continue.setTextColor(ContextCompat.getColor(button_continue.getContext(), R.color.active_fonts_dt));
            }
        }

    }

    @Override
    public void networkLoadHolder(boolean needShowLoader) {
        requireActivity().runOnUiThread(() -> {
            if (needShowLoader) {
                data_holder.setVisibility(View.GONE);
                load_from_network_holder.setVisibility(View.VISIBLE);
            } else {
                data_holder.setVisibility(View.VISIBLE);
                load_from_network_holder.setVisibility(View.GONE);
            }
        });
        if (needShowLoader) startSyncService();
    }

    @Override
    public void roomLoadShowHolder() {

    }
}