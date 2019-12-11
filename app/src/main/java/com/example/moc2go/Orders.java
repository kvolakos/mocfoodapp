package com.example.moc2go;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.moc2go.ui.buckstopmenu.BuckStopMenuFragment;
import com.example.moc2go.ui.home.HomeFragment;
import com.example.moc2go.ui.orders.OrdersFragment;

public class Orders extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orders_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, OrdersFragment.newInstance())
                    .commitNow();
        }

    }
}
