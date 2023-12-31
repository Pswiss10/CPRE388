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

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.notesapp.Activities.MainActivity;
import com.example.notesapp.Activities.NoteEditor;
import com.example.notesapp.Activities.NoteViewer;
import com.example.notesapp.Models.Notes;
import com.example.notesapp.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import androidx.core.content.ContextCompat;

/**
 * Adapter class for displaying notes and folders in a RecyclerView.
 * This adapter extends FirestoreAdapter for handling Firestore queries.
 */
public class NotesAdapter extends FirestoreAdapter<NotesAdapter.ViewHolder> {
    private Context context;

    /**
     * Interface to handle item click events in the RecyclerView.
     * Contains handlers for both note and folder selected
     */
    public interface OnNoteSelectedListener {
        void onNoteSelected(DocumentSnapshot note);
        void onFolderSelected(DocumentSnapshot note);
    }

    private OnNoteSelectedListener mListener;

    /**
     * Constructor for NotesAdapter.
     *
     * @param query query to fetch data.
     * @param listener Listener for onClick events
     */
    public NotesAdapter(Query query, OnNoteSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    /**
     * Called when RecyclerView needs a new ViewHolder.
     *
     * @param parent   The ViewGroup of the new View to be added
     * @param viewType The type of the new View
     * @return A new ViewHolder that holds a View
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.item_notebook, parent, false));
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     *
     * @param holder   The ViewHolder that should be updated
     * @param position The position of the item within the adapter's data set
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    /**
     * ViewHolder class for holding item views in the RecyclerView.
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        Drawable c;
        ImageView imageView;
        TextView nameView;

        /**
         * ViewHolder constructor
         * @param itemView the root view of the ViewHolder
         */
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.notebook_item_image);
            nameView = itemView.findViewById(R.id.notebook_item_name);
        }

        /**
         * Binds data to the ViewHolder.
         *
         * Hide items that are marked as isHidden
         * Correctly display the folder/note icons and respective colors
         *
         * @param snapshot data snapshot
         * @param listener The listener for onClick events
         */
        public void bind(final DocumentSnapshot snapshot,
                         final OnNoteSelectedListener listener) {

            Notes notes = snapshot.toObject(Notes.class);
            if (notes != null || !notes.getIsHidden()) {
                // Add logging to check the values
                Log.d("NotesAdapter", "Name: " + notes.getName() + ", Color: " + notes.getColor());
                String type = notes.getType();
                Resources resources = itemView.getResources();
                if (notes.getColor() != null) {
                    switch (notes.getColor()) {
                        case "red":
                            if (type.equals("folder"))
                                c = ContextCompat.getDrawable(itemView.getContext(), R.drawable.red_folder);
                            else
                                c = ContextCompat.getDrawable(itemView.getContext(), R.drawable.red_notebook);
                            break;
                        case "orange":
                            if (type.equals("folder"))
                                c = ContextCompat.getDrawable(itemView.getContext(), R.drawable.orange_folder);
                            else
                                c = ContextCompat.getDrawable(itemView.getContext(), R.drawable.orange_notebook);
                            break;
                        case "black":
                            if (type.equals("folder"))
                                c = ContextCompat.getDrawable(itemView.getContext(), R.drawable.black_folder);
                            else
                                c = ContextCompat.getDrawable(itemView.getContext(), R.drawable.black_notebook);
                            break;
                        case "blue":
                            if (type.equals("folder"))
                                c = ContextCompat.getDrawable(itemView.getContext(), R.drawable.blue_folder);
                            else
                                c = ContextCompat.getDrawable(itemView.getContext(), R.drawable.blue_notebook);
                            break;
                        case "green":
                            if (type.equals("folder"))
                                c = ContextCompat.getDrawable(itemView.getContext(), R.drawable.green_folder);
                            else
                                c = ContextCompat.getDrawable(itemView.getContext(), R.drawable.green_notebook);
                            break;
                        case "grey":
                            if (type.equals("folder"))
                                c = ContextCompat.getDrawable(itemView.getContext(), R.drawable.grey_folder);
                            else
                                c = ContextCompat.getDrawable(itemView.getContext(), R.drawable.grey_notebook);
                            break;
                        case "pink":
                            if (type.equals("folder"))
                                c = ContextCompat.getDrawable(itemView.getContext(), R.drawable.pink_folder);
                            else
                                c = ContextCompat.getDrawable(itemView.getContext(), R.drawable.pink_notebook);
                            break;
                        case "purple":
                            if (type.equals("folder"))
                                c = ContextCompat.getDrawable(itemView.getContext(), R.drawable.purple_folder);
                            else
                                c = ContextCompat.getDrawable(itemView.getContext(), R.drawable.purple_notebook);
                            break;
                        case "yellow":
                            if (type.equals("folder"))
                                c = ContextCompat.getDrawable(itemView.getContext(), R.drawable.yellow_folder);
                            else
                                c = ContextCompat.getDrawable(itemView.getContext(), R.drawable.yellow_notebook);
                            break;
                        // Add cases for other colors
                        default:
                            // Handle the case where the color is not recognized
                            break;
                    }

                    Glide.with(imageView.getContext())
                            .load(c)
                            .into(imageView);

                    nameView.setText(notes.getName());  // Use the name from the Notes object

                    //TODO: FOR SOME REASON, THERE IS SPACE AT THE TOP
                    if (notes.getIsHidden()) {
                        // If the note is hidden, do not show it in the RecyclerView
                        itemView.setVisibility(View.GONE);
                        ViewGroup.LayoutParams params = itemView.getLayoutParams();
                        params.height = 0;
                        params.width = 0;
                        itemView.setLayoutParams(params);
                        return;
                    } else {
                        // If the note is not hidden, make sure the itemView is visible
                        itemView.setVisibility(View.VISIBLE);
                        ViewGroup.LayoutParams params = itemView.getLayoutParams();
                        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                        itemView.setLayoutParams(params);
                    }

                    // Click listener
                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (listener != null) {
                                if (notes.getType().equals("folder")) {
                                    // Handle click for folders
                                    listener.onFolderSelected(snapshot);
                                } else {
                                    // Handle click for notes
                                    listener.onNoteSelected(snapshot);
                                }
                            }
                        }
                    });
                } else {
                    // Log a warning if the color is null
                    Log.w("NotesAdapter", "Color is null for note with name: " + notes.getName());
                }
            } else {
                // Log a warning if the Notes object is null
                Log.w("NotesAdapter", "Notes object is null");
            }
        }
    }
}