package com.example.moc2go;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.moc2go.ui.buckstopmenu.BuckStopMenuFragment;
import com.example.moc2go.ui.home.HomeFragment;
import com.example.moc2go.ui.restaurants.RestaurantsFragment;

public class Restaurants extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.restaurants_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, RestaurantsFragment.newInstance())
                    .commitNow();
        }
        final Button buckStopMenuBtn = findViewById(R.id.BuckStopMenu);
        buckStopMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Restaurants.this, BuckStopMenu.class);
                Restaurants.this.startActivity(myIntent);
            }
        });
    }
}
