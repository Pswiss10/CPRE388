package com.example.notesapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.notesapp.R;

public class NoteEditor extends AppCompatActivity {

    private TextView noteTextView;
    private EditText noteEditText;
    private Button updateNoteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);

        noteEditText = findViewById(R.id.noteEditText);
        noteTextView = findViewById(R.id.noteTextViewEditor);
        updateNoteButton = findViewById(R.id.updateNoteButton);

        Bundle receivedBundle = getIntent().getExtras();

        noteTextView.setText(receivedBundle.getCharSequence("NoteTextValue").toString());

        noteEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    noteEditText.setText(noteTextView.getText().toString());
                }
            }
        });

        updateNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateNote();
                hideKeyboard();
                noteEditText.clearFocus();
            }
        });


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