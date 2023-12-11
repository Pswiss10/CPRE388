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

    private FilterListener mFilterListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.dialog_filters, container, false);

        mCategorySpinner = mRootView.findViewById(R.id.spinner_category);
        mSortSpinner = mRootView.findViewById(R.id.spinner_sort);

        mRootView.findViewById(R.id.button_search).setOnClickListener(this);
        mRootView.findViewById(R.id.button_cancel).setOnClickListener(this);

        return mRootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof FilterListener) {
            mFilterListener = (FilterListener) context;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_search) {
            onSearchClicked();
        } else if (v.getId() == R.id.button_cancel) {
            onCancelClicked();
        }
    }

    public void onSearchClicked() {
        if (mFilterListener != null) {
            mFilterListener.onFilter(getFilters());
        }
        dismiss();
    }

    public void onCancelClicked() {
        dismiss();
    }

    @Nullable
    private String getSelectedType() {
        String selected = (String) mCategorySpinner.getSelectedItem();
        String notes = "Note";
        String folders = "Folder";
        if (selected.equals(notes) || selected.equals(folders)) {
            return selected;
        } else {
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
            return Notes.FIELD_LAST_OPENED;
        } if (sortCreatedDate.equals(selected)) {
            return Notes.FIELD_CREATED;
        }
        return null;
    }

    @Nullable
    private Query.Direction getSortDirection() {
        String selected = (String) mSortSpinner.getSelectedItem();
        if (getString(R.string.sortAlphabetically).equals(selected)) {
            return Query.Direction.ASCENDING;
        } if (getString(R.string.sortLastModifiedDate).equals(selected)) {
            return Query.Direction.ASCENDING;
        } if (getString(R.string.sortCreatedDate).equals(selected)) {
            return Query.Direction.ASCENDING;
        }
        return null;
    }

    public void resetFilters() {
        if (mRootView != null) {
            mCategorySpinner.setSelection(0);
            mSortSpinner.setSelection(0);
        }
    }

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
