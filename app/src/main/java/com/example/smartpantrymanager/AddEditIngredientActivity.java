package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

// Activity for manually adding or editing an ingredient in the pantry database
public class AddEditIngredientActivity extends AppCompatActivity {

    // Form UI view variables and database helper
    private EditText etName, etQty, etUnit, etExpiry;
    private Button btnSave;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Link layout XML
        setContentView(R.layout.activity_add_edit_ingredient);

        // Bind layout views
        etName = findViewById(R.id.etIngredientName);
        etQty = findViewById(R.id.etQuantity);
        etUnit = findViewById(R.id.etUnit);
        etExpiry = findViewById(R.id.etExpiryDate); // Ensure this View ID exists if using expiry dates
        btnSave = findViewById(R.id.btnSaveIngredient);

        // Initialize SQLite helper instance
        dbHelper = new DatabaseHelper(this);

        // Set click listener to trigger form validation and saving
        btnSave.setOnClickListener(v -> saveIngredient());
    }

    // Validates input fields and saves the ingredient to SQLite
    private void saveIngredient() {
        String name = etName.getText().toString().trim();
        String qtyStr = etQty.getText().toString().trim();
        String unit = etUnit.getText().toString().trim();
        String expiry = etExpiry != null ? etExpiry.getText().toString().trim() : "";

        // Form Validation: Ensure mandatory fields are filled
        if (name.isEmpty() || qtyStr.isEmpty() || unit.isEmpty()) {
            Toast.makeText(this, "Please complete all required fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Parse and validate numeric quantity input
        double qty;
        try {
            qty = Double.parseDouble(qtyStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Quantity must be a valid number.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Insert or update record in SQLite database
        boolean success = dbHelper.addOrUpdatePantryItem(name, qty, unit, expiry);

        if (success) {
            Toast.makeText(this, "Ingredient saved successfully!", Toast.LENGTH_SHORT).show();
            finish(); // Close activity and return to previous screen
        } else {
            Toast.makeText(this, "Failed to save ingredient.", Toast.LENGTH_SHORT).show();
        }
    }
}