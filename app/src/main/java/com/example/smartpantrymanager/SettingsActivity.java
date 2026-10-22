package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Button btnClear = findViewById(R.id.btnClearPantry);
        btnClear.setOnClickListener(v -> {
            DatabaseHelper dbHelper = new DatabaseHelper(this);
            dbHelper.getWritableDatabase().execSQL("DELETE FROM " + DatabaseHelper.TABLE_PANTRY);
            Toast.makeText(this, "Pantry cleared!", Toast.LENGTH_SHORT).show();
        });
    }
}