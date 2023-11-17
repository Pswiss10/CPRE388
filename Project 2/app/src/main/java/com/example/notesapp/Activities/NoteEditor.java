package com.example.notesapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notesapp.R;

public class NoteEditor extends AppCompatActivity {

    private TextView noteTextView;
    private EditText noteEditText;
    private Button updateNoteButton;
    private Button completeNoteButton;
    private Button updateDesignButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);

        noteEditText = findViewById(R.id.noteEditText);
        noteTextView = findViewById(R.id.noteTextViewEditor);
        updateNoteButton = findViewById(R.id.updateNoteButton);
       // completeNoteButton = findViewById(R.id.completeNoteButton);
       // updateDesignButton = findViewById(R.id.updateDesignButton);

        if (getIntent().hasExtra("NoteTextValue")){
            Bundle receivedBundle = getIntent().getExtras();
            noteTextView.setText(receivedBundle.getCharSequence("NoteTextValue").toString());
        } else {
            Toast.makeText(getApplicationContext(), "There was no note passed to the activity", Toast.LENGTH_LONG).show();
        }


        noteEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    noteEditText.setText(noteTextView.getText().toString());
                    updateNoteButton.setVisibility(View.VISIBLE);
                }
            }
        });

        updateNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateNote();
                hideKeyboard();
                noteEditText.clearFocus();
                updateNoteButton.setVisibility(View.INVISIBLE);
            }
        });

//        completeNoteButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(NoteEditor.this, NoteViewer.class);
//                Bundle bundle = new Bundle();
//                bundle.putCharSequence("newNoteText", noteTextView.getText());
//                intent.putExtras(bundle);
//                startActivity(intent);
//            }
//        });

//        updateDesignButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
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
            bundle.putCharSequence("newNoteText", noteTextView.getText());
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