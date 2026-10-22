package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

// Activity that displays detailed information for a selected recipe
public class RecipeDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Link activity layout UI
        setContentView(R.layout.activity_recipe_detail);

        // Bind TextView UI elements from layout XML
        TextView tvTitle = findViewById(R.id.tvDetailTitle);
        TextView tvIngredients = findViewById(R.id.tvDetailIngredients);
        TextView tvInstructions = findViewById(R.id.tvDetailInstructions);

        // Extract recipe data passed from RecipeAdapter via Intent extras
        String title = getIntent().getStringExtra("RECIPE_NAME");
        String ingredients = getIntent().getStringExtra("RECIPE_INGREDIENTS");
        String instructions = getIntent().getStringExtra("RECIPE_INSTRUCTIONS");

        // Safely update TextView contents if data exists
        if (title != null) tvTitle.setText(title);
        if (ingredients != null) tvIngredients.setText(ingredients);
        if (instructions != null) tvInstructions.setText(instructions);
    }
}