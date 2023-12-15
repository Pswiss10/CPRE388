package com.example.notesapp;

import android.content.Context;
import android.text.TextUtils;

import com.example.notesapp.Models.Notes;
import com.google.firebase.firestore.Query;

public class Filters {

    private String type = null;
    private String sortBy = null;
    private Query.Direction sortDirection = null;

    /**
     * Default constructor for Filters class
     */
    public Filters() {}

    /**
     * Return a Filters object that contains the default values for sort by and sort direction
     * @return default Filters object that is sorted alphabetically in ascending order
     */
    public static Filters getDefault() {
        Filters filters = new Filters();
        filters.setSortBy(Notes.FIELD_ALPHABETICAL);
        filters.setSortDirection(Query.Direction.ASCENDING);
        return filters;
    }

    /**
     * Check if filter has a sort type (note or folder)
     * @return Check if filter has a sort type (true), otherwise it is null and applies to all items
     */
    public boolean hasType() {
        return !(TextUtils.isEmpty(type));
    }

    /**
     * Check if filter has a sort by value
     * @return true if filter has a sort by value
     */
    public boolean hasSortBy() {
        return !(TextUtils.isEmpty(sortBy));
    }

    /**
     * Get type of filter (Note, folder)
     * @return the filter type (note, folder)
     */
    public String getType() {
        return type;
    }

    /**
     * Set the type for the filter
     * @param type (note, folder)
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Get sort by value
     * @return alphabetical, last modified, or create string
     */
    public String getSortBy() {
        return sortBy;
    }

    /**
     * Set sortBy value
     * @param sortBy alphabetical, last modified, or create string
     */
    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    /**
     * Get sort direction
     * @return ascending or descending query direction
     */
    public Query.Direction getSortDirection() {
        return sortDirection;
    }

    /**
     * Set sort direction if filter is changed
     * @param sortDirection ascending or descending query direction
     */
    public void setSortDirection(Query.Direction sortDirection) {
        this.sortDirection = sortDirection;
    }

    /**
     * If a type is not specified, return search description as all items
     * If a type is specified, return the type
     * @return string containing
     */
    public String getSearchDescription() {
        StringBuilder desc = new StringBuilder();

        if (type == null) {
            desc.append("All Items");
        }

        if (type != null) {
            desc.append(type);
        }
        return desc.toString();
    }

    /**
     * Get the order that the items should be sorted in
     * Alphabetical, by created date, or by last modified date
     * @return the order description string
     */
    public String getOrderDescription() {
        if (Notes.FIELD_ALPHABETICAL.equals(sortBy)) {
            return "sorted alphabetically";
        } else if (Notes.FIELD_CREATED.equals(sortBy)) {
            return "sorted by date created";
        } else {
            return "sorted by date last modified";
        }
    }
}

