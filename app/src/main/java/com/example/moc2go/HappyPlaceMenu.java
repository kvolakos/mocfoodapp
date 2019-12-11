package com.example.moc2go;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.moc2go.ui.happyplacemenu.HappyPlaceMenuFragment;

public class HappyPlaceMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.happy_place_menu_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, HappyPlaceMenuFragment.newInstance())
                    .commitNow();
        }
    }
}
