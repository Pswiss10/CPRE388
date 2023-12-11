package com.example.notesapp.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * This class is the Firebase helper
 * A singleton instance to ensure a single instance throughout the application
 */
public class FirebaseHelper {
    // Singleton instance
    private static FirebaseHelper instance;

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    /**
     * Private constructor to enforce singleton pattern.
     * Initializes the FirebaseAuth and FirebaseFirestore instances.
     */
    private FirebaseHelper() {
        // Private constructor to enforce singleton pattern
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }

    /**
     * Gets the singleton instance of FirebaseHelper
     *
     * @return The singleton instance of FirebaseHelper
     */
    public static synchronized FirebaseHelper getInstance() {
        if (instance == null) {
            instance = new FirebaseHelper();
        }
        return instance;
    }

    /**
     * Gets the ID of the currently authenticated user
     *
     * @return The user ID or null if no user is authenticated
     */
    public String getCurrentUserId() {
        FirebaseUser currentUser = auth.getCurrentUser();
        return (currentUser != null) ? currentUser.getUid() : null;
    }

    /**
     * Gets the instance of FirebaseFirestore
     *
     * @return The instance of FirebaseFirestore
     */
    public FirebaseFirestore getFirestore() {
        return firestore;
    }
}
