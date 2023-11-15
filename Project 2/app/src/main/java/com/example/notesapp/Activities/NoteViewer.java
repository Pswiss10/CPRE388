package com.example.notesapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.notesapp.R;

public class NoteViewer extends AppCompatActivity {

    private TextView noteTextView;
    private Button editNoteButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_viewer);

        noteTextView = findViewById(R.id.noteTextViewViewer);
        editNoteButton = findViewById(R.id.editNoteButton);

        editNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NoteViewer.this, NoteEditor.class);
                Bundle options = new Bundle();
                options.putCharSequence("NoteTextValue", noteTextView.getText());
                intent.putExtras(options);
                startActivity(intent);
            }
        });
    }
}