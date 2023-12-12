package com.example.notesapp;

import android.content.Context;
import android.text.TextUtils;

import com.example.notesapp.Models.Notes;
import com.google.firebase.firestore.Query;

public class Filters {

    private String type = null;
    private String sortBy = null;
    private Query.Direction sortDirection = null;

    public Filters() {}

    public static Filters getDefault() {
        Filters filters = new Filters();
        filters.setSortBy(Notes.FIELD_ALPHABETICAL);
        filters.setSortDirection(Query.Direction.ASCENDING);
        return filters;
    }

    public boolean hasType() {
        return !(TextUtils.isEmpty(type));
    }

    public boolean hasSortBy() {
        return !(TextUtils.isEmpty(sortBy));
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public Query.Direction getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(Query.Direction sortDirection) {
        this.sortDirection = sortDirection;
    }

    public String getSearchDescription(Context context) {
        StringBuilder desc = new StringBuilder();

        if (type == null) {
            desc.append("<b>");
            desc.append("All Items");
            desc.append("</b>");
        }

        if (type != null) {
            desc.append("<b>");
            desc.append(type);
            desc.append("</b>");
        }
        return desc.toString();
    }

    public String getOrderDescription(Context context) {
        if (Notes.FIELD_ALPHABETICAL.equals(sortBy)) {
            return "sorted alphabetically";
        } else if (Notes.FIELD_CREATED.equals(sortBy)) {
            return "sorted by date created";
        } else {
            return "sorted by date last modified";
        }
    }
}

