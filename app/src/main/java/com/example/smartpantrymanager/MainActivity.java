package com.example.smartpantrymanager;

import android.content.Intent;
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

// Main dashboard activity managing pantry inventory display, adding items, and app navigation
public class MainActivity extends AppCompatActivity {

    // UI View references
    private EditText etName, etQty, etUnit;
    private Button btnAdd, btnViewSuggestions;
    private RecyclerView rvPantry;
    private TextView tvEmpty;

    // Database and data adapter variables
    private DatabaseHelper dbHelper;
    private PantryAdapter adapter;
    private List<PantryItem> pantryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bind layout UI elements
        etName = findViewById(R.id.etIngredientName);
        etQty = findViewById(R.id.etQuantity);
        etUnit = findViewById(R.id.etUnit);
        btnAdd = findViewById(R.id.btnAddIngredient);
        btnViewSuggestions = findViewById(R.id.btnViewSuggestions);
        rvPantry = findViewById(R.id.rvPantry);
        tvEmpty = findViewById(R.id.tvEmptyPantry);

        // Initialize database helper and pantry list
        dbHelper = new DatabaseHelper(this);
        pantryList = new ArrayList<>();

        // Set up RecyclerView with linear layout manager and delete click listener
        rvPantry.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PantryAdapter(pantryList, item -> deletePantryItem(item));
        rvPantry.setAdapter(adapter);

        // Load existing pantry items from database on startup
        loadPantryItems();

        // Button click listeners
        btnAdd.setOnClickListener(v -> addIngredient());

        btnViewSuggestions.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RecipeSuggestionActivity.class);
            startActivity(intent);
        });
    }

    // Inflates options menu layout into the app toolbar
    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    // Handles top action bar navigation menu item selections
    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_add) {
            // Navigate to Add/Edit Ingredient screen
            Intent intent = new Intent(MainActivity.this, AddEditIngredientActivity.class);
            startActivity(intent);
            return true;

        } else if (id == R.id.menu_recipes) {
            // Navigate to Recipe Suggestions screen
            Intent intent = new Intent(MainActivity.this, RecipeSuggestionActivity.class);
            startActivity(intent);
            return true;

        } else if (id == R.id.menu_settings) {
            // Navigate to Settings screen
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Retrieves all pantry records from SQLite database and updates list UI
    private void loadPantryItems() {
        pantryList.clear();
        Cursor cursor = dbHelper.getAllPantryItems();

        // Iterate through cursor rows to populate data list
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

        // Toggle empty list feedback message
        if (pantryList.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            rvPantry.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            rvPantry.setVisibility(View.VISIBLE);
        }
    }

    // Validates form input fields and saves new ingredient to SQLite
    private void addIngredient() {
        String name = etName.getText().toString().trim();
        String qtyStr = etQty.getText().toString().trim();
        String unit = etUnit.getText().toString().trim();

        // Ensure fields are not blank
        if (name.isEmpty() || qtyStr.isEmpty() || unit.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields (Name, Quantity, Unit)", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate numeric format for quantity input
        double qty;
        try {
            qty = Double.parseDouble(qtyStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid numeric quantity", Toast.LENGTH_SHORT).show();
            return;
        }

        // Insert or update record in database
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

    // Deletes specified pantry item from database and refreshes view
    private void deletePantryItem(PantryItem item) {
        boolean deleted = dbHelper.deletePantryItem(item.getId());
        if (deleted) {
            Toast.makeText(this, item.getName() + " removed", Toast.LENGTH_SHORT).show();
            loadPantryItems(); // Refresh RecyclerView
        } else {
            Toast.makeText(this, "Failed to delete item", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPantryItems(); // Call method that fetches items from SQLite and updates the RecyclerView adapter
    }
}