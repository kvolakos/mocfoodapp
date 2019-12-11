package com.example.moc2go.database;

import android.support.annotation.NonNull;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class OrderAdapter extends FirestoreRecyclerAdapter<Order, OrderAdapter.OrderHolder> {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference orderRef = db.collection("users").document(
            "1000000").collection("order");

    public OrderAdapter(@NonNull FirestoreRecyclerOptions options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull OrderHolder holder, int position, @NonNull Order model) {
        holder.textViewName.setText(model.getName());
        holder.textViewPrice.setText(model.getPrice());
        holder.textViewQuantity.setText(String.valueOf(model.getQuantity()));
        //holder.textViewPrice.setText(String.valueOf(model.getPrice()));
    }

    @NonNull
    @Override
    public OrderHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.order_item,
                viewGroup, false);

        final OrderHolder newHolder = new OrderHolder(v);

        setOnClickListener(newClickListener);
        return newHolder;
    }

    class OrderHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewPrice;
        TextView textViewQuantity;

        public OrderHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_view_name);
            textViewPrice = itemView.findViewById(R.id.text_view_price);
            textViewQuantity = itemView.findViewById(R.id.text_view_quantity);

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
            TextView itemName_v = (TextView) view.findViewById(R.id.text_view_name);
            TextView price_v = (TextView) view.findViewById(R.id.text_view_price);
            String itemName = itemName_v.getText().toString();
            String price = price_v.getText().toString();

            orderRef.whereEqualTo("name", itemName).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if (queryDocumentSnapshots.size() > 0) {
                        DocumentSnapshot ds = queryDocumentSnapshots.getDocuments().get(0);
                        String id = ds.getId();
                        int old_quant = Integer.parseInt(ds.get("quantity").toString());

                        // Log.e("OrderAdapter", String.valueOf(old_quant));

                        if (old_quant > 1) {

                            orderRef.document(id).update("quantity", old_quant - 1 ).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(view.getContext(), "Item removed from order.", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(view.getContext(), "Error removing item from order.", Toast.LENGTH_SHORT).show();
                                }
                            });

                        } else {

                            orderRef.document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(view.getContext(), "Item removed from order.", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(view.getContext(), "Error removing item from order.", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }

                    } else {

                        Toast.makeText(view.getContext(), "Something bad happened. Contact developers.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    };
}
