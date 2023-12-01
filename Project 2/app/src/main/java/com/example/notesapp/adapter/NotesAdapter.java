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
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
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
import com.example.notesapp.Models.Notes;
import com.example.notesapp.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import androidx.core.content.ContextCompat;


public class NotesAdapter extends FirestoreAdapter<NotesAdapter.ViewHolder> {
    private Context context;

    public interface OnNoteSelectedListener {
        void onNoteSelected(DocumentSnapshot note);
    }

    private OnNoteSelectedListener mListener;

    public NotesAdapter(Query query, OnNoteSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.item_notebook, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        Drawable c;

        ImageView imageView;
        TextView nameView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.notebook_item_image);
            nameView = itemView.findViewById(R.id.notebook_item_name);
        }

        public void bind(final DocumentSnapshot snapshot,
                         final OnNoteSelectedListener listener) {

            Notes notes = snapshot.toObject(Notes.class);
            if (notes != null) {
                // Add logging to check the values
                Log.d("NotesAdapter", "Name: " + notes.getName() + ", Color: " + notes.getColor());

                Resources resources = itemView.getResources();

                if (notes.getColor() != null) {
                    switch (notes.getColor()) {
                        case "red":
                            c = ContextCompat.getDrawable(itemView.getContext(), R.drawable.red_notebook);
                            break;
                        case "orange":
                            c = ContextCompat.getDrawable(itemView.getContext(), R.drawable.orange_notebook);
                            break;
                        case "black":
                            c = ContextCompat.getDrawable(itemView.getContext(), R.drawable.black_notebook);
                            break;
                        case "blue":
                            c = ContextCompat.getDrawable(itemView.getContext(), R.drawable.blue_notebook);
                            break;
                        case "green":
                            c = ContextCompat.getDrawable(itemView.getContext(), R.drawable.green_notebook);
                            break;
                        case "grey":
                            c = ContextCompat.getDrawable(itemView.getContext(), R.drawable.grey_notebook);
                            break;
                        case "pink":
                            c = ContextCompat.getDrawable(itemView.getContext(), R.drawable.pink_notebook);
                            break;
                        case "purple":
                            c = ContextCompat.getDrawable(itemView.getContext(), R.drawable.purple_notebook);
                            break;
                        case "yellow":
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

                    // Click listener
                    itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (listener != null) {
                                listener.onNoteSelected(snapshot);

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