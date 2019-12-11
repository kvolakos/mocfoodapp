package com.example.moc2go;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.moc2go.ui.tutuscybercafemenu.TutusCyberCafeMenuFragment;

public class TutusCyberCafeMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutus_cyber_cafe_menu_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, TutusCyberCafeMenuFragment.newInstance())
                    .commitNow();
        }
    }
}
