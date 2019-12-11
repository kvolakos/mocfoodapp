package com.example.moc2go;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.moc2go.ui.buckstopmenu.BuckStopMenuFragment;

public class BuckStopMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buck_stop_menu_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, BuckStopMenuFragment.newInstance())
                    .commitNow();
        }
    }
}
