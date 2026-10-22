package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddEditIngredientActivity extends AppCompatActivity {

    private EditText etName, etQty, etUnit, etExpiry;
    private Button btnSave;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_ingredient);

        etName = findViewById(R.id.etIngredientName);
        etQty = findViewById(R.id.etQuantity);
        etUnit = findViewById(R.id.etUnit);
        etExpiry = findViewById(R.id.etExpiryDate);
        btnSave = findViewById(R.id.btnSaveIngredient);

        dbHelper = new DatabaseHelper(this);

        btnSave.setOnClickListener(v -> saveIngredient());
    }

    private void saveIngredient() {
        String name = etName.getText().toString().trim();
        String qtyStr = etQty.getText().toString().trim();
        String unit = etUnit.getText().toString().trim();
        String expiry = etExpiry.getText().toString().trim();

        // Strict Input Validation (Requirement 3.1)
        if (name.isEmpty() || qtyStr.isEmpty() || unit.isEmpty()) {
            Toast.makeText(this, "Please fill in Name, Quantity, and Unit.", Toast.LENGTH_SHORT).show();
            return;
        }

        double qty;
        try {
            qty = Double.parseDouble(qtyStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Quantity must be a valid number.", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean inserted = dbHelper.addOrUpdatePantryItem(name, qty, unit, expiry);
        if (inserted) {
            Toast.makeText(this, "Ingredient saved successfully!", Toast.LENGTH_SHORT).show();
            finish(); // Returns back to Pantry List screen
        } else {
            Toast.makeText(this, "Error saving ingredient.", Toast.LENGTH_SHORT).show();
        }
    }
}