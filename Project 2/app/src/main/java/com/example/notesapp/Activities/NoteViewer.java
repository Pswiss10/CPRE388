package com.example.notesapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notesapp.Firebase.FirebaseHelper;
import com.example.notesapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class NoteViewer extends AppCompatActivity {

    private TextView noteTextView;
    private String documentID;

    private String textColor = "Black";
    private String font = "noto sans";

    private String noteName = "New Note";

    private String notebookColor = "blue";
    private boolean inSubFolder;
    private String folderID;

    /**
     * onCreate for the NoteViewer activity on launch
     * Populates using the activity view layout
     * Reads the bundle sent from the previous intent
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_viewer);

        noteTextView = findViewById(R.id.noteTextViewViewer);
        noteTextView.setMovementMethod(new ScrollingMovementMethod());
        Bundle receivedBundle = getIntent().getExtras();

        if (getIntent().hasExtra("NoteTextValue")){

            noteTextView.setText(receivedBundle.getCharSequence("NoteTextValue").toString());
        } else {
            Toast.makeText(getApplicationContext(), "There was no note passed to the activity", Toast.LENGTH_LONG).show();
        }

        if (getIntent().hasExtra("documentID")){
            documentID = receivedBundle.getString("documentID");
        }

        if(getIntent().hasExtra("Text Color")) {
            if (!(receivedBundle.getString("Text Color") == null)) {
                textColor = receivedBundle.getString("Text Color");
            }
        }

        if(getIntent().hasExtra("Font")) {
            if (!(receivedBundle.getString("Font") == null)) {
                font = receivedBundle.getString("Font");
            }
        }

        if(getIntent().hasExtra("noteName")) {
            noteName = receivedBundle.getString("noteName");
        }
        if(getIntent().hasExtra("notebookColor")) {
            notebookColor = receivedBundle.getString("notebookColor");
        }

        if(getIntent().hasExtra("notebookColor")) {
            notebookColor = receivedBundle.getString("notebookColor");
        }
        if(getIntent().hasExtra("inSubFolder")) {
            inSubFolder = receivedBundle.getBoolean("inSubFolder");
            folderID = receivedBundle.getString("folderID");
        }

        updateLookOfTextView();
    }

    /**
     * Populate the options menu with the correct options
     * as given by the note_viewer_menu menu
     * @param menu the menu to be populated with items.
     * @return true if successful
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.note_viewer_menu, menu);
        return true;
    }

    /**
     * Handle the options selected in the NoteViewer Menu
     *
     * Edit Note
     * Go to Main Menu
     * Delete Note
     *
     * @param item that has been selected in the menu
     * @return true if successful
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        int itemId = item.getItemId();

        if (itemId == R.id.editNoteButton){
            Intent intent = new Intent(NoteViewer.this, NoteEditor.class);
            Bundle options = new Bundle();
            options.putCharSequence("NoteTextValue", noteTextView.getText());
            options.putString("documentID", documentID);
            options.putString("Font", font);
            options.putString("Text Color", textColor);
            options.putString("noteName", noteName);
            options.putString("notebookColor", notebookColor.toLowerCase());
            if(inSubFolder) {
                options.putBoolean("inSubFolder", true);
                options.putString("folderID", folderID);
            }
            intent.putExtras(options);
            startActivity(intent);
            return true;
        }
        else if (itemId == R.id.mainMenuOptionsButton) {
            Intent intent = new Intent(NoteViewer.this, MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putCharSequence("UpdatedNote", noteTextView.getText());

            intent.putExtras(bundle);
            startActivity(intent);
            return true;
        } else if(itemId == R.id.deleteNoteOptionsButton) {

            CollectionReference db = FirebaseHelper.getInstance().getFirestore().collection("users");

            String currUserID = FirebaseHelper.getInstance().getCurrentUserId();
            DocumentReference deleteItem = db.document(currUserID).collection("notebooks").document(documentID);

            deleteItem.delete()
                    .addOnSuccessListener(aVoid -> {
                        // Document successfully deleted
                        Log.d("Firestore", "deleteItem successfully deleted!");
                    })
                    .addOnFailureListener(e -> {
                        // Handle errors
                        Log.e("Firestore", "Error deleting document", e);
                    });


            Intent intent = new Intent(NoteViewer.this, MainActivity.class);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Handles the look of the note depending on the user preferences
     * These preferences are sent in using the values passed in from
     * the bundle in the intent
     *
     * Update the font and text color
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