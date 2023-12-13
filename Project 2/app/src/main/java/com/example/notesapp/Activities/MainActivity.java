package com.example.notesapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.PopupMenu;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notesapp.FilterDialogFragment;
import com.example.notesapp.Filters;
import com.example.notesapp.Firebase.FirebaseHelper;
import com.example.notesapp.Enums.FileType;
import com.example.notesapp.Firebase.CreateAccount;
import com.example.notesapp.MainActivityViewModel;
import com.example.notesapp.R;

import com.example.notesapp.adapter.NotesAdapter;
import com.example.notesapp.util.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import java.util.HashMap;
import java.util.Map;

/**
 * Class that handles the MainActivity of the application
 * Handles navigation through folders
 * Displays the Recycler View
 * Allows users to click through menu options
 */
public class MainActivity extends AppCompatActivity implements
        View.OnClickListener,
        NotesAdapter.OnNoteSelectedListener,
        FilterDialogFragment.FilterListener {

    private static final String TAG = "MainActivity";
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
    private String newAppColor = "";
    private FilterDialogFragment mFilterDialog;
    private TextView mCurrentSearchView;
    private TextView mCurrentSortByView;
    private TextView mCurrentDirectionView;
    private MainActivityViewModel mViewModel;
    boolean inSubFolder= false;

    private String globalTheme;

    /**
     * onCreate for the Main Activity class
     * Populates components and handles logic for the
     * launching of this activity
     * @param savedInstanceState
     */
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currUser = FirebaseAuth.getInstance().getCurrentUser();
        mFirestore = FirebaseUtil.getFirestore();
        userID = FirebaseHelper.getInstance().getCurrentUserId();

        FirebaseFirestore.setLoggingEnabled(true);

        collectionPathsArray = createNewCollectionPath(); // set path initially as root collection

        Bundle receivedBundle = getIntent().getExtras();
        if (getIntent().hasExtra("type") && receivedBundle.getCharSequence("type").toString().equals("folder")) {
            collectionPathsArray = receivedBundle.getStringArrayList("path");
        } else if (getIntent().hasExtra("bundlePath")) {
            collectionPathsArray = receivedBundle.getStringArrayList("bundlePath");
        }

        updateCollectionPathString(collectionPathsArray);

        currCollection = mFirestore.collection("users").document(userID)
                .collection(collectionPathString);

        if (currUser != null) {

            try {
                mFirestore.collection("users")
                        .document(userID)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        // Get the "theme" attribute from the user document
                                        String theme = document.getString("theme");
                                        // Now you have the "theme" attribute, you can use it as needed
                                        Log.d(TAG, "Theme: " + theme);
                                        globalTheme = theme;
                                        applyThemeAndSetContentView();
                                    } else {
                                        Log.d(TAG, "No such document");
                                    }
                                } else {
                                    Log.w(TAG, "Error getting document", task.getException());
                                }
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("Error", "error", e);
            }
        } else {
            Log.d("Theme", "Skipping");
        }

    }

    /**
     * This method is important for setting the theme and continuing the
     * onCreate() process, since we have to start a new intent to update the
     * theme.
     */
    private void applyThemeAndSetContentView() {
        switch (globalTheme) {
            case "black":
                setTheme(R.style.blackTheme);
                break;
            case "blue":
                setTheme(R.style.blueTheme);
                break;
            case "green":
                setTheme(R.style.Base_Theme_NotesApp);
                break;
            case "red":
                setTheme(R.style.redTheme);
                break;
            case "teal":
                setTheme(R.style.tealTheme);
                break;
        }

        setContentView(R.layout.activity_main);
        NotebooksRecycler = findViewById(R.id.recycler_notebooks);
        mCurrentSearchView = findViewById(R.id.text_current_search);
        mCurrentSortByView = findViewById(R.id.text_current_sort_by);

        View filterBar = findViewById(R.id.filter_bar);
        filterBar.setOnClickListener(this);
        mFilterDialog = new FilterDialogFragment();
        initRecyclerView();
        currUser = FirebaseAuth.getInstance().getCurrentUser();

        // View model
        mViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        onFilter(Filters.getDefault());
    }

    /**
     * Update collectionPathString to represent all the paths listed
     * in the array given by collectionPathList
     *
     * {notebooks, 1, notes} returns notebooks/1/notes
     * @param collectionPathList
     */
    public void updateCollectionPathString(ArrayList<String> collectionPathList) {
        StringBuilder pathBuilder = new StringBuilder();
        for (String collection : collectionPathList) {
            pathBuilder.append(collection).append("/");
        }
        pathBuilder.deleteCharAt(pathBuilder.length() - 1); // Remove the trailing '/'
        collectionPathString = pathBuilder.toString();
    }

    /**
     * Initialize the recycler view that stores the notes and folders
     */
    private void initRecyclerView() {
        if (currCollection == null) {
            Log.w(TAG, "No query, not initializing RecyclerView");
        }

        mAdapter = new NotesAdapter(currCollection, this) {

            /**
             * When data is changed, show or hide the content
             * depending on the contents of the query
             */
            @Override
            protected void onDataChanged() {
                if (getItemCount() == 0) {
                    NotebooksRecycler.setVisibility(View.GONE);
                } else {
                    NotebooksRecycler.setVisibility(View.VISIBLE);
                }
            }
        };

        NotebooksRecycler.setLayoutManager(new LinearLayoutManager(this));
        NotebooksRecycler.setAdapter(mAdapter);
    }

    /**
     * Call mAdapter.startListening() to start listening for Firestore updates
     */
    @Override
    public void onStart() {
        super.onStart();
        if (mAdapter != null) {
            mAdapter.startListening();
        }
    }

    /**
     * Cause mAdapter to stop listening for Firestore updates
     */
    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }

    /**
     *
     * @param filters
     */
    @Override
    public void onFilter(Filters filters) {
        Query query = currCollection;
        String s = collectionPathString;

        // User can filter by notes, folders, or all items
        if (filters.hasType()) {
            query = query.whereEqualTo("type", filters.getType().toLowerCase());
        }

        /**
         * NOTE: alphabetical sorting sorts A-Z, then a-z, not aA-zZ
         */
        if(filters.hasSortBy()) {
            String sortBy = filters.getSortBy();
            Query.Direction sortDirection = filters.getSortDirection();
            if(sortBy.equals("alphabetical")) {
                query = query.orderBy("name", sortDirection);
            }
            else if(sortBy.equals("created")) {
                query = query.orderBy("created", sortDirection);

            } else if(sortBy.equals("lastModified")) {
                query = query.orderBy("lastModified", sortDirection);
            }
        }

        // Limit items
        query = query.limit(50);
        // Update the query
        currCollection = query;
        mAdapter.setQuery(query);
        NotebooksRecycler.setAdapter(mAdapter);

        // Set header
        mCurrentSearchView.setText(Html.fromHtml(filters.getSearchDescription(this)));
        mCurrentSortByView.setText(filters.getOrderDescription(this));

        // Save filters
        mViewModel.setFilters(filters);
    }

    /**
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.filter_bar) {
            onFilterClicked();
        } else if (v.getId() == R.id.button_clear_filter) {
            onClearFilterClicked();
        }
    }

    /**
     *
     */
    public void onFilterClicked() {
        // Show the dialog containing filter options
        mFilterDialog.show(getSupportFragmentManager(), FilterDialogFragment.TAG);
    }

    /**
     *
     */
    public void onClearFilterClicked() {
        mFilterDialog.resetFilters();
        onFilter(Filters.getDefault());
    }

    /**
     * Populate the options menu in the header depending on the directory of the user
     *
     * If the user is in home, they may:
     * Create a new note
     * Create a new folder
     * Change application color
     *
     * If the user is in a subfolder, they may:
     * Go back to the main directory
     * Create a new note
     * Change application color
     * Change current folder color
     *
     * @author Charlene, Peter
     * @param menu
     * @return true if successful
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        MenuItem changeFolderColorButton = menu.findItem(R.id.changeFolderColorButton);
        MenuItem createNewFolderButton = menu.findItem(R.id.createFolderButton);
        MenuItem backToMainMenuButton = menu.findItem(R.id.backToMainButton);
        if(collectionPathString.equals("notebooks")) {
            changeFolderColorButton.setVisible(false);
            createNewFolderButton.setVisible(true);
            backToMainMenuButton.setVisible(false);
        } else {
            changeFolderColorButton.setVisible(true);
            createNewFolderButton.setVisible(false);
            backToMainMenuButton.setVisible(true);
        }
        return true;
    }

    /**
     * When a menu option is selected,
     *
     * Menu Options:
     * Create new note
     * Create new folder
     * Change current folder color
     * Change application color
     * Go back to main
     *
     * @author Charlene, Peter
     * @param item
     * @return true if successful
     */
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
        } else if (itemId == R.id.changeAppColorButton) {
            changeAppColor();
            return true;
        } else if (itemId == R.id.backToMainButton) {
            onBackToMenuSelected();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    /**
     * When a user selects a menu item, a popup will appear asking for
     * the name of the new item
     * @author Charlene
     * @param fileType, NOTE or FOLDER
     * @return name entered by user
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

    /**
     * After user has provided a name for their new item,
     * they will be prompted to select an icon color
     *
     * Options: {"Red", "Blue", "Green", "Grey", "Black",
     * "Orange", "Pink", "Yellow", "Purple"}
     *
     * @author Charlene
     * @param name of the item as entered by user
     * @param fileType of the item selected by user
     */
    public void newColorPopup(String name, FileType fileType) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

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
                    createNewNote(name, selectedValue.toLowerCase());
                } else if (fileType == FileType.FOLDER) {
                    createNewFolder(name, selectedValue.toLowerCase());
                }
            } else {
                Toast.makeText(this, "No option selected or 'None' selected", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Create a new note
     * Add a new document to the current collection and move to the NoteEditor,
     * send a bundle to the new intent with document information
     * @param name of the item
     * @param color of the item
     */
    public void createNewNote(String name, String color) {
        Intent intent = new Intent(MainActivity.this, NoteEditor.class);
        Bundle bundle = new Bundle();
        CollectionReference usersCollection = mFirestore.collection("users");
        CollectionReference currCollectionRef = usersCollection.document(FirebaseHelper.getInstance().getCurrentUserId()).collection(collectionPathString);

        // Data to Add to document
        Map<String, Object> dataToAdd = new HashMap<>();
        dataToAdd.put("color", color);
        dataToAdd.put("name", name);
        dataToAdd.put("Text Color", "Black");
        dataToAdd.put("Font", "noto sans");
        dataToAdd.put("type", "note");
        dataToAdd.put("isHidden", false);
        dataToAdd.put("content", "Blank Note");
        dataToAdd.put("created", Timestamp.now());
        dataToAdd.put("lastModified", Timestamp.now());
        if(!collectionPathString.equals("notebooks")) {
            inSubFolder = true;
            dataToAdd.put("inSubFolder", true);
            dataToAdd.put("folderID", getFolderID(collectionPathString));
        } else {
            inSubFolder = false;
            dataToAdd.put("inSubFolder", false);
        }

        // Add other fields as needed
        // Add data to the user's subcollection
        currCollectionRef.add(dataToAdd)
                .addOnSuccessListener(documentReference -> {
                    // Document added successfully
                    String subDocumentId = documentReference.getId();
                    bundle.putString("documentID", subDocumentId);
                    bundle.putCharSequence("NoteTextValue", "Blank Note");
                    bundle.putString("noteName", name);
                    bundle.putString("Font", "noto sans");
                    bundle.putString("Text Color", "Black");
                    bundle.putString("notebookColor", color);
                    bundle.putString("type", "note");
                    bundle.putBoolean("inSubFolder", inSubFolder);
                    bundle.putString("folderID", getFolderID(collectionPathString));
                    intent.putExtras(bundle);
                    startActivity(intent);
                    Log.d("Firestore", "Document added to userData with ID: " + subDocumentId);
                })
                .addOnFailureListener(e -> {
                    // Handle errors
                    Log.e("Firestore", "Error adding document to userData", e);
                });
    }

    /**
     * Create a new folder for the user
     * Note 1: This method does not create a new intent, it just adds the folder
     * to the recycler view
     * Note 2: Creating a subcollection requires the creation of a document. In our case,
     * we don't need this document. this is why it is true for isHidden
     * @param name of the item
     * @param color of the item
     */
    public void createNewFolder(String name, String color) {
        CollectionReference usersCollection = mFirestore.collection("users");
        CollectionReference currCollectionRef = usersCollection.document(FirebaseHelper.getInstance().getCurrentUserId()).collection(collectionPathString);

        // Data to Add to document
        Map<String, Object> dataToAdd = new HashMap<>();
        dataToAdd.put("color", color);
        dataToAdd.put("name", name);
        dataToAdd.put("type", "folder");
        dataToAdd.put("isHidden", false);
        // Add other fields as needed

        // Add data to the user's subcollection
        currCollectionRef.add(dataToAdd)
                .addOnSuccessListener(documentReference -> {
                    // Document added successfully, create subcollection
                    String subDocumentId = documentReference.getId();
                    DocumentReference newDocumentRef = currCollectionRef.document(subDocumentId);
                    addToCollectionPathArray(collectionPathsArray, subDocumentId);
                    CollectionReference subCollectionRef = newDocumentRef.collection("notes");
                    Map<String, Object> subCollectionData = new HashMap<>();
                    subCollectionData.put("isHidden", true);

                    subCollectionRef.add(subCollectionData)
                            .addOnSuccessListener(subDocumentReference -> {
                                // Subdocument added successfully (Note: corrected term, it's subCollectionReference
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
     * Get the document ID of the folder from the collectionPathString
     * @param collectionPathString
     * @return string of the document iD
     */
    public String getFolderID(String collectionPathString) {
        String newPath = "";
        String[] pathSegments = collectionPathString.split("/");

        if (pathSegments.length == 3) {
            return pathSegments[1];
        } else {
            Log.e(TAG, "Invalid path format");
        }
        return newPath;
    }

    /**
     * Create a new collection path array list that stores all the collection paths
     * that a user has iterated through
     * This method is only called on the first onCreate of MainActivity
     * @return array list containing "notebooks"
     */
    public ArrayList<String> createNewCollectionPath() {
        ArrayList<String> newList = new ArrayList<>();
        newList.add("notebooks");
        return newList;
    }

    /**
     * Add a new collection path if the user is moving to a new folder
     * @param list of current paths
     * @param collectionId current collectionId to be added to the path
     * @return ArrayList of paths that lead to current user location
     */
    public ArrayList<String> addToCollectionPathArray(ArrayList<String> list, String collectionId) {
        ArrayList<String> newList = list;
        newList.add(collectionId);
        return newList;
    }

    /**
     * Allow user to select a new folder color using an alert dialog that allows them to select a color
     * Note: this will not take the user back to the main folder, but the change can be observed
     * when the user goes back to the main page
     */
    public void changeFolderColor() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select a Color:");
        final String[] options = {"Red", "Blue", "Green", "Grey", "Black", "Orange", "Pink", "Yellow", "Purple"};

        builder.setSingleChoiceItems(options, selectedOption, (dialog, which) -> {
            // Handle radio button selection
            selectedOption = which;
        });

        CollectionReference usersCollection = mFirestore.collection("users");
        CollectionReference currCollectionRef = usersCollection.document(FirebaseHelper.getInstance().getCurrentUserId()).collection("notebooks");

        String docID = getFolderID(collectionPathString);
        builder.setPositiveButton("OK", (dialog, which) -> {
            if (selectedOption != -1 && selectedOption != options.length - 1) {
                String selectedValue = options[selectedOption];
                Toast.makeText(this, "Selected Option: " + selectedValue, Toast.LENGTH_SHORT).show();

                Map<String, Object> updates = new HashMap<>();
                updates.put("color", selectedValue.toLowerCase());

                // Add data to the user's subcollection
                currCollectionRef.document(docID)
                        .update(updates)
                        .addOnSuccessListener(documentReference -> {
                            // Document added successfully
                            Log.d("Color", "Folder color changed");
                        })
                        .addOnFailureListener(e -> {
                            // Handle errors
                            Log.e("Color", "Error when changing folder color", e);
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

    /**
     * When a note is selected in the recycler view,
     * Move to the NoteViewer class for that given note
     * @param note to move to
     */
    @Override
    public void onNoteSelected(DocumentSnapshot note) {
        Intent intent = new Intent(this, NoteViewer.class);
        Bundle bundle = new Bundle();
        boolean inSubFolder = !collectionPathString.equals("notebooks") ? true : false;

        bundle.putString("documentID", note.getId());
        bundle.putString("NoteTextValue", note.get("content", String.class));
        bundle.putString("noteName", note.get("name", String.class));
        bundle.putString("Font", note.get("Font", String.class));
        bundle.putString("Text Color", note.get("Text Color", String.class));
        bundle.putString("notebookColor", note.get("color", String.class));
        bundle.putBoolean("inSubFolder", inSubFolder);
        if(inSubFolder) {
            bundle.putString("folderID", getFolderID(collectionPathString));
        }

        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * When a folder is selected in the recycler viewer,
     * Move to that subcollection and recreate the MainActivity class
     * @param folder that has been selected
     */
    @Override
    public void onFolderSelected(DocumentSnapshot folder) {
        Intent intent = new Intent(this, MainActivity.class);
        Bundle bundle = new Bundle();
        ArrayList<String> path;
        if(collectionPathsArray.size() == 1) {
           path = addToCollectionPathArray(collectionPathsArray, folder.getId());
        }
        path = addToCollectionPathArray(collectionPathsArray, "notes");
        bundle.putStringArrayList("path", path);
        bundle.putString("type", "folder");
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * When main menu is selected, rerender Main Activity class
     * Create bundle that contains toHome to be true
     * Note: this will cause onCreate to update the path correctly
     */
    public void onBackToMenuSelected() {
        Intent intent = new Intent(this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean("toHome", true);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * Allow the user to select a new application color from an Alert Dialog
     */
    private void changeAppColor() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.main_menu_popup, null);


        final TextView appColorTextView = dialogView.findViewById(R.id.appColorTextView);
        Spinner appColorSpinner = dialogView.findViewById(R.id.appColorSpinner);

        ArrayAdapter<CharSequence> appColorAdapter = ArrayAdapter.createFromResource(this, R.array.app_Colors, android.R.layout.simple_spinner_item);
        appColorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        appColorSpinner.setAdapter(appColorAdapter);

        appColorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                newAppColor = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        builder.setView(dialogView)
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {

                        changeColorWithPrefrences(newAppColor);

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

    }

    /**
     * Change the color (theme) of the application based on the color the user selects
     * @param color that has been selected
     */
    private void changeColorWithPrefrences(String color) {
        CollectionReference db = FirebaseHelper.getInstance().getFirestore().collection("users");
        String currUserID = FirebaseHelper.getInstance().getCurrentUserId();
        DocumentReference updateTheme = db.document(currUserID);
        Map<String, Object> dataToUpdate = new HashMap<>();
        //TODO change the app color within this method and save it to system preferences
        switch (color){
            case "Black":
                dataToUpdate.put("theme", "black");

                break;
            case "Blue":
                dataToUpdate.put("theme", "blue");

                break;
            case "Green":
                dataToUpdate.put("theme", "green");

                break;
            case "Red":
                dataToUpdate.put("theme", "red");

                break;
            case "Teal":
                dataToUpdate.put("theme", "teal");
                break;
        }
        updateTheme.update(dataToUpdate)
                .addOnSuccessListener(aVoid -> Log.d("Firestore", "Document successfully updated"))
                .addOnFailureListener(e -> Log.e("Firestore", "Error updating document", e));

        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        //if we are not in the root folder, send bundlePath
        if(!collectionPathString.equals("notebooks")) {
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("bundlePath", collectionPathsArray);
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }
}