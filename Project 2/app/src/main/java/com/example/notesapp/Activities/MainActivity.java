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

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private static final int LIMIT = 50;
    private static final String TAG = "MainActivity";
    private ViewGroup mEmptyView;
    String enteredText = "";

    private FirebaseUser currUser;
    private RecyclerView NotebooksRecycler;

    private FirebaseFirestore mFirestore;
    private NotesAdapter mAdapter;
    private String userID;
    Query currCollection;
    String collectionPathString = "notebooks";
    ArrayList<String> collectionPathsArray;
    private int selectedOption = -1;
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
        collectionPathsArray = createNewCollectionPath();
        updateCollectionPathString(collectionPathsArray); // set path initially as root collection

        Bundle receivedBundle = getIntent().getExtras();
        // if the bundle indicates we have moved to a new folder, update the collectionPath
        if (getIntent().hasExtra("type") && receivedBundle.getCharSequence("type").toString().equals("folder")){
            updateCollectionPathString(receivedBundle.getStringArrayList("path"));
        }
        currCollection = mFirestore.collection("users").document(userID)
                .collection(collectionPathString);

        initRecyclerView();
        currUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void updateCollectionPathString(ArrayList<String> collectionPathList) {
        StringBuilder pathBuilder = new StringBuilder();
        for (String collection : collectionPathList) {
            pathBuilder.append(collection).append("/");
        }
        pathBuilder.deleteCharAt(pathBuilder.length() - 1); // Remove the trailing '/'
        collectionPathString = pathBuilder.toString();
    }

    private void initRecyclerView() {
        if (currCollection == null) {
            Log.w(TAG, "No query, not initializing RecyclerView");
        }

        mAdapter = new NotesAdapter(currCollection, listener) {

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

        /**
         * TODO: When populating the viewer, check the isHidden attribute
         * If isHidden = true, hide it, since it is the "fake" document
         * that we must create in order to create a subcollection
         */
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
        MenuItem changeFolderColorButton = menu.findItem(R.id.changeFolderColorButton);
        if(collectionPathString.equals("notebooks")) {
            changeFolderColorButton.setVisible(false);
        } else {
            changeFolderColorButton.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        int itemId = item.getItemId();

        if (itemId == R.id.createNoteButton) {
            newNamePopup(FileType.NOTE);
            return true;
        } else if(itemId == R.id.createFolderButton) {
            newNamePopup(FileType.FOLDER);
            return true;
        } else if(itemId == R.id.changeFolderColorButton) {
            changeFolderColor();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * When a user selects a menu item, a popup will appear asking for
     * the name of the new item
     * @param fileType, NOTE or FOLDER
     * @return
     */
    public String newNamePopup(FileType fileType) {
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
                            newColorPopup(enteredText, fileType);
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

    public void newColorPopup(String name, FileType fileType) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        builder.setTitle("Select a Color:");
        final String[] options = {"Red", "Blue", "Green", "Grey", "Black", "Orange", "Pink", "Yellow", "Purple"};

        builder.setSingleChoiceItems(options, selectedOption, (dialog, which) -> {
            // Handle radio button selection
            selectedOption = which;
        });

        // Set OK button
        builder.setPositiveButton("OK", (dialog, which) -> {
            // Handle OK button click
            if (selectedOption != -1 && selectedOption != options.length - 1) {
                String selectedValue = options[selectedOption];
                Toast.makeText(this, "Selected Option: " + selectedValue, Toast.LENGTH_SHORT).show();
                if(fileType == FileType.NOTE) {
                    createNewNote(enteredText, selectedValue.toLowerCase());
                } else if (fileType == FileType.FOLDER) {
                    createNewFolder(enteredText, selectedValue.toLowerCase());
                }
            } else {
                Toast.makeText(this, "No option selected or 'None' selected", Toast.LENGTH_SHORT).show();
            }
        });

        // Set Cancel button
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            // Handle Cancel button click
            dialog.dismiss(); // Dismiss the dialog
        });

        // Create and show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void createNewNote(String name, String color) {
        Intent intent = new Intent(MainActivity.this, NoteEditor.class);
        Bundle bundle = new Bundle();
        CollectionReference usersCollection = mFirestore.collection("users");
        CollectionReference currCollectionRef = usersCollection.document(FirebaseHelper.getInstance().getCurrentUserId()).collection(collectionPathString);

        // Data to Add to document
        Map<String, Object> dataToAdd = new HashMap<>();
        dataToAdd.put("color", color);
        dataToAdd.put("name", name);
        dataToAdd.put("type", "note");

        // Add other fields as needed
        // Add data to the user's subcollection
        currCollectionRef.add(dataToAdd)
                .addOnSuccessListener(documentReference -> {
                    // Document added successfully
                    String subDocumentId = documentReference.getId();
                    bundle.putString("documentID", subDocumentId);
                    bundle.putCharSequence("NoteTextValue", "Blank Note");
                    bundle.putString("noteName", name);
                    bundle.putString("type", "folder");
                    intent.putExtras(bundle);
                    startActivity(intent);
                    Log.d("Firestore", "Document added to userData with ID: " + subDocumentId);
                })
                .addOnFailureListener(e -> {
                    // Handle errors
                    Log.e("Firestore", "Error adding document to userData", e);
                });
    }

    // Note: The user will stay in the current directory after creating a folder
    public void createNewFolder(String name, String color) {
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        Bundle bundle = new Bundle();
        CollectionReference usersCollection = mFirestore.collection("users");
        CollectionReference currCollectionRef = usersCollection.document(FirebaseHelper.getInstance().getCurrentUserId()).collection(collectionPathString);

        // Data to Add to document
        Map<String, Object> dataToAdd = new HashMap<>();
        dataToAdd.put("color", color);
        dataToAdd.put("name", name);
        dataToAdd.put("type", "folder");
        // Add other fields as needed

        // Add data to the user's subcollection
        currCollectionRef.add(dataToAdd)
                .addOnSuccessListener(documentReference -> {
                    // Document added successfully, create subcollection
                    String subDocumentId = documentReference.getId();
                    DocumentReference newDocumentRef = currCollectionRef.document(subDocumentId);
                    CollectionReference subCollectionRef = newDocumentRef.collection("notes");
                    Map<String, Object> subCollectionData = new HashMap<>();
                    subCollectionData.put("isHidden", "true");

                    subCollectionRef.add(subCollectionData)
                            .addOnSuccessListener(subDocumentReference -> {
                                // Subdocument added successfully (Note: corrected term, it's subCollectionReference)

                                Log.d("Firestore", "Subdocument added to subcollection with ID: " + subDocumentReference.getId());
                            })
                            .addOnFailureListener(e -> {
                                // Handle errors
                                Log.e("Firestore", "Error adding subdocument to subcollection", e);
                            });
                    Log.d("Firestore", "Folder added");
                })
                .addOnFailureListener(e -> {
                    // Handle errors
                    Log.e("Firestore", "Error adding document to userData", e);
                });
    }

    /**
     * Create a new collection path array list that stores all the collection paths
     * that a user has iterated through
     * This method is only called on the first onCreate of MainActivity
     * @return
     */
    public ArrayList<String> createNewCollectionPath() {
        ArrayList<String> newList = new ArrayList<>();
        newList.add("notebooks");
        return newList;
    }

    /**
     * Add a new collection path if the user is moving to a new folder
     * @param list
     * @param collectionId
     * @return
     */
    public ArrayList<String> addToCollectionPathArray(ArrayList<String> list, String collectionId) {
        ArrayList<String> newList = list;
        newList.add(collectionId);
        return newList;
    }

    public void changeFolderColor() {
        //TODO: GET SELECTED COLOR FROM ALERT DIALOG
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select a Color:");
        final String[] options = {"Red", "Blue", "Green", "Grey", "Black", "Orange", "Pink", "Yellow", "Purple"};

        builder.setSingleChoiceItems(options, selectedOption, (dialog, which) -> {
            // Handle radio button selection
            selectedOption = which;
        });

        CollectionReference usersCollection = mFirestore.collection("users");
        CollectionReference currCollectionRef = usersCollection.document(FirebaseHelper.getInstance().getCurrentUserId()).collection(collectionPathString);

        builder.setPositiveButton("OK", (dialog, which) -> {
            if (selectedOption != -1 && selectedOption != options.length - 1) {
                String selectedValue = options[selectedOption];
                Toast.makeText(this, "Selected Option: " + selectedValue, Toast.LENGTH_SHORT).show();

                Map<String, Object> dataToAdd = new HashMap<>();
                dataToAdd.put("color", selectedValue.toLowerCase());

                // Add data to the user's subcollection
                currCollectionRef.add(dataToAdd)
                        .addOnSuccessListener(documentReference -> {
                            // Document added successfully
                            Log.d("Color", "Folder color changed");
                        })
                        .addOnFailureListener(e -> {
                            // Handle errors
                            Log.e("Color", "Error when changing folder color");
                        });
            } else {
                Toast.makeText(this, "No option selected or 'None' selected", Toast.LENGTH_SHORT).show();
            }
        });
        // Set Cancel button
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            // Handle Cancel button click
            dialog.dismiss(); // Dismiss the dialog
        });

        // Create and show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}