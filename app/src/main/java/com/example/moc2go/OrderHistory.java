package com.example.moc2go;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.moc2go.ui.orderhistory.OrderHistoryFragment;

public class OrderHistory extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_history_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, OrderHistoryFragment.newInstance())
                    .commitNow();
        }
    }
}
