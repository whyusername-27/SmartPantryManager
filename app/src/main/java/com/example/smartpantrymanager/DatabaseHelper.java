package com.example.smartpantrymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SmartPantry.db";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    public static final String TABLE_PANTRY = "pantry";
    public static final String TABLE_RECIPES = "recipes";

    // Pantry Columns
    public static final String COL_PANTRY_ID = "id";
    public static final String COL_PANTRY_NAME = "ingredient_name";
    public static final String COL_PANTRY_QTY = "quantity";
    public static final String COL_PANTRY_UNIT = "unit";
    public static final String COL_PANTRY_EXPIRY = "expiry_date";

    // Recipe Columns
    public static final String COL_RECIPE_ID = "id";
    public static final String COL_RECIPE_NAME = "recipe_name";
    public static final String COL_RECIPE_INGREDIENTS = "ingredients_list";
    public static final String COL_RECIPE_STEPS = "instructions";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Creating Pantry Table
        String createPantryTable = "CREATE TABLE " + TABLE_PANTRY + " (" +
                COL_PANTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_PANTRY_NAME + " TEXT UNIQUE, " +
                COL_PANTRY_QTY + " REAL, " +
                COL_PANTRY_UNIT + " TEXT, " +
                COL_PANTRY_EXPIRY + " TEXT)";
        db.execSQL(createPantryTable);

        // Creating Recipes Table
        String createRecipeTable = "CREATE TABLE " + TABLE_RECIPES + " (" +
                COL_RECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_RECIPE_NAME + " TEXT, " +
                COL_RECIPE_INGREDIENTS + " TEXT, " +
                COL_RECIPE_STEPS + " TEXT)";
        db.execSQL(createRecipeTable);

        // Initial 15 Recipes
        seedRecipes(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PANTRY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        onCreate(db);
    }

    private void seedRecipes(SQLiteDatabase db) {
        insertRecipe(db, "Scrambled Eggs", "egg:2:pcs,butter:1:tbsp,salt:1:pinch",
                "1. Beat eggs with salt.\n2. Melt butter in pan over low heat.\n3. Cook eggs gently, stirring constantly until soft curds form.");

        insertRecipe(db, "Pancakes", "flour:1:cup,milk:1:cup,egg:1:pcs,sugar:2:tbsp",
                "1. Mix flour and sugar in a bowl.\n2. Whisk in milk and egg until smooth.\n3. Pour batter portions onto hot lightly greased griddle.");

        insertRecipe(db, "Grilled Cheese", "bread:2:slices,cheese:2:slices,butter:1:tbsp",
                "1. Butter outer sides of bread slices.\n2. Place cheese between bread.\n3. Grill in skillet over medium heat until golden brown and cheese melts.");

        insertRecipe(db, "Omelette", "egg:3:pcs,cheese:1:slice,salt:1:pinch,butter:1:tbsp",
                "1. Whisk eggs with salt.\n2. Melt butter in skillet, add eggs.\n3. Place cheese on top and fold over when set.");

        insertRecipe(db, "French Toast", "bread:2:slices,egg:1:pcs,milk:0.5:cup,cinnamon:1:pinch",
                "1. Whisk egg, milk, and cinnamon in a shallow dish.\n2. Dip bread slices to coat.\n3. Cook on greased skillet until golden on both sides.");

        insertRecipe(db, "Garlic Rice", "rice:2:cups,garlic:3:cloves,oil:2:tbsp,salt:1:pinch",
                "1. Sauté minced garlic in hot oil until golden.\n2. Add cooked rice and stir-fry evenly.\n3. Season with salt and serve hot.");

        insertRecipe(db, "Tomato Soup", "tomato:4:pcs,onion:1:pcs,garlic:2:cloves,butter:2:tbsp",
                "1. Chop tomatoes and onion.\n2. Sauté onion and garlic in butter.\n3. Add tomatoes, simmer for 15 mins, and blend smooth.");

        insertRecipe(db, "Mashed Potatoes", "potato:4:pcs,butter:2:tbsp,milk:0.5:cup,salt:1:tsp",
                "1. Peel and boil potatoes until soft.\n2. Drain and mash thoroughly.\n3. Mix in butter, warm milk, and salt until creamy.");

        insertRecipe(db, "Boiled Eggs", "egg:2:pcs,water:2:cups,salt:1:pinch",
                "1. Place eggs in saucepan covered with water.\n2. Bring to boil, then cover and remove from heat.\n3. Let stand 9-12 minutes depending on desired firmness.");

        insertRecipe(db, "Simple Oatmeal", "oats:1:cup,water:2:cups,milk:0.5:cup,sugar:1:tbsp",
                "1. Combine oats and water in saucepan.\n2. Bring to boil and simmer for 5 minutes.\n3. Stir in milk and sugar before serving.");

        insertRecipe(db, "Fried Rice", "rice:2:cups,egg:2:pcs,oil:2:tbsp,soy sauce:1:tbsp",
                "1. Scramble eggs in oiled wok and set aside.\n2. Stir-fry rice with soy sauce.\n3. Mix scrambled eggs back into hot rice.");

        insertRecipe(db, "Butter Toast", "bread:2:slices,butter:1:tbsp",
                "1. Toast bread slices in toaster until golden.\n2. Spread butter evenly over warm toast.");

        insertRecipe(db, "Lemon Tea", "tea bag:1:pcs,water:1:cup,sugar:1:tbsp,lemon:1:pcs",
                "1. Steep tea bag in boiling water for 3 minutes.\n2. Remove tea bag, stir in sugar and fresh lemon juice.");

        insertRecipe(db, "Cucumber Salad", "cucumber:2:pcs,salt:0.5:tsp,olive oil:1:tbsp,lemon:1:pcs",
                "1. Slice cucumbers thinly.\n2. Toss with olive oil, fresh lemon juice, and salt.");

        insertRecipe(db, "Pasta with Garlic and Oil", "pasta:200:g,garlic:3:cloves,olive oil:3:tbsp,salt:1:tsp",
                "1. Boil pasta in salted water until al dente.\n2. Sauté sliced garlic gently in olive oil.\n3. Toss drained pasta into garlic oil.");
    }

    private void insertRecipe(SQLiteDatabase db, String name, String ingredients, String steps) {
        ContentValues cv = new ContentValues();
        cv.put(COL_RECIPE_NAME, name);
        cv.put(COL_RECIPE_INGREDIENTS, ingredients);
        cv.put(COL_RECIPE_STEPS, steps);
        db.insert(TABLE_RECIPES, null, cv);
    }
    // CREATE: Adding or Updating Pantry Item
    public boolean addOrUpdatePantryItem(String name, double qty, String unit, String expiry) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_PANTRY_NAME, name.trim().toLowerCase());
        cv.put(COL_PANTRY_QTY, qty);
        cv.put(COL_PANTRY_UNIT, unit.trim().toLowerCase());
        cv.put(COL_PANTRY_EXPIRY, expiry);

        // Replace on conflict if item exists
        long result = db.insertWithOnConflict(TABLE_PANTRY, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        return result != -1;
    }

    // READ: Getting All Pantry Items
    public Cursor getAllPantryItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PANTRY + " ORDER BY " + COL_PANTRY_NAME + " ASC", null);
    }

    // DELETE: Deleting Pantry Item by ID
    public boolean deletePantryItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_PANTRY, COL_PANTRY_ID + "=?", new String[]{String.valueOf(id)}) > 0;
    }

    // READ: Get All Pre-loaded Recipes
    public Cursor getAllRecipes() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_RECIPES, null);
    }
}
