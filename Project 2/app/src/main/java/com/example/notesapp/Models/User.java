package com.example.notesapp.Models;

/**
 * Class to store user information
 */
public class User {
    private String id;
    private String firstName;
    private String lastName;
    private String email;

    /**
     * Constructor for the User class
     * @param id
     * @param firstName
     * @param lastName
     * @param email
     */
    public User(String id, String firstName, String lastName, String email) {
       this.id = id;
       this.firstName = firstName;
       this.lastName = lastName;
       this.email = email;
    }

    /**
     * Get the id of the user
     * @return string, userID
     */
    public String getId() {
        return id;
    }
}
