package com.example.notesapp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.notesapp.Models.Notes;
import com.google.firebase.firestore.Query;

/**
 * Dialog Fragment containing filter form.
 */
public class FilterDialogFragment extends DialogFragment implements View.OnClickListener {

    public static final String TAG = "FilterDialog";

    public interface FilterListener {
        void onFilter(Filters filters);

    }

    private View mRootView;
    private Spinner mCategorySpinner;
    private Spinner mSortSpinner;
    private Spinner mDirectionSpinner;

    private FilterListener mFilterListener;

    /**
     * Initialize the components and onClick listeners to be used by the FilterDialogFragment
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return mRootView with the onClickListeners
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.dialog_filters, container, false);

        mCategorySpinner = mRootView.findViewById(R.id.spinner_category);
        mSortSpinner = mRootView.findViewById(R.id.spinner_sort);
        mDirectionSpinner = mRootView.findViewById(R.id.spinner_direction);

        mRootView.findViewById(R.id.button_search).setOnClickListener(this);
        mRootView.findViewById(R.id.button_cancel).setOnClickListener(this);

        return mRootView;
    }

    /**
     * Attaches the fragment to the activity
     * @param context the context to attach to
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof FilterListener) {
            mFilterListener = (FilterListener) context;
        }
    }

    /**
     * Called when the dialog is to be displayed, and adjusts the window
     */
    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    /**
     * OnClick of the cancel or search button, navigate to onClick listeners
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_search) {
            onSearchClicked();
        } else if (v.getId() == R.id.button_cancel) {
            onCancelClicked();
        }
    }

    /**
     * When filter is applied, update the filters to be of the user's selection
     */
    public void onSearchClicked() {
        if (mFilterListener != null) {
            mFilterListener.onFilter(getFilters());
        }
        dismiss();
    }

    /**
     * If cancel is clicked, dismiss the fragment
     */
    public void onCancelClicked() {
        dismiss();
    }

    @Nullable
    private String getSelectedType() {
        String selected = (String) mCategorySpinner.getSelectedItem();
        String notes = "Notes";
        String folders = "Folders";
        if(selected.equals(notes)){
            return "note";
        } else if (selected.equals(folders)) {
            return "folder";
        }
        else {
            return null;
        }
    }

    @Nullable
    private String getSelectedSortBy() {
        String selected = (String) mSortSpinner.getSelectedItem();
        String sortAlphabetically= "Sort Alphabetically";
        String sortLastModifiedDate = "Sort By Date Last Modified";
        String sortCreatedDate = "Sort By Date Created";

        if (sortAlphabetically.equals(selected)) {
            return Notes.FIELD_ALPHABETICAL;
        } if (sortLastModifiedDate.equals(selected)) {
            return Notes.FIELD_LAST_MODIFIED;
        } if (sortCreatedDate.equals(selected)) {
            return Notes.FIELD_CREATED;
        }
        return null;
    }

    /**
     * Get sort direction, ascending or descending based on the user input of the fragment
     * @return the query direction selected by the user
     */
    @Nullable
    private Query.Direction getSortDirection() {
        String selected = (String) mDirectionSpinner.getSelectedItem();
        String ascending= "Ascending";
        String descending = "Descending";

        if (ascending.equals(selected)) {
            return Query.Direction.ASCENDING;
        } else if (descending.equals(selected)) {
            return Query.Direction.DESCENDING;
        }

        return null;
    }

    /**
     * Reset filters
     */
    public void resetFilters() {
        if (mRootView != null) {
            mCategorySpinner.setSelection(0);
            mSortSpinner.setSelection(0);
        }
    }

    /**
     * Get current filters that user has set
     * @return Filters object containing sortBy, type, and direction
     */
    public Filters getFilters() {
        Filters filters = new Filters();
        if (mRootView != null) {
            filters.setType(getSelectedType());
            filters.setSortBy(getSelectedSortBy());
            filters.setSortDirection(getSortDirection());
        }
        return filters;
    }
}
