package com.example.notesapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ArrayAdapter;

import com.example.notesapp.Enums.FileType;
import com.example.notesapp.Firebase.FirebaseHelper;
import com.example.notesapp.R;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.units.qual.A;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;


public class NoteEditor extends AppCompatActivity {

    private TextView noteTextView;
    private EditText noteEditText;
    private Button updateNoteButton;
    private String documentID;
    private String newNoteName = "New Note";
    private String textColor = "Black";
    private String font = "noto sans";
    private String noteName = "New Note";
    private String notebookColor = "blue";
    private boolean inSubFolder;
    private String folderID;


    /**
    * OnCreate method for the Note Editor
     * Gathers information from bundles to display on the screen
     * sets listeners for the Activity
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);

        noteEditText = findViewById(R.id.noteEditText);
        noteTextView = findViewById(R.id.noteTextViewEditor);
        noteTextView.setMovementMethod(new ScrollingMovementMethod());
        updateNoteButton = findViewById(R.id.updateNoteButton);
        Bundle receivedBundle = getIntent().getExtras();
        if (getIntent().hasExtra("NoteTextValue")){
            noteTextView.setText(receivedBundle.getCharSequence("NoteTextValue").toString());
        } else {
            Toast.makeText(getApplicationContext(), "There was no note passed to the activity", Toast.LENGTH_LONG).show();
        }
        if (getIntent().hasExtra("documentID")){
            documentID = receivedBundle.getString("documentID");
        }
        if(getIntent().hasExtra("noteName")) {
            newNoteName = receivedBundle.getCharSequence("noteName").toString();
        }

        if(getIntent().hasExtra("Text Color")) {
            textColor = receivedBundle.getString("Text Color");
        }

        if(getIntent().hasExtra("Font")) {
            font = receivedBundle.getString("Font");
        }

        if(getIntent().hasExtra("noteName")) {
            noteName = receivedBundle.getString("noteName");
        }
        if(getIntent().hasExtra("notebookColor")) {
            notebookColor = receivedBundle.getString("notebookColor");
        }
        if(getIntent().hasExtra("inSubFolder")) {
            inSubFolder = receivedBundle.getBoolean("inSubFolder");
            folderID = receivedBundle.getString("folderID");
        }

        updateLookOfTextView();

        noteEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    updateNoteButton.setVisibility(View.VISIBLE);
                    noteEditText.setText(noteTextView.getText().toString());

                    noteTextView.setVisibility(View.GONE);

                }
            }
        });

        updateNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateNote();
                hideKeyboard();
                noteEditText.clearFocus();
                updateNoteButton.setVisibility(View.GONE);
                noteTextView.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * creates the menu drop down for this activity
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.note_editor_menu, menu);
        return true;
    }

    /**
     * Method for when the user clicks an item in the menu
     * CompleteNote sends all the current content and styling about the note to firebase
     * update styles allows the user to change the note's sytle but doesn't push it to the database
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        int itemId = item.getItemId();
        if (itemId == R.id.completeNoteMenuOption) {
            Intent intent = new Intent(NoteEditor.this, NoteViewer.class);
            Bundle bundle = new Bundle();

            CollectionReference db = FirebaseHelper.getInstance().getFirestore().collection("users");

            String currUserID = FirebaseHelper.getInstance().getCurrentUserId();
            String path = "notebooks";
            if(inSubFolder) {
                path = "notebooks/" + folderID + "/notes/";
            }
            DocumentReference updateNote = db.document(currUserID).collection(path).document(documentID);

            Map<String, Object> dataToUpdate = new HashMap<>();
            dataToUpdate.put("content", noteTextView.getText().toString());
            dataToUpdate.put("Text Color", textColor);
            dataToUpdate.put("Font", font);
            dataToUpdate.put("name", noteName);
            dataToUpdate.put("color", notebookColor.toLowerCase());
            dataToUpdate.put("lastModified", Timestamp.now());

            updateNote.update(dataToUpdate)
                    .addOnSuccessListener(aVoid -> Log.d("Firestore", "Document successfully updated"))
                    .addOnFailureListener(e -> Log.e("Firestore", "Error updating document", e));

            bundle.putCharSequence("NoteTextValue", noteTextView.getText());
            bundle.putString("documentID", documentID);
            bundle.putString("Font", font);
            bundle.putString("Text Color", textColor);
            bundle.putString("noteName", noteName);
            bundle.putString("notebookColor", notebookColor);
            intent.putExtras(bundle);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.customizeStyleMenuOption) {
            newPopup();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * updates the textview with the user's edited note from the edit text
     */
    public void updateNote(){
        String editedText = noteEditText.getText().toString();
        noteTextView.setText(editedText);
        noteEditText.getText().clear();
    }

    /**
     * changes focus from the edit text to the activity
     */
    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * popup for when a user clicks edit styles
     * this method contains all the logic for when a user changes items in the update styles popup
     * contains listeners for the popup and then updates the note based off of these inputs
     */
    public void newPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.edit_note_styling_popup, null);
        final TextView colorTextView = dialogView.findViewById(R.id.colorTextView);
        final TextView fontTextView = dialogView.findViewById(R.id.fontTextView);
        Spinner colorSpinner = dialogView.findViewById(R.id.colorSpinner);
        Spinner fontSpinner = dialogView.findViewById(R.id.fontSpinner);
        final TextView header = dialogView.findViewById(R.id.noteStyleHeader);
        EditText nameEditText = dialogView.findViewById(R.id.nameEditText);

        final TextView noteBookTextView = dialogView.findViewById(R.id.notebookColorTextView);
        Spinner notebookSpinner = dialogView.findViewById(R.id.notebookColorSpinner);

        ArrayAdapter<CharSequence> colorAdapter = ArrayAdapter.createFromResource(this, R.array.Text_colors, android.R.layout.simple_spinner_item);
        colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        colorSpinner.setAdapter(colorAdapter);

        colorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                textColor = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                textColor = "Black";
            }
        });

        ArrayAdapter<CharSequence> fontAdapter = ArrayAdapter.createFromResource(this, R.array.Fonts, android.R.layout.simple_spinner_item);
        fontAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        fontSpinner.setAdapter(fontAdapter);

        fontSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                font = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                font = "noto sans";
            }
        });

        ArrayAdapter<CharSequence> notebookColorAdapter = ArrayAdapter.createFromResource(this, R.array.Text_colors, android.R.layout.simple_spinner_item);
        notebookColorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        notebookSpinner.setAdapter(notebookColorAdapter);

        notebookSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                notebookColor = adapterView.getItemAtPosition(i).toString().toLowerCase();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                notebookColor = "blue";
            }
        });

        nameEditText.setText(noteName);

        builder.setView(dialogView)
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {

                        noteName = nameEditText.getText().toString();

                        updateLookOfTextView();

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
     * Method that changes the style of the note when the user is done with the popup or when the page loads
     */
    private void updateLookOfTextView(){
        int fontId;
        switch (font){
            case "arimo":
                fontId = R.font.arimo;
                break;
            case "basic":
                fontId = R.font.basic;
                break;
            case "capriola":
                fontId = R.font.capriola;
                break;
            case "merienda":
                fontId = R.font.merienda;
                break;
            default:
                fontId = R.font.noto_sans;
                break;
        }
        Typeface newTypeFace = ResourcesCompat.getFont(this, fontId);
        noteTextView.setTypeface(newTypeFace);
        noteTextView.setTextColor(Color.parseColor(textColor));
    }
}
