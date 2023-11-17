package com.example.notesapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.notesapp.R;

public class MainActivity extends AppCompatActivity {

    private Button noteEditorButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        noteEditorButton = findViewById(R.id.noteEditorButton);

        noteEditorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NoteViewer.class);
                startActivity(intent);
            }
        });
    }
}