package com.example.moc2go.ui.tutuscybercafemenu;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.moc2go.R;
import com.example.moc2go.database.Item;
import com.example.moc2go.database.ItemAdapter;
import com.example.moc2go.ui.buckstopmenu.BuckStopMenuViewModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class TutusCyberCafeMenuFragment extends Fragment {

    private TutusCyberCafeMenuViewModel mViewModel;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference orderRef = db.collection("shops").document(
            "CyberCafe").collection("items");
    private ItemAdapter adapter;

    public static TutusCyberCafeMenuFragment newInstance() {
        return new TutusCyberCafeMenuFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tutus_cyber_cafe_menu_fragment, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_t);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // recyclerView.setHasFixedSize(true);

        Query query = orderRef.orderBy("name", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Item> options = new FirestoreRecyclerOptions.Builder<Item>()
                .setQuery(query, Item.class)
                .build();

        adapter = new ItemAdapter(options);

        recyclerView.setAdapter(adapter);

        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(TutusCyberCafeMenuViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public
    void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}