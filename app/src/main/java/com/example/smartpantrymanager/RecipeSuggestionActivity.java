package com.example.smartpantrymanager;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// Activity that compares pantry stock against saved recipes to show matching suggestions
public class RecipeSuggestionActivity extends AppCompatActivity {

    // UI elements and data structures
    private RecyclerView rvRecipes;
    private TextView tvNoSuggestions;
    private DatabaseHelper dbHelper;
    private RecipeAdapter adapter;
    private List<Recipe> suggestedRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Link screen layout XML
        setContentView(R.layout.activity_recipe_suggestions);

        // Bind layout views
        rvRecipes = findViewById(R.id.rvRecipes);
        tvNoSuggestions = findViewById(R.id.tvNoSuggestions);

        // Initialize helper objects and list container
        dbHelper = new DatabaseHelper(this);
        suggestedRecipes = new ArrayList<>();

        // Configure RecyclerView layout manager and adapter
        rvRecipes.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecipeAdapter(suggestedRecipes);
        rvRecipes.setAdapter(adapter);

        // Search for matching recipes based on current pantry items
        findMatchingRecipes();
    }

    // Queries pantry items and matches them against all available recipes
    private void findMatchingRecipes() {
        suggestedRecipes.clear();

        // 1. Fetch current pantry ingredient names (normalized to lowercase)
        Set<String> pantryIngredients = new HashSet<>();
        Cursor pantryCursor = dbHelper.getAllPantryItems();
        if (pantryCursor != null && pantryCursor.moveToFirst()) {
            do {
                String name = pantryCursor.getString(pantryCursor.getColumnIndexOrThrow(DatabaseHelper.COL_PANTRY_NAME));
                if (name != null) {
                    pantryIngredients.add(name.trim().toLowerCase());
                }
            } while (pantryCursor.moveToNext());
            pantryCursor.close();
        }

        // 2. Fetch all database recipes and evaluate strict matching logic
        Cursor recipeCursor = dbHelper.getAllRecipes();
        if (recipeCursor != null && recipeCursor.moveToFirst()) {
            do {
                int id = recipeCursor.getInt(recipeCursor.getColumnIndexOrThrow(DatabaseHelper.COL_RECIPE_ID));
                String name = recipeCursor.getString(recipeCursor.getColumnIndexOrThrow(DatabaseHelper.COL_RECIPE_NAME));
                String ingredientsRaw = recipeCursor.getString(recipeCursor.getColumnIndexOrThrow(DatabaseHelper.COL_RECIPE_INGREDIENTS));
                String instructions = recipeCursor.getString(recipeCursor.getColumnIndexOrThrow(DatabaseHelper.COL_RECIPE_STEPS));

                // Strict Match Evaluation using robust substring/plural check
                if (hasAllIngredientsRobust(pantryIngredients, ingredientsRaw)) {
                    suggestedRecipes.add(new Recipe(id, name, ingredientsRaw, instructions));
                }
            } while (recipeCursor.moveToNext());
            recipeCursor.close();
        }

        adapter.notifyDataSetChanged();

        // Toggle visibility between suggestions list and empty state message
        if (suggestedRecipes.isEmpty()) {
            tvNoSuggestions.setVisibility(View.VISIBLE);
            rvRecipes.setVisibility(View.GONE);
        } else {
            tvNoSuggestions.setVisibility(View.GONE);
            rvRecipes.setVisibility(View.VISIBLE);
        }
    }

    // Helper method incorporating the robust strict-matching logic snippet (Section 6.1/2.3)
    private boolean hasAllIngredientsRobust(Set<String> pantrySet, String ingredientsRaw) {
        if (ingredientsRaw == null || ingredientsRaw.trim().isEmpty()) {
            return false;
        }

        // Parse ingredient pairs (Format: "egg:2:pcs,butter:1:tbsp" or plain text)
        String[] items = ingredientsRaw.split(",");
        for (String item : items) {
            String[] parts = item.split(":");
            if (parts.length > 0) {
                String requiredIngredient = parts[0].trim().toLowerCase();
                // Clean plural suffix for robust comparison ("tomatoes" -> "tomato")
                String reqClean = requiredIngredient.replaceAll("s$", "");

                boolean found = false;
                for (String userItem : pantrySet) {
                    String itemClean = userItem.replaceAll("s$", "");
                    if (itemClean.contains(reqClean) || reqClean.contains(itemClean)) {
                        found = true;
                        break;
                    }
                }

                // Strict rule: If ANY required ingredient is missing, reject recipe entirely
                if (!found) {
                    return false;
                }
            }
        }
        return true; // All required ingredients are available in pantry
    }
}