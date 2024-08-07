package ru.softvillage.onlineseller.util;

import android.annotation.SuppressLint;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import lombok.Getter;
import lombok.Setter;
import ru.softvillage.onlineseller.R;

public class FragmentDispatcher {
    @Getter
    @Setter
    private boolean allowBack = true;

    @Getter
    @Setter
    private AppCompatActivity activity;

    @SuppressLint("LongLogTag")
    public void replaceFragment(Fragment fragment) {
        activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_holder, fragment)
                .addToBackStack(String.valueOf(fragment.getId()))
                .commit();
    }
}
