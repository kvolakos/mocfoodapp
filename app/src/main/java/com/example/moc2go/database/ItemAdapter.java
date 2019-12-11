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

public class ItemAdapter extends FirestoreRecyclerAdapter<Item, ItemAdapter.ItemHolder> {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference orderRef = db.collection("users").document(
            "1000000").collection("order");

    public ItemAdapter(@NonNull FirestoreRecyclerOptions options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ItemHolder holder, int position, @NonNull Item model) {
        holder.textViewName.setText(model.getName());
        holder.textViewPrice.setText(model.getPrice());
        //holder.textViewPrice.setText(String.valueOf(model.getPrice()));
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.menu_item,
                viewGroup, false);

        ItemHolder newHolder = new ItemHolder(v);

        setOnClickListener(newClickListener);
        return newHolder;
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewPrice;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_view_name);
            textViewPrice = itemView.findViewById(R.id.text_view_price);

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
        public void onItemClick (View view, int position);
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

            final Order selected = new Order(itemName, price, 1);

            orderRef.whereEqualTo("name", itemName).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                    if (queryDocumentSnapshots.size() > 0) {
                        DocumentSnapshot ds = queryDocumentSnapshots.getDocuments().get(0);
                        String id = ds.getId();
                        int old_quant = Integer.parseInt(ds.get("quantity").toString());

                        orderRef.document(id).update("quantity", old_quant + 1).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(view.getContext(), "Item added to order!", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(view.getContext(), "Error adding item to order.", Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {

                        orderRef.add(selected).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(view.getContext(), "Item added to order!", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(view.getContext(), "Error adding item to order.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }
    };
}
