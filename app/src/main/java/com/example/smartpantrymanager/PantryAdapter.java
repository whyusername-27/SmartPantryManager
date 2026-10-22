package com.example.smartpantrymanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

// Adapter class to bind the PantryItem list data to RecyclerView items
public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.PantryViewHolder> {

    private List<PantryItem> pantryList;
    private OnItemDeleteListener deleteListener;

    // Interface callback for item deletion clicks
    public interface OnItemDeleteListener {
        void onDeleteClick(PantryItem item);
    }

    // Constructor initializing the dataset and click listener
    public PantryAdapter(List<PantryItem> pantryList, OnItemDeleteListener deleteListener) {
        this.pantryList = pantryList;
        this.deleteListener = deleteListener;
    }

    // Inflates the individual item XML layout and creates the ViewHolder
    @NonNull
    @Override
    public PantryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pantry, parent, false);
        return new PantryViewHolder(view);
    }

    // Binds data from the PantryItem object to the UI components at the given position
    @Override
    public void onBindViewHolder(@NonNull PantryViewHolder holder, int position) {
        PantryItem item = pantryList.get(position);

        // Capitalize the first letter of ingredient name
        holder.tvName.setText(item.getName().substring(0, 1).toUpperCase() + item.getName().substring(1));

        // Display quantity and unit formatting
        holder.tvQtyUnit.setText("Quantity: " + item.getQuantity() + " " + item.getUnit());

        // Toggle visibility and set text for expiry date if present
        if (item.getExpiryDate() != null && !item.getExpiryDate().isEmpty()) {
            holder.tvExpiry.setText("Expires: " + item.getExpiryDate());
            holder.tvExpiry.setVisibility(View.VISIBLE);
        } else {
            holder.tvExpiry.setVisibility(View.GONE);
        }

        // Set click listener on the delete button
        holder.btnDelete.setOnClickListener(v -> deleteListener.onDeleteClick(item));
    }

    // Returns total count of items in the list
    @Override
    public int getItemCount() {
        return pantryList.size();
    }

    // ViewHolder class caching UI component references for each item view
    public static class PantryViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvQtyUnit, tvExpiry;
        Button btnDelete;

        public PantryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvIngredientName);
            tvQtyUnit = itemView.findViewById(R.id.tvQuantityUnit);
            tvExpiry = itemView.findViewById(R.id.tvExpiryDate);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}