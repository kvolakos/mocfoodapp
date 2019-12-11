package com.example.moc2go;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.moc2go.ui.undercafmenu.UnderCafMenuFragment;

public class UnderCafMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.under_caf_menu_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, UnderCafMenuFragment.newInstance())
                    .commitNow();
        }
    }
}
