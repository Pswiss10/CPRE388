/**
 * Copyright 2017 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.notesapp.adapter;

import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * RecyclerView adapter for displaying the results of a Firestore {@link Query}.
 *
 * Note that this class forgoes some efficiency to gain simplicity. For example, the result of
 * {@link DocumentSnapshot#toObject(Class)} is not cached so the same object may be deserialized
 * many times as the user scrolls.
 *
 * See the adapter classes in FirebaseUI (https://github.com/firebase/FirebaseUI-Android/tree/master/firestore) for a
 * more efficient implementation of a Firestore RecyclerView Adapter.
 */
public abstract class FirestoreAdapter<VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> implements EventListener<QuerySnapshot> {

    private static final String TAG = "Firestore Adapter";

    private Query mQuery;
    private ListenerRegistration mRegistration;

    private ArrayList<DocumentSnapshot> mSnapshots = new ArrayList<>();

    /**
     * Set the query to be the param input
     * @param query The Firestore query to be used for data retrieval
     */
    public FirestoreAdapter(Query query) {
        mQuery = query;
    }

    /**
     * Start listening for changes in the Firestore query
     */
    public void startListening() {
        if (mQuery != null && mRegistration == null) {
            mRegistration = mQuery.addSnapshotListener(this);
        }
    }

    /**
     * A method to stop listening for changes in mRegistration
     */
    public void stopListening() {
        if (mRegistration != null) {
            mRegistration.remove();
            mRegistration = null;
        }

        mSnapshots.clear();
        notifyDataSetChanged();
    }

    /**
     * A method for setting the Query for a Firestore object
     * @param query The current Firestore query
     */
    public void setQuery(Query query) {
        // Stop listening
        stopListening();

        // Clear existing data
        mSnapshots.clear();
        notifyDataSetChanged();

        // Listen to new query
        mQuery = query;
        startListening();
    }

    /**
     * Gets the item count of the snapshot
     * @return The size of the snapshot
     */
    @Override
    public int getItemCount() {
        return mSnapshots.size();
    }

    /**
     * Gets the snapshot index of the DocumentSnapshot
     * @param index the index of the snapshot
     * @return The document snapshot from the Firebase
     */
    protected DocumentSnapshot getSnapshot(int index) {
        return mSnapshots.get(index);
    }

    /**
     *  This method watches for changes within the DocumentSnapshot and updates accordingly
     * @param documentSnapshots The value of the event. {@code null} if there was an error.
     * @param e The error if there was error. {@code null} otherwise.
     */
    @Override
    public void onEvent(QuerySnapshot documentSnapshots,
                        FirebaseFirestoreException e) {
        // Handle errors
        if (e != null) {
            Log.w(TAG, "onEvent:error", e);
            return;
        }
        // Dispatch the event
        for (DocumentChange change : documentSnapshots.getDocumentChanges()) {
            // Snapshot of the changed document
            DocumentSnapshot snapshot = change.getDocument();
            switch (change.getType()) {
                case ADDED:
                    onDocumentAdded(change);
                    break;
                case MODIFIED:
                    onDocumentModified(change);
                    break;
                case REMOVED:
                    onDocumentRemoved(change);
                    break;
            }
        }
        onDataChanged();
    }

    /**
     * This method notifies the Firebase of a new item being added
     * @param change A change in the DocumentSnapshot
     */
    protected void onDocumentAdded(DocumentChange change) {
        mSnapshots.add(change.getNewIndex(), change.getDocument());
        notifyItemInserted(change.getNewIndex());
    }

    /**
     * This method notifies the Firebase of an item being changed
     * @param change A change in the DocumentSnapshot
     */
    protected void onDocumentModified(DocumentChange change) {
        if (change.getOldIndex() == change.getNewIndex()) {
            // Item changed but remained in same position
            mSnapshots.set(change.getOldIndex(), change.getDocument());
            notifyItemChanged(change.getOldIndex());
        } else {
            // Item changed and changed position
            mSnapshots.remove(change.getOldIndex());
            mSnapshots.add(change.getNewIndex(), change.getDocument());
            notifyItemMoved(change.getOldIndex(), change.getNewIndex());
        }
    }

    /**
     * This method notifies the Firebase of an item being removed
     * @param change A change in the DocumentSnapshot
     */
    protected void onDocumentRemoved(DocumentChange change) {
        mSnapshots.remove(change.getOldIndex());
        notifyItemRemoved(change.getOldIndex());
    }

    protected void onDataChanged() {}
}
