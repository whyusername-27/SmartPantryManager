package com.example.smartpantrymanager;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText etName, etQty, etUnit;
    private Button btnAdd, btnViewSuggestions;
    private RecyclerView rvPantry;
    private TextView tvEmpty;

    private DatabaseHelper dbHelper;
    private PantryAdapter adapter;
    private List<PantryItem> pantryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bind UI Elements
        etName = findViewById(R.id.etIngredientName);
        etQty = findViewById(R.id.etQuantity);
        etUnit = findViewById(R.id.etUnit);
        btnAdd = findViewById(R.id.btnAddIngredient);
        btnViewSuggestions = findViewById(R.id.btnViewSuggestions);
        rvPantry = findViewById(R.id.rvPantry);
        tvEmpty = findViewById(R.id.tvEmptyPantry);

        dbHelper = new DatabaseHelper(this);
        pantryList = new ArrayList<>();

        // Configure RecyclerView
        rvPantry.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PantryAdapter(pantryList, item -> deletePantryItem(item));
        rvPantry.setAdapter(adapter);

        // Load existing pantry records from database
        loadPantryItems();

        // Handle Add/Update Ingredient
        btnAdd.setOnClickListener(v -> addIngredient());
    }

    private void loadPantryItems() {
        pantryList.clear();
        Cursor cursor = dbHelper.getAllPantryItems();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PANTRY_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PANTRY_NAME));
                double qty = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PANTRY_QTY));
                String unit = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PANTRY_UNIT));
                String expiry = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_PANTRY_EXPIRY));

                pantryList.add(new PantryItem(id, name, qty, unit, expiry));
            } while (cursor.moveToNext());
            cursor.close();
        }

        adapter.notifyDataSetChanged();

        // Show/Hide Empty State Feedback
        if (pantryList.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            rvPantry.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            rvPantry.setVisibility(View.VISIBLE);
        }
    }

    private void addIngredient() {
        String name = etName.getText().toString().trim();
        String qtyStr = etQty.getText().toString().trim();
        String unit = etUnit.getText().toString().trim();

        // Input Validation
        if (name.isEmpty() || qtyStr.isEmpty() || unit.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields (Name, Quantity, Unit)", Toast.LENGTH_SHORT).show();
            return;
        }

        double qty;
        try {
            qty = Double.parseDouble(qtyStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid numeric quantity", Toast.LENGTH_SHORT).show();
            return;
        }

        // Insert into SQLite Database
        boolean success = dbHelper.addOrUpdatePantryItem(name, qty, unit, "");
        if (success) {
            Toast.makeText(this, "Ingredient saved!", Toast.LENGTH_SHORT).show();
            etName.setText("");
            etQty.setText("");
            etUnit.setText("");
            loadPantryItems(); // Refresh RecyclerView
        } else {
            Toast.makeText(this, "Failed to save ingredient", Toast.LENGTH_SHORT).show();
        }
    }

    private void deletePantryItem(PantryItem item) {
        boolean deleted = dbHelper.deletePantryItem(item.getId());
        if (deleted) {
            Toast.makeText(this, item.getName() + " removed", Toast.LENGTH_SHORT).show();
            loadPantryItems(); // Refresh RecyclerView
        } else {
            Toast.makeText(this, "Failed to delete item", Toast.LENGTH_SHORT).show();
        }
    }
}