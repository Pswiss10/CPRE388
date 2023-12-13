package com.example.notesapp.Models;

import android.widget.TextView;

import java.io.Serializable;

/**
 * Class to store item (notes/folder) information
 */
public class Notes implements Serializable {

    public static final String FIELD_ALPHABETICAL = "alphabetical";
    public static final String FIELD_CREATED = "created";
    public static final String FIELD_LAST_MODIFIED = "lastModified";

    private String name;
    private String color;
    private String type;
    private boolean isHidden;

    // Constructors, getters, and setters
    /**
     * Default blank constructor
     */
    public Notes() {
        // Default constructor required for Firestore
    }

    // Assuming you have another constructor for creating Notes instances
    /**
     * Notes constructor
     * @param name The name of the note
     * @param color The color of the note icon
     * @param type The type (note or folder)
     * @param isHidden Decides if an object should be hidden
     */
    public Notes(String name, String color, String type, boolean isHidden) {
        this.name = name;
        this.color = color;
        this.type = type;
        this.isHidden = isHidden;
    }

    /**
     * Get name of item
     * @return name of item
     */
    public String getName() {
        return name;
    }

    /**
     * Set name of item
     * @param name of item
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the note icon color
     * @return color of note icon
     */
    public String getColor() {
        return color;
    }

    /**
     * Set note icon color
     * @param color of note icon
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * Get type of item, NOTE or FOLDER
     * @return note or folder
     */
    public String getType() { return type; }

    /**
     * Get display status of item
     * true = hidden
     * false = not hidden
     * @return if note is hidden
     */
    public boolean getIsHidden() { return isHidden; }
}