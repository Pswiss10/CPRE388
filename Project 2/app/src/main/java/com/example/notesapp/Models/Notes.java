package com.example.notesapp.Models;

import android.widget.TextView;

import java.io.Serializable;

public class Notes implements Serializable {
    private String name;
    private String color;

    // Constructors, getters, and setters
    public Notes() {
        // Default constructor required for Firestore
    }

    // Assuming you have another constructor for creating Notes instances
    public Notes(String name, String color) {
        this.name = name;
        this.color = color;
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
}