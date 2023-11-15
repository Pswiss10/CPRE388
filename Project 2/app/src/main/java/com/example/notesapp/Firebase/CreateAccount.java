package com.example.notesapp.Firebase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.notesapp.R;

public class CreateAccount extends AppCompatActivity {
    Button createAccountButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        createAccountButton = findViewById(R.id.CreateAccountConfirmButton);
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //If user successfully creates an account, they must login
                startActivity(new Intent(CreateAccount.this, Login.class));
            }
        });
    }
}
