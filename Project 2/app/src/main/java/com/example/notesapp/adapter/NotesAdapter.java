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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
            Resources resources = itemView.getResources();

            if (notes.getColor().equals("red")) {
                c = ContextCompat.getDrawable(itemView.getContext(), R.drawable.red_notebook);
            }
            if (notes.getColor().equals("orange")) {
                c = ContextCompat.getDrawable(itemView.getContext(), R.drawable.orange_notebook);
            }
            if (notes.getColor().equals("black")) {
                c = ContextCompat.getDrawable(itemView.getContext(), R.drawable.black_notebook);
            }
            if (notes.getColor().equals("blue")) {
                c = ContextCompat.getDrawable(itemView.getContext(), R.drawable.blue_notebook);
            }
            if (notes.getColor().equals("green")) {
                c = ContextCompat.getDrawable(itemView.getContext(), R.drawable.green_notebook);
            }
            if (notes.getColor().equals("grey")) {
                c = ContextCompat.getDrawable(itemView.getContext(), R.drawable.grey_notebook);
            }
            if (notes.getColor().equals("pink")) {
                c = ContextCompat.getDrawable(itemView.getContext(), R.drawable.pink_notebook);
            }
            if (notes.getColor().equals("purple")) {
                c = ContextCompat.getDrawable(itemView.getContext(), R.drawable.purple_notebook);
            }
            if (notes.getColor().equals("yellow")) {
                c = ContextCompat.getDrawable(itemView.getContext(), R.drawable.yellow_notebook);
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
        }

    }
}