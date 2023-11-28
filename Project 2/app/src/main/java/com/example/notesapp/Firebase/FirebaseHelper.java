package com.example.notesapp.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseHelper {
    // Singleton instance
    private static FirebaseHelper instance;

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    private FirebaseHelper() {
        // Private constructor to enforce singleton pattern
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }

    public static synchronized FirebaseHelper getInstance() {
        if (instance == null) {
            instance = new FirebaseHelper();
        }
        return instance;
    }

    public FirebaseUser getCurrentUser() {
        return auth.getCurrentUser();
    }

    public String getCurrentUserId() {
        FirebaseUser currentUser = auth.getCurrentUser();
        return (currentUser != null) ? currentUser.getUid() : null;
    }

    public FirebaseFirestore getFirestore() {
        return firestore;
    }
}
