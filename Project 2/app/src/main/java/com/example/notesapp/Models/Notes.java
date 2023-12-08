package com.example.notesapp.Models;

import android.widget.TextView;

import java.io.Serializable;

public class Notes implements Serializable {
    private String name;
    private String color;
    private String type;
    private boolean isHidden;

    // Constructors, getters, and setters
    public Notes() {
        // Default constructor required for Firestore
    }

    // Assuming you have another constructor for creating Notes instances
    public Notes(String name, String color, String type, boolean isHidden) {
        this.name = name;
        this.color = color;
        this.type = type;
        this.isHidden = isHidden;
    }

    // Other methods as needed

    // Getter and setter for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter and setter for color
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public boolean getIsHidden() { return isHidden; }

    public void setIsHidden(boolean isHidden) { this.isHidden = isHidden; }
}