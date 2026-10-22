package com.example.smartpantrymanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private Switch switchExpiry, switchMetric;
    private SharedPreferences sharedPreferences;

    private static final String PREFS_NAME = "SmartPantryPrefs";
    private static final String KEY_EXPIRY_ALERTS = "expiry_alerts";
    private static final String KEY_METRIC_UNITS = "metric_units";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        switchExpiry = findViewById(R.id.switchExpiryAlerts);
        switchMetric = findViewById(R.id.switchMetricUnits);
        Button btnClear = findViewById(R.id.btnClearPantry);

        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // Load saved preferences
        boolean alertsEnabled = sharedPreferences.getBoolean(KEY_EXPIRY_ALERTS, true);
        boolean useMetric = sharedPreferences.getBoolean(KEY_METRIC_UNITS, true);

        switchExpiry.setChecked(alertsEnabled);
        switchMetric.setChecked(useMetric);

        // Save toggle preference changes
        switchExpiry.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sharedPreferences.edit().putBoolean(KEY_EXPIRY_ALERTS, isChecked).apply();
            String status = isChecked ? "enabled" : "disabled";
            Toast.makeText(this, "Expiring-soon alerts " + status, Toast.LENGTH_SHORT).show();
        });

        switchMetric.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sharedPreferences.edit().putBoolean(KEY_METRIC_UNITS, isChecked).apply();
            String unitSystem = isChecked ? "Metric (kg, g)" : "Imperial (lbs, oz)";
            Toast.makeText(this, "Unit preference set to " + unitSystem, Toast.LENGTH_SHORT).show();
        });

        // Clear database action
        btnClear.setOnClickListener(v -> {
            DatabaseHelper dbHelper = new DatabaseHelper(this);
            dbHelper.getWritableDatabase().execSQL("DELETE FROM " + DatabaseHelper.TABLE_PANTRY);
            Toast.makeText(this, "Pantry database cleared!", Toast.LENGTH_SHORT).show();
        });
    }
}