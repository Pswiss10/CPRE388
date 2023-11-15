package com.example.notesapp.Firebase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notesapp.Models.User;
import com.example.notesapp.R;
import com.example.notesapp.util.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class CreateAccount extends AppCompatActivity {
    Button createAccountButton;
    private FirebaseFirestore mFirestore;
    private Query mQuery;
    private FirebaseAuth mAuth;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText firstNameEditText;
    private EditText lastNameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        // Enable Firestore logging
        FirebaseFirestore.setLoggingEnabled(true);

        // Initialize Firestore and the main RecyclerView
        mFirestore = FirebaseUtil.getFirestore();
        mQuery = mFirestore.collection("users");
        mAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);

        createAccountButton = findViewById(R.id.CreateAccountConfirmButton);
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                createAccount();
            }
        });
    }


    /**
     * TODO : IMPLEMENT USER INPUT VALIDATION FOR CREATE ACCOUNT
     * Return true if user meets validation criteria:
     * (Determine if we need to spend time on this, its a fast fix)
     */
    public boolean validateUserInput() {
        return true;
    }

    private void createAccount() {
        final String email = emailEditText.getText().toString();
        final String password = passwordEditText.getText().toString();
        final String firstName = firstNameEditText.getText().toString();
        final String lastName = lastNameEditText.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            Toast.makeText(CreateAccount.this, "Account created successfully.",
                                    Toast.LENGTH_SHORT).show();

                            // Get the user ID of the newly created account
                            String userId = mAuth.getCurrentUser().getUid();

                            // Store additional user information in Firestore
                            storeUserDataInFirestore(userId, firstName, lastName, email);

                            toLoginPage();

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(CreateAccount.this, "Account creation failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void storeUserDataInFirestore(String userId, String firstName, String lastName, String email) {
        // Access Firestore instance
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Create a new user document in the "users" collection
        // Here, you can add other fields such as first name, last name, etc.
        User user = new User(userId, firstName, lastName, email); // Replace with actual user data
        db.collection("users")
                .document(userId)
                .set(user)
                .addOnSuccessListener(aVoid -> Log.d("CreateAccount", "User information stored in Firestore"))
                .addOnFailureListener(e -> Log.e("CreateAccount", "Error storing user information", e));
    }

    public void toLoginPage() {
        // Redirect to the login activity or perform other actions
        Intent intent = new Intent(CreateAccount.this, Login.class);
        startActivity(intent);
        finish(); // Close the current activity
    }

}
