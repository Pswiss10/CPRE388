package com.example.notesapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notesapp.Firebase.FirebaseHelper;
import com.example.notesapp.Enums.FileType;
import com.example.notesapp.Firebase.CreateAccount;
import com.example.notesapp.R;

import com.example.notesapp.adapter.NotesAdapter;
import com.example.notesapp.util.FirebaseUtil;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements
        NotesAdapter.OnNoteSelectedListener {

    private static final int LIMIT = 50;
    private static final String TAG = "MainActivity";
    private ViewGroup mEmptyView;
    String enteredText = "";

    private FirebaseUser currUser;
    private RecyclerView NotebooksRecycler;

    private FirebaseFirestore mFirestore;
    private NotesAdapter mAdapter;
    private String userID;

    Query notebooksCollection;
    private NotesAdapter.OnNoteSelectedListener listener;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NotebooksRecycler = findViewById(R.id.recycler_notebooks);

        FirebaseFirestore.setLoggingEnabled(true);
        mFirestore = FirebaseUtil.getFirestore();
        userID = FirebaseHelper.getInstance().getCurrentUserId();
        notebooksCollection = mFirestore.collection("users").document(userID)
                .collection("notebooks");
        initRecyclerView();

        currUser = FirebaseAuth.getInstance().getCurrentUser();

    }


    private void initRecyclerView() {
        if (notebooksCollection == null) {
            Log.w(TAG, "No query, not initializing RecyclerView");
        }

        mAdapter = new NotesAdapter(notebooksCollection, this) {

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
    public void onStart() {
        super.onStart();

        // Start listening for Firestore updates
        if (mAdapter != null) {
            mAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
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
            createNewNote();
            return true;
        } else if(itemId == R.id.createFolderButton) {
            String name = createNewFolder();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     *
     * @return true if a new folder is created, false if a new folder is not created
     */
    public String createNewFolder() {
        return newPopup(FileType.FOLDER);
    }

    /**
     *
     * @return true if a new note is created, false if a new note is not created
     */
    public String createNewNote() {
        return newPopup(FileType.NOTE);
    }

    public String newPopup(FileType fileType) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_popup, null);
        final EditText editText = dialogView.findViewById(R.id.editText);
        TextView header = dialogView.findViewById(R.id.editTextHeader);

        if(fileType == FileType.NOTE) {
            header.setText("Enter New Note Name:");
        } else if (fileType == FileType.FOLDER) {
            header.setText("Enter New Folder Name:");
        }

        builder.setView(dialogView)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        // Handle the OK button click
                        enteredText = editText.getText().toString();

                        if(!(enteredText.length() > 0)) {
                            Toast.makeText(MainActivity.this, "Name length must be greater than 0 characters",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            String type = "";
                            if(fileType == FileType.NOTE) {
                                type = "Note";
                                toNewNote(enteredText);
                            } else if (fileType == FileType.FOLDER) {
                                type = "Folder";
                            }

                            Toast.makeText(MainActivity.this, "New " + type + " created",
                                    Toast.LENGTH_SHORT).show();
                        }
                        //Process the entered text
                        dialogInterface.dismiss();  // Dismiss the dialog if needed
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        // Handle the Cancel button click
                        dialogInterface.dismiss();  // Dismiss the dialog
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
        return enteredText;
    }

    public void toNewNote(String name) {
        Intent intent = new Intent(MainActivity.this, NoteEditor.class);
        Bundle bundle = new Bundle();
        CollectionReference usersCollection = mFirestore.collection("users");
        CollectionReference noteBooksCollection = usersCollection.document(FirebaseHelper.getInstance().getCurrentUserId()).collection("notebooks");
        Map<String, Object> dataToAdd = new HashMap<>();
        dataToAdd.put("color", "black");
        dataToAdd.put("content", "Blank Note");
        dataToAdd.put("name", name);
        ////TODO set font and color into dataToAdd and set them
        // Add other fields as needed

        // Add data to the user's subcollection
        noteBooksCollection.add(dataToAdd)
                .addOnSuccessListener(documentReference -> {
                    // Document added successfully
                    String subDocumentId = documentReference.getId();
                    bundle.putString("documentID", subDocumentId);
                    bundle.putCharSequence("NoteTextValue", "Blank Note");
                    bundle.putString("noteName", name);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    Log.d("Firestore", "Document added to userData with ID: " + subDocumentId);
                })
                .addOnFailureListener(e -> {
                    // Handle errors
                    Log.e("Firestore", "Error adding document to userData", e);
                });
    }

    @Override
    public void onNoteSelected(DocumentSnapshot note) {
        Intent intent = new Intent(this, NoteViewer.class);
        Bundle bundle = new Bundle();
        bundle.putString("documentID", note.getId());
        bundle.putCharSequence("NoteTextValue", note.get("content", String.class));
        bundle.putString("noteName", note.get("name", String.class));
        intent.putExtras(bundle);
        startActivity(intent);
    }

}