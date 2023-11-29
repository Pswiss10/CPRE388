package com.example.notesapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notesapp.Firebase.FirebaseHelper;
import com.example.notesapp.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class NoteEditor extends AppCompatActivity {

    private TextView noteTextView;
    private EditText noteEditText;
    private Button updateNoteButton;
    private String documentID;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.note_editor_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        int itemId = item.getItemId();
        if (itemId == R.id.completeNoteMenuOption) {
            Intent intent = new Intent(NoteEditor.this, NoteViewer.class);
            Bundle bundle = new Bundle();

            CollectionReference db = FirebaseHelper.getInstance().getFirestore().collection("users");

            String currUserID = FirebaseHelper.getInstance().getCurrentUserId();
            DocumentReference updateNote = db.document(currUserID).collection("notebooks").document(documentID);

            Map<String, Object> dataToUpdate = new HashMap<>();
            dataToUpdate.put("content", noteTextView.getText().toString());
            dataToUpdate.put("name", "new name 2");
            dataToUpdate.put("color", "blue");

            updateNote.update(dataToUpdate)
                    .addOnSuccessListener(aVoid -> Log.d("Firestore", "Document successfully updated"))
                    .addOnFailureListener(e -> Log.e("Firestore", "Error updating document", e));

            bundle.putCharSequence("NoteTextValue", noteTextView.getText());
            bundle.putString("documentID", documentID);
            intent.putExtras(bundle);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.customizeStyleMenuOption) {
            Toast.makeText(getApplicationContext(), "Customize Button clicked", Toast.LENGTH_LONG).show();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }



    public void updateNote(){
        String editedText = noteEditText.getText().toString();
        noteTextView.setText(editedText);
        noteEditText.getText().clear();
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}