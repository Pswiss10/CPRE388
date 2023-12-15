package com.example.notesapp.util;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseUtil {

    /** Use emulators only in debug builds **/
    private static final boolean sUseEmulators = false;
    private static FirebaseFirestore FIRESTORE;
    private static FirebaseAuth AUTH;
    private static AuthUI AUTH_UI;

    /**
     * Get the Firestore to be used
     * @return Instance of FIrebaseFirestory
     */
    public static FirebaseFirestore getFirestore() {
        if (FIRESTORE == null) {
            FIRESTORE = FirebaseFirestore.getInstance();

            // Connect to the Cloud Firestore emulator when appropriate. The host '10.0.2.2' is a
            // special IP address to let the Android emulator connect to 'localhost'.
            if (sUseEmulators) {
                FIRESTORE.useEmulator("10.0.2.2", 8080);
            }
        }

        return FIRESTORE;
    }

}

