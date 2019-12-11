package com.example.moc2go.database;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moc2go.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class SubmittedOrderAdapter extends FirestoreRecyclerAdapter<SubmittedOrder, SubmittedOrderAdapter.SubmittedOrderHolder> {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference orderRef = db.collection("submittedOrders");

    public SubmittedOrderAdapter(@NonNull FirestoreRecyclerOptions options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final SubmittedOrderHolder holder, int position, @NonNull SubmittedOrder model) {
        orderRef.whereEqualTo("submitted", model.getSubmitted()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.size() > 0) {
                    final List<String> itemList = new ArrayList<>();
                    final List<String> priceList = new ArrayList<>();
                    final List<Integer> quantList = new ArrayList<>();

                    DocumentSnapshot ds = queryDocumentSnapshots.getDocuments().get(0);

                    if(ds.getBoolean("fulfilled")) {
                        holder.cardView.setCardBackgroundColor(Color.rgb(200, 255, 200));
                    } else {
                        holder.cardView.setCardBackgroundColor(Color.rgb(255, 255, 200));
                    }

                    CollectionReference itemsRef = orderRef.document(ds.getId()).collection("items");
                    itemsRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                            for (DocumentSnapshot doc : queryDocumentSnapshots) {
                                if (doc.exists()) {
                                    itemList.add(doc.getString("name"));
                                    int quant = doc.getDouble("quantity").intValue();
                                    priceList.add(doc.getString("price"));
                                    quantList.add(quant);
                                }
                            }

                            int dollars = 0;
                            int cents = 0;

                            for (int i = 0; i < priceList.size(); i++) {
                                String pr = priceList.get(i);
                                int quant = quantList.get(i);

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

                            String itemText = itemList.remove(0) + " x" + quantList.remove(0);

                            for (String i: itemList) {
                                itemText = itemText + ", " + i + " x" + quantList.remove(0);
                            }

                            holder.textViewPrice.setText(final_price);
                            holder.textViewDesc.setText(itemText);
                        }


                    });

                } else {
                    holder.textViewPrice.setText("...");
                    holder.textViewDesc.setText("Loading...");
                }
            }
        });

        holder.textViewDate.setText(model.getSubmittedStr());
    }

    @NonNull
    @Override
    public SubmittedOrderHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sub_order_item,
                viewGroup, false);

        final SubmittedOrderHolder newHolder = new SubmittedOrderHolder(v);

        setOnClickListener(newClickListener);
        return newHolder;
    }

    class SubmittedOrderHolder extends RecyclerView.ViewHolder {
        TextView textViewDate;
        TextView textViewPrice;
        TextView textViewDesc;
        CardView cardView;

        public SubmittedOrderHolder(@NonNull View itemView) {
            super(itemView);
            textViewDate = itemView.findViewById(R.id.text_view_date);
            textViewPrice = itemView.findViewById(R.id.text_view_price);
            textViewDesc = itemView.findViewById(R.id.text_view_desc);
            cardView = itemView.findViewById(R.id.sub_order_item);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onItemClick(v, getAdapterPosition());
                }
            });
        }
                }

    private ClickListener mClickListener;

    public interface ClickListener {
        public void onItemClick(View view, int position);
    }

    public void setOnClickListener(ClickListener clickListener) {
        mClickListener = clickListener;
    }

    private ClickListener newClickListener = new ClickListener() {

        @Override
        public void onItemClick(final View view, int position) {
            // orderRef: submittedOrders, where it should come from
            // submitRef: users/1000000/order, where each item goes

            Log.e("help", "CLICK!");

            TextView dateTextView = view.findViewById(R.id.text_view_date);
            String dateText = dateTextView.getText().toString();

            orderRef.whereEqualTo("submittedStr", dateText).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot order = queryDocumentSnapshots.getDocuments().get(0);
                        orderRef.document(order.getId()).collection("items").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                final List<DocumentSnapshot> ds = queryDocumentSnapshots.getDocuments();
                                CollectionReference cr = db.collection("users")
                                        .document("1000000").collection("order");

                                for(DocumentSnapshot doc : ds) {
                                    if(doc.exists()) {
                                        String itemName = doc.getString("name");
                                        String itemPrice = doc.getString("price");
                                        int quant = 1;

                                        try {
                                            quant = doc.getDouble("quantity").intValue();
                                        } catch (Exception e) {
                                            quant = 1;
                                        }

                                        Order newOrder = new Order(itemName, itemPrice, quant);
                                        cr.add(newOrder);
                                    }
                                }

                                Toast.makeText(view.getContext(), "Successfully queued order! Go to Orders Tab to submit.", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }
            });

        }
    };
}
