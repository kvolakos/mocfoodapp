package com.example.moc2go.ui.orders;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moc2go.R;
import com.example.moc2go.database.Item;
import com.example.moc2go.database.ItemAdapter;
import com.example.moc2go.database.Order;
import com.example.moc2go.database.OrderAdapter;
import com.example.moc2go.database.SubmittedOrder;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrdersFragment extends Fragment {

    private OrdersViewModel mViewModel;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference orderRef = db.collection("users").document(
            "1000000").collection("order");
    private CollectionReference submitRef = db.collection("submittedOrders");
    private OrderAdapter adapter;

    public static OrdersFragment newInstance() {
        return new OrdersFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.orders_fragment, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_o);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(mLayoutManager);
        // recyclerView.setHasFixedSize(true);

        Query query = orderRef.orderBy("name", Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Order> options = new FirestoreRecyclerOptions.Builder<Order>()
                .setQuery(query, Order.class)
                .build();

        adapter = new OrderAdapter(options);

        recyclerView.setAdapter(adapter);

        FloatingActionButton submit = (FloatingActionButton) view.findViewById(R.id.submitFAB);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots.size() > 0) {
                            final List<DocumentSnapshot> ds = queryDocumentSnapshots.getDocuments();

                            Date current = new Date();

                            SubmittedOrder submission = new SubmittedOrder(false, current);

                            submitRef.add(submission).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    for (DocumentSnapshot doc : ds) {
                                        Order order = new Order(doc.getString("name"), doc.getString("price"), doc.getDouble("quantity").intValue());
                                        documentReference.collection("items").add(order);
                                    }
                                    Toast.makeText(view.getContext(), "Order submitted!", Toast.LENGTH_SHORT).show();
                                    for (DocumentSnapshot doc : ds) {
                                        orderRef.document(doc.getId()).delete();
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });

        orderRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("FRAG_E", "Woops, something went wrong!");
                }

                List<String> prices_str = new ArrayList<>();
                List<Integer> quants = new ArrayList<>();

                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    if (doc.get("name") != null) {
                        prices_str.add(doc.getString("price"));
                        quants.add(Integer.valueOf(doc.get("quantity").toString()));
                    }
                }

                int dollars = 0;
                int cents = 0;

                for (int i = 0; i < prices_str.size(); i++) {
                    String pr = prices_str.get(i);
                    int quant = quants.get(i);

                    dollars += Integer.parseInt(pr.substring(0, pr.indexOf('.'))) * quant;
                    cents += Integer.parseInt(pr.substring(pr.indexOf('.') + 1)) * quant;

                    while (cents >= 100) {
                        dollars += 1;
                        cents -= 100;
                    }
                }

                String final_price = "DEBUG";

                if(cents < 10) {
                    final_price = dollars + ".0" + cents;
                } else {
                    final_price = dollars + "." + cents;
                }

                TextView priceText = (TextView)view.findViewById(R.id.total_price);

                priceText.setText("Price: $" + final_price);

                Log.e("TEST", "Current prices: " + prices_str);
                Log.e("TEST", "Current quants: " + quants);
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(OrdersViewModel.class);
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
