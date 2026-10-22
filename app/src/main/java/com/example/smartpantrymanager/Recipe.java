package com.example.smartpantrymanager;

public class Recipe {
    private int id;
    private String name;
    private String ingredientsRaw;
    private String instructions;

    public Recipe(int id, String name, String ingredientsRaw, String instructions) {
        this.id = id;
        this.name = name;
        this.ingredientsRaw = ingredientsRaw;
        this.instructions = instructions;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getIngredientsRaw() { return ingredientsRaw; }
    public String getInstructions() { return instructions; }
}