package com.example.smartpantrymanager;

// Represents a Recipe data model in the app
public class Recipe {
    // Private fields storing recipe details
    private int id;
    private String name;
    private String ingredientsRaw;
    private String instructions;

    // Constructor to initialize a new Recipe object
    public Recipe(int id, String name, String ingredientsRaw, String instructions) {
        this.id = id;
        this.name = name;
        this.ingredientsRaw = ingredientsRaw;
        this.instructions = instructions;
    }

    // Getters to retrieve the private field values
    public int getId() { return id; }
    public String getName() { return name; }
    public String getIngredientsRaw() { return ingredientsRaw; }
    public String getInstructions() { return instructions; }
}