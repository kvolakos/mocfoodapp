package com.example.moc2go.ui.restaurants;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.moc2go.BuckStopMenu;
import com.example.moc2go.GrillMasterMenu;
import com.example.moc2go.HappyPlaceMenu;
import com.example.moc2go.R;
import com.example.moc2go.TutusCyberCafeMenu;
import com.example.moc2go.UnderCafMenu;

public class RestaurantsFragment extends Fragment{

    private RestaurantsViewModel mViewModel;

    public static RestaurantsFragment newInstance() {
        return new RestaurantsFragment();
    }

    @Nullable

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.restaurants_fragment, container, false);
        Button buckStopMenuBtn = view.findViewById(R.id.BuckStopMenu);
        Button underCafMenuBtn =  view.findViewById(R.id.UnderCafMenu);
        Button tutusMenuBtn =  view.findViewById(R.id.tutusMenu);
        Button grillMasterMenuBtn =  view.findViewById(R.id.grillMasterMenu);
        Button happyPlaceMenuBtn =  view.findViewById(R.id.happyPlaceMenu);
       buckStopMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(),  BuckStopMenu.class);
                startActivity(intent);
            }
        });
        underCafMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),  UnderCafMenu.class);
                startActivity(intent);
            }
        });
        tutusMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(),  TutusCyberCafeMenu.class);
                startActivity(intent);
            }
        });
        grillMasterMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),  GrillMasterMenu.class);
                startActivity(intent);
            }
        });
        happyPlaceMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HappyPlaceMenu.class);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(RestaurantsViewModel.class);
        // TODO: Use the ViewModel
    }

}
