package com.example.smartpantrymanager;

import android.view.LayoutInflater;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

// Adapter class to display a list of Recipe items inside a RecyclerView
public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private List<Recipe> recipeList;

    // Constructor to initialize the list of recipes
    public RecipeAdapter(List<Recipe> recipeList) {
        this.recipeList = recipeList;
    }

    // Inflates the individual item XML layout and creates the ViewHolder
    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe, parent, false);
        return new RecipeViewHolder(view);
    }

    // Binds recipe data to the UI views and handles click navigation
    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipeList.get(position);

        // Display recipe title, ingredients, and step-by-step instructions
        holder.tvName.setText(recipe.getName());
        holder.tvIngredients.setText("Required: " + recipe.getIngredientsRaw());
        holder.tvInstructions.setText("Steps:\n" + recipe.getInstructions());

        // Handle item clicks to pass recipe details and open RecipeDetailActivity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), RecipeDetailActivity.class);
            intent.putExtra("RECIPE_NAME", recipe.getName());
            intent.putExtra("RECIPE_INGREDIENTS", recipe.getIngredientsRaw());
            intent.putExtra("RECIPE_INSTRUCTIONS", recipe.getInstructions());
            v.getContext().startActivity(intent);
        });
    }

    // Returns the total number of recipes in the list
    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    // ViewHolder class caching layout view references for each recipe item
    public static class RecipeViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvIngredients, tvInstructions;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvRecipeName);
            tvIngredients = itemView.findViewById(R.id.tvRecipeIngredients);
            tvInstructions = itemView.findViewById(R.id.tvRecipeInstructions);
        }
    }
}