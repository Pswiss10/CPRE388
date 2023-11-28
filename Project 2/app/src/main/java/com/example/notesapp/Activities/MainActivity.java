package com.example.notesapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.notesapp.Firebase.FirebaseHelper;
import com.example.notesapp.R;

import com.example.notesapp.adapter.NotesAdapter;
import com.example.notesapp.util.FirebaseUtil;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private static final int LIMIT = 50;
    private static final String TAG = "MainActivity";
    private ViewGroup mEmptyView;

    private static FirebaseUser currUser;
    private RecyclerView NotebooksRecycler;

    private FirebaseFirestore mFirestore;
    private NotesAdapter mAdapter;
    private Query mQuery;
    private NotesAdapter.OnNoteSelectedListener listener;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NotebooksRecycler = findViewById(R.id.recycler_notebooks);

        FirebaseFirestore.setLoggingEnabled(true);
        mFirestore = FirebaseUtil.getFirestore();

        mQuery = mFirestore.collection("users");
        initRecyclerView();

        currUser = FirebaseAuth.getInstance().getCurrentUser();

    }


    private void initRecyclerView() {
        if (mQuery == null) {
            Log.w(TAG, "No query, not initializing RecyclerView");
        }

        mAdapter = new NotesAdapter(mQuery, listener) {

            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.
                if (getItemCount() == 0) {
                    NotebooksRecycler.setVisibility(View.GONE);
                } else {
                    NotebooksRecycler.setVisibility(View.VISIBLE);
                }
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                // Show a snackbar on errors
                Snackbar.make(findViewById(android.R.id.content),
                        "Error: check logs for info.", Snackbar.LENGTH_LONG).show();
            }
        };

        NotebooksRecycler.setLayoutManager(new LinearLayoutManager(this));
        NotebooksRecycler.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        int itemId = item.getItemId();

        if (itemId == R.id.createNoteButton) {
            Intent intent = new Intent(MainActivity.this, NoteEditor.class);
            Bundle bundle = new Bundle();
            CollectionReference usersCollection = mFirestore.collection("users");

            CollectionReference noteBooksCollection = usersCollection.document(FirebaseHelper.getInstance().getCurrentUserId()).collection("notebooks");

            Map<String, Object> dataToAdd = new HashMap<>();
            dataToAdd.put("color", "black");
            dataToAdd.put("content", "Blank Note");
            dataToAdd.put("name", "New Note");
            // Add other fields as needed

            // Add data to the user's subcollection
            noteBooksCollection.add(dataToAdd)
                    .addOnSuccessListener(documentReference -> {
                        // Document added successfully
                        String subDocumentId = documentReference.getId();
                        bundle.putString("documentID", subDocumentId);
                        bundle.putCharSequence("NoteTextValue", "Blank Note");
                        intent.putExtras(bundle);
                        startActivity(intent);
                        Log.d("Firestore", "Document added to userData with ID: " + subDocumentId);
                    })
                    .addOnFailureListener(e -> {
                        // Handle errors
                        Log.e("Firestore", "Error adding document to userData", e);
                    });


            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
    private void showPopupMenu(android.view.View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            // Handle popup menu item clicks here
            if (item.getItemId() == R.id.popup_option_1) {

                return true;
            }
            if (item.getItemId() == R.id.popup_option_2) {
                return true;
            }
            return true;

        });

        popupMenu.show();
    }
}