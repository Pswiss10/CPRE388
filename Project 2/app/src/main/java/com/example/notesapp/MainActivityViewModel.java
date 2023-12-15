package com.example.notesapp;

import androidx.lifecycle.ViewModel;

/**
 * ViewModel class for managing the data of the MainActivity.
 * Handles filters used in the MainActivity.
 */
public class MainActivityViewModel extends ViewModel {

    private Filters mFilters;

    /**
     * Constructor class for the MainActivityView Model that sets mFilters as the default filters
     */
    public MainActivityViewModel() {
        mFilters = Filters.getDefault();
    }

    /**
     * Set filters in the onCreate
     * @param mFilters filters being used
     */
    public void setFilters(Filters mFilters) {
        this.mFilters = mFilters;
    }
}
