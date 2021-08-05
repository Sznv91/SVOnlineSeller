package ru.softvillage.onlineseller.ui.recyclerView;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import org.jetbrains.annotations.NotNull;

import ru.softvillage.onlineseller.R;
import ru.softvillage.onlineseller.dataBase.entity.LocalUser;
import ru.softvillage.onlineseller.presenter.UiPresenter;

public class UserViewHolder extends RecyclerView.ViewHolder {
    private ConstraintLayout main;
    private TextView user_name,
            last_auth_date,
            content_last_auth_date;
    private boolean isSelect = false;


    Observer<Integer> colorThemeObserver = this::changeColor;

    public UserViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        main = itemView.findViewById(R.id.main);
        user_name = itemView.findViewById(R.id.user_name);
        last_auth_date = itemView.findViewById(R.id.title_last_auth_date);
        content_last_auth_date = itemView.findViewById(R.id.content_last_auth_date);

        UiPresenter.getInstance().getCurrentThemeLiveData().observeForever(colorThemeObserver);
    }

    public void bind(LocalUser user) {
        user_name.setText(String.format("%s %s %s", user.getSurname(), user.getName(), user.getPatronymic()));
        if (user.getLastDateAuth() != null) {
            content_last_auth_date.setText(user.getLastDateAuth().toString("dd.MM.YYYY"));
        } else {
            content_last_auth_date.setText("не производился");
        }
    }

    public void unBind() {
        UiPresenter.getInstance().getCurrentThemeLiveData().removeObserver(colorThemeObserver);
    }

    public void setUnSelect() {
        isSelect = false;
        changeColor(UiPresenter.getInstance().getCurrentTheme());
    }

    public void setSelect() {
        isSelect = true;
        changeColor(UiPresenter.getInstance().getCurrentTheme());
    }

    private void changeColor(int colorStyle) {
        if (colorStyle == UiPresenter.THEME_LIGHT) {
            if (isSelect) {
                main.setBackgroundColor(ContextCompat.getColor(main.getContext(), R.color.background_dt));
                user_name.setTextColor(ContextCompat.getColor(user_name.getContext(), R.color.fonts_dt));
                last_auth_date.setTextColor(ContextCompat.getColor(last_auth_date.getContext(), R.color.active_fonts_dt));
                content_last_auth_date.setTextColor(ContextCompat.getColor(user_name.getContext(), R.color.fonts_dt));
            } else {
                main.setBackgroundColor(ContextCompat.getColor(main.getContext(), R.color.background_lt));
                user_name.setTextColor(ContextCompat.getColor(user_name.getContext(), R.color.fonts_lt));
                last_auth_date.setTextColor(ContextCompat.getColor(last_auth_date.getContext(), R.color.active_fonts_lt));
                content_last_auth_date.setTextColor(ContextCompat.getColor(user_name.getContext(), R.color.fonts_lt));
            }
        } else {
            if (isSelect) {
                main.setBackgroundColor(ContextCompat.getColor(main.getContext(), R.color.background_lt));
                user_name.setTextColor(ContextCompat.getColor(user_name.getContext(), R.color.fonts_lt));
                last_auth_date.setTextColor(ContextCompat.getColor(last_auth_date.getContext(), R.color.fonts_lt));
            } else {
                main.setBackgroundColor(ContextCompat.getColor(main.getContext(), R.color.background_dt));
                user_name.setTextColor(ContextCompat.getColor(user_name.getContext(), R.color.fonts_dt));
                last_auth_date.setTextColor(ContextCompat.getColor(last_auth_date.getContext(), R.color.fonts_dt));
            }

        }
    }
}
