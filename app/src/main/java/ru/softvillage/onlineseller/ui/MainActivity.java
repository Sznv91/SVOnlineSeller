package ru.softvillage.onlineseller.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import ru.softvillage.onlineseller.R;
import ru.softvillage.onlineseller.ui.left_menu.DrawerMenuManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DrawerMenuManager<MainActivity> manager = new DrawerMenuManager<>(this); // Инициализация бокового меню
    }
}