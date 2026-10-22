package com.example.smartpantrymanager;

// Model class representing a single item stored in the pantry
public class PantryItem {
    // Private properties of a pantry item
    private int id;
    private String name;
    private double quantity;
    private String unit;
    private String expiryDate;

    // Constructor to initialize all fields when creating a new PantryItem
    public PantryItem(int id, String name, double quantity, String unit, String expiryDate) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.expiryDate = expiryDate;
    }

    // Getter methods to safely access private fields
    public int getId() { return id; }
    public String getName() { return name; }
    public double getQuantity() { return quantity; }
    public String getUnit() { return unit; }
    public String getExpiryDate() { return expiryDate; }
}